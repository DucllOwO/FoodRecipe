package com.nmuddd.foodrecipeapp.view.favorite;

import static com.nmuddd.foodrecipeapp.view.home.HomeActivity.EXTRA_DETAIL;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nmuddd.foodrecipeapp.R;
import com.nmuddd.foodrecipeapp.adapter.RecyclerViewMealFavoriteAdapter;
import com.nmuddd.foodrecipeapp.model.Meal;
import com.nmuddd.foodrecipeapp.view.detail.DetailActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavoriteActivity extends AppCompatActivity implements FavoriteView {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setClipToPadding(false);
        
    }


    @Override
    public void setFavoriteList(List<Meal> mealFavorite) {
        if (mealFavorite != null) {
            RecyclerViewMealFavoriteAdapter adapter = new RecyclerViewMealFavoriteAdapter(this, mealFavorite);
            recyclerView.setAdapter(adapter);

            adapter.setOnItemClickListener((view, position) -> {
                TextView strMealName = view.findViewById(R.id.mealName);
                Intent intent = new Intent(this, DetailActivity.class);
                intent.putExtra(EXTRA_DETAIL, strMealName.getText().toString());
                startActivity(intent);
                finish();
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        FavoritePresenter favoritePresenter = new FavoritePresenter(this);
        favoritePresenter.getMealFavorite();
    }

    @Override
    public void displayToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

}
