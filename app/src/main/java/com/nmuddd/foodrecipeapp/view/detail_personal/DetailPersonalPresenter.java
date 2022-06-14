package com.nmuddd.foodrecipeapp.view.detail_personal;

import com.nmuddd.foodrecipeapp.database.Firebase;

public class DetailPersonalPresenter {
    Firebase firebase;
    private DetailPersonalView view;

    public DetailPersonalPresenter(DetailPersonalView view) {
        firebase = new Firebase();
        this.view = view;
    }
}
