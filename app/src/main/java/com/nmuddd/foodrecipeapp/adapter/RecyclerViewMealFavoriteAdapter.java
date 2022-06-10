package com.nmuddd.foodrecipeapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nmuddd.foodrecipeapp.R;
import com.nmuddd.foodrecipeapp.Utils.CurrentUser;
import com.nmuddd.foodrecipeapp.database.Firebase;
import com.nmuddd.foodrecipeapp.model.Meal;
import com.nmuddd.foodrecipeapp.model.User;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecyclerViewMealFavoriteAdapter extends RecyclerView.Adapter<RecyclerViewMealFavoriteAdapter.RecyclerViewHolder> {

    private List<Meal> meals;
    private Context context;
    private static ClickListener clickListener;
    private Firebase firebase;

    public RecyclerViewMealFavoriteAdapter(Context context, List<Meal> meals) {
        this.meals = meals;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recycler_meal,
                viewGroup, false);
        firebase = new Firebase();
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder viewHolder, int i) {

        String strMealThumb = meals.get(i).getStrMealThumb();
        Picasso.get().load(strMealThumb).placeholder(R.drawable.shadow_bottom_to_top).into(viewHolder.mealThumb);

        String strMealName = meals.get(i).getStrMeal();
        viewHolder.mealName.setText(strMealName);

        if (isFavorite(strMealName)) {
            viewHolder.love.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_favorite));
        } else {
            viewHolder.love.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_favorite_border));
        }

        viewHolder.love.setOnClickListener(v -> {
            addOrRemoveToFavorite(meals.get(i), viewHolder);
        });
    }

    private boolean isFavorite(String strMealName) {
        if (strMealName != null) {
            for (Meal meal : CurrentUser.mealFavorite) {
                if (meal.getStrMeal().equals(strMealName))
                    return true;

            }
        }
        return false;
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
        RecyclerViewMealFavoriteAdapter.clickListener = clickListener;
    }


    public interface ClickListener {
        void onClick(View view, int position);
    }

    private void addOrRemoveToFavorite(Meal meal, RecyclerViewHolder viewHolder) {
        Query query = firebase.dbReference.child(firebase.tableNameUser).orderByChild("idUser").equalTo(CurrentUser.idUser);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        User user = new User();
                        user.setIdUser(dataSnapshot.getValue(User.class).getIdUser());
                        user.setEmail(dataSnapshot.getValue(User.class).getEmail());
                        user.setPassword(dataSnapshot.getValue(User.class).getPassword());
                        user.setIdMealFavorite(dataSnapshot.getValue(User.class).getIdMealFavorite());
                        if (user != null && user.getIdMealFavorite() != null) {
                            Boolean hasFavorite = false;
                            for (String idmeal : user.getIdMealFavorite()) {
                                if (meal.getIdMeal().equals(idmeal)) {
                                    hasFavorite = true;
                                    user.getIdMealFavorite().remove(meal.getIdMeal());
                                    break;
                                }
                            }
                            if (hasFavorite) {
                                dataSnapshot.getRef().setValue(user);
                                viewHolder.love.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_favorite_border));
                            } else {
                                user.getIdMealFavorite().add(meal.getIdMeal());
                                dataSnapshot.getRef().setValue(user);
                                viewHolder.love.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_favorite));
                            }
                        } else
                        {
                            user.getIdMealFavorite().add(meal.getIdMeal());
                            dataSnapshot.getRef().setValue(user);
                            viewHolder.love.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_favorite));
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}
