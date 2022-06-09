package com.nmuddd.foodrecipeapp.view.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.nmuddd.foodrecipeapp.R;
import com.nmuddd.foodrecipeapp.Utils.Utils;
import com.nmuddd.foodrecipeapp.adapter.RecyclerViewHomeAdapter;
import com.nmuddd.foodrecipeapp.adapter.RecyclerViewSearchItemAdapter;
import com.nmuddd.foodrecipeapp.adapter.ViewPagerHeaderAdapter;
import com.nmuddd.foodrecipeapp.model.Category;
import com.nmuddd.foodrecipeapp.model.Meal;
import com.nmuddd.foodrecipeapp.view.category.CategoryActivity;
import com.nmuddd.foodrecipeapp.view.detail.DetailActivity;
import com.nmuddd.foodrecipeapp.view.favorite.FavoriteActivity;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity implements HomeView {

    public static final String API_KEY = "1a6fb5e756684298b67fbd7e9d8ffd77";

    public static final String EXTRA_CATEGORY = "category";
    public static final String EXTRA_POSITION = "position";
    public static final String EXTRA_DETAIL = "detail";

    @BindView(R.id.viewPagerHeader) ViewPager viewPagerMeal;
    @BindView(R.id.recyclerCategory)
    RecyclerView recyclerViewCategory;
    @BindView(R.id.favorite) ImageView favorite;
    @BindView(R.id.recyclerSearchItem) RecyclerView recyclerSearchItem;
    HomePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        setupUI(findViewById(R.id.homeActivity));
        favorite.setOnClickListener(v -> {
            startActivity(new Intent(this, FavoriteActivity.class));
        });
        presenter = new HomePresenter(this);
        presenter.getRandomMeals();
        presenter.getCategories();

        SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                presenter.getMealsByName(s);
                Log.i("AAA", "Thoat ra khoi presenter");
                /*RecyclerViewSearchItemAdapter recyclerViewSearchItemAdapter = new RecyclerViewSearchItemAdapter(HomeActivity.this,  HomePresenter.mealList);
                //Log.i("AAA", presenter.getMealsByName("beef").toString());
                recyclerSearchItem.setAdapter(recyclerViewSearchItemAdapter);

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(HomeActivity.this);
                recyclerSearchItem.setLayoutManager(linearLayoutManager);*/

                /*Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
                intent.putExtra(EXTRA_DETAIL, searchView.getQuery().toString());
                startActivity(intent);
                return true;*/
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {


                return false;
            }
        });
    }



    // auto hide soft keyboard when click outside
    public void setupUI(View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    Utils.hideSoftKeyboard(HomeActivity.this);
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
        ViewPagerHeaderAdapter headerAdapter = new ViewPagerHeaderAdapter(meal, this);
        viewPagerMeal.setAdapter(headerAdapter);
        viewPagerMeal.setPadding(20, 0, 20, 0);
        headerAdapter.notifyDataSetChanged();

        headerAdapter.setOnItemClickListener((view, position) -> {
            TextView mealName = view.findViewById(R.id.mealName);
            Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
            intent.putExtra(EXTRA_DETAIL, mealName.getText().toString());
            startActivity(intent);
        });
    }

    @Override
    public void setMealSearchItem(List<Meal> meal) {
        RecyclerViewSearchItemAdapter recyclerViewSearchItemAdapter = new RecyclerViewSearchItemAdapter(HomeActivity.this,  meal);
        //Log.i("AAA", presenter.getMealsByName("beef").toString());
        recyclerSearchItem.setAdapter(recyclerViewSearchItemAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(HomeActivity.this);
        recyclerSearchItem.setLayoutManager(linearLayoutManager);

        recyclerViewSearchItemAdapter.setOnItemClickListener(new RecyclerViewSearchItemAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                int positionMeal = recyclerSearchItem.getChildAdapterPosition(view);
                Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
                intent.putExtra(EXTRA_DETAIL, recyclerViewSearchItemAdapter.getMeal(positionMeal).getStrMeal());
                startActivity(intent);
            }
        });
    }

    @Override
    public void setCategory(List<Category> category) {
        RecyclerViewHomeAdapter homeAdapter = new RecyclerViewHomeAdapter(category, this);
        recyclerViewCategory.setAdapter(homeAdapter);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3,
                GridLayoutManager.VERTICAL, false);
        recyclerViewCategory.setLayoutManager(layoutManager);
        recyclerViewCategory.setNestedScrollingEnabled(true);
        homeAdapter.notifyDataSetChanged();

        homeAdapter.setOnItemClickListener((view, position) -> {
            Intent intent = new Intent(this, CategoryActivity.class);
            intent.putExtra(EXTRA_CATEGORY, (Serializable) category);
            intent.putExtra(EXTRA_POSITION, position);
            startActivity(intent);
        });
    }

    @Override
    public void onErrorLoading(String message) {
        Utils.showDialogMessage(this, "Title", message);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void displayToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoading() {
        findViewById(R.id.shimmerMeal).setVisibility(View.VISIBLE);
        findViewById(R.id.shimmerCategory).setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        findViewById(R.id.shimmerMeal).setVisibility(View.GONE);
        findViewById(R.id.shimmerCategory).setVisibility(View.GONE);
    }
}
