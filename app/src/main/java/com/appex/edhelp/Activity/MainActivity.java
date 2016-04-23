package com.appex.edhelp.Activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.appex.edhelp.Adapters.CollegesAdapter;
import com.appex.edhelp.Models.College;
import com.appex.edhelp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    static final String url = "https://api.myjson.com/bins/41tkm";
    RecyclerView mRecyclerView;
    ProgressDialog mProgressDialog;
    CollegesAdapter collegesAdapter;
    ArrayList<College> collegeArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.college_recycler_view);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Loading Data...");
        mProgressDialog.setCancelable(true);
        loadData();
    }

    private void loadData(){

        if(!mProgressDialog.isShowing())
            mProgressDialog.show();
        final JsonObjectRequest request = new
                JsonObjectRequest(Request.Method.GET, url, (String) null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.d("JSON", response.toString());

                try{
                    JSONArray collegeArray = response.getJSONArray("");
                    for(int i = 0; i < collegeArray.length(); i++){

                        College college = new College();
                        college.setName(collegeArray.getJSONObject(i).getString("Full College Name"));
                        college.setLocation(collegeArray.getJSONObject(i).getString("City"));
                        college.setContact(collegeArray.getJSONObject(i).getString("Contact No."));
                        college.setIsMains(collegeArray.getJSONObject(i).getString("Is Mains?"));
                        college.setPincode(collegeArray.getJSONObject(i).getDouble("Pincode"));
                        college.setId(collegeArray.getJSONObject(i).getInt("id"));
                        college.setState(collegeArray.getJSONObject(i).getString("State"));
                        college.setBranches(collegeArray.getJSONObject(i).getString("Branches"));
                        collegeArrayList.add(college);
                    }
                }
                catch (JSONException e){
                    e.printStackTrace();
                }
                display();
            }

            },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        Volley.newRequestQueue(getApplicationContext()).add(request);
    }

    private void display(){

        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        collegesAdapter = new CollegesAdapter(getApplicationContext(),collegeArrayList);
        mRecyclerView.setAdapter(collegesAdapter);
        mProgressDialog.dismiss();
    }
}
