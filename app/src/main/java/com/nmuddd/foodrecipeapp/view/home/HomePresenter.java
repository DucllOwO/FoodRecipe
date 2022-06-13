package com.nmuddd.foodrecipeapp.view.home;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nmuddd.foodrecipeapp.database.Firebase;
import com.nmuddd.foodrecipeapp.model.Category;
import com.nmuddd.foodrecipeapp.model.Meal;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

class HomePresenter {

    Firebase firebase;
    private HomeView view;

    public HomePresenter(HomeView view) {
        firebase = new Firebase();
        this.view = view;
    }

    // get random meal
    void getRandomMeals() {

        view.showLoading();

        Query query = firebase.dbReference.child(firebase.tableNameMeal);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    view.hideLoading();
                    Random rand = new Random();
                    int n = rand.nextInt(20);
                    int i = 0;
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        if (i == n) {
                            List<Meal> meals = new ArrayList<>();
                            meals.add(dataSnapshot.getValue(Meal.class));
                            view.setMeal(meals);
                            break;
                        } else
                            i++;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                view.displayToast("Get random meal error!!!");
            }
        });
    }

    void getMealsByName(String mealName) {
        Query query = firebase.dbReference.child(firebase.tableNameMeal);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    List<Meal> mealTemp = new ArrayList<>();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        if (Pattern.compile(Pattern.quote(mealName), Pattern.CASE_INSENSITIVE).matcher(dataSnapshot.getValue(Meal.class).getStrMeal()).find())
                            mealTemp.add(dataSnapshot.getValue(Meal.class));
                    }
                    view.setMealSearchItem(mealTemp);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                view.displayToast("Get search meal error!!!");
            }
        });
    }

    void getCategories() {

        view.showLoading();

        Query query = firebase.dbReference.child(firebase.tableNameCategory);
        List<Category> categories = new ArrayList<>();
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    view.hideLoading();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        categories.add(dataSnapshot.getValue(Category.class));
                    }
                    view.setCategory(categories);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                view.displayToast("Get category error!!!");
            }
        });

    }

}

