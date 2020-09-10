package com.example.recipeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RecipesAdapter.ListItemClickListener {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter; // private RecipeAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // create a list of recipe
        List<Recipe> recipes = new ArrayList<>();
        recipes.add(new Recipe(R.drawable.tiramisu, "Tiramisu"));
        recipes.add(new Recipe(R.drawable.brownie, "Brownie"));
        recipes.add(new Recipe(R.drawable.cake_pop, "Cake Pop"));
        recipes.add(new Recipe(R.drawable.cheese_burger, "Cheese burger"));
        recipes.add(new Recipe(R.drawable.fruit_tart, "Fruit Tart"));
        recipes.add(new Recipe(R.drawable.tiramisu, "Tiramisu"));
        recipes.add(new Recipe(R.drawable.brownie, "Brownie"));
        recipes.add(new Recipe(R.drawable.cake_pop, "Cake Pop"));
        recipes.add(new Recipe(R.drawable.cheese_burger, "Cheese burger"));
        recipes.add(new Recipe(R.drawable.fruit_tart, "Fruit Tart"));

        // Find the Recycler that displays a list of recipes
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_main);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2, RecyclerView.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);

        // specify an adapter (see also next example)
        mAdapter = new RecipesAdapter(recipes, this);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onListItemClicked(Recipe currentRecipe) {
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
        return super.onOptionsItemSelected(item);
    }
}