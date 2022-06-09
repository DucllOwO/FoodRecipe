package com.nmuddd.foodrecipeapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {
    @SerializedName("idUser")
    @Expose
    private String idUser;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("idMealFavorite")
    @Expose
    private List<String> idMealFavorite;

    public User() {
    }

    public User(String idUser, String email, String password, List<String> idMealFavorite) {
        this.idUser = idUser;
        this.email = email;
        this.password = password;
        this.idMealFavorite = idMealFavorite;
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

    public List<String> getIdMealFavorite() {
        return idMealFavorite;
    }

    public void setIdMealFavorite(List<String> idMealFavorite) {
        this.idMealFavorite = idMealFavorite;
    }
}
