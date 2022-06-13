package com.nmuddd.foodrecipeapp.view.detail;


import com.nmuddd.foodrecipeapp.model.Meal;

public interface DetailView {
    void showLoading();

    void hideLoading();

    void setMeal(Meal meal);

    void onErrorLoading(String message);

    void displayToast(String message);
}
