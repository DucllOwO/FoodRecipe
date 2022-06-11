package com.nmuddd.foodrecipeapp.view.login;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nmuddd.foodrecipeapp.R;

public class ContainerFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_container, container, false);

        LoginTabFragment loginTabFragment = new LoginTabFragment();

        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
        fragmentTransaction.setReorderingAllowed(true).replace(R.id.fragment_container_login_forgot, loginTabFragment);
        fragmentTransaction.addToBackStack(null);

        fragmentTransaction.commit();

        return view;
    }
}