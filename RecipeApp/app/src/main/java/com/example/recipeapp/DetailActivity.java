package com.example.recipeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    private TextView mTitleTextView;
    private String mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        setTitle("Detailed Recipe");

        mTitleTextView = findViewById(R.id.tv_title);

        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra("title") ) {
                mTitle = intent.getStringExtra("title");
                mTitleTextView.setText(mTitle);
            }
        }
    }
}