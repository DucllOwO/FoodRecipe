//package com.nmuddd.foodrecipeapp.adapter;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.nmuddd.foodrecipeapp.R;
//import com.nmuddd.foodrecipeapp.database.FavoriteRepository;
//import com.nmuddd.foodrecipeapp.model.MealFavorite;
//import com.squareup.picasso.Picasso;
//
//import java.util.List;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//
//public class RecyclerViewMealFavoriteAdapter extends RecyclerView.Adapter<RecyclerViewMealFavoriteAdapter.RecyclerViewHolder> {
//
//    private List<MealFavorite> meals;
//    private Context context;
//    private static ClickListener clickListener;
//    private FavoriteRepository repository;
//
//    public RecyclerViewMealFavoriteAdapter(Context context, List<MealFavorite> meals, FavoriteRepository repository) {
//        this.meals = meals;
//        this.context = context;
//        this.repository = repository;
//    }
//
//    @NonNull
//    @Override
//    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
//        View view = LayoutInflater.from(context).inflate(R.layout.item_recycler_meal,
//                viewGroup, false);
//        return new RecyclerViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull RecyclerViewHolder viewHolder, int i) {
//
//        String strMealThumb = meals.get(i).strMealThumb;
//        Picasso.get().load(strMealThumb).placeholder(R.drawable.shadow_bottom_to_top).into(viewHolder.mealThumb);
//
//        String strMealName = meals.get(i).strMeal;
//        viewHolder.mealName.setText(strMealName);
//
//        if (isFavorite(strMealName)) {
//            viewHolder.love.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_favorite));
//        } else {
//            viewHolder.love.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_favorite_border));
//        }
//
//        viewHolder.love.setOnClickListener(v -> {
//            repository.delete(strMealName);
//            meals.remove(i);
//            notifyItemRemoved(i);
//            notifyDataSetChanged();
//        });
//    }
//
//
//    @Override
//    public int getItemCount() {
//        return meals.size();
//    }
//
//    static class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
//        @BindView(R.id.mealThumb)
//        ImageView mealThumb;
//        @BindView(R.id.mealName)
//        TextView mealName;
//        @BindView(R.id.love)
//        ImageView love;
//        RecyclerViewHolder(@NonNull View itemView) {
//            super(itemView);
//            ButterKnife.bind(this, itemView);
//            itemView.setOnClickListener(this);
//        }
//
//        @Override
//        public void onClick(View v) {
//            clickListener.onClick(v, getAdapterPosition());
//        }
//    }
//
//
//    public void setOnItemClickListener(ClickListener clickListener) {
//        RecyclerViewMealFavoriteAdapter.clickListener = clickListener;
//    }
//
//
//    public interface ClickListener {
//        void onClick(View view, int position);
//    }
//
//    private boolean isFavorite(String strMealName) {
//        return repository.isFavorite(strMealName);
//    }
//}
