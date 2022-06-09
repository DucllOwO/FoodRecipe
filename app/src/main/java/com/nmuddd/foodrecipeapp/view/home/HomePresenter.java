package com.nmuddd.foodrecipeapp.view.home;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.nmuddd.foodrecipeapp.Utils.Utils;
import com.nmuddd.foodrecipeapp.adapter.RecyclerViewSearchItemAdapter;
import com.nmuddd.foodrecipeapp.model.Categories;
import com.nmuddd.foodrecipeapp.model.Meals;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class HomePresenter {

    private HomeView view;

    public HomePresenter(HomeView view) {
        this.view = view;
    }
    // get random meal
    void getRandomMeals() {

        view.showLoading();

        Call<Meals> mealsCall = Utils.getApi().getRandomMeal();
        mealsCall.enqueue(new Callback<Meals>() {
            @Override
            public void onResponse(@NonNull Call<Meals> call, @NonNull Response<Meals> response) {
                view.hideLoading();

                if (response.isSuccessful() && response.body() != null) {

                    view.setMeal(response.body().getMeals());

                }
                else {
                    view.onErrorLoading(response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Meals> call, @NonNull Throwable t) {
                view.hideLoading();
                view.onErrorLoading(t.getLocalizedMessage());
            }
        });
    }
    public static List<Meals.Meal> mealList;

    void getMealsByName(String mealName) {
        Log.i("AAA", "getMealsByName execute");

        Call<Meals> mealsCall = Utils.getApi().getMealByName(mealName);
        mealsCall.enqueue(new Callback<Meals>() {
            @Override
            public void onResponse(@NonNull Call<Meals> call, @NonNull Response<Meals> response) {
                Log.i("AAA", "onResponse execute duoc ne");
                Log.i("AAA", response.body().getMeals().get(0).getStrMeal());
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        //view.setMeal(response.body().getMeals());
                        view.setMealSearchItem(response.body().getMeals());
                    } catch (Exception e) {
                        Log.i("AAA", "onResponse execute exception");
                    }

                }
                else {
                    view.onErrorLoading(response.message());
                }
                Log.i("AAA", "onResponse execute hoan thanh");
            }

            @Override
            public void onFailure(@NonNull Call<Meals> call, @NonNull Throwable t) {
                Log.i("AAA", "OnFailute execute");
                view.onErrorLoading(t.getLocalizedMessage());
            }
        });
    }

    void getCategories() {

        view.showLoading();

        Call<Categories> categoriesCall = Utils.getApi().getCategories();
        categoriesCall.enqueue(new Callback<Categories>() {
            @Override
            public void onResponse(@NonNull Call<Categories> call,
                                   @NonNull Response<Categories> response) {

                view.hideLoading();
                if (response.isSuccessful() && response.body() != null) {

                    view.setCategory(response.body().getCategories());

                }
                else {
                    view.onErrorLoading(response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Categories> call, @NonNull Throwable t) {
                view.hideLoading();
                view.onErrorLoading(t.getLocalizedMessage());
            }
        });
    }



}

