package com.example.recipeapp.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName = "instructions")
public class InstructionEntry {
    @PrimaryKey
    private int id;
    private String instructions;

}
