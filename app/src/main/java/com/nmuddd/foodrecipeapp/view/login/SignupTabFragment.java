package com.nmuddd.foodrecipeapp.view.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.SuccessContinuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nmuddd.foodrecipeapp.R;
import com.nmuddd.foodrecipeapp.database.Firebase;
import com.nmuddd.foodrecipeapp.model.User;

public class SignupTabFragment extends Fragment {
    EditText email_et;
    EditText password_et;
    TextView repassword_et;
    Button signup_button;
    Firebase firebase;
    private FirebaseAuth mAuth;
    float v = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_signup_tab, container, false);

        email_et = root.findViewById(R.id.email_signup_edit_text);
        password_et = root.findViewById(R.id.password_signup_edit_text);
        repassword_et = root.findViewById(R.id.repassword_edit_text);
        signup_button = root.findViewById(R.id.button_signup);
        firebase = new Firebase();
        /*email_et.setTranslationY(300);
        password_et.setTranslationY(300);
        repassword_et.setTranslationY(300);
        signup_button.setTranslationY(300);

        email_et.setAlpha(v);
        password_et.setAlpha(v);
        repassword_et.setAlpha(v);
        signup_button.setAlpha(v);

        email_et.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();
        password_et.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();
        repassword_et.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();
        signup_button.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();*/
        setupSignup();



        // Inflate the layout for this fragment
        return root;
    }

    private void setupSignup() {
        mAuth = FirebaseAuth.getInstance();
        //setupAnimate();
        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email_et.getText().toString() == "" || password_et.getText().toString() == "" ||
                        repassword_et.getText().toString() == "")
                    Toast.makeText(getContext(), "Input information!!!", Toast.LENGTH_SHORT).show();
                else {
                    if (!password_et.getText().toString().equals(repassword_et.getText().toString()))
                        Toast.makeText(getContext(), "Repassword is different!!!", Toast.LENGTH_SHORT).show();
                    else {
                        mAuth.createUserWithEmailAndPassword(email_et.getText().toString(), password_et.getText().toString())
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            mAuth.getCurrentUser().sendEmailVerification()
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        User user = new User();
                                                        user.setIdUser(mAuth.getUid());
                                                        user.setEmail(email_et.getText().toString());
                                                        user.setPassword(password_et.getText().toString());
                                                        user.setIdMealFavorite(null);
                                                        Task<Void> taskAddUser = firebase.dbReference.child(firebase.tableNameUser).push().setValue(user);
                                                        taskAddUser.addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                Toast.makeText(getContext(), "Resistered successfully. Please check your email for activation", Toast.LENGTH_SHORT).show();
                                                                email_et.setText("");
                                                                password_et.setText("");
                                                                repassword_et.setText("");
                                                            }
                                                        });
                                                    } else {
                                                        Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                        } else
                                            Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                                    }
                                });

                    }

                }

            }
        });
    }
}