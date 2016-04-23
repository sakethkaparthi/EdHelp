package com.appex.edhelp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.appex.edhelp.Activity.CollegeInfoActivity;
import com.appex.edhelp.Models.College;
import com.appex.edhelp.R;

import java.util.ArrayList;

/**
 * Created by saketh on 23/4/16.
 */
public class CollegesAdapter extends RecyclerView.Adapter<CollegesAdapter.ViewHolder> {

    Context mContext;
    ArrayList<College> collegeArrayList;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.college_brief_layout,parent,false);
        return new ViewHolder(view);
    }

    public CollegesAdapter(Context mContext, ArrayList<College> collegeArrayList) {
        this.mContext = mContext;
        this.collegeArrayList = collegeArrayList;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final College college = collegeArrayList.get(position);
        holder.collegeNameTextView.setText(college.getName());
        holder.addressTextView.setText(college.getLocation());
        holder.contactTextView.setText(college.getContact());
        holder.cardView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, CollegeInfoActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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
            cardView =(CardView) v.findViewById(R.id.eventCards);
            collegeNameTextView = (TextView)v.findViewById(R.id.college_name);
            addressTextView = (TextView)v.findViewById(R.id.college_location);
            contactTextView = (TextView)v.findViewById(R.id.college_contact);
        }
    }
}
