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
import com.nmuddd.foodrecipeapp.database.Firebase;
import com.nmuddd.foodrecipeapp.model.Meal;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecyclerViewPersonalMealAdapter extends RecyclerView.Adapter<RecyclerViewPersonalMealAdapter.RecyclerViewHolder>{


    private List<Meal> meals;
    private Context context;
    private static RecyclerViewPersonalMealAdapter.ClickListener clickListener;
    private Firebase firebase;

    public RecyclerViewPersonalMealAdapter(Context context, List<Meal> meals) {
        this.meals = meals;
        this.context = context;

    }
    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recycler_meal_personal,
                parent, false);
        firebase = new Firebase();

        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int pos) {
        String strMealThumb = meals.get(pos).getStrMealThumb();
        Picasso.get().load(strMealThumb).placeholder(R.drawable.shadow_bottom_to_top).into(holder.mealThumb);

        String strMealName = meals.get(pos).getStrMeal();
        holder.mealName.setText(strMealName);

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

    public void setOnItemClickListener(RecyclerViewPersonalMealAdapter.ClickListener clickListener) {
        RecyclerViewPersonalMealAdapter.clickListener = clickListener;
    }


    public interface ClickListener {
        void onClick(View view, int position);
    }
}
