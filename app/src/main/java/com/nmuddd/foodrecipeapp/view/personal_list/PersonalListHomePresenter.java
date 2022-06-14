package com.nmuddd.foodrecipeapp.view.personal_list;

import com.google.firebase.auth.FirebaseUser;
import com.nmuddd.foodrecipeapp.database.Firebase;

public class PersonalListHomePresenter {
    Firebase firebase;
    private PersonalListView view;
    FirebaseUser firebaseUser;

    public PersonalListHomePresenter(PersonalListView view) {
        firebase = new Firebase();
        this.view = view;
    }
}
