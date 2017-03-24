package com.labrosse.suivicommercial.ui;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.Button;

import com.labrosse.suivicommercial.R;

/**
 * Created by ahmedhammami on 01/01/2017.
 */

public class CustomButton extends Button {

    private boolean mIsSelected;

    public CustomButton(Context context) {
        super(context);
        mIsSelected = false;
    }

    public CustomButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void setSelected(){
        setBackgroundResource(R.drawable.selected_button);
        setTextColor(Color.WHITE);
        mIsSelected = true;
    }

    public void setDeselected(){
        setBackgroundResource(R.drawable.unselected_button);
        setTextColor(Color.BLUE);
        mIsSelected = false;
    }

    public boolean isSelected (){
        return mIsSelected;
    }

}
