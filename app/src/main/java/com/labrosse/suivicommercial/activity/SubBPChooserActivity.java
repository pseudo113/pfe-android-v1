package com.labrosse.suivicommercial.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.Button;

import com.labrosse.suivicommercial.R;
import com.labrosse.suivicommercial.adapter.SubBPartnerAdapter;
import com.labrosse.suivicommercial.model.database.City;
import com.labrosse.suivicommercial.model.database.SubBPartner;

import java.util.ArrayList;
import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;
import static android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP;

public class SubBPChooserActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private Button btnSelection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_bpchooser);

        ArrayList<SubBPartner> mSubBPartners = new ArrayList<SubBPartner>();

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.sub_bpartner_recycler_view);
        btnSelection = (Button) findViewById(R.id.btnShow);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // create an Object for Adapter
        mAdapter = new SubBPartnerAdapter(mSubBPartners);

        // set the adapter object to the Recyclerview
        mRecyclerView.setAdapter(mAdapter);

        btnSelection.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SubBPChooserActivity.this, MapsActivity.class);
                intent.addFlags(FLAG_ACTIVITY_CLEAR_TOP|FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });

    }

    private static List<City> filter1(List<City> models, String query) {
        final String lowerCaseQuery = query.toLowerCase();

        final List<City> filteredModelList = new ArrayList<>();
        for (City model : models) {
            final String text = model.getName().toLowerCase();
            if (text.contains(lowerCaseQuery)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    private static List<SubBPartner> filter(List<SubBPartner> models, String query) {
        final String lowerCaseQuery = query.toLowerCase();

        final List<SubBPartner> filteredModelList = new ArrayList<>();
        for (SubBPartner model : models) {
            final String text = model.getName().toLowerCase();
            if (text.contains(lowerCaseQuery)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
