package com.labrosse.suivicommercial.adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.labrosse.suivicommercial.R;
import com.labrosse.suivicommercial.database.dao.BPparcVisiteProductDAO;
import com.labrosse.suivicommercial.model.database.ExtendedBPparcVisiteProduct;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class VisitProductDataAdapter extends RecyclerView.Adapter<VisitProductDataAdapter.ViewHolder> {

	private ArrayList<ExtendedBPparcVisiteProduct> mProductList;
	private Context mContext;
	private boolean mIsEditMod = false;

	public VisitProductDataAdapter(Context context) {
		this.mContext = context;
		this.mProductList = new ArrayList<>();
	}

	public VisitProductDataAdapter(Context context, ArrayList<ExtendedBPparcVisiteProduct> products) {
		this.mContext = context;
		this.mProductList = products;
	}

	public void setProductList(ArrayList<ExtendedBPparcVisiteProduct> products){
		this.mProductList = products;
		notifyDataSetChanged();
	}

	// Create new views
	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(
				R.layout.item_product, parent, false);

		// create ViewHolder
		ViewHolder viewHolder = new ViewHolder(itemLayoutView, new MyTextWatcher());
		return viewHolder;
	}

	@Override
	public void onBindViewHolder(ViewHolder viewHolder, final int position) {
		ExtendedBPparcVisiteProduct product = mProductList.get(position);
		viewHolder.product_value_textView.setText(product.getProduct_value());
		viewHolder.product_name_textView.setText(product.getProduct_name());
		viewHolder.myCustomEditTextListener.updatePosition(viewHolder.getAdapterPosition());

		int qty = mProductList.get(viewHolder.getAdapterPosition()).getQty();
		viewHolder.quantity.setText(String.valueOf(qty));

		if(position == mProductList.size() - 1){
			setmIsEditMod(true);
		}

		Log.e("Position" ,"" +position);


	}

	// Return the size arraylist
	@Override
	public int getItemCount() {
		return (mProductList ==  null ? 0 : mProductList.size());
	}

	public ArrayList<ExtendedBPparcVisiteProduct> getProductList() {
		return mProductList;
	}

	public boolean isEditMod() {
		return mIsEditMod;
	}

	public void setmIsEditMod(boolean mIsEditMod) {
		this.mIsEditMod = mIsEditMod;
	}

	public static class ViewHolder extends RecyclerView.ViewHolder {

		public TextView product_value_textView;
		public TextView product_name_textView;
		public EditText quantity;
		public MyTextWatcher myCustomEditTextListener;

		public ViewHolder(View itemLayoutView, MyTextWatcher myCustomEditTextListener) {
			super(itemLayoutView);

			product_value_textView = (TextView) itemLayoutView.findViewById(R.id.product_value_textView);
			product_name_textView = (TextView) itemLayoutView.findViewById(R.id.product_name_textView);

			quantity = (EditText) itemLayoutView.findViewById(R.id.quantity);
			this.myCustomEditTextListener = myCustomEditTextListener;
			quantity.addTextChangedListener(myCustomEditTextListener);
		}
	}

	public class MyTextWatcher implements TextWatcher {
		private Timer timer = new Timer();
		private final long DELAY = 1000; // in

		public MyTextWatcher() {

		}

		private int position;

		public void updatePosition(int position) {
			this.position = position;
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {

		}

		@Override
		public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
			if(!TextUtils.isEmpty(charSequence)){
				try{
					int newValue = Integer.valueOf(charSequence.toString());
					mProductList.get(position).setQty(newValue);

					if(timer != null)
						timer.cancel();

				}catch (NumberFormatException exp){
					Log.e("VisitProductDataAdapter", "NumberFormatException" , exp);
				}
			}
		}

		@Override
		public void afterTextChanged(Editable s) {

			if(isEditMod()){

				timer = new Timer();
				timer.schedule(new TimerTask() {
					@Override
					public void run() {
						if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
							new SaveProductTask(mProductList.get(position)).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
						}
						else {
							new SaveProductTask(mProductList.get(position)).execute();
						}
					}
				}, DELAY);
			}
		}
	}

	public class SaveProductTask extends AsyncTask<Void, Void, Boolean> {

		private ExtendedBPparcVisiteProduct mProduct;

		public SaveProductTask(ExtendedBPparcVisiteProduct mProduct) {
			this.mProduct = mProduct;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			int update = BPparcVisiteProductDAO.getInstance(mContext).updateProduct(mProduct);
			return update > 0;
		}

		@Override
		protected void onPostExecute(final Boolean success) {
		}

		@Override
		protected void onCancelled() {
		}
	}

}
