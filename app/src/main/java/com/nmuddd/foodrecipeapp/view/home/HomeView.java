package com.nmuddd.foodrecipeapp.view.home;

import com.nmuddd.foodrecipeapp.model.Categories;
import com.nmuddd.foodrecipeapp.model.Meals;

import java.util.List;

public interface HomeView {
    void showLoading();
    void hideLoading();
    void setMeal(List<Meals.Meal> meal);
    void setCategory(List<Categories.Category> category);
    void setMealSearchItem(List<Meals.Meal> meal);
    void onErrorLoading(String message);
}
