package com.nmuddd.foodrecipeapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class User implements Serializable {
    @SerializedName("avatar")
    @Expose
    private String avatar;
    @SerializedName("idUser")
    @Expose
    private String idUser;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("mealFavorite")
    @Expose
    private List<Meal> mealFavorite;
    @SerializedName("myMeal")
    @Expose
    private List<Meal> myMeal;


    public User() {
    }

    public User(String idUser, String email, String password, List<Meal> mealFavorite) {
        this.idUser = idUser;
        this.email = email;
        this.password = password;
        this.mealFavorite = mealFavorite;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Meal> getMealFavorite() {
        return mealFavorite;
    }

    public void setMealFavorite(List<Meal> mealFavorite) {
        this.mealFavorite = mealFavorite;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public List<Meal> getMyMeal() {
        return myMeal;
    }

    public void setMyMeal(List<Meal> myMeal) {
        this.myMeal = myMeal;
    }
}

