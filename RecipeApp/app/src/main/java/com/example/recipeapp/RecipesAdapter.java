package com.example.recipeapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipeapp.database.RecipeEntry;

import java.util.List;

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.RecipeViewHolder> {
    List<RecipeEntry> mRecipeEntries;
    final private ListItemClickListener mOnClickListener;
    private Context mContext;

    //constructor (take in an ArrayList of RecipeEntry objects)
    public RecipesAdapter(Context context, List<RecipeEntry> recipeEntries, ListItemClickListener listener) {
        mRecipeEntries = recipeEntries;
        mContext = context;
        mOnClickListener = listener;
    }

    public void setRecipes(List<RecipeEntry> recipeEntries) {
        mRecipeEntries = recipeEntries;
        notifyDataSetChanged();
    }

    public interface ListItemClickListener {
        void onListItemClicked(RecipeEntry recipe);
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
            RecipeEntry currentRecipeEntry = mRecipeEntries.get(getAdapterPosition());
            mOnClickListener.onListItemClicked(currentRecipeEntry);
        }
    }

    // create new views
    @NonNull
    @Override
    public RecipesAdapter.RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create a new view
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(mContext)
                .inflate(R.layout.recipe_list_item, parent, false);

        RecipeViewHolder viewHolder = new RecipeViewHolder(linearLayout);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        RecipeEntry currentRecipeEntry = mRecipeEntries.get(position);
        String title = currentRecipeEntry.getTitle();
        int image = currentRecipeEntry.getImage();

        holder.listItemImage.setImageResource(image);
        holder.listItemTitle.setText(title);
    }

    @Override
    public int getItemCount() {
        return mRecipeEntries.size();
    }
}
