package com.appex.edhelp;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.appex.edhelp.Models.College;
import com.bumptech.glide.Glide;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

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
        RealmConfiguration realmConfig = new RealmConfiguration.Builder(this).build();
        Realm realm = Realm.getInstance(realmConfig);
        int id = getIntent().getExtras().getInt("id");
        RealmResults<College> colleges = realm.where(College.class).equalTo("id", id)
                .beginGroup().findAll();
        College college  = colleges.get(0);
        getSupportActionBar().setTitle(college.getName());
    }
}
