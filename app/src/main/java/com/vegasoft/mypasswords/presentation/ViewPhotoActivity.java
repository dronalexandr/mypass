package com.vegasoft.mypasswords.presentation;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.squareup.picasso.Picasso;
import com.vegasoft.mypasswords.R;
import com.vegasoft.mypasswords.presentation.custom_view.CustomImageView;

import java.io.File;

public class ViewPhotoActivity extends AppCompatActivity {
    public static String ARG_PHOTO = "photo_arguments";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_photo);
        Bundle extras = getIntent().getExtras();
        if (extras != null)
            loadImage(extras.getString(ARG_PHOTO));
    }

    private void loadImage(String url) {
        if (url == null)
            return;
        CustomImageView imageView = findViewById(R.id.photo_button);
        Picasso.with(this).load(new File(url)).into(imageView);
        findViewById(R.id.photo_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewPhotoActivity.this.finish();
            }
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        hide();
    }

    private void hide() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }
}
