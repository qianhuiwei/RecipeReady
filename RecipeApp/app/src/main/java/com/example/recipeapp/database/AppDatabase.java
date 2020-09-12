package com.example.recipeapp.database;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {RecipeEntry.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase sInstance;
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "recipeApp";

    /* Use singleton pattern to ensure only one object of AppDatabase is created.
    * First time getInstance get called, sInstance will be null,
    * then new object will be created and assigned to the sInstance variable.
    * For any future call to getInstance, the sInstance variable won't be null,
    * then it will be returned straight away without instantiating a new object. */
    public static AppDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(AppDatabase.class.getName(), "Creating new database instance");
                sInstance = Room.databaseBuilder(
                        context.getApplicationContext(),
                        AppDatabase.class,
                        AppDatabase.DATABASE_NAME)
                        // temporarily load data from db on the MainThread simply for testing
                        // will need to change to do in background thread
                        .allowMainThreadQueries()
                        .fallbackToDestructiveMigration()
                        .build();
            }
        }
        Log.d(AppDatabase.class.getName(), "Getting the database instance");
        return sInstance;
    }

    // Add the RecipeDao
    public abstract RecipeDao recipeDao();
}
