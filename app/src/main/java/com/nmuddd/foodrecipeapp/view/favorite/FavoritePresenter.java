package com.nmuddd.foodrecipeapp.view.favorite;

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

import java.util.ArrayList;
import java.util.List;

class FavoritePresenter {
    Firebase firebase;
    private FavoriteView view;
    FirebaseUser firebaseUser;

    public FavoritePresenter(FavoriteView view) {
        firebase = new Firebase();
        this.view = view;
    }

    void getMealFavorite() {
        Query query = firebase.dbReference.child("User").orderByChild("idUser").equalTo(CurrentUser.idUser);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    List<Meal> meals = new ArrayList<>();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        meals = dataSnapshot.getValue(User.class).getMealFavorite();
                    }
                    view.setFavoriteList(meals);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
