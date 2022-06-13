package com.nmuddd.foodrecipeapp.view.home;

import com.nmuddd.foodrecipeapp.model.Category;
import com.nmuddd.foodrecipeapp.model.Meal;

import java.util.List;

public interface HomeView {
    void showLoading();

    void hideLoading();

    void setMeal(List<Meal> meal);

    void setCategory(List<Category> category);

    void setMealSearchItem(List<Meal> meal);

    void onErrorLoading(String message);

    void displayToast(String message);
}
