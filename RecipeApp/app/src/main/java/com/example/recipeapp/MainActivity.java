package com.example.recipeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // create a list of recipe
        ArrayList<Recipe> recipes = new ArrayList<>();
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

        // Find the GridView that displays a list of recipes with ID "grid_view"
        GridView gridView = (GridView)findViewById(R.id.grid_view);

        // initialize the RecipeAdapter that knows how to display a list of Recipe object
        RecipeAdapter adapter = new RecipeAdapter(this, recipes);

        // Set the adapter on the gridview
        gridView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search) {
            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
            startActivity(intent);
//            Toast.makeText(this, "clicked", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}