package com.example.recipeapp;

public class Recipe {
    // member variable
    private int mImage;
    private String mTitle;

    // constructor
    public Recipe(int image, String title) {
        this.mImage = image;
        this.mTitle = title;
    }

    // getter methods
    public int getImage() {
        return mImage;
    }

    public String getTitle() {
        return mTitle;
    }
}
