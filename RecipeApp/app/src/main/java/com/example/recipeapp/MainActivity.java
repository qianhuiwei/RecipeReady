package com.example.recipeapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipeapp.database.AppDatabase;
import com.example.recipeapp.database.RecipeEntry;

import java.util.List;

public class MainActivity extends AppCompatActivity implements RecipesAdapter.ListItemClickListener {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private AppDatabase mDb;
    private List<RecipeEntry> mQueryRecipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialize our local database
        mDb = AppDatabase.getInstance(getApplicationContext());

        // Setup the RecyclerView to display a list of recipes
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_main);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2, RecyclerView.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        // Get recipes from the server and bind them with RecyclerView and Adapter
        mQueryRecipes = mDb.recipeDao().loadAllRecipes();
        mAdapter = new RecipesAdapter(this, mQueryRecipes, this);
        mRecyclerView.setAdapter(mAdapter);
    }

    /* this is just for testing inserting data to db,
       we won't handle insertion in our app
    *  We'll only need to query data (JSON strings) from our web server
       and display the returned recipes on UI */
    public void saveRecipeToDb() {
        RecipeEntry recipeEntry = new RecipeEntry(R.drawable.cheese_burger, "Cheese Burger");
        mDb.recipeDao().insertRecipe(recipeEntry);
    }

    @Override
    public void onListItemClicked(RecipeEntry currentRecipe) {
        //create intent that carries data to destination activity
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra("resourseInt", currentRecipe.getImage()); // add image resource id
        intent.putExtra("title", currentRecipe.getTitle()); // add recipe title
        startActivity(intent); // send the intent
    }

    @Override // this method inflate a menu item in the upper tool bar
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        return true;
    }

    @Override // this method defines each menu item behavior when clicked
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search) {
            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_insert) {
            saveRecipeToDb();
        }
        return super.onOptionsItemSelected(item);
    }
}
