package com.nmuddd.foodrecipeapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

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
    private String[] idMealFavorite;

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

    public String[] getIdMealFavorite() {
        return idMealFavorite;
    }

    public void setIdMealFavorite(String[] idMealFavorite) {
        this.idMealFavorite = idMealFavorite;
    }
}
