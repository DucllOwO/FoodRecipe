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

public class SignupTabFragment extends Fragment {
    EditText email_et;
    EditText password_et;
    TextView repassword_et;
    Button signup_button;
    float v = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_signup_tab, container, false);

        email_et = root.findViewById(R.id.email_signup_edit_text);
        password_et = root.findViewById(R.id.password_signup_edit_text);
        repassword_et = root.findViewById(R.id.repassword_edit_text);
        signup_button = root.findViewById(R.id.button_signup);

        email_et.setTranslationY(300);
        password_et.setTranslationY(300);
        repassword_et.setTranslationY(300);
        signup_button.setTranslationY(300);

        email_et.setAlpha(v);
        password_et.setAlpha(v);
        repassword_et.setAlpha(v);
        signup_button.setAlpha(v);

        email_et.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();
        password_et.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();
        repassword_et.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();
        signup_button.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();

        //setupAnimate();

        // Inflate the layout for this fragment
        return root;
    }
}