package com.nmuddd.foodrecipeapp.view.personal_list;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nmuddd.foodrecipeapp.R;
import com.nmuddd.foodrecipeapp.Utils.CurrentUser;
import com.nmuddd.foodrecipeapp.adapter.RecyclerViewPersonalMealAdapter;
import com.nmuddd.foodrecipeapp.view.detail.DetailActivity;
import com.nmuddd.foodrecipeapp.view.home.HomeFragment;

public class PersonalListFragment extends Fragment implements PersonalListView {
    private View view;

    FloatingActionButton openAddBtn;
    RecyclerView recyclerView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_personal_list, container, false);

        openAddBtn = view.findViewById(R.id.fab_add_meal);
        recyclerView = view.findViewById(R.id.personal_recipe_recycler_view);

        setMyMeal();

        openAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddMealFragment editMealFragment = new AddMealFragment();
                FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
                fragmentTransaction.setReorderingAllowed(true).replace(R.id.fragment_personal_list, editMealFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
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
            Intent intent = new Intent(getContext(), DetailActivity.class);
            intent.putExtra(HomeFragment.EXTRA_DETAIL, CurrentUser.myMeal.get(position));
            startActivity(intent);
        });

    }
}
