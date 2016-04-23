package com.appex.edhelp;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by saketh on 23/4/16.
 */
public class CollegesAdapter extends RecyclerView.Adapter<CollegesAdapter.ViewHolder> {

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
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
