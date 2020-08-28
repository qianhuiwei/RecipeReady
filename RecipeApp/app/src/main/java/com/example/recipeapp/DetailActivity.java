package com.example.recipeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

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

        // receive intent from MainActivity
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra("title") ) { // if the intent contains data we want, extract data from it
                mTitle = intent.getStringExtra("title"); // get title string from the intent
                mTitleTextView.setText(mTitle); // set title string on TextView to display
            }
        }
    }
}