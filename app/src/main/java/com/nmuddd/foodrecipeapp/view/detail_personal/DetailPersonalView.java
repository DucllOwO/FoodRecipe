package com.nmuddd.foodrecipeapp.view.detail_personal;

import com.nmuddd.foodrecipeapp.model.Meal;

public interface DetailPersonalView {
    void showLoading();

    void hideLoading();

    void setMeal(Meal meal);

    void onErrorLoading(String message);

    void displayToast(String message);

    void displayAlertYESNO(String message);
}
