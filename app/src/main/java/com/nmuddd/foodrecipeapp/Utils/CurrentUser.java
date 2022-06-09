package com.nmuddd.foodrecipeapp.Utils;

import com.google.firebase.auth.FirebaseUser;

public class CurrentUser {
    public static FirebaseUser firebaseUser;

    public CurrentUser(FirebaseUser firebaseUser1) {
        firebaseUser = firebaseUser1;
    }

    public CurrentUser() {
    }
}
