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
//import com.nmuddd.foodrecipeapp.model.Meals;
//import com.squareup.picasso.Picasso;
//
//import java.util.List;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//
//public class RecyclerViewSearchItemAdapter extends RecyclerView.Adapter<RecyclerViewSearchItemAdapter.RecyclerViewHolder> {
//    private static ClickListener clickListener;
//    private  List<Meals.Meal> meals;
//    private Context context;
//    public RecyclerViewSearchItemAdapter(Context context,  List<Meals.Meal> meals) {
//        this.meals = meals;
//        this.context = context;
//    }
//
//    @NonNull
//    @Override
//    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
//        View view = LayoutInflater.from(context).inflate(R.layout.item_recycler_search,
//                viewGroup, false);
//        return new RecyclerViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
//        Meals.Meal meal = meals.get(position);
//        String strMealThumb = meals.get(position).getStrMealThumb();
//
//        Picasso.get().load(strMealThumb).placeholder(R.drawable.shadow_bottom_to_top).into(holder.mealThumb);
//        holder.mealName.setText(meal.getStrMeal());
//    }
//
//    @Override
//    public int getItemCount() {
//        return meals.size();
//    }
//
//    public Meals.Meal getMeal(int position) {
//        return meals.get(position);
//    }
//
//
//
//    static class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
//        @BindView(R.id.mealImageSearchItem)
//        ImageView mealThumb;
//        @BindView(R.id.mealNameSearchItem)
//        TextView mealName;
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
//    public void setOnItemClickListener(ClickListener clickListener) {
//        RecyclerViewSearchItemAdapter.clickListener = clickListener;
//    }
//
//    public interface ClickListener {
//        void onClick(View view, int position);
//    }
//}
