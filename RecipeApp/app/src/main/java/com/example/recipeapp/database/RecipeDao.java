package com.example.recipeapp.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface RecipeDao {

    @Query("SELECT * FROM recipes")
    List<RecipeEntry> loadAllRecipes();

    @Query("SELECT * FROM recipes WHERE id IN (:recipeIds)")
    List<RecipeEntry> loadAllRecipesByIds(int[] recipeIds); // may need to pass in List<Integer> later

    @Insert
    void insertRecipe(RecipeEntry recipeEntry);

    @Insert
    void insertAllRecipes(RecipeEntry... recipeEntries);

    /* these methods may not be used for now */
    @Update (onConflict = OnConflictStrategy.REPLACE)
    void updateRecipe(RecipeEntry recipeEntry);

    @Delete
    void deleteRecipe(RecipeEntry recipeEntry);

}
