package com.labrosse.suivicommercial.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.labrosse.suivicommercial.R;
import com.labrosse.suivicommercial.database.DatabaseHelper;
import com.labrosse.suivicommercial.database.dao.AdUserDAO;
import com.labrosse.suivicommercial.database.dao.BPparcSubDAO;
import com.labrosse.suivicommercial.database.dao.BPparcVisiteDAO;
import com.labrosse.suivicommercial.database.dao.BPparcVisitePictureDAO;
import com.labrosse.suivicommercial.database.dao.BPparcVisiteProductDAO;
import com.labrosse.suivicommercial.database.dao.BPparcoursDAO;
import com.labrosse.suivicommercial.helper.ExtraKeys;
import com.labrosse.suivicommercial.model.database.AdUser;
import com.labrosse.suivicommercial.model.database.BPparcSub;
import com.labrosse.suivicommercial.model.database.BPparcVisite;
import com.labrosse.suivicommercial.model.database.BPparcVisitePicture;
import com.labrosse.suivicommercial.model.database.BPparcVisiteProduct;
import com.labrosse.suivicommercial.model.database.BPparcours;
import com.labrosse.suivicommercial.service.client.GoogleMapsAPIClient;
import com.labrosse.suivicommercial.service.response.map.direction.DirectionResults;
import com.labrosse.suivicommercial.service.response.map.direction.Leg;
import com.labrosse.suivicommercial.service.response.map.direction.Route;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.labrosse.suivicommercial.activity.MapsActivity.FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS;
import static com.labrosse.suivicommercial.activity.MapsActivity.UPDATE_INTERVAL_IN_MILLISECONDS;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener{

    private AdUser mCureentUser;
    private long mCurrentParcoursId;

    private LinearLayout mResumeParcoursLinearLayout;

    private RelativeLayout mLoadingIndicator;

    private CardView mStartNewParcoursCardview;
    private CardView mResumeParcoursCardview;
    private CardView mStopParcoursCardview;
    private CardView mSychroniseCompiereCardview;

    private GoogleApiClient mGoogleClient;
    private Location mCurrentLocation;
    protected LocationRequest mLocationRequest;
    boolean mRequestNewLocation = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Views
        initViews();

        // Events
        initEvents();

        // Update Views
        mCureentUser = AdUserDAO.getInstance(MainActivity.this).getActiveUser();

         // Connect google client
        mLoadingIndicator = (RelativeLayout) findViewById(R.id.loading_indicator);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateViews();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Stop location updates to save battery, but don't disconnect the GoogleApiClient object.
        if (mGoogleClient != null && mGoogleClient.isConnected()) {
            stopLocationUpdates();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        disConnectGoogleClient();
    }

    private void updateViews() {
        mCurrentParcoursId = BPparcoursDAO.getInstance(this).getActiveBPparcoursforUser(mCureentUser);

        if(mCurrentParcoursId == -1 ){
            connectGoogleClient();
            mResumeParcoursLinearLayout.setVisibility(View.GONE);
            mStartNewParcoursCardview.setVisibility(View.VISIBLE);

            mSychroniseCompiereCardview.setCardElevation(4);
            mSychroniseCompiereCardview.setCardBackgroundColor(Color.WHITE);
            mSychroniseCompiereCardview.setClickable(true);

        }
        else{
            mResumeParcoursLinearLayout.setVisibility(View.VISIBLE);
            mStartNewParcoursCardview.setVisibility(View.GONE);

            mSychroniseCompiereCardview.setCardElevation(0);
            mSychroniseCompiereCardview.setCardBackgroundColor(Color.LTGRAY);
            mSychroniseCompiereCardview.setClickable(false);
        }
    }

    private void initViews() {

        mResumeParcoursLinearLayout = (LinearLayout) findViewById(R.id.resume_parcours_linearLayout);

        mStartNewParcoursCardview = (CardView) findViewById(R.id.start_new_parcours_cardview);
        mResumeParcoursCardview = (CardView) findViewById(R.id.resume_parcours_cardview);
        mStopParcoursCardview = (CardView) findViewById(R.id.stop_parcours_cardview);
        mSychroniseCompiereCardview = (CardView) findViewById(R.id.sychronise_compiere_cardview);
    }

    private void initEvents(){

        mStartNewParcoursCardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            mCurrentParcoursId = BPparcoursDAO.getInstance(MainActivity.this).addBPparcours(mCureentUser);

            // TODO Decomment
            if(mCurrentLocation == null){
                mRequestNewLocation = true;
                Toast.makeText(MainActivity.this, "En attente de récupération de votre position", Toast.LENGTH_SHORT).show();
            }
            else{
                String location = mCurrentLocation.getLatitude() + "," + mCurrentLocation.getLongitude();
                BPparcoursDAO.getInstance(MainActivity.this).updateParcoursLocation(mCureentUser.getAd_user_id(), mCurrentParcoursId, location, mCureentUser.getGps());
                Intent intent = new Intent(MainActivity.this, SelectSubBPartnerActivity.class);
                intent.putExtra(ExtraKeys.EXTRA_CURRENT_USER, mCureentUser);
                intent.putExtra(ExtraKeys.EXTRA_PARCOURS_ID, mCurrentParcoursId);
                startActivity(intent);
            }
            }
        });

        mResumeParcoursCardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Update parcours status
                BPparcoursDAO.getInstance(MainActivity.this).updateParcoursStatus(mCureentUser.getAd_user_id(), mCurrentParcoursId, "Y");

                // Check BPparcSub count
                ArrayList<BPparcSub> getBPparcSub = BPparcSubDAO.getInstance(MainActivity.this).getBPparcSub(mCurrentParcoursId);

                // Start next activity
                Intent intent = null;
                if(getBPparcSub.isEmpty()){
                    intent = new Intent(MainActivity.this, SelectSubBPartnerActivity.class);
                }
                else{
                    intent = new Intent(MainActivity.this, MapsActivity.class);
                }

                intent.putExtra(ExtraKeys.EXTRA_PARCOURS_ID, mCurrentParcoursId);
                intent.putExtra(ExtraKeys.EXTRA_CURRENT_USER, mCureentUser);
                startActivity(intent);
            }
        });

        mStopParcoursCardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Save current status
                calculateTraject();
            }
        });

        mSychroniseCompiereCardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SynchroniseActivity.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(mCureentUser.getName().equals("admin")){
            getMenuInflater().inflate(R.menu.main_menu, menu);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.action_save_table){
            new GetSubPatnerTask().execute();
        }

        if(item.getItemId() == R.id.action_clear_table){
            AdUserDAO.getInstance(this).clearTable(DatabaseHelper.TABLE_AD_USER);
            AdUserDAO.getInstance(this).clearTable(DatabaseHelper.TABLE_C_CITY);
            AdUserDAO.getInstance(this).clearTable(DatabaseHelper.TABLE_C_BPSUB);
            AdUserDAO.getInstance(this).clearTable(DatabaseHelper.TABLE_C_BPSUB_PRODUCT);
            AdUserDAO.getInstance(this).clearTable(DatabaseHelper.TABLE_C_BPPARCOURS);
            AdUserDAO.getInstance(this).clearTable(DatabaseHelper.TABLE_C_BPPARC_SUB);
            AdUserDAO.getInstance(this).clearTable(DatabaseHelper.TABLE_C_BPPARC_VISIT);
            AdUserDAO.getInstance(this).clearTable(DatabaseHelper.TABLE_C_BPPARC_VISIT_PIC);
            AdUserDAO.getInstance(this).clearTable(DatabaseHelper.TABLE_C_BPPARC_VISIT_PROD);
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();

        }
        return super.onOptionsItemSelected(item);
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();

        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void connectGoogleClient() {
        if (mGoogleClient == null) {
            mGoogleClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            createLocationRequest();
        }

        if (!mGoogleClient.isConnected()) {
            mGoogleClient.connect();
        } else {
            startLocationUpdates();
        }
    }

    private void disConnectGoogleClient() {
        if (mGoogleClient != null && mGoogleClient.isConnected()) {
            mGoogleClient.disconnect();
        }
    }

    protected void startLocationUpdates() {
        // The final argument to {@code requestLocationUpdates()} is a LocationListener
        // (http://developer.android.com/reference/com/google/android/gms/location/LocationListener.html).
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleClient, mLocationRequest, this);
    }

    protected void stopLocationUpdates() {
        // It is a good practice to remove location requests when the activity is in a paused or
        // stopped state. Doing so helps battery performance and is especially
        // recommended in applications that request frequent location updates.

        // The final argument to {@code requestLocationUpdates()} is a LocationListener
        // (http://developer.android.com/reference/com/google/android/gms/location/LocationListener.html).
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleClient, this);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;

        if(mRequestNewLocation){

            String gps = mCurrentLocation.getLatitude() +","+ mCurrentLocation.getLongitude();
            BPparcoursDAO.getInstance(MainActivity.this).updateParcoursLocation(mCureentUser.getAd_user_id(), mCurrentParcoursId, gps, mCureentUser.getGps());
            Intent intent = new Intent(MainActivity.this, SelectSubBPartnerActivity.class);
            intent.putExtra(ExtraKeys.EXTRA_CURRENT_USER, mCureentUser);
            intent.putExtra(ExtraKeys.EXTRA_PARCOURS_ID, mCurrentParcoursId);
            startActivity(intent);
        }
    }

    public class GetSubPatnerTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            File f=new File("/data/data/com.labrosse.suivicommercial/databases/cupboardTest.db");
            FileInputStream fis=null;
            FileOutputStream fos=null;

            try
            {
                fis=new FileInputStream(f);
                fos=new FileOutputStream("/mnt/sdcard/db_dump.db");
                while(true)
                {
                    int i=fis.read();
                    if(i!=-1)
                    {fos.write(i);}
                    else
                    {break;}
                }
                fos.flush();
                return true;
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            finally
            {
                try
                {
                    fos.close();
                    fis.close();
                }
                catch(IOException ioe)
                {}
            }
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
        }

        @Override
        protected void onCancelled() {
        }
    }

    private void calculateTraject(){

        String origin = mCureentUser.getGps();
        String destination = mCureentUser.getGps();

        String  bPparcVisites = "optimize:true|" ;
        bPparcVisites += BPparcVisiteDAO.getInstance(MainActivity.this).getVisitsForParcours(mCureentUser.getAd_user_id());

        showLoadingDialog();

        Call<DirectionResults> gps = GoogleMapsAPIClient.getInstance().getExploreService().getDirectionJson(origin, destination, bPparcVisites, getString(R.string.google_maps_key));
        gps.enqueue(new Callback<DirectionResults>() {
            @Override
            public void onResponse(Call<DirectionResults> call, Response<DirectionResults> response) {

                if(response.body() != null && !response.body().getRoutes().isEmpty()){
                    int total = 0;
                    for(Route route : response.body().getRoutes()){
                        if (! route.getLegs().isEmpty() ){
                            for(Leg leg : route.getLegs()){
                                total += leg.getDistance().getValue();
                            }
                        }
                    }

                    stopActiveParcours(total);
                }
            }

            @Override
            public void onFailure(Call<DirectionResults> call, Throwable t) {

            }
        });
    }

    private void stopActiveParcours(int total) {
        BPparcVisiteDAO.getInstance(MainActivity.this).stopActiveVisits(mCurrentParcoursId);
        BPparcoursDAO.getInstance(MainActivity.this).updateParcoursThericDistanceDone(mCureentUser.getAd_user_id(), mCurrentParcoursId, total);
        BPparcoursDAO.getInstance(MainActivity.this).stopParcours(mCureentUser.getAd_user_id() , mCurrentParcoursId);
        updateViews();
        Toast.makeText(MainActivity.this, "Total : " + total, Toast.LENGTH_SHORT).show();
        hideLoadingDialog();
    }

    private void showLoadingDialog() {
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    private void hideLoadingDialog() {
        mLoadingIndicator.setVisibility(View.GONE);
    }

    private void getparcours(){
        ArrayList<BPparcours> parcours = BPparcoursDAO.getInstance(this).getBPparcoursforUser(mCureentUser.getAd_user_id());

        for(BPparcours parcour : parcours){
            Log.e("BPparcours" , "BPparcours ID " + parcour.getC_bpparcours_id() + " BPparcours date " + parcour.getStartDate() +  " BPparcours sync " + parcour.getEndDate());
        }

    }

    public class GetDataTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {

            mBPparcourses = BPparcoursDAO.getInstance(MainActivity.this).getParcoursToSynchronise(mCureentUser.getAd_user_id());

            for(BPparcours parcour : mBPparcourses){
                Log.e("BPparcours" , "BPparcours ID " + parcour.getC_bpparcours_id() + " BPparcours date " + parcour.getStartDate() +  " BPparcours sync " + parcour.getSynchronised());
            }

            mBPparcSubs = BPparcSubDAO.getInstance(MainActivity.this).getBPparcSubTosSychronise();

            for(BPparcSub parcour : mBPparcSubs){
                Log.e("mBPparcSubs" , "BPparcours ID " + parcour.getC_bpparcours_id() + " mBPparcSubs ID " + parcour.getC_bpparc_sub_id() +  " mBPparcSubs sync " + parcour.getSynchronised());
            }

            mBPparcVisites = BPparcVisiteDAO.getInstance(MainActivity.this).getVisitsToSynchronise();

            for(BPparcVisite parcour : mBPparcVisites){
                Log.e("mBPparcVisites" , "BPparcours ID " + parcour.getC_bpparcours_id() + " mBPparcVisites ID " + parcour.getC_bpparc_visit_id() +  " mBPparcVisites sync " + parcour.getSynchronised()) ;
            }

            mBPparcVisiteProducts =  BPparcVisiteProductDAO.getInstance(MainActivity.this).getProductsToSychronise();

            for(BPparcVisiteProduct parcour : mBPparcVisiteProducts){
                Log.e("Products" , "mBPparcVisites ID " + parcour.getC_bpparc_visit() + "Products ID " + parcour.getC_bpparc_visit_prod() +  " Products sync " + parcour.getSynchronised());
            }

            mBPparcVisitePictures = BPparcVisitePictureDAO.getInstance(MainActivity.this).getPictureToSychronise();

            for(BPparcVisitePicture parcour : mBPparcVisitePictures){
                Log.e("mBPparcVisitePictures" , "mBPparcVisites ID " + mBPparcVisitePictures.indexOf(parcour));
                Log.e("mBPparcVisitePictures" , "mBPparcVisites ID " + parcour.getC_bpparc_visit_id() + "mBPparcVisitePictures ID " + parcour.getC_visit_pic_id() +  " mBPparcVisitePictures sync " + parcour.getSynchronised());
            }

            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {

        }

        @Override
        protected void onCancelled() {
        }
    }

    private ArrayList<BPparcours> mBPparcourses;

    private ArrayList<BPparcSub> mBPparcSubs;

    private ArrayList<BPparcVisite> mBPparcVisites;

    private ArrayList<BPparcVisiteProduct> mBPparcVisiteProducts;

    private ArrayList<BPparcVisitePicture> mBPparcVisitePictures;
}
