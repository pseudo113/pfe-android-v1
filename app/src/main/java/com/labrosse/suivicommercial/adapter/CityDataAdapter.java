package com.labrosse.suivicommercial.adapter;

import android.view.View;
import android.widget.CheckBox;

import com.labrosse.suivicommercial.model.database.City;

import java.util.ArrayList;

public class CityDataAdapter extends AbstractAdapter<City> {

    public CityDataAdapter(ArrayList<City> students) {
        super(students);
    }

    public CityDataAdapter() {

    }

    @Override
    public void customBindViewHolder(ViewHolder viewHolder, int position) {
        final int pos = position;
        viewHolder.tvName.setText(mList.get(position).getName());

        viewHolder.chkSelected.setTag(mList.get(position));

        viewHolder.chkSelected.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                City city = (City) cb.getTag();
                city.setSelected(cb.isChecked());

                if (cb.isChecked()) {
                    addSelectedItem(city);
                }
            }
        });
    }

    public ArrayList<City> getSelectdList() {

        if(mSelectedlist != null ){
            mSelectedlist.clear();
            mSelectedlist = null;
        }

        mSelectedlist = new ArrayList<>();
        for (City subBPartner : mList) {
            if (subBPartner.isSelected()) {
                mSelectedlist.add(subBPartner);
            }
        }
        return mSelectedlist;
    }


}
