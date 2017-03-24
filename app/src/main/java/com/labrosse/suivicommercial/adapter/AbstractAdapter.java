package com.labrosse.suivicommercial.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.labrosse.suivicommercial.R;

import java.util.ArrayList;

/**
 * Created by ahmedhammami on 02/01/2017.
 */

public abstract class AbstractAdapter<E> extends RecyclerView.Adapter<AbstractAdapter.ViewHolder> {

    ArrayList<E> mList;
    ArrayList<E> mSelectedlist;

    public AbstractAdapter(ArrayList<E> list) {
        this.mList = list;
    }

    public AbstractAdapter() {
    }

    public ArrayList<E> getList() {
        return mList;
    }

    public void clearSelected(){
        if(mSelectedlist != null){
            mSelectedlist = null;
        }
    }

    public void addSelectedItem(E item){
        if(mSelectedlist ==  null){
            mSelectedlist = new ArrayList<E>();
        }
        mSelectedlist.add(item);
    }

    public void setList(ArrayList<E> mList) {
        if(this.mList == null){
            this.mList = new ArrayList<>();
        }
        this.mList = mList;
        notifyDataSetChanged();
    }

    @Override
    public AbstractAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.cardview_row, parent, false);

        // create ViewHolder
        AbstractAdapter.ViewHolder viewHolder = new AbstractAdapter.ViewHolder(itemLayoutView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AbstractAdapter.ViewHolder holder, int position) {
        customBindViewHolder(holder, position);
    }

    @Override
    public int getItemCount() {
        return (mList == null) ? 0 : mList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvName;
        public CheckBox chkSelected;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);

            tvName = (TextView) itemLayoutView.findViewById(R.id.tvEmailId);
            chkSelected = (CheckBox) itemLayoutView
                    .findViewById(R.id.chkSelected);

        }
    }

    abstract public void customBindViewHolder(AbstractAdapter.ViewHolder holder, int position);
}
