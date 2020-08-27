package com.example.recipeapp;

public class Recipe {
    // member variable
    private int image;
    private String name;

    // constructor
    public Recipe(int image, String name) {
        this.image = image;
        this.name = name;
    }

    // getter methods
    public int getImage() {
        return image;
    }

    public String getName() {
        return name;
    }
}
