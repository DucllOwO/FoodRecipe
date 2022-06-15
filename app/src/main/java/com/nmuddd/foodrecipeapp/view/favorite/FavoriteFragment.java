package com.nmuddd.foodrecipeapp.view.favorite;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.nmuddd.foodrecipeapp.R;
import com.nmuddd.foodrecipeapp.Utils.ConnectionReceiver;
import com.nmuddd.foodrecipeapp.adapter.RecyclerViewMealFavoriteAdapter;
import com.nmuddd.foodrecipeapp.model.Meal;
import com.nmuddd.foodrecipeapp.view.LostInternetConnectionActivity;
import com.nmuddd.foodrecipeapp.view.detail.DetailActivity;
import com.nmuddd.foodrecipeapp.view.home.HomeFragment;

import java.util.List;

public class FavoriteFragment extends Fragment implements FavoriteView, ConnectionReceiver.ReceiverListener {

    private View view;
    RecyclerView recyclerView;
    Toolbar toolbar;
    FavoriteView favoriteView;
    SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_farvorite, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        toolbar = view.findViewById(R.id.toolbar);
        swipeRefreshLayout = view.findViewById(R.id.swiperefresh);


        FavoritePresenter favoritePresenter = new FavoritePresenter(this);
        if (checkConnection())
            favoritePresenter.getMealFavorite();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                favoritePresenter.getMealFavorite();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setClipToPadding(false);
        return view;
    }

    @Override
    public void setFavoriteList(List<Meal> mealFavorite) {
            RecyclerViewMealFavoriteAdapter adapter = new RecyclerViewMealFavoriteAdapter(getContext(), mealFavorite);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
            adapter.notifyDataSetChanged();


            adapter.setOnItemClickListener((view, position) -> {
                if (checkConnection()) {
                    TextView strMealName = view.findViewById(R.id.mealName);
                    Intent intent = new Intent(getContext(), DetailActivity.class);
                    intent.putExtra(HomeFragment.EXTRA_DETAIL, strMealName.getText().toString());
                    startActivity(intent);
                }
            });
    }

    @Override
    public void displayToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    private Boolean checkConnection() {

        // initialize intent filter
        IntentFilter intentFilter = new IntentFilter();

        // add action
        intentFilter.addAction("android.new.conn.CONNECTIVITY_CHANGE");

        // register receiver
        getContext().registerReceiver(new ConnectionReceiver(), intentFilter);

        // Initialize listener
        ConnectionReceiver.Listener = this;

        // Initialize connectivity manager
        ConnectivityManager manager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        // Initialize network info
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        // get connection status
        boolean isConnected = networkInfo != null && networkInfo.isConnectedOrConnecting();

        if (!isConnected)
            startActivityLostInternetConnection(isConnected); 
    return isConnected;}

    private void startActivityLostInternetConnection(boolean isConnected) {
        Intent intent = new Intent(getContext(), LostInternetConnectionActivity.class);
        startActivity(intent);
    }

    @Override
    public void onNetworkChange(boolean isConnected) {

    }
}
