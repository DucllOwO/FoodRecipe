package com.nmuddd.foodrecipeapp.database;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Firebase {
    private String DatabaseURLScheme = "https://food-recipe-app-374eb-default-rtdb.asia-southeast1.firebasedatabase.app";
    public final DatabaseReference dbReference = FirebaseDatabase.getInstance(DatabaseURLScheme).getReference();
    public final String tableNameMeal = "Meal";
    public final String tableNameCategory = "Category";
    public final String tableNameUser = "User";
}
