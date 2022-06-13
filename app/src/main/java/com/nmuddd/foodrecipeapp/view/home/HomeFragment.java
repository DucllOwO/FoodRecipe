package com.nmuddd.foodrecipeapp.view.home;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.nmuddd.foodrecipeapp.R;
import com.nmuddd.foodrecipeapp.Utils.ConnectionReceiver;
import com.nmuddd.foodrecipeapp.Utils.Utils;
import com.nmuddd.foodrecipeapp.adapter.RecyclerViewHomeAdapter;
import com.nmuddd.foodrecipeapp.adapter.RecyclerViewSearchItemAdapter;
import com.nmuddd.foodrecipeapp.adapter.ViewPagerHeaderAdapter;
import com.nmuddd.foodrecipeapp.model.Category;
import com.nmuddd.foodrecipeapp.model.Meal;
import com.nmuddd.foodrecipeapp.view.LostInternetConnectionActivity;
import com.nmuddd.foodrecipeapp.view.category.CategoryActivity;
import com.nmuddd.foodrecipeapp.view.detail.DetailActivity;
import com.nmuddd.foodrecipeapp.view.login.LoginActivity;

import java.io.Serializable;
import java.util.List;


public class HomeFragment extends Fragment implements HomeView, ConnectionReceiver.ReceiverListener {
    private View view;

    public static final String EXTRA_CATEGORY = "category";
    public static final String EXTRA_POSITION = "position";
    public static final String EXTRA_DETAIL = "detail";

    ViewPager viewPagerMeal;
    RecyclerView recyclerViewCategory;
    ImageView favorite;
    RecyclerView recyclerSearchItem;
    HomePresenter presenter;
    SearchView searchView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);

        viewPagerMeal = view.findViewById(R.id.viewPagerHeader);
        recyclerViewCategory = view.findViewById(R.id.recyclerCategory);
        favorite = view.findViewById(R.id.favorite);
        recyclerSearchItem = view.findViewById(R.id.recyclerSearchItem);

        searchView = view.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                checkConnection();
                if (!s.isEmpty()) {
                    recyclerSearchItem.setVisibility(View.VISIBLE);
                    presenter.getMealsByName(s);
                } else
                    recyclerSearchItem.setVisibility(View.GONE);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (s.isEmpty()) {
                    recyclerSearchItem.removeAllViewsInLayout();
                    recyclerSearchItem.setVisibility(View.GONE);
                }
                return true;
            }
        });

        presenter = new HomePresenter(this);
        presenter.getRandomMeals();
        presenter.getCategories();

        setupUI(view);
        return view;
    }

    public void setupUI(View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    Utils.hideSoftKeyboard(getActivity());
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }


    @Override
    public void setMeal(List<Meal> meal) {
        ViewPagerHeaderAdapter headerAdapter = new ViewPagerHeaderAdapter(meal, getContext());
        viewPagerMeal.setAdapter(headerAdapter);
        viewPagerMeal.setPadding(20, 0, 20, 0);
        headerAdapter.notifyDataSetChanged();

        headerAdapter.setOnItemClickListener((view, position) -> {
            if (checkConnection()) {
                TextView mealName = view.findViewById(R.id.mealName);
                Intent intent = new Intent(getContext(), DetailActivity.class);
                intent.putExtra(EXTRA_DETAIL, mealName.getText().toString());
                startActivity(intent);
            }
        });
    }

    @Override
    public void setMealSearchItem(List<Meal> meal) {
        RecyclerViewSearchItemAdapter recyclerViewSearchItemAdapter = new RecyclerViewSearchItemAdapter(getContext(), meal);
        //Log.i("AAA", presenter.getMealsByName("beef").toString());
        recyclerSearchItem.setAdapter(recyclerViewSearchItemAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerSearchItem.setLayoutManager(linearLayoutManager);

        recyclerViewSearchItemAdapter.setOnItemClickListener(new RecyclerViewSearchItemAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                if (checkConnection()) {
                    int positionMeal = recyclerSearchItem.getChildAdapterPosition(view);
                    Intent intent = new Intent(getContext(), DetailActivity.class);
                    intent.putExtra(EXTRA_DETAIL, recyclerViewSearchItemAdapter.getMeal(positionMeal).getStrMeal());
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void setCategory(List<Category> category) {
        RecyclerViewHomeAdapter homeAdapter = new RecyclerViewHomeAdapter(category, getContext());
        recyclerViewCategory.setAdapter(homeAdapter);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3,
                GridLayoutManager.VERTICAL, false);
        recyclerViewCategory.setLayoutManager(layoutManager);
        recyclerViewCategory.setNestedScrollingEnabled(true);
        homeAdapter.notifyDataSetChanged();

        homeAdapter.setOnItemClickListener((view, position) -> {
            checkConnection();
            Intent intent = new Intent(getContext(), CategoryActivity.class);
            intent.putExtra(EXTRA_CATEGORY, (Serializable) category);
            intent.putExtra(EXTRA_POSITION, position);
            startActivity(intent);
        });
    }

    @Override
    public void onErrorLoading(String message) {
        Utils.showDialogMessage(getContext(), "Title", message);
    }

    @Override
    public void displayToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoading() {
        view.findViewById(R.id.shimmerMeal).setVisibility(View.VISIBLE);
        view.findViewById(R.id.shimmerCategory).setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        view.findViewById(R.id.shimmerMeal).setVisibility(View.GONE);
        view.findViewById(R.id.shimmerCategory).setVisibility(View.GONE);
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

        return isConnected;
    }

    private void startActivityLostInternetConnection(boolean isConnected) {
        Intent intent = new Intent(getContext(), LostInternetConnectionActivity.class);
        startActivity(intent);
    }

    @Override
    public void onNetworkChange(boolean isConnected) {
    }
}
