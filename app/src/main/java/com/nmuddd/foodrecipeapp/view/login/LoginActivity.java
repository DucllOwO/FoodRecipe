package com.nmuddd.foodrecipeapp.view.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nmuddd.foodrecipeapp.R;
import com.nmuddd.foodrecipeapp.Utils.CurrentUser;
import com.nmuddd.foodrecipeapp.adapter.LoginAdapter;
import com.nmuddd.foodrecipeapp.database.Firebase;
import com.nmuddd.foodrecipeapp.model.User;
import com.nmuddd.foodrecipeapp.view.MainActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        firebase = new Firebase();
        setupUI();
        mAuth = FirebaseAuth.getInstance();

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

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            getCurrenUser();
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
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
                        getCurrentUserData(dataSnapshot);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
