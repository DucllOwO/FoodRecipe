package com.nmuddd.foodrecipeapp.view.login;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.nmuddd.foodrecipeapp.R;
import com.nmuddd.foodrecipeapp.adapter.LoginAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.login_viewPager)
    ViewPager viewPager;
    @BindView(R.id.tab_layout_login)
    TabLayout tabLayout;

    @BindView(R.id.splash_background)
    ImageView imageView;

    @BindView(R.id.lottie)
    LottieAnimationView lottie;

    @BindView(R.id.fab_google)
    FloatingActionButton google_button;
    float v = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        imageView.animate().translationY(-1800).alpha(1).setDuration(3000).setStartDelay(4000).start();
        lottie.animate().translationY(1400).alpha(1).setDuration(3000).setStartDelay(4000).start();

        tabLayout.addTab(tabLayout.newTab().setText("Login"));
        tabLayout.addTab(tabLayout.newTab().setText("Signup"));

        LoginAdapter loginAdapter = new LoginAdapter(getSupportFragmentManager(), this, tabLayout.getTabCount());
        //TabLayoutMediator()

        viewPager.setAdapter(loginAdapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setupWithViewPager(viewPager);




    }

    private void startLogin() {
    }
}