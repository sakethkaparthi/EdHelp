package com.appex.edhelp.Activity;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.appex.edhelp.R;
import com.bumptech.glide.Glide;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ImageView imageView = (ImageView) findViewById(R.id.college_image_view);
        Glide.with(this).load("https://lh3.googleusercontent.com/-Wwn3Zq54Oy8/AAAAAAAAAAI/AAAAAAAAABI/OOYfPYANtOg/s0-c-k-no-ns/photo.jpg")
                .into(imageView);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
    }
}
