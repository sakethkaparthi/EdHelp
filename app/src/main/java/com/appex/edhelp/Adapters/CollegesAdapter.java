package com.appex.edhelp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.appex.edhelp.Activity.DetailsActivity;
import com.appex.edhelp.Models.College;
import com.appex.edhelp.Models.Favourites;
import com.appex.edhelp.R;

import java.util.ArrayList;

/**
 * Created by saketh on 23/4/16.
 */
public class CollegesAdapter extends RecyclerView.Adapter<CollegesAdapter.ViewHolder> {

    Context mContext;
    ArrayList<College> collegeArrayList;
    ArrayList<College> allColleges;
    String branch;
    int colors[] = {R.color.a,R.color.b,R.color.c,R.color.d,R.color.e};
    public CollegesAdapter(Context mContext,ArrayList<College> collegeArray, String branch) {
        this.collegeArrayList = new ArrayList<>();
        for (College college : collegeArray) {
            String[] branches = college.getBranches().split(",", 0);
            for (String br : branches)
                if (br.toLowerCase().contains(branch.toLowerCase()))
                    collegeArrayList.add(college);
        }
        this.mContext = mContext;
        this.branch = branch;
    }

    public CollegesAdapter(Context mContext, ArrayList<College> collegeArray, ArrayList<Favourites> favourites){
        this.collegeArrayList = new ArrayList<>();
        for(College college : collegeArray){
            for(int i = 0; i < favourites.size(); i++)
                if(favourites.get(i).getId() == college.getId())
                    collegeArrayList.add(college);
        }
        this.mContext = mContext;
        this.branch = null;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.college_brief_layout, parent, false);
        return new ViewHolder(view);
    }

    public CollegesAdapter(Context mContext, ArrayList<College> collegeArrayList) {
        this.mContext = mContext;
        this.collegeArrayList = collegeArrayList;
        allColleges = new ArrayList<>();
        allColleges.addAll(collegeArrayList);
        branch = null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        int k = colors[position % colors.length];
        final College college = collegeArrayList.get(position);
        holder.collegeNameTextView.setText(college.getName());
        holder.addressTextView.setText(college.getLocation());
        holder.contactTextView.setText(college.getContact());
        holder.cardView.setCardBackgroundColor(ContextCompat.getColor(mContext,k));
        holder.cardView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, DetailsActivity.class);
                        intent.putExtra("id", college.getId());
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);
                    }
                }
        );


    }

    @Override
    public int getItemCount() {
        return collegeArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public CardView cardView;
        public TextView collegeNameTextView;
        public TextView addressTextView;
        public TextView contactTextView;

        public ViewHolder(View v) {
            super(v);
            cardView = (CardView) v.findViewById(R.id.eventCards);
            collegeNameTextView = (TextView) v.findViewById(R.id.college_name);
            addressTextView = (TextView) v.findViewById(R.id.college_location);
            contactTextView = (TextView) v.findViewById(R.id.college_contact);
        }
    }

    public void filter(String query) {
        Log.d("query", query);
        collegeArrayList.clear();
        for (College college : allColleges) {
            Log.d("college", college.getName());
            if (college.getName().toLowerCase().contains(query.toLowerCase()))
                collegeArrayList.add(college);
        }
        notifyDataSetChanged();
    }
}
