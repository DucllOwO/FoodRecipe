package com.nmuddd.foodrecipeapp.view;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.nmuddd.foodrecipeapp.R;

import butterknife.BindView;

public class MainActivity extends AppCompatActivity {

//    @BindView(R.id.view_pager2) private ViewPager2 viewPager2;
//    @BindView(R.id.bottom_nav) private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }
}
