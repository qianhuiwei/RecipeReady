package com.example.recipeapp.database;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity (tableName = "recipes")
public class RecipeEntry{
    @PrimaryKey (autoGenerate = true)
    private int id;
    private int image; // the datatype might changed to be an Uri, filepath, or byte[]
    private String title;
    private String ingredients;
    private String instructions;

    /* Constructors */
    @Ignore
    public RecipeEntry(int image, String title) {
        this.image = image;
        this.title = title;
    }

    public RecipeEntry(int id, int image, String title) {
        this.id = id;
        this.image = image;
        this.title = title;
    }

    /* getters */
    public int getId() {
        return id;
    }

    public int getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public String getIngredients() {
        return ingredients;
    }

    public String getInstructions() {
        return instructions;
    }

    /* setters */
    public void setId(int id) {
        this.id = id;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }
}
