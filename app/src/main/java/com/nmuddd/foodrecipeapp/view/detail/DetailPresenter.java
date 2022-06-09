package com.nmuddd.foodrecipeapp.view.detail;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.nmuddd.foodrecipeapp.Utils.Utils;
import com.nmuddd.foodrecipeapp.model.Meals;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailPresenter {
    private DetailView view;
    private Context context;
    public DetailPresenter(DetailView view, Context applicationContext) {
        this.view = view;
        this.context = applicationContext;
    }

    void getMealById(String mealName) {
            view.showLoading();

            Utils.getApi().getMealByName(mealName)
                    .enqueue(new Callback<Meals>() {
                        @Override
                        public void onResponse(@NonNull Call<Meals> call,@NonNull Response<Meals> response) {
                            view.hideLoading();
                            if (response.isSuccessful() && response.body() != null) {
                                try {
                                    view.setMeal(response.body().getMeals().get(0));
                                } catch (Exception e) {
                                    Toast.makeText(context, "Món ăn" + mealName + " không tồn tại", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                view.onErrorLoading(response.message());
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<Meals> call,@NonNull Throwable t) {
                            view.hideLoading();
                            view.onErrorLoading(t.getLocalizedMessage());
                        }
                    });
    }
}
