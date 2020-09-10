package com.example.recipeapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.RecipeViewHolder> {
    private List<Recipe> mDataset;
    final private ListItemClickListener mOnClickListener;

    //constructor (take in an Arraylist of Recipe objects)
    public RecipesAdapter(List<Recipe> recipes, ListItemClickListener listener) {
        mDataset = recipes;
        mOnClickListener = listener;
    }

    public interface ListItemClickListener {
        void onListItemClicked(Recipe recipe);
    }

    // Inner viewHolder class. Provide a reference to the views for each data item
    public class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView listItemImage;
        public TextView listItemTitle;

        public RecipeViewHolder(View itemView) {
            super(itemView);
            listItemImage = itemView.findViewById(R.id.iv_recipe_image);
            listItemTitle = itemView.findViewById(R.id.tv_recipe_title);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Recipe currentRecipe = mDataset.get(getAdapterPosition());
            mOnClickListener.onListItemClicked(currentRecipe);
        }
    }

    // create new views
    @NonNull
    @Override
    public RecipesAdapter.RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create a new view
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_list_item, parent, false);

        RecipeViewHolder viewHolder = new RecipeViewHolder(linearLayout);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        holder.listItemImage.setImageResource(mDataset.get(position).getImage());
        holder.listItemTitle.setText(mDataset.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
