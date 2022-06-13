package com.nmuddd.foodrecipeapp.view.login;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.nmuddd.foodrecipeapp.R;
import com.nmuddd.foodrecipeapp.Utils.ConnectionReceiver;
import com.nmuddd.foodrecipeapp.database.Firebase;
import com.nmuddd.foodrecipeapp.model.User;
import com.nmuddd.foodrecipeapp.view.LostInternetConnectionActivity;

public class SignupTabFragment extends Fragment implements ConnectionReceiver.ReceiverListener {
    EditText email_et;
    EditText password_et;
    TextView repassword_et;
    Button signup_button;
    Firebase firebase;
    private FirebaseAuth mAuth;
    ProgressBar progressBar;
    float v = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_signup_tab, container, false);

        progressBar = root.findViewById(R.id.progress_bar_signup);
        email_et = root.findViewById(R.id.email_signup_edit_text);
        password_et = root.findViewById(R.id.password_signup_edit_text);
        repassword_et = root.findViewById(R.id.repassword_edit_text);
        signup_button = root.findViewById(R.id.button_signup);
        firebase = new Firebase();

        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupSignup();
            }
        });


        // Inflate the layout for this fragment
        return root;
    }

    private void setupSignup() {
        if (checkConnection()) {
            try {
                mAuth = FirebaseAuth.getInstance();
                //setupAnimate();
                signup_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (email_et.getText().toString().equals("") || password_et.getText().toString().equals("") ||
                                repassword_et.getText().toString().equals(""))
                            Toast.makeText(getContext(), "Input information!!!", Toast.LENGTH_SHORT).show();
                        else {
                            if (!password_et.getText().toString().equals(repassword_et.getText().toString()))
                                Toast.makeText(getContext(), "Repassword is different!!!", Toast.LENGTH_SHORT).show();
                            else {
                                progressBar.setVisibility(View.VISIBLE);
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
                                                                        user.setMealFavorite(null);
                                                                        user.setAvatar("");
                                                                        user.setMyMeal(null);
                                                                        Task<Void> taskAddUser = firebase.dbReference.child(firebase.tableNameUser).push().setValue(user);
                                                                        taskAddUser.addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                progressBar.setVisibility(View.GONE);
                                                                                Toast.makeText(getContext(), "Resistered successfully. Please check your email for activation", Toast.LENGTH_LONG).show();
                                                                                email_et.setText("");
                                                                                password_et.setText("");
                                                                                repassword_et.setText("");
                                                                            }
                                                                        });
                                                                        progressBar.setVisibility(View.GONE);
                                                                    } else {
                                                                        progressBar.setVisibility(View.GONE);
                                                                        Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                                                    }
                                                                }
                                                            });
                                                } else
                                                {
                                                    progressBar.setVisibility(View.GONE);
                                                    Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                                }


                                            }
                                        });

                            }

                        }

                    }
                });
            } catch (Exception e) {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }


    }

    private Boolean checkConnection() {

        // initialize intent filter
        IntentFilter intentFilter = new IntentFilter();

        // add action
        intentFilter.addAction("android.new.conn.CONNECTIVITY_CHANGE");

        // register receiver
        getContext().registerReceiver(new ConnectionReceiver(), intentFilter);

        // Initialize listener
        ConnectionReceiver.Listener = this;

        // Initialize connectivity manager
        ConnectivityManager manager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        // Initialize network info
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        // get connection status
        boolean isConnected = networkInfo != null && networkInfo.isConnectedOrConnecting();

        if (!isConnected)
            startActivityLostInternetConnection(isConnected);
    return isConnected;}

    private void startActivityLostInternetConnection(boolean isConnected) {
        Intent intent = new Intent(getContext(), LostInternetConnectionActivity.class);
        startActivity(intent);
    }

    @Override
    public void onNetworkChange(boolean isConnected) {

    }
}