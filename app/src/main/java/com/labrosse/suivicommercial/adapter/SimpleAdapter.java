package com.labrosse.suivicommercial.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.labrosse.suivicommercial.model.database.ExtendedParcoursSubPartner;

import java.util.List;

/**
 * Created by user on 14/01/2017.
 */

public class SimpleAdapter extends RecyclerView.Adapter<SimpleAdapter.SimpleViewHolder> {

    private final Context mContext;
    private List<ExtendedParcoursSubPartner> mData;

    public void add(ExtendedParcoursSubPartner s,int position) {
        position = position == -1 ? getItemCount() : position;
        mData.add(position,s);
        notifyItemInserted(position);
    }

    public void setLis(List<ExtendedParcoursSubPartner> Data){
        this.mData = Data;
        notifyDataSetChanged();
    }

    public void remove(int position){
        if (position < getItemCount()  ) {
            mData.remove(position);
            notifyItemRemoved(position);
        }
    }

    public static class SimpleViewHolder extends RecyclerView.ViewHolder {
        public final TextView title;

        public SimpleViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(android.R.id.text1);
        }
    }

    public SimpleAdapter(Context context) {
        mContext = context;
    }

    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder holder, final int position) {
        holder.title.setText(mData.get(position).getName());
        if(mData.get(position).getProcesseing().equals("Y")){
            holder.title.setTypeface(Typeface.DEFAULT_BOLD);
            holder.title.setTextColor(Color.BLACK);
        }else{
            holder.title.setTypeface(Typeface.DEFAULT);
            holder.title.setTextColor(Color.LTGRAY);
        }
    }

    @Override
    public int getItemCount() {
        return mData != null ? mData.size() : 0;
    }
}