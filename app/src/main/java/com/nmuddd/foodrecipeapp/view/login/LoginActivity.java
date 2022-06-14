package com.nmuddd.foodrecipeapp.view.login;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nmuddd.foodrecipeapp.R;
import com.nmuddd.foodrecipeapp.Utils.ConnectionReceiver;
import com.nmuddd.foodrecipeapp.Utils.CurrentUser;
import com.nmuddd.foodrecipeapp.Utils.Utils;
import com.nmuddd.foodrecipeapp.adapter.LoginAdapter;
import com.nmuddd.foodrecipeapp.database.Firebase;
import com.nmuddd.foodrecipeapp.model.User;
import com.nmuddd.foodrecipeapp.view.LostInternetConnectionActivity;
import com.nmuddd.foodrecipeapp.view.MainActivity;
import com.nmuddd.foodrecipeapp.view.edit_meal.EditMealActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity implements ConnectionReceiver.ReceiverListener {
    private static final int RC_SIGN_IN = 123;
    public static final String EXTRA_LOGIN_GOOGLE = "google";
    @BindView(R.id.login_viewPager)
    ViewPager viewPager;
    @BindView(R.id.tab_layout_login)
    TabLayout tabLayout;

    @BindView(R.id.splash_background)
    ImageView imageView;

    @BindView(R.id.lottie)
    LottieAnimationView lottie;

    Button backButton;

    TextView forgotPasswordTV;
    FragmentManager fragmentManager;

    float v = 0;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    private Firebase firebase;
    public static int count_back_click = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) this
                .findViewById(android.R.id.content)).getChildAt(0);

        ButterKnife.bind(this);
        firebase = new Firebase();
        setupUI();
        mAuth = FirebaseAuth.getInstance();

        setupUIHideSoftKeyboard(viewGroup);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private Boolean checkConnection() {

        // initialize intent filter
        IntentFilter intentFilter = new IntentFilter();

        // add action
        intentFilter.addAction("android.new.conn.CONNECTIVITY_CHANGE");

        // register receiver
        registerReceiver(new ConnectionReceiver(), intentFilter);

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
        Intent intent = new Intent(LoginActivity.this, LostInternetConnectionActivity.class);
        startActivity(intent);
    }

    @Override
    public void onNetworkChange(boolean isConnected) {
    }

    public void setupUIHideSoftKeyboard(View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    Utils.hideSoftKeyboard(LoginActivity.this);
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUIHideSoftKeyboard(innerView);
            }
        }
    }


    private void setupUI() {
        imageView.animate().translationY(-3000).alpha(1).setDuration(3000).setStartDelay(4000).start();
        lottie.animate().translationY(1600).alpha(1).setDuration(3000).setStartDelay(4000).start();

        tabLayout.addTab(tabLayout.newTab().setText("Login"));
        tabLayout.addTab(tabLayout.newTab().setText("Signup"));

        LoginAdapter loginAdapter = new LoginAdapter(getSupportFragmentManager(), this, tabLayout.getTabCount());
        //TabLayoutMediator()

        viewPager.setAdapter(loginAdapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (checkConnection()) {
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser != null) {
                getCurrenUser();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }
        }
    }

    private void getCurrenUser() {
        Query query = firebase.dbReference.child(firebase.tableNameUser).orderByChild("idUser")
                .equalTo(mAuth.getCurrentUser().getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        try {
                            getCurrentUserData(dataSnapshot);
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), "Get user fail", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
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

}
