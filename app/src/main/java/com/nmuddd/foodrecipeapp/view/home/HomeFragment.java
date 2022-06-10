package com.nmuddd.foodrecipeapp.view.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.nmuddd.foodrecipeapp.Utils.Utils;
import com.nmuddd.foodrecipeapp.adapter.RecyclerViewHomeAdapter;
import com.nmuddd.foodrecipeapp.adapter.RecyclerViewSearchItemAdapter;
import com.nmuddd.foodrecipeapp.adapter.ViewPagerHeaderAdapter;
import com.nmuddd.foodrecipeapp.model.Category;
import com.nmuddd.foodrecipeapp.model.Meal;
import com.nmuddd.foodrecipeapp.view.category.CategoryActivity;
import com.nmuddd.foodrecipeapp.view.detail.DetailActivity;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;

public class HomeFragment extends Fragment implements HomeView {
    private View view;

    public static final String EXTRA_CATEGORY = "category";
    public static final String EXTRA_POSITION = "position";
    public static final String EXTRA_DETAIL = "detail";

    ViewPager viewPagerMeal;
    RecyclerView recyclerViewCategory;
    ImageView favorite;
    RecyclerView recyclerSearchItem;
    HomePresenter presenter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);

        viewPagerMeal = view.findViewById(R.id.viewPagerHeader);
        recyclerViewCategory = view.findViewById(R.id.recyclerCategory);
        favorite = view.findViewById(R.id.favorite);
        recyclerSearchItem = view.findViewById(R.id.recyclerSearchItem);


        presenter = new HomePresenter(this);
        presenter.getRandomMeals();
        presenter.getCategories();

        SearchView searchView = view.findViewById(R.id.searchView);
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
        return view;
    }

    /*public void setupUI(View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    Utils.hideSoftKeyboard(HomeFragment.this);
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
    }*/



    @Override
    public void setMeal(List<Meal> meal) {
        ViewPagerHeaderAdapter headerAdapter = new ViewPagerHeaderAdapter(meal, getContext());
        viewPagerMeal.setAdapter(headerAdapter);
        viewPagerMeal.setPadding(20, 0, 20, 0);
        headerAdapter.notifyDataSetChanged();

        headerAdapter.setOnItemClickListener((view, position) -> {
            TextView mealName = view.findViewById(R.id.mealName);
            Intent intent = new Intent(getContext(), DetailActivity.class);
            intent.putExtra(EXTRA_DETAIL, mealName.getText().toString());
            startActivity(intent);
        });
    }

    @Override
    public void setMealSearchItem(List<Meal> meal) {
        RecyclerViewSearchItemAdapter recyclerViewSearchItemAdapter = new RecyclerViewSearchItemAdapter(getContext(),  meal);
        //Log.i("AAA", presenter.getMealsByName("beef").toString());
        recyclerSearchItem.setAdapter(recyclerViewSearchItemAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerSearchItem.setLayoutManager(linearLayoutManager);

        recyclerViewSearchItemAdapter.setOnItemClickListener(new RecyclerViewSearchItemAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                int positionMeal = recyclerSearchItem.getChildAdapterPosition(view);
                Intent intent = new Intent(getContext(), DetailActivity.class);
                intent.putExtra(EXTRA_DETAIL, recyclerViewSearchItemAdapter.getMeal(positionMeal).getStrMeal());
                startActivity(intent);
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

}