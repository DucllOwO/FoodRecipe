package com.nmuddd.foodrecipeapp.view.personal_list;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nmuddd.foodrecipeapp.R;
import com.nmuddd.foodrecipeapp.Utils.CurrentUser;
import com.nmuddd.foodrecipeapp.adapter.RecyclerViewPersonalMealAdapter;
import com.nmuddd.foodrecipeapp.view.add_meal.AddMealActivity;
import com.nmuddd.foodrecipeapp.view.detail_personal.DetailPersonalActivity;

import java.io.Serializable;

public class PersonalListFragment extends Fragment implements PersonalListView {
    public static final String EXTRA_DETAIL_PERSONAL = "detail_personal";
    private View view;

    FloatingActionButton openAddBtn;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_personal_list, container, false);

        openAddBtn = view.findViewById(R.id.fab_add_meal);
        recyclerView = view.findViewById(R.id.personal_recipe_recycler_view);
        swipeRefreshLayout = view.findViewById(R.id.swiperefresh_personal);

        setMyMeal();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //recyclerView.notifyAll();
                setMyMeal();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        openAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddMealActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void setMyMeal() {
        RecyclerViewPersonalMealAdapter recyclerViewPersonalMealAdapter =
                new RecyclerViewPersonalMealAdapter(getActivity(), CurrentUser.myMeal);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerView.setClipToPadding(false);
        recyclerView.setAdapter(recyclerViewPersonalMealAdapter);
        recyclerViewPersonalMealAdapter.notifyDataSetChanged();

        recyclerViewPersonalMealAdapter.setOnItemClickListener((view, position) -> {
            Intent intent = new Intent(view.getContext(), DetailPersonalActivity.class);
            intent.putExtra(EXTRA_DETAIL_PERSONAL, (Serializable) CurrentUser.myMeal.get(position));
            startActivity(intent);
        });

    }
}
