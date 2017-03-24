package com.labrosse.suivicommercial.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.labrosse.suivicommercial.R;
import com.labrosse.suivicommercial.adapter.CityDataAdapter;
import com.labrosse.suivicommercial.adapter.SubBPartnerAdapter;
import com.labrosse.suivicommercial.database.dao.BPparcSubDAO;
import com.labrosse.suivicommercial.database.dao.BPparcoursDAO;
import com.labrosse.suivicommercial.database.dao.CityDAO;
import com.labrosse.suivicommercial.database.dao.SubBPartnerDAO;
import com.labrosse.suivicommercial.helper.ExtraKeys;
import com.labrosse.suivicommercial.model.database.AdUser;
import com.labrosse.suivicommercial.model.database.BPparcours;
import com.labrosse.suivicommercial.model.database.City;
import com.labrosse.suivicommercial.model.database.SubBPartner;
import com.labrosse.suivicommercial.service.response.webservice.CityResponse;
import com.labrosse.suivicommercial.service.response.webservice.SubBPartnersResponse;
import com.labrosse.suivicommercial.ui.CustomButton;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class SelectSubBPartnerActivity extends AppCompatActivity  implements SearchView.OnQueryTextListener{

    public static final String JSON_PATH = "/Users/ahmedhammami/Work/Mobile/Android/Project/MyApplication/app/src/main/java/magasin.json";

    private SubBPartnerAdapter mSearchSubBpartnerAdapter;
    private SubBPartnerAdapter mSelectedSubBpartnerAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private ArrayList<SubBPartner> mSubBPartners;
    private ArrayList<City> mCities;

    private CustomButton mSearchZoneButton;
    private CustomButton mSearchFilialeButton;

    private Button mValidateSelectedButton;
    private CustomButton mValidateButton;
    private CustomButton mDeleteFilialeButton;

    private SearchView mSearchView;

    private RecyclerView mSearchSubBpartnerRecyclerView;
    private RecyclerView mSelectedSubBpartnerRecyclerView;

    private boolean mIsSearchByZone = true;

    // Get current User
    private AdUser mCureentUser;
    private CityDataAdapter mCityAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_bpartner);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().hide();

        initViews();
        initRecyclerView();
        initEvents();

        mSearchZoneButton.setSelected();
        mSearchFilialeButton.setDeselected();
        reloadList();

    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open("magasin.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSearchView.clearFocus();
    }

    private void initEvents() {

        mSearchView.setOnQueryTextListener(this);

        mSearchZoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIsSearchByZone = true;
                mSearchZoneButton.setSelected();
                mSearchFilialeButton.setDeselected();
                reloadList();

            }
        });

        mSearchFilialeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIsSearchByZone = false;
                mSearchFilialeButton.setSelected();
                mSearchZoneButton.setDeselected();
                reloadList();
            }
        });

        mValidateSelectedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<SubBPartner> subBPartners = new ArrayList<SubBPartner>();
                if(mIsSearchByZone){
                    ArrayList<City> cities = mCityAdapter.getSelectdList();
                    ArrayList<SubBPartner> subBPartners1 = new ArrayList<SubBPartner>();
                    for(City city : cities){
                        subBPartners1 = SubBPartnerDAO.getInstance(SelectSubBPartnerActivity.this).getSubBPartnerByCity(city.getId());
                        if(subBPartners1 != null){
                            subBPartners.addAll(subBPartners1);
                        }
                    }

                    if(subBPartners != null && subBPartners.isEmpty() == false){
                        mSelectedSubBpartnerAdapter.addElements(subBPartners);
                        mSelectedSubBpartnerAdapter.clearSelected();
                        mSelectedSubBpartnerAdapter.deselectElements();
                    }

                    mCities = null;
                    mCityAdapter = null;
                    mCityAdapter = new CityDataAdapter();
                    reloadList();

                }
                else{
                    subBPartners = mSearchSubBpartnerAdapter.getSelectdList();

                    if(subBPartners != null && subBPartners.isEmpty() == false){
                        mSelectedSubBpartnerAdapter.addElements(subBPartners);
                        mSelectedSubBpartnerAdapter.clearSelected();
                        mSelectedSubBpartnerAdapter.deselectElements();
                    }

                    mSubBPartners = null;
                    mSearchSubBpartnerAdapter = null;
                    mSearchSubBpartnerAdapter = new SubBPartnerAdapter();

                    reloadList();
                }
            }
        });

        mDeleteFilialeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSelectedSubBpartnerAdapter.removeSelectedElements();
            }
        });

        mValidateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            // Get list
            ArrayList<SubBPartner> subBPartners = mSelectedSubBpartnerAdapter.getList();
            if(subBPartners == null || subBPartners.isEmpty()){
                return;
            }

            long id = getIntent().getLongExtra(ExtraKeys.EXTRA_PARCOURS_ID, -1);

            if(id > -1){

                BPparcours parcours = BPparcoursDAO.getInstance(SelectSubBPartnerActivity.this).getParcours(id);

                for(SubBPartner subBPartner : subBPartners){
                    BPparcSubDAO.getInstance(SelectSubBPartnerActivity.this).addBPparcSub(parcours, subBPartner.getC_bpsub_id());
                }

                Intent intent = new Intent(SelectSubBPartnerActivity.this, MapsActivity.class);
                intent.putExtra(ExtraKeys.EXTRA_PARCOURS_ID, id);
                startActivity(intent);
                finish();
            }

            }
        });
    }

    private void reloadList() {

        if(mIsSearchByZone){
            if(mCities == null || mCities.isEmpty()){
                getCities();
            }

            mCityAdapter.setList(mCities);
            mSearchSubBpartnerRecyclerView.setAdapter(mCityAdapter);
        }else{

            if(mSubBPartners == null){
                getSubBPartners();
            }
            mSearchSubBpartnerAdapter.setList(mSubBPartners);
            mSearchSubBpartnerRecyclerView.setAdapter(mSearchSubBpartnerAdapter);
        }
    }

    private void getCities() {
        mCities = CityDAO.getInstance(this).getCities();

        // TODO comment this

        /*if(mCities == null || mCities.isEmpty()){
            String jsonString =  loadJSONFromAsset("cities.json");
            Gson gson = new GsonBuilder().create();
            CityResponse r = gson.fromJson(jsonString, CityResponse.class);

            if( r != null && !r.getCities().isEmpty()){
                mCities = r.getCities();

                for(City partner : mCities){
                    CityDAO.getInstance(this).addCity(partner);
                }
            }
        }*/
    }

    private void initRecyclerView() {

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mSearchSubBpartnerRecyclerView.setHasFixedSize(true);
        mSelectedSubBpartnerRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mSearchSubBpartnerRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mSelectedSubBpartnerRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // create an Object for Adapter
        mSearchSubBpartnerAdapter = new SubBPartnerAdapter();
        mSelectedSubBpartnerAdapter = new SubBPartnerAdapter();
        mCityAdapter = new CityDataAdapter();

        // set the adapter object to the Recyclerview
        mSearchSubBpartnerRecyclerView.setAdapter(mSearchSubBpartnerAdapter);
        mSelectedSubBpartnerRecyclerView.setAdapter(mSelectedSubBpartnerAdapter);

    }

    private void initViews() {
        mSearchZoneButton = (CustomButton) findViewById(R.id.search_zone_button);
        mSearchFilialeButton = (CustomButton) findViewById(R.id.search_filiale_button);
        mSearchView = (SearchView) findViewById(R.id.search_view);
        mSearchSubBpartnerRecyclerView = (RecyclerView) findViewById(R.id.search_sub_bpartner_recycler_view);
        mValidateButton = (CustomButton) findViewById(R.id.validate_button);
        mDeleteFilialeButton = (CustomButton) findViewById(R.id.delete_filiale_button);
        mSelectedSubBpartnerRecyclerView = (RecyclerView) findViewById(R.id.selected_sub_bpartner_recycler_view);
        mValidateSelectedButton = (Button) findViewById(R.id.validate_selected_button);

    }

    private void getSubBPartners() {
        mSubBPartners = SubBPartnerDAO.getInstance(this).getAllSubBPartner();

        // TODO comment this
        /*if(mSubBPartners == null || mSubBPartners.isEmpty()){
            String jsonString =  loadJSONFromAsset();
            Gson gson = new GsonBuilder().create();
            SubBPartnersResponse r = gson.fromJson(jsonString, SubBPartnersResponse.class);

            if( r != null && !r.getSubBPartners().isEmpty()){
                mSubBPartners = r.getSubBPartners();

                for(SubBPartner partner : mSubBPartners){
                    SubBPartnerDAO.getInstance(this).addSubBPartner(partner);
                }
            }
        }*/

        mSearchSubBpartnerAdapter.setList(mSubBPartners);
    }

    private static List<City> CityFilter(List<City> models, String query) {
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

    private static ArrayList<SubBPartner> subBPartnerFilter(ArrayList<SubBPartner> models, String query) {
        final String lowerCaseQuery = query.toLowerCase();

        final ArrayList<SubBPartner> filteredModelList = new ArrayList<>();
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

        if(newText == null || TextUtils.isEmpty(newText)){
            mSearchSubBpartnerAdapter.setList(mSubBPartners);
        }else{
            mSearchSubBpartnerAdapter.setList(subBPartnerFilter(mSubBPartners, newText));
        }
        return false;
    }

    public String loadJSONFromAsset(String file) {
        String json = null;
        try {
            InputStream is = getAssets().open(file);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}

