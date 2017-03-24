package com.labrosse.suivicommercial.adapter;

import android.util.Log;
import android.widget.CompoundButton;

import com.labrosse.suivicommercial.model.database.SubBPartner;

import java.util.ArrayList;

public class SubBPartnerAdapter extends AbstractAdapter<SubBPartner> {


	public SubBPartnerAdapter(){
	}

	public SubBPartnerAdapter(ArrayList<SubBPartner> subBPartners) {
		super(subBPartners);
	}

	public ArrayList<SubBPartner> getSelectdList() {

		if(mSelectedlist != null ){
			mSelectedlist.clear();
			mSelectedlist = null;
		}

		mSelectedlist = new ArrayList<>();
		for (SubBPartner subBPartner : mList) {
			if (subBPartner.isSelected()) {
				mSelectedlist.add(subBPartner);
			}
		}
		return mSelectedlist;
	}

	public void deselectElements(){

		for(int i = 0 ; i < mList.size() ; i ++){
			if(mList.get(i).isSelected()){
				Log.e("mListisSelected", "" + mList.get(i).getName());
			}
			mList.get(i).setSelected(false);
		}

		notifyDataSetChanged();
	}

	public void addElements(ArrayList<SubBPartner> subBPartners){
		if(mList == null){
			mList = new ArrayList<SubBPartner>();
			mList.addAll(subBPartners);
		}
		else{
			for(SubBPartner subBPartner : subBPartners){
				if(!mList.contains(subBPartner)){
					mList.add(subBPartner);
				}
			}
		}

		notifyDataSetChanged();
	}

	public void addElements(SubBPartner subBPartner){
		if(!mList.contains(subBPartner)){
			mList.add(subBPartner);
		}

		notifyDataSetChanged();
	}

	@Override
	public void customBindViewHolder(AbstractAdapter.ViewHolder viewHolder, int position) {
		final int pos = position;

		viewHolder.tvName.setText(mList.get(position).getName());
		viewHolder.chkSelected.setChecked(mList.get(position).isSelected());

		viewHolder.chkSelected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
				updateList(pos, b);
			}
		});
	}

	private void updateList(int pos, boolean b) {
		mList.get(pos).setSelected(b);
	}

	public void removeSelectedElements() {
		for(SubBPartner subBPartner : getSelectdList()){
			mList.remove(subBPartner);
		}
		notifyDataSetChanged();
	}

}
