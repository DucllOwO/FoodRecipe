package com.nmuddd.foodrecipeapp.view.category;

import com.nmuddd.foodrecipeapp.model.Meal;

import java.util.List;

public interface CategoryView {
    void showLoading();
    void hideLoading();
    void setMeals(List<Meal> meals);
    void onErrorLoading(String message);
    void displayToast(String messsage);
}
