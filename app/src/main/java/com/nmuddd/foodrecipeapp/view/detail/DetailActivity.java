package com.nmuddd.foodrecipeapp.view.detail;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nmuddd.foodrecipeapp.R;
import com.nmuddd.foodrecipeapp.Utils.ConnectionReceiver;
import com.nmuddd.foodrecipeapp.Utils.CurrentUser;
import com.nmuddd.foodrecipeapp.Utils.Utils;
import com.nmuddd.foodrecipeapp.database.Firebase;
import com.nmuddd.foodrecipeapp.model.Meal;
import com.nmuddd.foodrecipeapp.model.User;
import com.nmuddd.foodrecipeapp.view.LostInternetConnectionActivity;
import com.nmuddd.foodrecipeapp.view.home.HomeFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity implements DetailView, ConnectionReceiver.ReceiverListener {
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
    public Meal meal;
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
        strMealName = intent.getStringExtra(HomeFragment.EXTRA_DETAIL);

        DetailPresenter presenter = new DetailPresenter(this, getApplicationContext());
        if (checkConnection())
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
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.favorite:
                if (checkConnection()) {
                    addOrRemoveToFavorite();
                }

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
                        if (dataSnapshot.getValue(User.class).getMealFavorite() != null) {
                            user.setMealFavorite(dataSnapshot.getValue(User.class).getMealFavorite());
                            CurrentUser.mealFavorite = dataSnapshot.getValue(User.class).getMealFavorite();
                        } else {
                            List<Meal> meals = new ArrayList<>();
                            user.setMealFavorite(meals);
                        }
                        if (user != null && user.getMealFavorite() != null) {
                            Boolean hasFavorite = false;
                            for (Meal meal1 : user.getMealFavorite()) {
                                if (meal.getIdMeal().equals(meal1.getIdMeal())) {
                                    hasFavorite = true;
                                    user.getMealFavorite().remove(meal1);
                                    break;
                                }
                            }
                            if (hasFavorite) {
                                dataSnapshot.getRef().setValue(user);
                                favoriteItem.setIcon(getResources().getDrawable(R.drawable.ic_favorite_border));
                            } else {
                                user.getMealFavorite().add(meal);
                                dataSnapshot.getRef().setValue(user);
                                favoriteItem.setIcon(getResources().getDrawable(R.drawable.ic_favorite));
                            }
                        } else {
                            user.getMealFavorite().add(meal);
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
                        if (dataSnapshot.getValue(User.class).getMealFavorite() != null)
                            user.setMealFavorite(dataSnapshot.getValue(User.class).getMealFavorite());
                        else
                            user.setMealFavorite(new ArrayList<>());
                        if (user != null && user.getMealFavorite() != null) {
                            for (Meal meal1 : user.getMealFavorite()) {
                                if (meal.getIdMeal().equals(meal1.getIdMeal())) {
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
            try {
                if (!meal.getStrIngredient1().isEmpty()) {
                    ingredients.append("\n \u2022 " + meal.getStrIngredient1());
                }
                if (!meal.getStrIngredient2().isEmpty()) {
                    ingredients.append("\n \u2022 " + meal.getStrIngredient2());
                }
                if (!meal.getStrIngredient3().isEmpty()) {
                    ingredients.append("\n \u2022 " + meal.getStrIngredient3());
                }
                if (!meal.getStrIngredient4().isEmpty()) {
                    ingredients.append("\n \u2022 " + meal.getStrIngredient4());
                }
                if (!meal.getStrIngredient5().isEmpty()) {
                    ingredients.append("\n \u2022 " + meal.getStrIngredient5());
                }
                if (!meal.getStrIngredient6().isEmpty()) {
                    ingredients.append("\n \u2022 " + meal.getStrIngredient6());
                }
                if (!meal.getStrIngredient7().isEmpty()) {
                    ingredients.append("\n \u2022 " + meal.getStrIngredient7());
                }
                if (!meal.getStrIngredient8().isEmpty()) {
                    ingredients.append("\n \u2022 " + meal.getStrIngredient8());
                }
                if (!meal.getStrIngredient9().isEmpty()) {
                    ingredients.append("\n \u2022 " + meal.getStrIngredient9());
                }
                if (!meal.getStrIngredient10().isEmpty()) {
                    ingredients.append("\n \u2022 " + meal.getStrIngredient10());
                }
                if (!meal.getStrIngredient11().isEmpty()) {
                    ingredients.append("\n \u2022 " + meal.getStrIngredient11());
                }
                if (!meal.getStrIngredient12().isEmpty()) {
                    ingredients.append("\n \u2022 " + meal.getStrIngredient12());
                }
                if (!meal.getStrIngredient13().isEmpty()) {
                    ingredients.append("\n \u2022 " + meal.getStrIngredient13());
                }
                if (!meal.getStrIngredient14().isEmpty()) {
                    ingredients.append("\n \u2022 " + meal.getStrIngredient14());
                }
                if (!meal.getStrIngredient15().isEmpty()) {
                    ingredients.append("\n \u2022 " + meal.getStrIngredient15());
                }
                if (!meal.getStrIngredient16().isEmpty()) {
                    ingredients.append("\n \u2022 " + meal.getStrIngredient16());
                }
                if (!meal.getStrIngredient17().isEmpty()) {
                    ingredients.append("\n \u2022 " + meal.getStrIngredient17());
                }
                if (!meal.getStrIngredient18().isEmpty()) {
                    ingredients.append("\n \u2022 " + meal.getStrIngredient18());
                }
                if (!meal.getStrIngredient19().isEmpty()) {
                    ingredients.append("\n \u2022 " + meal.getStrIngredient19());
                }
                if (!meal.getStrIngredient20().isEmpty()) {
                    ingredients.append("\n \u2022 " + meal.getStrIngredient20());
                }
            } catch (Exception e) {

            }

            try {
                if (!meal.getStrMeasure1().isEmpty() && !Character.isWhitespace(meal.getStrMeasure1().charAt(0))) {
                    measures.append("\n : " + meal.getStrMeasure1());
                }
                if (!meal.getStrMeasure2().isEmpty() && !Character.isWhitespace(meal.getStrMeasure2().charAt(0))) {
                    measures.append("\n : " + meal.getStrMeasure2());
                }
                if (!meal.getStrMeasure3().isEmpty() && !Character.isWhitespace(meal.getStrMeasure3().charAt(0))) {
                    measures.append("\n : " + meal.getStrMeasure3());
                }
                if (!meal.getStrMeasure4().isEmpty() && !Character.isWhitespace(meal.getStrMeasure4().charAt(0))) {
                    measures.append("\n : " + meal.getStrMeasure4());
                }
                if (!meal.getStrMeasure5().isEmpty() && !Character.isWhitespace(meal.getStrMeasure5().charAt(0))) {
                    measures.append("\n : " + meal.getStrMeasure5());
                }
                if (!meal.getStrMeasure6().isEmpty() && !Character.isWhitespace(meal.getStrMeasure6().charAt(0))) {
                    measures.append("\n : " + meal.getStrMeasure6());
                }
                if (!meal.getStrMeasure7().isEmpty() && !Character.isWhitespace(meal.getStrMeasure7().charAt(0))) {
                    measures.append("\n : " + meal.getStrMeasure7());
                }
                if (!meal.getStrMeasure8().isEmpty() && !Character.isWhitespace(meal.getStrMeasure8().charAt(0))) {
                    measures.append("\n : " + meal.getStrMeasure8());
                }
                if (!meal.getStrMeasure9().isEmpty() && !Character.isWhitespace(meal.getStrMeasure9().charAt(0))) {
                    measures.append("\n : " + meal.getStrMeasure9());
                }
                if (!meal.getStrMeasure10().isEmpty() && !Character.isWhitespace(meal.getStrMeasure10().charAt(0))) {
                    measures.append("\n : " + meal.getStrMeasure10());
                }
                if (!meal.getStrMeasure11().isEmpty() && !Character.isWhitespace(meal.getStrMeasure11().charAt(0))) {
                    measures.append("\n : " + meal.getStrMeasure11());
                }
                if (!meal.getStrMeasure12().isEmpty() && !Character.isWhitespace(meal.getStrMeasure12().charAt(0))) {
                    measures.append("\n : " + meal.getStrMeasure12());
                }
                if (!meal.getStrMeasure13().isEmpty() && !Character.isWhitespace(meal.getStrMeasure13().charAt(0))) {
                    measures.append("\n : " + meal.getStrMeasure13());
                }
                if (!meal.getStrMeasure14().isEmpty() && !Character.isWhitespace(meal.getStrMeasure14().charAt(0))) {
                    measures.append("\n : " + meal.getStrMeasure14());
                }
                if (!meal.getStrMeasure15().isEmpty() && !Character.isWhitespace(meal.getStrMeasure15().charAt(0))) {
                    measures.append("\n : " + meal.getStrMeasure15());
                }
                if (!meal.getStrMeasure16().isEmpty() && !Character.isWhitespace(meal.getStrMeasure16().charAt(0))) {
                    measures.append("\n : " + meal.getStrMeasure16());
                }
                if (!meal.getStrMeasure17().isEmpty() && !Character.isWhitespace(meal.getStrMeasure17().charAt(0))) {
                    measures.append("\n : " + meal.getStrMeasure17());
                }
                if (!meal.getStrMeasure18().isEmpty() && !Character.isWhitespace(meal.getStrMeasure18().charAt(0))) {
                    measures.append("\n : " + meal.getStrMeasure18());
                }
                if (!meal.getStrMeasure19().isEmpty() && !Character.isWhitespace(meal.getStrMeasure19().charAt(0))) {
                    measures.append("\n : " + meal.getStrMeasure19());
                }
                if (!meal.getStrMeasure20().isEmpty() && !Character.isWhitespace(meal.getStrMeasure20().charAt(0))) {
                    measures.append("\n : " + meal.getStrMeasure20());
                }
            } catch (Exception e ) {

            }


            youtube.setOnClickListener(v -> {
                if (meal.getStrYoutube() != null && !meal.getStrYoutube().isEmpty())  {
                    Intent intentYoutube = new Intent(Intent.ACTION_VIEW);
                    intentYoutube.setData(Uri.parse(meal.getStrYoutube()));
                    startActivity(intentYoutube);
                } else
                {
                    displayToast("Sorry this recipe don't have a tutorial on youtube");
                }
            });

            source.setOnClickListener(v -> {
                if (meal.getStrSource() != null && !meal.getStrSource().isEmpty())  {
                    Intent intentYoutube = new Intent(Intent.ACTION_VIEW);
                    intentYoutube.setData(Uri.parse(meal.getStrSource()));
                    startActivity(intentYoutube);
                } else
                {
                    displayToast("Sorry this recipe don't have a source");
                }
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
        finish();
    }

    @Override
    public void displayToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private Boolean checkConnection() {

        // initialize intent filter
        IntentFilter intentFilter = new IntentFilter();

        // add action
        intentFilter.addAction("android.new.conn.CONNECTIVITY_CHANGE");

        // register receiver
        getApplicationContext().registerReceiver(new ConnectionReceiver(), intentFilter);

        // Initialize listener
        ConnectionReceiver.Listener = this;

        // Initialize connectivity manager
        ConnectivityManager manager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        // Initialize network info
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        // get connection status
        boolean isConnected = networkInfo != null && networkInfo.isConnectedOrConnecting();

        if (!isConnected)
            startActivityLostInternetConnection();

        return isConnected;
    }

    private void startActivityLostInternetConnection() {
        Intent intent = new Intent(getApplicationContext(), LostInternetConnectionActivity.class);
        startActivity(intent);
    }

    @Override
    public void onNetworkChange(boolean isConnected) {

    }
}
