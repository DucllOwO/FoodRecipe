package com.nmuddd.foodrecipeapp.view.login;

import android.content.Intent;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.nmuddd.foodrecipeapp.R;
import com.nmuddd.foodrecipeapp.view.home.HomeActivity;

public class LoginTabFragment extends Fragment {

    EditText email_et;
    EditText password_et;
    TextView forgot_password;
    Button login_button;
    float v = 0;

    private FirebaseAuth mAuth;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_login_tab, container, false);

        email_et = root.findViewById(R.id.email_login_edit_text);
        password_et = root.findViewById(R.id.password_login_edit_text);
        forgot_password = root.findViewById(R.id.forgot_password);
        login_button = root.findViewById(R.id.button_login);/*

        email_et.setTranslationY(300);
        password_et.setTranslationY(300);
        forgot_password.setTranslationY(300);
        login_button.setTranslationY(300);

        email_et.setAlpha(v);
        password_et.setAlpha(v);
        forgot_password.setAlpha(v);
        login_button.setAlpha(v);

        email_et.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();
        password_et.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();
        forgot_password.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();
        login_button.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();*/

        setupLogin();

        //setupAnimate();
        // Inflate the layout for this fragment
        return root;
    }

    private void setupLogin() {
        mAuth = FirebaseAuth.getInstance();

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email_et.getText().toString() == "" || password_et.getText().toString() == "")
                    Toast.makeText(getContext(), "Input information!!!", Toast.LENGTH_SHORT).show();
                else
                    mAuth.signInWithEmailAndPassword(email_et.getText().toString(), password_et.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful())
                                        if (mAuth.getCurrentUser().isEmailVerified())
                                            startActivity(new Intent(getContext(), HomeActivity.class));
                                        else
                                            Toast.makeText(getContext(), "Please verify your email address", Toast.LENGTH_SHORT).show();
                                    else
                                        Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
            }
        });
    }
}