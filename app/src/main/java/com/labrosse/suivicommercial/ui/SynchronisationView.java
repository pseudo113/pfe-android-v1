package com.labrosse.suivicommercial.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.labrosse.suivicommercial.R;

/**
 * Created by ahmedhammami on 22/01/2017.
 */

public class SynchronisationView extends LinearLayout {

    Context mContext;

    private TextView mSynchronisationTextView;
    private ProgressBar mSynchronisationProgressBar;
    private ImageView mSynchronisationStatus;
    private int mTotal;

    private void assignViews() {

        View.inflate(mContext, R.layout.item_synchronisation, this);
        mSynchronisationTextView = (TextView) findViewById(R.id.synchronisation_textView);
        mSynchronisationProgressBar = (ProgressBar) findViewById(R.id.synchronisation_progress_bar);
        mSynchronisationStatus = (ImageView) findViewById(R.id.synchronisation_status);
    }

    public SynchronisationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        assignViews();

    }

    public SynchronisationView(Context context) {
        this(context, null);
    }

    public SynchronisationView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public void setText(int text){
        mSynchronisationTextView.setText(text);
    }

    public void setStatus(int status){
        /*int res = -1;
        switch (status){
            case 0 :

        }*/
        mSynchronisationStatus.setImageResource(status);
    }

    public void setMax(int max){
        setTotal(max);
        mSynchronisationProgressBar.setMax(max);
    }

    public void updateProgess(int update){
        float step = update/getmTotal();
        mSynchronisationProgressBar.setProgress(update);
    }


    public int getmTotal() {
        return mTotal;
    }

    public void setTotal(int mTotal) {
        this.mTotal = mTotal;
    }
}
