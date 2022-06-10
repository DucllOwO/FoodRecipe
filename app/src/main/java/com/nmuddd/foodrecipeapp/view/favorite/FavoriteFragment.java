package com.nmuddd.foodrecipeapp.view.favorite;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
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

import com.nmuddd.foodrecipeapp.R;
import com.nmuddd.foodrecipeapp.adapter.RecyclerViewMealFavoriteAdapter;
import com.nmuddd.foodrecipeapp.model.Meal;
import com.nmuddd.foodrecipeapp.view.detail.DetailActivity;
import com.nmuddd.foodrecipeapp.view.home.HomeFragment;

import java.util.List;

import butterknife.BindView;

public class FavoriteFragment extends Fragment implements FavoriteView {

    private View view;
    RecyclerView recyclerView;
    Toolbar toolbar;
    FavoriteView favoriteView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_farvorite, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        toolbar = view.findViewById(R.id.toolbar);

        FavoritePresenter favoritePresenter = new FavoritePresenter(this);
        favoritePresenter.getMealFavorite();

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setClipToPadding(false);
        return view;
    }

    @Override
    public void setFavoriteList(List<Meal> mealFavorite) {
        if (mealFavorite != null) {
            RecyclerViewMealFavoriteAdapter adapter = new RecyclerViewMealFavoriteAdapter(getContext(), mealFavorite);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();

            adapter.setOnItemClickListener((view, position) -> {
                TextView strMealName = view.findViewById(R.id.mealName);
                Intent intent = new Intent(getContext(), DetailActivity.class);
                intent.putExtra(HomeFragment.EXTRA_DETAIL, strMealName.getText().toString());
                startActivity(intent);
            });
        }
    }

    @Override
    public void displayToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
