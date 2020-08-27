package com.example.recipeapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class RecipeAdapter extends ArrayAdapter<Recipe> {
    public RecipeAdapter(Context context, ArrayList<Recipe> recipes) {
        super(context, 0, recipes);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Check if there's an existing grid item view. If not, inflate a new one
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        // Get the Recipe object at given position
        Recipe currentRecipe = getItem(position);

        // Find the ImageView in the list_item.xlm with ID "recipe_image"
        ImageView imageView = convertView.findViewById(R.id.recipe_image);
        // Display the provided image based on the resource ID
        imageView.setImageResource(currentRecipe.getImage());

        // Find the TextView in the list_item.xml layout with the ID recipe_name.
        TextView textView = (TextView) convertView.findViewById(R.id.recipe_name);
        // Get the recipe name from the current Recipe object and set this text on TextView.
        textView.setText(currentRecipe.getName());

        return convertView;
    }
}
