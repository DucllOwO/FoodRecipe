package com.nmuddd.foodrecipeapp.view.favorite;

import com.nmuddd.foodrecipeapp.model.Meal;

import java.util.List;

public interface FavoriteView {
    void setFavoriteList(List<Meal> mealFavorite);

    void displayToast(String message);
}
