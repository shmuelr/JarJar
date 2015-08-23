package com.sdr.jarjar;

import android.location.Location;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sdr.jarjar.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by user on 8/23/15.
 */
public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int LOCATION_VIEW_ROW = 0;
    public static final int HEADER_VIEW_ROW = 1;


    public static class LocationViewHolder extends RecyclerView.ViewHolder{
        public TextView textViewAccuracyLevel;

        public LocationViewHolder(View itemView) {
            super(itemView);
            textViewAccuracyLevel = (TextView) itemView.findViewById(R.id.tvAccuracy);
        }
    }

    public static class HeaderViewHolder extends  RecyclerView.ViewHolder{

        public HeaderViewHolder(View itemView) {
            super(itemView);
        }
    }

    private List<Object> objectsList = new ArrayList<>();

    public void addItem(Object o){
        objectsList.add(o);
        notifyItemInserted(objectsList.size()-1);
    }

    public Object getLastItem(){
        if(objectsList.size() > 0)
            return objectsList.get(objectsList.size()-1);
        else
            return new Object();
    }

    @Override
    public int getItemViewType(int position) {
        Object item = objectsList.get(position);
        return (item instanceof Location) ? LOCATION_VIEW_ROW : HEADER_VIEW_ROW;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        int resID;

        if(viewType == HEADER_VIEW_ROW){
            resID = R.layout.list_item_header;
        }else {
            resID = R.layout.list_item_location;
        }


        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(resID,
                        viewGroup,
                        false);

        RecyclerView.ViewHolder viewHolder;

        if(viewType == HEADER_VIEW_ROW){
            viewHolder = new HeaderViewHolder(itemView);
        }else {
            viewHolder = new LocationViewHolder(itemView);
        }


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if(viewHolder instanceof LocationViewHolder){
            Object item = objectsList.get(position);

            ((LocationViewHolder) viewHolder)
                    .textViewAccuracyLevel
                    .setText(
                            String.valueOf(((Location) item).getAccuracy())
                    );

        }
    }

    @Override
    public int getItemCount() {
        return objectsList.size();
    }


}
