package com.nmuddd.foodrecipeapp.view.personal_list;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nmuddd.foodrecipeapp.Utils.CurrentUser;
import com.nmuddd.foodrecipeapp.database.Firebase;
import com.nmuddd.foodrecipeapp.model.Meal;
import com.nmuddd.foodrecipeapp.model.User;
import com.nmuddd.foodrecipeapp.view.favorite.FavoriteView;

import java.util.ArrayList;
import java.util.List;

public class PersonalListHomePresenter {
    Firebase firebase;
    private PersonalListView view;
    FirebaseUser firebaseUser;

    public PersonalListHomePresenter(PersonalListView view) {
        firebase = new Firebase();
        this.view = view;
    }
}
