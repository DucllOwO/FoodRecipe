package com.nmuddd.foodrecipeapp.Utils;

import com.google.firebase.auth.FirebaseUser;
import com.nmuddd.foodrecipeapp.model.Meal;

import java.util.List;

public class CurrentUser {
    public static String idUser;
    public static String email;
    public static String password;
    public static List<String> strMealFavorite;
    public static List<Meal> mealFavorite;
    public CurrentUser() {

    }
}
