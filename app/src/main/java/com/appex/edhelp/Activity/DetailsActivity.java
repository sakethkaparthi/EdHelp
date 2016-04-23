package com.appex.edhelp.Activity;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.appex.edhelp.Models.College;
import com.appex.edhelp.R;
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
        RealmConfiguration realmConfig = new RealmConfiguration.Builder(this)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm realm = Realm.getInstance(realmConfig);
        int id = getIntent().getExtras().getInt("id");
        RealmResults<College> colleges = realm.where(College.class).equalTo("id", id)
                .beginGroup().findAll();
        College college  = colleges.get(0);
        getSupportActionBar().setTitle(college.getName());
        ImageView imageView = (ImageView) findViewById(R.id.college_image_view);
        Glide.with(this).load(college.getImage())
                .into(imageView);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);


    }
}
