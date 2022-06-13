package com.nmuddd.foodrecipeapp.view.detail;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nmuddd.foodrecipeapp.database.Firebase;
import com.nmuddd.foodrecipeapp.model.Meal;

public class DetailPresenter {
    private DetailView view;
    private Context context;
    Firebase firebase;

    public DetailPresenter(DetailView view, Context applicationContext) {
        this.view = view;
        this.context = applicationContext;
        firebase = new Firebase();
    }

    void getMealById(String mealName) {
        view.showLoading();

        Query query = firebase.dbReference.child(firebase.tableNameMeal).orderByChild("strMeal")
                .equalTo(mealName);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        view.hideLoading();
                        view.setMeal(dataSnapshot.getValue(Meal.class));
                    }
                } else {
                    view.displayToast("Get meal on database error!!!");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                view.displayToast("Get meal on database error!!!");
            }
        });
    }
}
