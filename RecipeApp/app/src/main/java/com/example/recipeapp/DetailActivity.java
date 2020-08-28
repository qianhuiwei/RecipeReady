package com.example.recipeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    // ImageView for display recipe picture
    private ImageView mRecipeImageView;
    // resource id that stores the recipe picture
    private int mImageResourceId;

    // TextView for display recipe title
    private TextView mTitleTextView;
    // String that stores recipe title
    private String mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        setTitle("Detailed Recipe"); // set activity title

        // access TextView object related to recipe title
        mTitleTextView = findViewById(R.id.tv_title);
        // access Imageview object related to recipe picture
        mRecipeImageView = (ImageView) findViewById(R.id.iv_recipe_image);

        // receive intent from MainActivity
        Bundle extras = getIntent().getExtras();
        if (extras == null)
        {
            return;
        }
        mImageResourceId = extras.getInt("resourseInt"); // get picture resource id
        mRecipeImageView.setImageResource(mImageResourceId); // display picture on Imageview
        mTitle = extras.getString("title"); // get title string from the intent
        mTitleTextView.setText(mTitle); // display title string on TextView to display
    }
}