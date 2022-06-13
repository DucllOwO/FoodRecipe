package com.nmuddd.foodrecipeapp.view.category;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nmuddd.foodrecipeapp.database.Firebase;
import com.nmuddd.foodrecipeapp.model.Meal;

import java.util.ArrayList;
import java.util.List;

public class CategoryPresenter {
    private CategoryView view;
    Firebase firebase;

    public CategoryPresenter(CategoryView view) {
        this.view = view;
        firebase = new Firebase();
    }

    void getMealByCategory(String category) {

        view.showLoading();

        Query query = firebase.dbReference.child(firebase.tableNameMeal).orderByChild("strCategory")
                .equalTo(category);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    view.hideLoading();
                    List<Meal> meals = new ArrayList<>();
                    for (DataSnapshot meal : snapshot.getChildren()) {
                        meals.add(meal.getValue(Meal.class));
                    }
                    view.setMeals(meals);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                view.displayToast(error.getMessage());
            }
        });

    }
}
