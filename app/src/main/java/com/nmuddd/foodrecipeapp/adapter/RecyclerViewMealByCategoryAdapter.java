package com.nmuddd.foodrecipeapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nmuddd.foodrecipeapp.R;
import com.nmuddd.foodrecipeapp.model.Meal;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecyclerViewMealByCategoryAdapter extends RecyclerView.Adapter<RecyclerViewMealByCategoryAdapter.RecyclerViewHolder> {

    private List<Meal> meals;
    private Context context;
    private static ClickListener clickListener;

    public RecyclerViewMealByCategoryAdapter(Context context, List<Meal> meals) {
        this.meals = meals;
        this.context = context;

    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recycler_meal,
                viewGroup, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder viewHolder, int i) {

        String strMealThumb = meals.get(i).getStrMealThumb();
        Picasso.get().load(strMealThumb).placeholder(R.drawable.shadow_bottom_to_top).into(viewHolder.mealThumb);

        String strMealName = meals.get(i).getStrMeal();
        viewHolder.mealName.setText(strMealName);

        /*if (isFavorite(strMealName)) {
            viewHolder.love.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_favorite));
        } else {
            viewHolder.love.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_favorite_border));
        }

        viewHolder.love.setOnClickListener(v -> {
            addOrRemoveToFavorite(meals.get(i));
            if (isFavorite(strMealName)) {
                viewHolder.love.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_favorite));
            } else {
                viewHolder.love.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_favorite_border));
            }
        });*/
    }


    @Override
    public int getItemCount() {
        return meals.size();
    }

    static class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.mealThumb)
        ImageView mealThumb;
        @BindView(R.id.mealName)
        TextView mealName;
        @BindView(R.id.love) 
        ImageView love;
        RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.onClick(v, getAdapterPosition());
        }
    }


    public void setOnItemClickListener(ClickListener clickListener) {
        RecyclerViewMealByCategoryAdapter.clickListener = clickListener;
    }


    public interface ClickListener {
        void onClick(View view, int position);
    }

    /*private void addOrRemoveToFavorite(Meal meal) {
        if (isFavorite(meal.getStrMeal())) {
            repository.delete(meal.getStrMeal());
        } else {
            MealFavorite mealFavorite = new MealFavorite();
            mealFavorite.idMeal = meal.getIdMeal();
            mealFavorite.strMeal = meal.getStrMeal();
            mealFavorite.strMealThumb = meal.getStrMealThumb();
            repository.insert(mealFavorite);
        }
    }

    private boolean isFavorite(String strMealName) {
        return repository.isFavorite(strMealName);
    }*/
}
