package com.nmuddd.foodrecipeapp.view.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.nmuddd.foodrecipeapp.R;

public class LoginTabFragment extends Fragment {

    EditText email_et;
    EditText password_et;
    TextView forgot_password;
    Button login_button;
    float v = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_login_tab, container, false);

        email_et = root.findViewById(R.id.email_login_edit_text);
        password_et = root.findViewById(R.id.password_login_edit_text);
        forgot_password = root.findViewById(R.id.forgot_password);
        login_button = root.findViewById(R.id.button_login);

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
        login_button.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();

        //setupAnimate();
        // Inflate the layout for this fragment
        return root;
    }
}