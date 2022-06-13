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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.nmuddd.foodrecipeapp.R;
import com.nmuddd.foodrecipeapp.Utils.ConnectionReceiver;
import com.nmuddd.foodrecipeapp.view.LostInternetConnectionActivity;

public class ForgotPasswordFragment extends Fragment implements View.OnClickListener, ConnectionReceiver.ReceiverListener {
    Button backButton;
    FragmentManager fragmentManager;
    Button forgot_button;
    EditText email;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forgot_password, container, false);

        fragmentManager = getParentFragmentManager();
        backButton = view.findViewById(R.id.back_forgot_button);
        forgot_button = view.findViewById(R.id.button_forgot);
        email = view.findViewById(R.id.email_forgot_edit_text);

        forgot_button.setOnClickListener(this);

        backButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_forgot_button:
                if (checkConnection()) {
                    LoginTabFragment loginTabFragment = new LoginTabFragment();
                    FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
                    fragmentTransaction.setReorderingAllowed(true).replace(R.id.fragment_container_login_forgot, loginTabFragment);
                    fragmentTransaction.addToBackStack(null);

                    fragmentTransaction.commit();
                }
                break;
            case R.id.button_forgot:
               if (checkConnection()) {
                   FirebaseAuth auth = FirebaseAuth.getInstance();
                   String emailAddress = email.getText().toString();

                   auth.sendPasswordResetEmail(emailAddress)
                           .addOnCompleteListener(new OnCompleteListener<Void>() {
                               @Override
                               public void onComplete(@NonNull Task<Void> task) {
                                   if (task.isSuccessful()) {
                                       Toast.makeText(getContext(), "Please check your email", Toast.LENGTH_LONG);
                                   } else {
                                       Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_LONG);
                                   }
                               }
                           });
               }
            default:
                break;
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