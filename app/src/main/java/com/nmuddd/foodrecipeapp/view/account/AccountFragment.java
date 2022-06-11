package com.nmuddd.foodrecipeapp.view.account;

import static android.app.Activity.RESULT_OK;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.nmuddd.foodrecipeapp.R;
import com.nmuddd.foodrecipeapp.Utils.CurrentUser;
import com.nmuddd.foodrecipeapp.database.Firebase;
import com.nmuddd.foodrecipeapp.database.FirebaseStorageInstance;
import com.nmuddd.foodrecipeapp.model.Meal;
import com.nmuddd.foodrecipeapp.model.User;
import com.nmuddd.foodrecipeapp.view.login.LoginActivity;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AccountFragment extends Fragment implements View.OnClickListener {
    private static final int PICK_IMAGE_REQUEST = 234;
    private View view;
    ImageView avatar;
    Button upload_btn;
    Button change_password_btn;
    Button logout_btn;
    FirebaseStorageInstance firebaseStorageInstance;
    Firebase firebase;
    private Uri filePath;
    ProgressBar progressBar;
    TextView emailTV;
    TextView numOfFavoriteTV;
    TextView numofYourRecipeTV;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_account, container, false);
        firebaseStorageInstance = new FirebaseStorageInstance();
        firebase = new Firebase();

        numofYourRecipeTV = view.findViewById(R.id.your_meal_amount_tv);
        numOfFavoriteTV = view.findViewById(R.id.favorite_amount_tv);
        emailTV = view.findViewById(R.id.email_account_tv);
        progressBar = view.findViewById(R.id.progress_bar_account);
        avatar = view.findViewById(R.id.user_avatar);
        upload_btn = view.findViewById(R.id.upload_iamge);
        change_password_btn = view.findViewById(R.id.change_password_btn);
        logout_btn = view.findViewById(R.id.logout);

        upload_btn.setOnClickListener(this);
        change_password_btn.setOnClickListener(this);
        logout_btn.setOnClickListener(this);

        loadImage();

        numofYourRecipeTV.setText(numofYourRecipeTV.getText() + Integer.toString(CurrentUser.myMeal.size()));
        numOfFavoriteTV.setText(numOfFavoriteTV.getText() + Integer.toString(CurrentUser.mealFavorite.size()));
        emailTV.setText(emailTV.getText() + CurrentUser.email);


        return view;
    }

    private void loadImage() {
        progressBar.setVisibility(View.VISIBLE);
        Query query = firebase.dbReference.child(firebase.tableNameUser).orderByChild("idUser").equalTo(CurrentUser.idUser);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot user : snapshot.getChildren()) {

                        if (user.getValue(User.class).getAvatar() != null) {
                            Picasso.get().load(user.getValue(User.class).getAvatar()).placeholder(R.drawable.shadow_bottom_to_top).into(avatar);
                        }
                        progressBar.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.upload_iamge:
                showFileChooser();
                break;
            case R.id.change_password_btn:
                displayAlertDialog();
                break;
            case R.id.logout:
                Logout();
                break;
            default:
                break;
        }
    }

    private void uploadImageToStorage() {
        deleteOldImage();
        uploadImageToStorageAndDatabase();
    }

    private void deleteOldImage() {
        StorageReference storageReference = firebaseStorageInstance.firebaseStorage
                .child(FirebaseStorageInstance.STORAGE_PATH_UPLOADS + CurrentUser.idUser + "." + getFileExtension(filePath));
        storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Upload error", Toast.LENGTH_SHORT);
            }
        });
    }

    private void uploadImageToStorageAndDatabase() {
        if (filePath != null) {
            //displaying progress dialog while image is uploading
            final ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setTitle("Uploading");
            progressDialog.show();

            //getting the storage reference

            StorageReference storageReference = firebaseStorageInstance.firebaseStorage
                    .child(FirebaseStorageInstance.STORAGE_PATH_UPLOADS + CurrentUser.idUser + "." + getFileExtension(filePath));


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
                                                for (DataSnapshot userSnapshot: snapshot.getChildren()) {
                                                    userSnapshot.getRef().setValue(setUser(uri.toString()));
                                                    CurrentUser.avatar = userSnapshot.getValue(User.class).getAvatar();
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                    Toast.makeText(getContext(), "Upload Successfully", Toast.LENGTH_SHORT);
                                }
                            });
                            //displaying success toast
                            Toast.makeText(getContext(), "File Uploaded ", Toast.LENGTH_LONG).show();


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
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
            Toast.makeText(getContext(), "Please select a image file", Toast.LENGTH_LONG).show();
        }
    }

    private User setUser(String avatarURL) {
        User user = new User();
        user.setIdUser(CurrentUser.idUser);
        user.setEmail(CurrentUser.email);
        user.setPassword(CurrentUser.password);
        user.setAvatar(avatarURL);
        if (CurrentUser.mealFavorite != null) {
            user.setMealFavorite(CurrentUser.mealFavorite);
            CurrentUser.mealFavorite = CurrentUser.mealFavorite;
        }
        else {
            List<Meal> meals = new ArrayList<>();
            user.setMealFavorite(meals);
        }

        if (CurrentUser.myMeal != null) {
            user.setMyMeal(CurrentUser.myMeal);
            CurrentUser.mealFavorite = CurrentUser.myMeal;
        }
        else {
            List<Meal> meals = new ArrayList<>();
            user.setMyMeal(meals);
        }
        return user;
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    public String getFileExtension(Uri uri) {
        ContentResolver cR = getActivity().getApplicationContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getApplicationContext().getContentResolver(), filePath);
                avatar.setImageBitmap(bitmap);
                uploadImageToStorage();
            } catch (IOException e) {
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT);
            }
        }
    }

    private void Logout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle("Confirm");
        builder.setMessage("Are you sure want to log out?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                FirebaseAuth.getInstance().signOut();
                Intent intentToLogin = new Intent(getActivity(), LoginActivity.class);
                startActivity(intentToLogin);
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

    private void displayAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle("Confirm");
        builder.setMessage("Are you sure want to change password?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                String emailAddress = CurrentUser.email;

                auth.sendPasswordResetEmail(emailAddress)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());

                                    builder1.setTitle("Infor");
                                    builder1.setMessage("Please check your email to change password");

                                    builder1.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });

                                    AlertDialog alert1 = builder1.create();
                                    alert1.show();
                                }
                                else {
                                    Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_LONG);
                                }
                            }
                        });

                dialog.dismiss();
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(), "Please check your email to change password", Toast.LENGTH_LONG);
                // Do nothing
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }
}
