package com.example.recipeapp.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName = "ingredients")
public class IngredientEntry {
    @PrimaryKey
    private int id;
    private String ingredients;
}
