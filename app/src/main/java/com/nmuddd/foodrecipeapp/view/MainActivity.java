package com.nmuddd.foodrecipeapp.view;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.nmuddd.foodrecipeapp.R;
import com.nmuddd.foodrecipeapp.adapter.ViewPagerMainAdapter;

import butterknife.BindView;

public class MainActivity extends AppCompatActivity {

    private ViewPager2 viewPager2;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager2 = findViewById(R.id.view_pager2);
        bottomNavigationView = findViewById(R.id.bottom_nav);

        ViewPagerMainAdapter viewPagerMainAdapter = new ViewPagerMainAdapter(this);
        viewPager2.setAdapter(viewPagerMainAdapter);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.action_home:
                        viewPager2.setCurrentItem(0);
                        bottomNavigationView.getMenu().findItem(R.id.action_home).setChecked(true);
                        break;
                    case R.id.action_favorite:
                        viewPager2.setCurrentItem(1);
                        bottomNavigationView.getMenu().findItem(R.id.action_favorite).setChecked(true);
                        break;
                    case R.id.action_personal:
                        viewPager2.setCurrentItem(2);
                        bottomNavigationView.getMenu().findItem(R.id.action_personal).setChecked(true);
                        break;
                    case R.id.action_account:
                        viewPager2.setCurrentItem(3);
                        bottomNavigationView.getMenu().findItem(R.id.action_account).setChecked(true);
                        break;
                }

                return true;
            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        bottomNavigationView.getMenu().findItem(R.id.action_home).setChecked(true);
                        break;
                    case 1:
                        bottomNavigationView.getMenu().findItem(R.id.action_favorite).setChecked(true);
                        break;
                    case 2:
                        bottomNavigationView.getMenu().findItem(R.id.action_personal).setChecked(true);
                        break;
                    case 3:
                        bottomNavigationView.getMenu().findItem(R.id.action_account).setChecked(true);
                        break;
                }
            }
        });
    }
}
