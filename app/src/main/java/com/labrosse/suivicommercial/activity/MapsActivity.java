package com.labrosse.suivicommercial.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.labrosse.suivicommercial.R;
import com.labrosse.suivicommercial.adapter.SimpleAdapter;
import com.labrosse.suivicommercial.database.dao.BPparcoursDAO;
import com.labrosse.suivicommercial.helper.ExtraKeys;
import com.labrosse.suivicommercial.helper.SharedPrefsHelper;
import com.labrosse.suivicommercial.model.database.ExtendedParcoursSubPartner;
import com.labrosse.suivicommercial.model.database.SubBPartner;
import com.labrosse.suivicommercial.service.client.GoogleMapsAPIClient;
import com.labrosse.suivicommercial.service.response.map.distance.DirectionModel;
import com.labrosse.suivicommercial.ui.SelectDialog;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, SelectDialog.SelectionListener {

    private GoogleMap mMap;
    private GoogleApiClient mGoogleClient;
    private Location mCurrentLocation;

    protected LocationRequest mLocationRequest;
    private CameraPosition.Builder mCameraPositionBuilder = new CameraPosition.Builder();
    private ArrayList<ExtendedParcoursSubPartner> mExtendedParcoursSubPartnerArrayList;
    private ArrayList<ExtendedParcoursSubPartner> mCurrentVisite;

    private ExtendedParcoursSubPartner mActiveVisit;

    private RecyclerView mVisitsRecyclerView;

    private RelativeLayout mLoadingIndicator;

    private int mClosestSubBPartner = -1;
    private String mSubBPartnersGps = "";

    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */

    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 240000;

    /**
     * The fastest rate for active location updates. Exact. Updates will never be more frequent
     * than this value.
     */

    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    /**
     * Durations
     */

    private static final long INTER_PLOLATION_DURATION = 1500;

    /**
     * Requests
     */

    private static final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 10;
    private static final int TWO_MINUTES = 1000 * 60 * 2;

    private long mCurrentParcoursId;

    SimpleAdapter mVisiteAdapter;

    FloatingActionButton mFloatingActionButton;
    private int mCurrentSubParnter = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mLoadingIndicator = (RelativeLayout) findViewById(R.id.loading_indicator);

        mVisitsRecyclerView = (RecyclerView) findViewById(R.id.visits_recycler_view);

        mVisitsRecyclerView.setHasFixedSize(true);

        mVisitsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mVisiteAdapter = new SimpleAdapter(this);

        mVisitsRecyclerView.setAdapter(mVisiteAdapter);

        mFloatingActionButton = (FloatingActionButton) findViewById(R.id.fab);

        mFloatingActionButton.setEnabled(false);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            if(mActiveVisit != null){
                startVisitActivity(mActiveVisit, false, getCurrentLocation());
            }else {
                mCurrentSubParnter = -1;
                SelectDialog dialog = new SelectDialog();
                dialog.setListener(MapsActivity.this);

                Bundle bundle = new Bundle();
                bundle.putStringArrayList(SelectDialog.DATA, getItems());
                bundle.putInt(SelectDialog.SELECTED, mClosestSubBPartner);

                dialog.setArguments(bundle);
                dialog.show(getSupportFragmentManager(), "Dialog");
            }

            }
        });

        mCurrentParcoursId = getIntent().getLongExtra(ExtraKeys.EXTRA_PARCOURS_ID, -1);

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    private ArrayList<String> getItems() {

        ArrayList<String> items = new ArrayList<>();
        for (ExtendedParcoursSubPartner partner1 : mExtendedParcoursSubPartnerArrayList) {
            items.add(partner1.getName());
        }

        return items;
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mMap != null) {
            connectGoogleClient();
        }

        if (mCurrentParcoursId > -1) {
            new GetSubPatnerTask().execute();
        }
        else {
            finish();
        }
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

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        mMap.setMyLocationEnabled(true);

       /* // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/
        connectGoogleClient();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    }

    private void getMyLocation(float... bearings) {

        if (mCurrentLocation != null) {

            double longitude = mCurrentLocation.getLongitude();
            double latitude = mCurrentLocation.getLatitude();

            float bearing = 0;

            if (bearings != null && bearings.length == 1) {
                bearing = bearings[0];
            }

            if(mCameraPositionBuilder == null) {
                mCameraPositionBuilder = new CameraPosition.Builder();
            }

            mCameraPositionBuilder = mCameraPositionBuilder.target(new LatLng(latitude, longitude)).zoom(13f);

            if(bearing > 0){
                mCameraPositionBuilder.bearing(bearing)  ;
            }

            CameraPosition cameraPosition = mCameraPositionBuilder.build();

            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        }

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

    /**
    * GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener
    */

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

    @Override
    public void onConnected(Bundle bundle) {
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        if (mGoogleClient != null) {
            mGoogleClient.connect();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    /**
     * LocationListener
     */

    @Override
    public void onLocationChanged(Location location) {

        if (isBetterLocation(location, mCurrentLocation)) {

            float b;
            boolean useBearing = false;

            if (mCurrentLocation != null) {

                b = mCurrentLocation.bearingTo(location);

                float distance = mCurrentLocation.distanceTo(location);
                int retval = Float.compare(distance, 1.0f);
                if (retval > 0) {
                    useBearing = true;
                }
            }
            else {
                b = location.getBearing();
            }

            mCurrentLocation = location;


            SharedPrefsHelper.setListPreferences(this, R.string.commercial_position_list, mCurrentLocation.toString());

            if (useBearing) {
                getMyLocation(b);
            } else {
                getMyLocation();
            }

            if(!mSubBPartnersGps.equals("")){
                Call<DirectionModel> gps = GoogleMapsAPIClient.getInstance().getExploreService().getDistanceJson( getCurrentLocation(), mSubBPartnersGps.toString(), getString(R.string.google_maps_key));
                gps.enqueue(new Callback<DirectionModel>() {
                    @Override
                    public void onResponse(Call<DirectionModel> call, Response<DirectionModel> response) {
                        if(response.body() != null){
                            DirectionModel directionModel = response.body();
                            mClosestSubBPartner = directionModel.getClosetsLocation();
                        }
                    }

                    @Override
                    public void onFailure(Call<DirectionModel> call, Throwable t) {

                    }
                });
            }
        }
    }

    /**
     * Removes location updates from the FusedLocationApi.
     */

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

    /**
     * Determines whether one Location reading is better than the current Location fix
     *
     * @param location            The new Location that you want to evaluate
     * @param currentBestLocation The current Location fix, to which you want to compare the new one
     */

    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }

    /**
     * Checks whether two providers are the same
     */

    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }

    private void addMarkerOptions(Location trafficLocation) {

        Marker marker = null;
        marker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(trafficLocation.getLatitude(), trafficLocation.getLongitude()))
                .icon(BitmapDescriptorFactory.defaultMarker()));
    }

    @Override
    public void selectItem(int position) {
        mCurrentSubParnter = position;
    }

    @Override
    public void validateSelection() {
        int position = -1;

        if(mClosestSubBPartner != -1 ){
            position = mClosestSubBPartner;
        }

        if(mCurrentSubParnter != -1 ){
            position = mCurrentSubParnter;
        }

        if(position != -1 ){
            startVisitActivity(mExtendedParcoursSubPartnerArrayList.get(position), true, getCurrentLocation());
        }
    }

    private void startVisitActivity(ExtendedParcoursSubPartner subPartner, boolean isNewVisite, String gps){
        Intent intent = new Intent(MapsActivity.this, VisitActivity.class);
        intent.putExtra("SUB_PARTNER", subPartner);
        intent.putExtra("IS_NEW_VISIT", isNewVisite);
        intent.putExtra("SUB_PARTNER_LOCATION", gps);
        startActivity(intent);
    }

    public class GetSubPatnerTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showLoadingDialog();
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            mExtendedParcoursSubPartnerArrayList = BPparcoursDAO.getInstance(MapsActivity.this).getExtendedSubPartners(mCurrentParcoursId);
            mCurrentVisite = BPparcoursDAO.getInstance(MapsActivity.this).getProcessingVisit(mCurrentParcoursId);
            mActiveVisit = BPparcoursDAO.getInstance(MapsActivity.this).getActiveVisit(mCurrentParcoursId);

            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            hideLoadingDialog();
            mFloatingActionButton.setEnabled(true);

            if( mCurrentVisite !=  null ){
                mVisiteAdapter.setLis(null);
                mVisiteAdapter.setLis(mCurrentVisite);
            }

            if(mSubBPartnersGps.equals("")){

                StringBuilder gpsVisites = new StringBuilder("");
                int i =  0;
                for(ExtendedParcoursSubPartner subPartner :  mExtendedParcoursSubPartnerArrayList){
                    if( subPartner.getGps() != null && !subPartner.getGps().equals("")) {
                        if (i == 0) {
                            gpsVisites.append(subPartner.getGps());
                        } else {
                            gpsVisites.append("|").append(subPartner.getGps());
                        }
                        i++;
                    }
                }

                mSubBPartnersGps = gpsVisites.toString();
            }
        }

        @Override
        protected void onCancelled() {
            hideLoadingDialog();
        }
    }

    private void showLoadingDialog() {
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    private void hideLoadingDialog() {
        mLoadingIndicator.setVisibility(View.GONE);
    }

    private String getCurrentLocation(){
        if(mCurrentLocation != null){
            return mCurrentLocation.getLatitude() + "," + mCurrentLocation.getLongitude();
        }
        else{
            return "";
        }
    }

}
