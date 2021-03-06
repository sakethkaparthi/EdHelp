package com.appex.edhelp.Activity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.appex.edhelp.Models.College;
import com.appex.edhelp.Models.Favourites;
import com.appex.edhelp.R;
import com.bumptech.glide.Glide;

import chipset.potato.Potato;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class DetailsActivity extends AppCompatActivity {

    FloatingActionButton mapFloatingActionButton;
    TextView favouriteTextView;
    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        RealmConfiguration realmConfig = new RealmConfiguration.Builder(this)
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(realmConfig);
        int id = getIntent().getExtras().getInt("id");
        RealmResults<College> colleges = realm.where(College.class).equalTo("id", id)
                .beginGroup().findAll();
        final College college  = colleges.get(0);
        getSupportActionBar().setTitle(college.getName());
        ImageView imageView = (ImageView) findViewById(R.id.college_image_view);
        Glide.with(this).load(college.getImage())
                .into(imageView);
        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        Resources res = getResources();
        TextView branchesTextView = (TextView) findViewById(R.id.college_branches);
        TextView feesTextView = (TextView) findViewById(R.id.college_fees);
        TextView applicationTextView = (TextView) findViewById(R.id.college_application);
        TextView deadlineTextView = (TextView) findViewById(R.id.college_deadline);
        TextView websiteTextView = (TextView) findViewById(R.id.college_website);
        favouriteTextView = (TextView) findViewById(R.id.favourites_textview);

        CharSequence branches = Html.fromHtml(String.format(res.getString(R.string.branches)));
        branchesTextView.setText(branches);

        CharSequence fees = Html.fromHtml(String.format(res.getString(R.string.fees),Math.round(college.getFees())));
        feesTextView.setText(fees);

        CharSequence application = Html.fromHtml(String.format(res.getString(R.string.application),college.getIsMains()));
        applicationTextView.setText(application);

        CharSequence deadline = Html.fromHtml(String.format(res.getString(R.string.deadline),college.getDeadline()));
        deadlineTextView.setText(deadline);

        CharSequence website = Html.fromHtml(String.format(res.getString(R.string.website)));
        websiteTextView.setText(website);

        websiteTextView.append(
                Html.fromHtml("<a href=\""+college.getWebsite()+"\">"+college.getName()+"</a>")
        );

        String branchList[] = college.getBranches().split(",");
        for(String branch : branchList)
            branchesTextView.append("\n"+branch);

        mapFloatingActionButton = (FloatingActionButton) findViewById(R.id.map_floating_button);
        mapFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MapsActivity.class);
                intent.putExtra("lat",college.getLat());
                intent.putExtra("long",college.getLng());
                intent.putExtra("name",college.getName());
                startActivity(intent);
            }
        });

        favouriteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                realm.beginTransaction();
                Favourites favourites = realm.createObject(Favourites.class);
                favourites.setUserID(Potato.potate(getApplicationContext()).Preferences().getSharedPreferenceString("uid"));
                favourites.setId(college.getId());
                realm.commitTransaction();
                Toast.makeText(DetailsActivity.this, "Added to favorites", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
