package com.nmuddd.foodrecipeapp.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.nmuddd.foodrecipeapp.view.login.ContainerFragment;
import com.nmuddd.foodrecipeapp.view.login.SignupTabFragment;

public class LoginAdapter extends FragmentPagerAdapter {
    private Context context;
    int totalTabs;

    public LoginAdapter(FragmentManager fm, Context context, int totalTabs) {
        super(fm);

        this.context = context;
        this.totalTabs = totalTabs;
    }

    public LoginAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                ContainerFragment containerFragment = new ContainerFragment();
                return containerFragment;
            case 1:
                SignupTabFragment signupTabFragment = new SignupTabFragment();
                return signupTabFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return totalTabs;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch (position) {
            case 0:
                title = "Login";
                break;
            case 1:
                title = "Signup";
                break;
            default:
                break;
        }
        return title;
    }

}
