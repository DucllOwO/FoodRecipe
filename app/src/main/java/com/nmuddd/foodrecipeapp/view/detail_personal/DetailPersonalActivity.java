package com.nmuddd.foodrecipeapp.view.detail_personal;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.nmuddd.foodrecipeapp.EditMealActivity;
import com.nmuddd.foodrecipeapp.R;
import com.nmuddd.foodrecipeapp.Utils.CurrentUser;
import com.nmuddd.foodrecipeapp.Utils.Utils;
import com.nmuddd.foodrecipeapp.database.Firebase;
import com.nmuddd.foodrecipeapp.database.FirebaseStorageInstance;
import com.nmuddd.foodrecipeapp.model.Meal;
import com.nmuddd.foodrecipeapp.model.User;
import com.nmuddd.foodrecipeapp.view.personal_list.PersonalListFragment;
import com.squareup.picasso.Picasso;

import java.io.Serializable;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailPersonalActivity extends AppCompatActivity implements DetailPersonalView {
    public static final String EXTRA_DETAIL_PERSONAL = "detail_personal";
    public static final String EXTRA_EDIT_MEAL = "edit_meal";
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.appbar)
    AppBarLayout appBarLayout;

    @BindView(R.id.collapsing_toolbar_personal)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @BindView(R.id.mealThumb)
    ImageView mealThumb;

    @BindView(R.id.category)
    TextView category;

    @BindView(R.id.country)
    TextView country;

    @BindView(R.id.instructions)
    TextView instructions;

    @BindView(R.id.ingredient)
    TextView ingredients;

    @BindView(R.id.measure)
    TextView measures;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;


    Firebase firebase;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseStorageInstance firebaseStorageInstance;

    private Meal meal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_personal);
        ButterKnife.bind(this);
        progressBar.setVisibility(View.VISIBLE);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebase = new Firebase();
        firebaseStorageInstance = new FirebaseStorageInstance();
        setupActionBar();

        Intent intent = getIntent();
        meal = (Meal) intent.getSerializableExtra(PersonalListFragment.EXTRA_DETAIL_PERSONAL);

        setMeal(meal);

        progressBar.setVisibility(View.GONE);
    }

    private void setupActionBar() {
        setSupportActionBar(toolbar);
        collapsingToolbarLayout.setContentScrimColor(getResources().getColor(R.color.colorWhite));
        collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.colorPrimary));
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.colorWhite));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.edit:
                Intent intentToEditMeal = new Intent(this, EditMealActivity.class);
                intentToEditMeal.putExtra(EXTRA_EDIT_MEAL, (Serializable) meal);
                startActivity(intentToEditMeal);
                finish();
                return true;
            case R.id.delete:
                deleteRecipe();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void deleteRecipe() {
        displayAlertYESNO("Are you sure want to delete your recipe?");
    }

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void setMeal(Meal meal) {
        this.meal = meal;
        Picasso.get().load(meal.getStrMealThumb()).into(mealThumb);
        collapsingToolbarLayout.setTitle(meal.getStrMeal());
        category.setText(meal.getStrCategory());
        country.setText(meal.getStrArea());
        instructions.setText(meal.getStrInstructions());
        setupActionBar();

        //===
        if (meal != null) {
            if (meal.getStrIngredient1() != null) {
                ingredients.append("\n \u2022 " + meal.getStrIngredient1());
            }
            if (meal.getStrIngredient2() != null) {
                ingredients.append("\n \u2022 " + meal.getStrIngredient2());
            }
            if (meal.getStrIngredient3() != null) {
                ingredients.append("\n \u2022 " + meal.getStrIngredient3());
            }
            if (meal.getStrIngredient4() != null) {
                ingredients.append("\n \u2022 " + meal.getStrIngredient4());
            }
            if (meal.getStrIngredient5() != null) {
                ingredients.append("\n \u2022 " + meal.getStrIngredient5());
            }
            if (meal.getStrIngredient6() != null) {
                ingredients.append("\n \u2022 " + meal.getStrIngredient6());
            }
            if (meal.getStrIngredient7() != null) {
                ingredients.append("\n \u2022 " + meal.getStrIngredient7());
            }
            if (meal.getStrIngredient8() != null) {
                ingredients.append("\n \u2022 " + meal.getStrIngredient8());
            }
            if (meal.getStrIngredient9() != null) {
                ingredients.append("\n \u2022 " + meal.getStrIngredient9());
            }
            if (meal.getStrIngredient10() != null) {
                ingredients.append("\n \u2022 " + meal.getStrIngredient10());
            }
            if (meal.getStrIngredient11() != null) {
                ingredients.append("\n \u2022 " + meal.getStrIngredient11());
            }
            if (meal.getStrIngredient12() != null) {
                ingredients.append("\n \u2022 " + meal.getStrIngredient12());
            }
            if (meal.getStrIngredient13() != null) {
                ingredients.append("\n \u2022 " + meal.getStrIngredient13());
            }
            if (meal.getStrIngredient14() != null) {
                ingredients.append("\n \u2022 " + meal.getStrIngredient14());
            }
            if (meal.getStrIngredient15() != null) {
                ingredients.append("\n \u2022 " + meal.getStrIngredient15());
            }
            if (meal.getStrIngredient16() != null) {
                ingredients.append("\n \u2022 " + meal.getStrIngredient16());
            }
            if (meal.getStrIngredient17() != null) {
                ingredients.append("\n \u2022 " + meal.getStrIngredient17());
            }
            if (meal.getStrIngredient18() != null) {
                ingredients.append("\n \u2022 " + meal.getStrIngredient18());
            }
            if (meal.getStrIngredient19() != null) {
                ingredients.append("\n \u2022 " + meal.getStrIngredient19());
            }
            if (meal.getStrIngredient20() != null) {
                ingredients.append("\n \u2022 " + meal.getStrIngredient20());
            }

            if (meal.getStrMeasure1() != null) {
                measures.append("\n : " + meal.getStrMeasure1());
            }
            if (meal.getStrMeasure2() != null) {
                measures.append("\n : " + meal.getStrMeasure2());
            }
            if (meal.getStrMeasure3() != null) {
                measures.append("\n : " + meal.getStrMeasure3());
            }
            if (meal.getStrMeasure4() != null) {
                measures.append("\n : " + meal.getStrMeasure4());
            }
            if (meal.getStrMeasure5() != null) {
                measures.append("\n : " + meal.getStrMeasure5());
            }
            if (meal.getStrMeasure6() != null) {
                measures.append("\n : " + meal.getStrMeasure6());
            }
            if (meal.getStrMeasure7() != null) {
                measures.append("\n : " + meal.getStrMeasure7());
            }
            if (meal.getStrMeasure8() != null) {
                measures.append("\n : " + meal.getStrMeasure8());
            }
            if (meal.getStrMeasure9() != null) {
                measures.append("\n : " + meal.getStrMeasure9());
            }
            if (meal.getStrMeasure10() != null) {
                measures.append("\n : " + meal.getStrMeasure10());
            }
            if (meal.getStrMeasure11() != null) {
                measures.append("\n : " + meal.getStrMeasure11());
            }
            if (meal.getStrMeasure12() != null) {
                measures.append("\n : " + meal.getStrMeasure12());
            }
            if (meal.getStrMeasure13() != null) {
                measures.append("\n : " + meal.getStrMeasure13());
            }
            if (meal.getStrMeasure14() != null) {
                measures.append("\n : " + meal.getStrMeasure14());
            }
            if (meal.getStrMeasure15() != null) {
                measures.append("\n : " + meal.getStrMeasure15());
            }
            if (meal.getStrMeasure16() != null) {
                measures.append("\n : " + meal.getStrMeasure16());
            }
            if (meal.getStrMeasure17() != null) {
                measures.append("\n : " + meal.getStrMeasure17());
            }
            if (meal.getStrMeasure18() != null) {
                measures.append("\n : " + meal.getStrMeasure18());
            }
            if (meal.getStrMeasure19() != null) {
                measures.append("\n : " + meal.getStrMeasure19());
            }
            if (meal.getStrMeasure20() != null) {
                measures.append("\n : " + meal.getStrMeasure20());
            }
        }

    }

    @Override
    public void onErrorLoading(String message) {
        Utils.showDialogMessage(this, "Error", message);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void displayToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    public String getFileExtension(String url) {
        String mime = MimeTypeMap.getFileExtensionFromUrl(url);
        return mime;
    }

    @Override
    public void displayAlertYESNO(String alertMessage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Alert");
        builder.setMessage(alertMessage);

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                StorageReference storageReference = firebaseStorageInstance.firebaseStorage
                        .child(FirebaseStorageInstance.STORAGE_PATH_UPLOADS + CurrentUser.idUser + meal.getIdMeal() + "." + getFileExtension(meal.getStrMealThumb()));
                storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Query query = firebase.dbReference.child(firebase.tableNameUser).orderByChild("idUser")
                                .equalTo(CurrentUser.idUser);
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    for (DataSnapshot user : snapshot.getChildren()) {
                                        for (int i = 0; i < CurrentUser.myMeal.size(); i++) {
                                            if (meal.getIdMeal().equals(CurrentUser.myMeal.get(i).getIdMeal()))
                                                CurrentUser.myMeal.remove(i);
                                        }
                                        User user1 = new User();
                                        user1.setMyMeal(CurrentUser.myMeal);
                                        user1.setIdUser(CurrentUser.idUser);
                                        user1.setMealFavorite(CurrentUser.mealFavorite);
                                        user1.setPassword(CurrentUser.password);
                                        user1.setEmail(CurrentUser.email);
                                        user.getRef().setValue(user1);
                                    }
                                    onBackPressed();
                                    finish();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });

                dialog.dismiss();
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }
}