
package com.nmuddd.foodrecipeapp.view.detail;

import static com.nmuddd.foodrecipeapp.view.home.HomeActivity.EXTRA_DETAIL;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nmuddd.foodrecipeapp.R;
import com.nmuddd.foodrecipeapp.Utils.CurrentUser;
import com.nmuddd.foodrecipeapp.Utils.Utils;
import com.nmuddd.foodrecipeapp.database.Firebase;
import com.nmuddd.foodrecipeapp.model.Meal;
import com.nmuddd.foodrecipeapp.model.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity implements DetailView {
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.appbar)
    AppBarLayout appBarLayout;

    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @BindView(R.id.mealThumb)
    ImageView mealThumb;

    @BindView(R.id.category)
    TextView category;

    @BindView(R.id.country)
    TextView country;

    @BindView(R.id.instructions)
    TextView instructions;

    @BindView(R.id.ingredient)
    TextView ingredients;

    @BindView(R.id.measure)
    TextView measures;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.youtube)
    TextView youtube;

    @BindView(R.id.source)
    TextView source;
    Firebase firebase;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    private Meal meal;
    MenuItem favoriteItem;
    String strMealName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebase = new Firebase();
        setupActionBar();

        Intent intent = getIntent();
        strMealName = intent.getStringExtra(EXTRA_DETAIL);

        DetailPresenter presenter = new DetailPresenter(this, getApplicationContext());

        presenter.getMealById(strMealName);

    }

    private void setupActionBar() {
        setSupportActionBar(toolbar);
        collapsingToolbarLayout.setContentScrimColor(getResources().getColor(R.color.colorWhite));
        collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.colorPrimary));
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.colorWhite));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    void setupColorActionBarIcon(Drawable favoriteItemColor) {
        appBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            if ((collapsingToolbarLayout.getHeight() + verticalOffset) < (2 * ViewCompat.getMinimumHeight(collapsingToolbarLayout))) {
                if (toolbar.getNavigationIcon() != null)
                    toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
                favoriteItemColor.mutate().setColorFilter(getResources().getColor(R.color.colorPrimary),
                        PorterDuff.Mode.SRC_ATOP);

            } else {
                if (toolbar.getNavigationIcon() != null)
                    toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.colorWhite), PorterDuff.Mode.SRC_ATOP);
                favoriteItemColor.mutate().setColorFilter(getResources().getColor(R.color.colorWhite),
                        PorterDuff.Mode.SRC_ATOP);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        favoriteItem = menu.findItem(R.id.favorite);
        setFavoriteItem();
        Drawable favoriteItemColor = favoriteItem.getIcon();
        setupColorActionBarIcon(favoriteItemColor);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home :
                onBackPressed();
                return true;
            case R.id.favorite:
                addOrRemoveToFavorite();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void addOrRemoveToFavorite() {
        Query query = firebase.dbReference.child(firebase.tableNameUser).orderByChild("idUser").equalTo(firebaseUser.getUid());
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
                                favoriteItem.setIcon(getResources().getDrawable(R.drawable.ic_favorite_border));
                            } else {
                                user.getIdMealFavorite().add(meal.getIdMeal());
                                dataSnapshot.getRef().setValue(user);
                                favoriteItem.setIcon(getResources().getDrawable(R.drawable.ic_favorite));
                            }
                        } else
                        {
                            user.getIdMealFavorite().add(meal.getIdMeal());
                            dataSnapshot.getRef().setValue(user);
                            favoriteItem.setIcon(getResources().getDrawable(R.drawable.ic_favorite));
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void setFavoriteItem() {
        Query query = firebase.dbReference.child(firebase.tableNameUser).orderByChild("idUser").equalTo(firebaseUser.getUid());
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
                            for (String idmeal : user.getIdMealFavorite()) {
                                if (Objects.equals(meal.getIdMeal(), idmeal)) {
                                    favoriteItem.setIcon(getResources().getDrawable(R.drawable.ic_favorite));
                                    break;
                                } else {
                                    favoriteItem.setIcon(getResources().getDrawable(R.drawable.ic_favorite_border));
                                }
                            }
                        } else {
                            favoriteItem.setIcon(getResources().getDrawable(R.drawable.ic_favorite_border));
                        }
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                displayToast(error.getMessage());
            }
        });
    }

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void setMeal(Meal meal) {
        this.meal = meal;
        Picasso.get().load(meal.getStrMealThumb()).into(mealThumb);
        collapsingToolbarLayout.setTitle(meal.getStrMeal());
        category.setText(meal.getStrCategory());
        country.setText(meal.getStrArea());
        instructions.setText(meal.getStrInstructions());
        setupActionBar();

        //===
        if (meal != null) {
            if (meal.getStrIngredient1() != null) {
                ingredients.append("\n \u2022 " + meal.getStrIngredient1());
            }
            if (meal.getStrIngredient2() != null) {
                ingredients.append("\n \u2022 " + meal.getStrIngredient2());
            }
            if (meal.getStrIngredient3() != null) {
                ingredients.append("\n \u2022 " + meal.getStrIngredient3());
            }
            if (meal.getStrIngredient4() != null) {
                ingredients.append("\n \u2022 " + meal.getStrIngredient4());
            }
            if (meal.getStrIngredient5() != null) {
                ingredients.append("\n \u2022 " + meal.getStrIngredient5());
            }
            if (meal.getStrIngredient6() != null) {
                ingredients.append("\n \u2022 " + meal.getStrIngredient6());
            }
            if (meal.getStrIngredient7() != null) {
                ingredients.append("\n \u2022 " + meal.getStrIngredient7());
            }
            if (meal.getStrIngredient8() != null) {
                ingredients.append("\n \u2022 " + meal.getStrIngredient8());
            }
            if (meal.getStrIngredient9() != null) {
                ingredients.append("\n \u2022 " + meal.getStrIngredient9());
            }
            if (meal.getStrIngredient10() != null) {
                ingredients.append("\n \u2022 " + meal.getStrIngredient10());
            }
            if (meal.getStrIngredient11() != null) {
                ingredients.append("\n \u2022 " + meal.getStrIngredient11());
            }
            if (meal.getStrIngredient12() != null) {
                ingredients.append("\n \u2022 " + meal.getStrIngredient12());
            }
            if (meal.getStrIngredient13() != null) {
                ingredients.append("\n \u2022 " + meal.getStrIngredient13());
            }
            if (meal.getStrIngredient14() != null) {
                ingredients.append("\n \u2022 " + meal.getStrIngredient14());
            }
            if (meal.getStrIngredient15() != null) {
                ingredients.append("\n \u2022 " + meal.getStrIngredient15());
            }
            if (meal.getStrIngredient16() != null) {
                ingredients.append("\n \u2022 " + meal.getStrIngredient16());
            }
            if ( meal.getStrIngredient17() != null ) {
                ingredients.append("\n \u2022 " + meal.getStrIngredient17());
            }
            if (meal.getStrIngredient18() != null ) {
                ingredients.append("\n \u2022 " + meal.getStrIngredient18());
            }
            if (meal.getStrIngredient19() != null ) {
                ingredients.append("\n \u2022 " + meal.getStrIngredient19());
            }
            if (meal.getStrIngredient20() != null ) {
                ingredients.append("\n \u2022 " + meal.getStrIngredient20());
            }

            if (meal.getStrMeasure1() != null) {
                measures.append("\n : " + meal.getStrMeasure1());
            }
            if ( meal.getStrMeasure2() != null) {
                measures.append("\n : " + meal.getStrMeasure2());
            }
            if ( meal.getStrMeasure3() != null) {
                measures.append("\n : " + meal.getStrMeasure3());
            }
            if (meal.getStrMeasure4() != null) {
                measures.append("\n : " + meal.getStrMeasure4());
            }
            if ( meal.getStrMeasure5() != null) {
                measures.append("\n : " + meal.getStrMeasure5());
            }
            if (meal.getStrMeasure6() != null) {
                measures.append("\n : " + meal.getStrMeasure6());
            }
            if ( meal.getStrMeasure7() != null) {
                measures.append("\n : " + meal.getStrMeasure7());
            }
            if (meal.getStrMeasure8() != null) {
                measures.append("\n : " + meal.getStrMeasure8());
            }
            if ( meal.getStrMeasure9() != null) {
                measures.append("\n : " + meal.getStrMeasure9());
            }
            if (meal.getStrMeasure10() != null) {
                measures.append("\n : " + meal.getStrMeasure10());
            }
            if (meal.getStrMeasure11() != null) {
                measures.append("\n : " + meal.getStrMeasure11());
            }
            if ( meal.getStrMeasure12() != null) {
                measures.append("\n : " + meal.getStrMeasure12());
            }
            if ( meal.getStrMeasure13() != null) {
                measures.append("\n : " + meal.getStrMeasure13());
            }
            if ( meal.getStrMeasure14() != null) {
                measures.append("\n : " + meal.getStrMeasure14());
            }
            if ( meal.getStrMeasure15() != null) {
                measures.append("\n : " + meal.getStrMeasure15());
            }
            if ( meal.getStrMeasure16() != null) {
                measures.append("\n : " + meal.getStrMeasure16());
            }
            if ( meal.getStrMeasure17() != null) {
                measures.append("\n : " + meal.getStrMeasure17());
            }
            if ( meal.getStrMeasure18() != null) {
                measures.append("\n : " + meal.getStrMeasure18());
            }
            if ( meal.getStrMeasure19() != null) {
                measures.append("\n : " + meal.getStrMeasure19());
            }
            if ( meal.getStrMeasure20() != null) {
                measures.append("\n : " + meal.getStrMeasure20());
            }


            youtube.setOnClickListener(v -> {
                Intent intentYoutube = new Intent(Intent.ACTION_VIEW);
                intentYoutube.setData(Uri.parse(meal.getStrYoutube()));
                startActivity(intentYoutube);
            });

            source.setOnClickListener(v -> {
                Intent intentSource = new Intent(Intent.ACTION_VIEW);
                intentSource.setData(Uri.parse(meal.getStrSource()));
                startActivity(intentSource);
            });
        }

    }

    @Override
    public void onErrorLoading(String message) {
        Utils.showDialogMessage(this, "Error", message);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void displayToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}
