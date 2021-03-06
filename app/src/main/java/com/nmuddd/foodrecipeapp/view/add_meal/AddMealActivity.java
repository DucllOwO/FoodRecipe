package com.nmuddd.foodrecipeapp.view.add_meal;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.nmuddd.foodrecipeapp.R;
import com.nmuddd.foodrecipeapp.Utils.ConnectionReceiver;
import com.nmuddd.foodrecipeapp.Utils.CurrentUser;
import com.nmuddd.foodrecipeapp.Utils.Utils;
import com.nmuddd.foodrecipeapp.database.Firebase;
import com.nmuddd.foodrecipeapp.database.FirebaseStorageInstance;
import com.nmuddd.foodrecipeapp.model.Meal;
import com.nmuddd.foodrecipeapp.model.User;
import com.nmuddd.foodrecipeapp.view.LostInternetConnectionActivity;
import com.nmuddd.foodrecipeapp.view.account.AccountFragment;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AddMealActivity extends AppCompatActivity implements View.OnClickListener, ConnectionReceiver.ReceiverListener {
    ArrayList<EditText> listIngredientItemET = new ArrayList<>(20);
    ArrayList<EditText> listMeasureItemET = new ArrayList<>(20);
    EditText category;
    EditText country;
    EditText instructions;
    EditText mealName;
    private Uri filePath;
    FirebaseStorageInstance firebaseStorageInstance;
    Firebase firebase;
    AppBarLayout appBarLayout;
    CollapsingToolbarLayout collapsingToolbarLayout;
    Button addItem;
    Button createRecipe;
    Button uploadImage;
    Button subtractItem;
    ImageView mealThumb;
    LinearLayout ingredientLinearLayout;
    LinearLayout measureLinearLayout;
    ProgressBar progressBar;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meal);
        final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) this
                .findViewById(android.R.id.content)).getChildAt(0);

        firebaseStorageInstance = new FirebaseStorageInstance();
        firebase = new Firebase();

        createRecipe = findViewById(R.id.create_recipe_btn);
        addItem = findViewById(R.id.add_item_button);
        ingredientLinearLayout = findViewById(R.id.ingredient_list);
        measureLinearLayout = findViewById(R.id.measure_list);
        collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        appBarLayout = findViewById(R.id.appbar);
        mealThumb = findViewById(R.id.mealThumb);
        category = findViewById(R.id.category);
        country = findViewById(R.id.country);
        instructions = findViewById(R.id.instructions);
        mealName = findViewById(R.id.meal_name_et);
        uploadImage = findViewById(R.id.upload_image_create_recipe);
        subtractItem = findViewById(R.id.delete_item_button);
        toolbar = findViewById(R.id.toolbar);

        setupActionBar();
        addItem.setOnClickListener(this);
        uploadImage.setOnClickListener(this);
        createRecipe.setOnClickListener(this);
        subtractItem.setOnClickListener(this);

        setupUI(viewGroup);
    }

    public void setupUI(View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    Utils.hideSoftKeyboard(AddMealActivity.this);
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }

    private void setupActionBar() {
        setSupportActionBar(toolbar);
        collapsingToolbarLayout.setTitle("Add Recipe");
        collapsingToolbarLayout.setContentScrimColor(getResources().getColor(R.color.colorWhite));
        collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.colorPrimary));
        collapsingToolbarLayout.setExpandedTitleColor(Color.TRANSPARENT);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_meal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void displayAlertDialog(String alertMessage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Alert");
        builder.setMessage(alertMessage);

        builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void addEditTextMeasureToLayout() {
        EditText editTextMeasure = new EditText(this);
        editTextMeasure.setHint("Enter measure " + (listMeasureItemET.size() + 1));
        editTextMeasure.setTextSize(14);
        editTextMeasure.setHeight(200);
        editTextMeasure.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        listMeasureItemET.add(editTextMeasure);
        measureLinearLayout.addView(editTextMeasure);
    }

    private void addEditTextIngredientToLayout() {
        EditText editTextIngredient = new EditText(this);
        editTextIngredient.setHint("Enter ingredient " + (listIngredientItemET.size() + 1));
        editTextIngredient.setTextSize(14);
        editTextIngredient.setHeight(200);
        listIngredientItemET.add(editTextIngredient);
        ingredientLinearLayout.addView(editTextIngredient);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_item_button:
                if (checkConnection()) {
                    if (listIngredientItemET.size() <= 20) {
                        addEditTextIngredientToLayout();
                        addEditTextMeasureToLayout();
                    } else {
                        displayAlertDialog("Ingredients and measures don't allow to exceed 20");
                    }
                }
                break;
            case R.id.delete_item_button:
                if (checkConnection())
                    deleteEmptyItem();
                break;
            case R.id.create_recipe_btn:
                if (checkConnection()) {
                    if (checkDataValid())
                        addMyMealAndImageToFirebase();
                }

                break;
            case R.id.upload_image_create_recipe:
                if (checkConnection())
                    showFileChooser();
                break;
            default:
                break;
        }
    }

    private void deleteEmptyItem() {
        for (int i = 0; i < listIngredientItemET.size(); i++) {
            if (listIngredientItemET.get(i).getText().toString().isEmpty()
                    && listMeasureItemET.get(i).getText().toString().isEmpty()) {
                ingredientLinearLayout.removeView(listIngredientItemET.get(i));
                measureLinearLayout.removeView(listMeasureItemET.get(i));
                listIngredientItemET.remove(i);
                listMeasureItemET.remove(i);
                continue;
            }
        }
    }

    private boolean checkDataValid() {
        if (filePath == null) {
            displayAlertDialog("Please choose a image");
            return false;
        }
        if (mealName.getText().toString().isEmpty()) {
            displayAlertDialog("Please enter meal name");
            return false;
        }
        if (category.getText().toString().isEmpty()) {
            displayAlertDialog("Please enter category");
            return false;
        }
        if (country.getText().toString().isEmpty()) {
            displayAlertDialog("Please enter country");
            return false;
        }
        if (instructions.getText().toString().isEmpty()) {
            displayAlertDialog("Please enter instructions");
            return false;
        }
        if (listIngredientItemET.isEmpty()) {
            displayAlertDialog("Please enter at least 1 ingredient");
            return false;
        }
        for (int i = 0; i < listIngredientItemET.size(); i++) {
            if (listIngredientItemET.get(i).getText().toString().isEmpty()
                    && listMeasureItemET.get(i).getText().toString().isEmpty()) {
                ingredientLinearLayout.removeView(listIngredientItemET.get(i));
                measureLinearLayout.removeView(listMeasureItemET.get(i));
                listIngredientItemET.remove(i);
                listMeasureItemET.remove(i);
                continue;
            } else if (listIngredientItemET.get(i).getText().toString().isEmpty()) {
                displayAlertDialog("Please enter ingredient " + (i + 1));
                return false;
            } else if (listMeasureItemET.get(i).getText().toString().isEmpty()) {
                displayAlertDialog("Please enter measure " + (i + 1));
                return false;
            }
        }
        return true;
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), AccountFragment.PICK_IMAGE_REQUEST);
    }

    public String getFileExtension(Uri uri) {
        ContentResolver cR = getApplicationContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AccountFragment.PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), filePath);
                mealThumb.setImageBitmap(bitmap);
            } catch (IOException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT);
            }
        }
    }

    private void addMyMealAndImageToFirebase() {
        uploadImageToStorageAndDatabase();
    }

    private void uploadImageToStorageAndDatabase() {
        if (filePath != null) {
            //displaying progress dialog while image is uploading
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();

            //getting the storage reference
            String idMeal = Long.toString(System.currentTimeMillis());
            StorageReference storageReference = firebaseStorageInstance.firebaseStorage
                    .child(FirebaseStorageInstance.STORAGE_PATH_UPLOADS + CurrentUser.idUser + idMeal + "." + getFileExtension(filePath));


            //adding the file to reference
            storageReference.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //dismissing the progress dialog
                            progressDialog.dismiss();
                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Query query = firebase.dbReference.child(firebase.tableNameUser)
                                            .orderByChild("idUser").equalTo(CurrentUser.idUser);

                                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.exists()) {
                                                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                                                    userSnapshot.getRef().setValue(setUser(createMeal(uri, idMeal)));
                                                }
                                                onBackPressed();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                        }
                                    });
                                    Toast.makeText(getApplicationContext(), "Upload Successfully", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                }
            })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //displaying the upload progress
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                        }
                    });
        } else {
            //display an error if no file is selected
            Toast.makeText(getApplicationContext(), "Please select a image file", Toast.LENGTH_LONG).show();
        }

    }

    private Meal createMeal(Uri uri, String idMeal) {
        Meal meal = new Meal();
        meal.setStrMealThumb(uri.toString());
        meal.setStrMeal(mealName.getText().toString());
        meal.setIdMeal(idMeal);
        meal.setStrInstructions(instructions.getText().toString());
        meal.setStrArea(country.getText().toString());
        meal.setStrCategory(category.getText().toString());
        meal.setDateModified(LocalDateTime.now().toString());
        try {
            meal.setStrIngredient1(listIngredientItemET.get(0).getText().toString());
            meal.setStrIngredient2(listIngredientItemET.get(1).getText().toString());
            meal.setStrIngredient3(listIngredientItemET.get(2).getText().toString());
            meal.setStrIngredient4(listIngredientItemET.get(3).getText().toString());
            meal.setStrIngredient5(listIngredientItemET.get(4).getText().toString());
            meal.setStrIngredient6(listIngredientItemET.get(5).getText().toString());
            meal.setStrIngredient7(listIngredientItemET.get(6).getText().toString());
            meal.setStrIngredient8(listIngredientItemET.get(7).getText().toString());
            meal.setStrIngredient9(listIngredientItemET.get(8).getText().toString());
            meal.setStrIngredient10(listIngredientItemET.get(9).getText().toString());
            meal.setStrIngredient11(listIngredientItemET.get(10).getText().toString());
            meal.setStrIngredient12(listIngredientItemET.get(11).getText().toString());
            meal.setStrIngredient13(listIngredientItemET.get(12).getText().toString());
            meal.setStrIngredient14(listIngredientItemET.get(13).getText().toString());
            meal.setStrIngredient15(listIngredientItemET.get(14).getText().toString());
            meal.setStrIngredient16(listIngredientItemET.get(15).getText().toString());
            meal.setStrIngredient17(listIngredientItemET.get(16).getText().toString());
            meal.setStrIngredient18(listIngredientItemET.get(17).getText().toString());
            meal.setStrIngredient19(listIngredientItemET.get(18).getText().toString());
            meal.setStrIngredient20(listIngredientItemET.get(19).getText().toString());
        } catch (Exception e) {

        }

        try {
            meal.setStrMeasure1(listMeasureItemET.get(0).getText().toString());
            meal.setStrMeasure2(listMeasureItemET.get(1).getText().toString());
            meal.setStrMeasure3(listMeasureItemET.get(2).getText().toString());
            meal.setStrMeasure4(listMeasureItemET.get(3).getText().toString());
            meal.setStrMeasure5(listMeasureItemET.get(4).getText().toString());
            meal.setStrMeasure6(listMeasureItemET.get(5).getText().toString());
            meal.setStrMeasure7(listMeasureItemET.get(6).getText().toString());
            meal.setStrMeasure8(listMeasureItemET.get(7).getText().toString());
            meal.setStrMeasure9(listMeasureItemET.get(8).getText().toString());
            meal.setStrMeasure10(listMeasureItemET.get(9).getText().toString());
            meal.setStrMeasure11(listMeasureItemET.get(10).getText().toString());
            meal.setStrMeasure12(listMeasureItemET.get(11).getText().toString());
            meal.setStrMeasure13(listMeasureItemET.get(12).getText().toString());
            meal.setStrMeasure14(listMeasureItemET.get(13).getText().toString());
            meal.setStrMeasure15(listMeasureItemET.get(14).getText().toString());
            meal.setStrMeasure16(listMeasureItemET.get(15).getText().toString());
            meal.setStrMeasure17(listMeasureItemET.get(16).getText().toString());
            meal.setStrMeasure18(listMeasureItemET.get(17).getText().toString());
            meal.setStrMeasure19(listMeasureItemET.get(18).getText().toString());
            meal.setStrMeasure20(listMeasureItemET.get(19).getText().toString());
        } catch (Exception e) {

        }

        return meal;
    }

    private User setUser(Meal meal) {
        User user = new User();
        user.setIdUser(CurrentUser.idUser);
        user.setEmail(CurrentUser.email);
        user.setPassword(CurrentUser.password);
        user.setAvatar(CurrentUser.avatar);
        if (CurrentUser.mealFavorite != null) {
            user.setMealFavorite(CurrentUser.mealFavorite);
        } else {
            List<Meal> meals = new ArrayList<>();
            user.setMealFavorite(meals);
        }

        if (CurrentUser.myMeal != null) {
            CurrentUser.myMeal.add(meal);
            user.setMyMeal(CurrentUser.myMeal);
        } else {
            List<Meal> meals = new ArrayList<>();
            meals.add(meal);
            user.setMyMeal(meals);
        }
        return user;
    }

    private Boolean checkConnection() {

        // initialize intent filter
        IntentFilter intentFilter = new IntentFilter();

        // add action
        intentFilter.addAction("android.new.conn.CONNECTIVITY_CHANGE");

        // register receiver
        getApplicationContext().registerReceiver(new ConnectionReceiver(), intentFilter);

        // Initialize listener
        ConnectionReceiver.Listener = this;

        // Initialize connectivity manager
        ConnectivityManager manager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        // Initialize network info
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        // get connection status
        boolean isConnected = networkInfo != null && networkInfo.isConnectedOrConnecting();

        if (!isConnected)
            startActivityLostInternetConnection(isConnected);

        return isConnected;
    }

    private void startActivityLostInternetConnection(boolean isConnected) {
        Intent intent = new Intent(AddMealActivity.this, LostInternetConnectionActivity.class);
        startActivity(intent);
    }

    @Override
    public void onNetworkChange(boolean isConnected) {

    }
}