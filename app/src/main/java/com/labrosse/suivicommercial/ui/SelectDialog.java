package com.labrosse.suivicommercial.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import java.util.List;

/**
 * Created by ahmedhammami on 09/01/2017.
 */

public class SelectDialog extends DialogFragment{

    public static final String DATA = "items";
    public static final String SELECTED = "selected";

    private SelectionListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        Resources res = getActivity().getResources();
        Bundle bundle = getArguments();

        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());

        dialog.setTitle("Liste des magasins");
        dialog.setPositiveButton("Valider", new PositiveButtonClickListener());

        List<String> list = bundle.getStringArrayList(DATA);
        int selected = bundle.getInt(SELECTED, -1 );

        CharSequence[] cs = list.toArray(new CharSequence[list.size()]);
        dialog.setSingleChoiceItems(cs, selected , selectItemListener);

        return dialog.create();
    }

    public void setListener(SelectionListener listener) {
        this.listener = listener;
    }

    class PositiveButtonClickListener implements DialogInterface.OnClickListener
    {
        @Override
        public void onClick(DialogInterface dialog, int which)
        {
            if ( listener != null )
            {
                listener.validateSelection();
            }
            dialog.dismiss();
        }
    }

    DialogInterface.OnClickListener selectItemListener = new DialogInterface.OnClickListener()
    {

        @Override
        public void onClick(DialogInterface dialog, int which)
        {
            if ( listener != null )
            {
                listener.selectItem(which);
            }

        }

    };

    public interface SelectionListener
    {
        public void selectItem ( int position );
        public void validateSelection ();
    }


}
