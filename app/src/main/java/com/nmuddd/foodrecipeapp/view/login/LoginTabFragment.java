package com.nmuddd.foodrecipeapp.view.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nmuddd.foodrecipeapp.R;
import com.nmuddd.foodrecipeapp.Utils.CurrentUser;
import com.nmuddd.foodrecipeapp.database.Firebase;
import com.nmuddd.foodrecipeapp.model.User;
import com.nmuddd.foodrecipeapp.view.MainActivity;

import java.util.ArrayList;

public class LoginTabFragment extends Fragment implements View.OnClickListener {

    EditText email_et;
    EditText password_et;
    TextView forgot_password;
    Button login_button;
    float v = 0;
    Firebase firebase;
    private FirebaseAuth mAuth;
    FragmentManager fragmentManager;
    ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_login_tab, container, false);
        firebase = new Firebase();
        progressBar = root.findViewById(R.id.progress_bar_login);
        email_et = root.findViewById(R.id.email_login_edit_text);
        password_et = root.findViewById(R.id.password_login_edit_text);
        forgot_password = root.findViewById(R.id.forgot_password);
        login_button = root.findViewById(R.id.button_login);
        fragmentManager = getParentFragmentManager();

        forgot_password.setOnClickListener(this);

        setupLogin();

        return root;
    }

    private void setupLogin() {
        mAuth = FirebaseAuth.getInstance();

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                if (email_et.getText().toString() == "" || password_et.getText().toString() == "")
                    Toast.makeText(getContext(), "Input information!!!", Toast.LENGTH_SHORT).show();
                else
                    mAuth.signInWithEmailAndPassword(email_et.getText().toString(), password_et.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful())
                                        if (mAuth.getCurrentUser().isEmailVerified()) {
                                            Query query = firebase.dbReference.child(firebase.tableNameUser).orderByChild("idUser")
                                                    .equalTo(mAuth.getCurrentUser().getUid());
                                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    if (snapshot.exists()) {
                                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                            getCurrentUserData(dataSnapshot);
                                                            Toast.makeText(getContext(), "Login Successfully", Toast.LENGTH_SHORT).show();
                                                            progressBar.setVisibility(View.GONE);
                                                            startActivity(new Intent(getContext(), MainActivity.class));
                                                        }
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {
                                                    Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                        } else
                                            Toast.makeText(getContext(), "Please verify your email address", Toast.LENGTH_LONG).show();
                                    else
                                        Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
            }
        });
    }

    private void getCurrentUserData(DataSnapshot dataSnapshot) {
        CurrentUser.idUser = dataSnapshot.getValue(User.class).getIdUser();
        CurrentUser.email = dataSnapshot.getValue(User.class).getEmail();
        CurrentUser.password = dataSnapshot.getValue(User.class).getPassword();
        CurrentUser.avatar = dataSnapshot.getValue(User.class).getAvatar();
        if (dataSnapshot.getValue(User.class).getMealFavorite() != null)
            CurrentUser.mealFavorite = dataSnapshot.getValue(User.class).getMealFavorite();
        else
            CurrentUser.mealFavorite = new ArrayList<>();
        if (dataSnapshot.getValue(User.class).getMyMeal() != null)
            CurrentUser.myMeal = dataSnapshot.getValue(User.class).getMyMeal();
        else
            CurrentUser.myMeal = new ArrayList<>();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.forgot_password:
                ForgotPasswordFragment forgotPasswordFragment = new ForgotPasswordFragment();

                FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
                fragmentTransaction.setReorderingAllowed(true).replace(R.id.fragment_container_login_forgot, forgotPasswordFragment);
                fragmentTransaction.addToBackStack(null);

                fragmentTransaction.commit();
                break;
            default:
                break;
        }
    }
}