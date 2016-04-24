package com.appex.edhelp.Activity;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.appex.edhelp.Adapters.CollegesAdapter;
import com.appex.edhelp.Models.College;
import com.appex.edhelp.Models.Favourites;
import com.appex.edhelp.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    static final String url = "https://api.myjson.com/bins/2peim";
    RecyclerView mRecyclerView;
    ProgressDialog mProgressDialog;
    CollegesAdapter collegesAdapter;
    ArrayList<College> collegeArrayList;
    Realm realm;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        android.support.v7.widget.SearchView search = (android.support.v7.widget.SearchView) MenuItemCompat.getActionView(searchItem);
        // Configure the search info and add any event listeners
        SearchManager searchManager = (SearchManager) this.getSystemService(Context.SEARCH_SERVICE);
        search.setSearchableInfo(searchManager.getSearchableInfo(this.getComponentName()));
        search.setIconifiedByDefault(false);
        search.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                collegesAdapter.filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                collegesAdapter.filter(query);
                return false;
            }
        });
        search.setOnCloseListener(new android.support.v7.widget.SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                collegesAdapter.filter("");
                return false;
            }
        });
        search.setSubmitButtonEnabled(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_sort_name) {
            loadData();
        }
        else if (item.getItemId() == R.id.action_sort_branch) {
            final EditText editText = new EditText(getApplicationContext());
            editText.setTextColor(getResources().getColor(R.color.colorPrimary));
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            editText.setLayoutParams(lp);
            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setTitle("Enter The Branch Name")
                    .setView(editText)
                    .setPositiveButton("Search", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            collegesAdapter = new CollegesAdapter(getApplicationContext(), collegeArrayList, editText.getText().toString());
                            mRecyclerView.setAdapter(collegesAdapter);
                        }
                    });
            builder.create().show();
        }
        else if (item.getItemId() == R.id.action_logout){
            LoginActivity.ref.unauth();
            startActivity(new Intent(this, LoginActivity.class));
        }
        else if(item.getItemId() == R.id.action_saved){

            RealmResults<Favourites> favourites = realm.where(Favourites.class).equalTo("userID",LoginActivity.userID)
                    .beginGroup().findAll();
            ArrayList<Favourites> favouritesArrayList = new ArrayList<>(favourites.subList(0,favourites.size()));
            Log.d("Data",Integer.toString(favourites.size()));
            collegesAdapter = new CollegesAdapter(getApplicationContext(), collegeArrayList, favouritesArrayList );
            mRecyclerView.setAdapter(collegesAdapter);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.college_recycler_view);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Loading Data...");
        mProgressDialog.setCancelable(true);
        collegeArrayList = new ArrayList<>();
        RealmConfiguration realmConfig = new RealmConfiguration.Builder(this)
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(realmConfig);
        loadData();
    }

    private void loadData() {

        if (!mProgressDialog.isShowing())
            mProgressDialog.show();
        final JsonArrayRequest request = new
                JsonArrayRequest(Request.Method.GET, url, (String) null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                Log.d("JSON", response.toString());

                try {

                    JSONArray collegeArray = response;
                    for (int i = 0; i < collegeArray.length(); i++) {
                        realm.beginTransaction();
                        College college = realm.createObject(College.class);
                        college.setName(collegeArray.getJSONObject(i).getString("FullCollegeName"));
                        college.setLocation(collegeArray.getJSONObject(i).getString("City"));
                        college.setContact(collegeArray.getJSONObject(i).getString("ContactNo."));
                        college.setIsMains(collegeArray.getJSONObject(i).getString("IsMains?"));
                        college.setPincode(collegeArray.getJSONObject(i).getDouble("Pincode"));
                        college.setId(collegeArray.getJSONObject(i).getInt("id"));
                        college.setState(collegeArray.getJSONObject(i).getString("State"));
                        college.setBranches(collegeArray.getJSONObject(i).getString("Branches"));
                        college.setWebsite(collegeArray.getJSONObject(i).getString("Website"));
                        college.setLat(collegeArray.getJSONObject(i).getDouble("lat"));
                        college.setLng(collegeArray.getJSONObject(i).getDouble("long"));
                        college.setFees(collegeArray.getJSONObject(i).getDouble("Fees/Year"));
                        college.setImage(collegeArray.getJSONObject(i).getString("Image"));
                        college.setDeadline(collegeArray.getJSONObject(i).getString("applicationdeadline"));
                        collegeArrayList.add(college);
                        realm.commitTransaction();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                display();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        Volley.newRequestQueue(getApplicationContext()).add(request);
    }

    private void display() {

        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        collegesAdapter = new CollegesAdapter(getApplicationContext(), collegeArrayList);
        mRecyclerView.setAdapter(collegesAdapter);
        mProgressDialog.dismiss();
    }
}
