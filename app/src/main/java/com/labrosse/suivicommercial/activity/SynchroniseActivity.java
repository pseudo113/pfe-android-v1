package com.labrosse.suivicommercial.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.labrosse.suivicommercial.R;
import com.labrosse.suivicommercial.database.dao.AdUserDAO;
import com.labrosse.suivicommercial.database.dao.BPparcSubDAO;
import com.labrosse.suivicommercial.database.dao.BPparcVisiteDAO;
import com.labrosse.suivicommercial.database.dao.BPparcVisitePictureDAO;
import com.labrosse.suivicommercial.database.dao.BPparcVisiteProductDAO;
import com.labrosse.suivicommercial.database.dao.BPparcoursDAO;
import com.labrosse.suivicommercial.database.dao.CityDAO;
import com.labrosse.suivicommercial.database.dao.ProductDAO;
import com.labrosse.suivicommercial.database.dao.SubBPartnerDAO;
import com.labrosse.suivicommercial.model.database.AdUser;
import com.labrosse.suivicommercial.model.database.BPparcSub;
import com.labrosse.suivicommercial.model.database.BPparcVisite;
import com.labrosse.suivicommercial.model.database.BPparcVisitePicture;
import com.labrosse.suivicommercial.model.database.BPparcVisiteProduct;
import com.labrosse.suivicommercial.model.database.BPparcours;
import com.labrosse.suivicommercial.model.database.City;
import com.labrosse.suivicommercial.model.database.MProduct;
import com.labrosse.suivicommercial.model.database.SubBPartner;
import com.labrosse.suivicommercial.service.client.ApiInterface;
import com.labrosse.suivicommercial.service.client.Client;
import com.labrosse.suivicommercial.service.response.webservice.AdUserResponse;
import com.labrosse.suivicommercial.service.response.webservice.CityResponse;
import com.labrosse.suivicommercial.service.response.webservice.GlobalResponse;
import com.labrosse.suivicommercial.service.response.webservice.LoginResponse;
import com.labrosse.suivicommercial.service.response.webservice.ProductResponse;
import com.labrosse.suivicommercial.service.response.webservice.SubBPartnersResponse;
import com.labrosse.suivicommercial.service.resquest.GlobalRequest;
import com.labrosse.suivicommercial.service.resquest.LoginRequest;
import com.labrosse.suivicommercial.service.resquest.ParcoursRequest;
import com.labrosse.suivicommercial.service.resquest.SelectedSubBPartnerRequest;
import com.labrosse.suivicommercial.service.resquest.VisitPictureRequest;
import com.labrosse.suivicommercial.service.resquest.VisitProductRequest;
import com.labrosse.suivicommercial.service.resquest.VisitRequest;
import com.labrosse.suivicommercial.ui.SynchronisationView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SynchroniseActivity extends AppCompatActivity {

    ApiInterface mInterface;
    String mCurrentToken;
    private AdUser mCureentUser;

    private GlobalRequest mGlobalRequest;

    private ArrayList<BPparcours> mBPparcourses;

    private ArrayList<BPparcSub> mBPparcSubs;

    private ArrayList<BPparcVisite> mBPparcVisites;

    private ArrayList<BPparcVisiteProduct> mBPparcVisiteProducts;

    private ArrayList<BPparcVisitePicture> mBPparcVisitePictures;

    private SynchronisationView mUserSynchronisationview;
    private SynchronisationView mCitySynchronisationview;
    private SynchronisationView mSubPartnerSynchronisationview;
    private SynchronisationView mProductSynchronisationview;
    private SynchronisationView mParcoursSynchronisationview;
    private SynchronisationView mParcousSubPartnerSynchronisationview;
    private SynchronisationView mParcousVisiteSynchronisationview;
    private SynchronisationView mParcousVisiteProductSynchronisationview;
    private SynchronisationView mParcousVisitePictureSynchronisationview;
    private Button mSynchronisationButton;
    private TextView mResultTextView;

    private void assignViews() {
        mUserSynchronisationview = (SynchronisationView) findViewById(R.id.user_synchronisationview);
        mCitySynchronisationview = (SynchronisationView) findViewById(R.id.city_synchronisationview);
        mSubPartnerSynchronisationview = (SynchronisationView) findViewById(R.id.sub_partner_synchronisationview);
        mProductSynchronisationview = (SynchronisationView) findViewById(R.id.product_synchronisationview);

        mParcoursSynchronisationview = (SynchronisationView) findViewById(R.id.parcours_synchronisationview);
        mParcousSubPartnerSynchronisationview = (SynchronisationView) findViewById(R.id.parcous_sub_partner_synchronisationview);
        mParcousVisiteSynchronisationview = (SynchronisationView) findViewById(R.id.parcous_visite_synchronisationview);
        mParcousVisiteProductSynchronisationview = (SynchronisationView) findViewById(R.id.parcous_visite_product_synchronisationview);
        mParcousVisitePictureSynchronisationview = (SynchronisationView) findViewById(R.id.parcous_visite_picture_synchronisationview);
        mSynchronisationButton = (Button) findViewById(R.id.synchronisation_button);

        mUserSynchronisationview.setText(R.string.users);
        mCitySynchronisationview.setText(R.string.cities);
        mSubPartnerSynchronisationview.setText(R.string.subpartners);
        mProductSynchronisationview.setText(R.string.products);

        mParcoursSynchronisationview.setText(R.string.parcours);
        mParcousSubPartnerSynchronisationview.setText(R.string.parcours_subpartner);
        mParcousVisiteSynchronisationview.setText(R.string.parcours_visits);
        mParcousVisiteProductSynchronisationview.setText(R.string.parcours_visits_products);
        mParcousVisitePictureSynchronisationview.setText(R.string.parcours_visits_pictures);
        mResultTextView = (TextView) findViewById(R.id.result_textView);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_synchronise);

        mInterface = Client.getInstance().getExploreService();

        // Getting data
        mCureentUser = AdUserDAO.getInstance(SynchroniseActivity.this).getActiveUser();

        assignViews();

        mSynchronisationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mResultTextView.setText("Synchronisation en cours.");
                login();
            }
        });
    }

    private void login(){

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setLogin(mCureentUser.getName());
        loginRequest.setPassword(mCureentUser.getPassword());

        Call<LoginResponse> login = mInterface.login(loginRequest);
        login.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                if(response.body() != null && response.body().getErreur().equals(ApiInterface.SUCESS)){

                    // Get Current token
                    mCurrentToken = response.body().getToken();

                    mGlobalRequest = new GlobalRequest();
                    mGlobalRequest.setToken(mCurrentToken);
                    mGlobalRequest.setAdUserId(mCureentUser.getAd_user_id());

                    // Synchronise users
                    synchroniseUsers();
                }else{
                    mResultTextView.setText("Probleme de connexion au serveur.");
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {

            }
        });
    }

    /**
     * Download
     */

    private void synchroniseUsers(){
        Call<AdUserResponse> userResponse = mInterface.getUser(mGlobalRequest);
        userResponse.enqueue(new Callback<AdUserResponse>() {
            @Override
            public void onResponse(Call<AdUserResponse> call, Response<AdUserResponse> response) {

                if(response.body() != null && response.body().getErreur().equals(ApiInterface.SUCESS)) {

                    ArrayList <AdUser> users = response.body().getUsers();

                    if(users.isEmpty() == false){

                        mUserSynchronisationview.setMax(users.size());

                        for (AdUser user : users) {
                            int count = AdUserDAO.getInstance(SynchroniseActivity.this).updateUser(user);

                            if(count == 0){
                                AdUserDAO.getInstance(SynchroniseActivity.this).addUser(user);
                            }

                            mUserSynchronisationview.updateProgess(1);
                        }
                    }
                }

                // Update data
                synchroniseCities();
            }

            @Override
            public void onFailure(Call<AdUserResponse> call, Throwable t) {

            }
        });
    }

    private void synchroniseCities(){

        Call<CityResponse> userResponse = mInterface.getcities(mGlobalRequest);
        userResponse.enqueue(new Callback<CityResponse>() {
            @Override
            public void onResponse(Call<CityResponse> call, Response<CityResponse> response) {

                if(response.body() != null && response.body().getErreur().equals(ApiInterface.SUCESS)) {

                    ArrayList<City> cities = response.body().getCities();

                    mCitySynchronisationview.setMax(cities != null ? cities.size() : 0);

                    for (City city : cities) {
                        int count = CityDAO.getInstance(SynchroniseActivity.this).updateCity(city);

                        if(count == 0){
                            CityDAO.getInstance(SynchroniseActivity.this).addCity(city);
                        }

                        mCitySynchronisationview.updateProgess(1);
                    }
                }

                // Update data
                synchroniseSubPartners();
            }

            @Override
            public void onFailure(Call<CityResponse> call, Throwable t) {

            }
        });
    }

    private void synchroniseSubPartners(){
        Call<SubBPartnersResponse> subBPartnersResponse = mInterface.getMagasins(mGlobalRequest);
        subBPartnersResponse.enqueue(new Callback<SubBPartnersResponse>() {
            @Override
            public void onResponse(Call<SubBPartnersResponse> call, Response<SubBPartnersResponse> response) {

                if(response.body() != null && response.body().getErreur().equals(ApiInterface.SUCESS)) {

                    ArrayList<SubBPartner> subBPartners = response.body().getSubBPartners();

                    mSubPartnerSynchronisationview.setMax(subBPartners != null ? subBPartners.size() : 0);

                    for (SubBPartner subBPartner : subBPartners) {
                        int count = SubBPartnerDAO.getInstance(SynchroniseActivity.this).updateSubBPartner(subBPartner);

                        if(count == 0){
                            SubBPartnerDAO.getInstance(SynchroniseActivity.this).addSubBPartner(subBPartner);
                        }

                        mSubPartnerSynchronisationview.updateProgess(1);
                    }
                }

                // Update data
                synchroniseProducts();
            }

            @Override
            public void onFailure(Call<SubBPartnersResponse> call, Throwable t) {
                // Update data
                synchroniseProducts();
            }
        });
    }

    private void synchroniseProducts(){

        Call<ProductResponse> productResponse = mInterface.getProducts(mGlobalRequest);
        productResponse.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {

                if(response.body() != null && response.body().getErreur().equals(ApiInterface.SUCESS)) {

                    ArrayList<MProduct> products = response.body().getProducts();

                    mProductSynchronisationview.setMax(products != null ? products.size() : 0);

                    for (MProduct product : products) {
                        int count = ProductDAO.getInstance(SynchroniseActivity.this).updateProduct(product);

                        if(count == 0){
                            ProductDAO.getInstance(SynchroniseActivity.this).addProduct(product);
                        }

                        mProductSynchronisationview.updateProgess(1);
                    }


                }

                new GetDataTask().execute();
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                //synchroniseParcours();
                new GetDataTask().execute();
            }
        });
    }

    /**
     * Upload
     */

    private void synchroniseParcours(){

        if(mBPparcourses != null && !mBPparcourses.isEmpty()){

            ParcoursRequest parcoursRequest = new ParcoursRequest(mBPparcourses.get(0));
            parcoursRequest.setToken(mCurrentToken);

            Call<GlobalResponse> userResponse = mInterface.setParcours(parcoursRequest);
            userResponse.enqueue(new Callback<GlobalResponse>() {
                @Override
                public void onResponse(Call<GlobalResponse> call, Response<GlobalResponse> response) {

                    if(response.body() != null && response.body().getOk().equals(ApiInterface.OK)) {
                        Log.e("synchroniseParcours ", "Parcous " + mBPparcourses.get(0).getC_bpparcours_id());


                        BPparcoursDAO.getInstance(SynchroniseActivity.this).setParcourSychrnised(mCureentUser.getAd_user_id() , mBPparcourses.get(0).getC_bpparcours_id());
                        mBPparcourses.remove(0);

                        mParcoursSynchronisationview.updateProgess(1);
                        synchroniseParcours();
                    }
                }

                @Override
                public void onFailure(Call<GlobalResponse> call, Throwable t) {

                }
            });
        }else{
            synchroniseBPparcSubs();
        }
    }

    private void synchroniseBPparcSubs(){

        if(mBPparcSubs != null && !mBPparcSubs.isEmpty()){

            SelectedSubBPartnerRequest selectedSubBPartnerRequest = new SelectedSubBPartnerRequest(mBPparcSubs.get(0));
            selectedSubBPartnerRequest.setToken(mCurrentToken);
            selectedSubBPartnerRequest.setAdUserId(mCureentUser.getAd_user_id());

            Call<GlobalResponse> userResponse = mInterface.setMagasinsChoisis(selectedSubBPartnerRequest);
            userResponse.enqueue(new Callback<GlobalResponse>() {
                @Override
                public void onResponse(Call<GlobalResponse> call, Response<GlobalResponse> response) {

                    if(response.body() != null && response.body().getOk().equals(ApiInterface.OK)) {
                        Log.e("synchroniseBPparcSubs ", "BPparcSubs " + mBPparcSubs.get(0).getC_bpparc_sub_id());

                        BPparcSubDAO.getInstance(SynchroniseActivity.this).setParcSubSychronised(mBPparcSubs.get(0).getC_bpparcours_id(), mBPparcSubs.get(0).getC_bpparc_sub_id());
                        mBPparcSubs.remove(0);
                        mParcousSubPartnerSynchronisationview .updateProgess(1);
                        synchroniseBPparcSubs();
                    }
                }

                @Override
                public void onFailure(Call<GlobalResponse> call, Throwable t) {

                }
            });
        }else{
            synchroniseVisites();
        }
    }

    private void synchroniseVisites(){

        if(mBPparcVisites != null && !mBPparcVisites.isEmpty()){

            VisitRequest visitRequest = new VisitRequest(mBPparcVisites.get(0));
            visitRequest.setToken(mCurrentToken);
            visitRequest.setAdUserId(mCureentUser.getAd_user_id());

            Call<GlobalResponse> userResponse = mInterface.setVisite(visitRequest);
            userResponse.enqueue(new Callback<GlobalResponse>() {
                @Override
                public void onResponse(Call<GlobalResponse> call, Response<GlobalResponse> response) {

                    if(response.body() != null && response.body().getOk().equals(ApiInterface.OK)) {
                        BPparcVisiteDAO.getInstance(SynchroniseActivity.this)
                                .setVisiteSynchronsied( mBPparcVisites.get(0).getC_bpparcours_id(),
                                        mBPparcVisites.get(0).getC_bpparc_visit_id());

                        mBPparcVisites.remove(0);
                        mParcousVisiteSynchronisationview .updateProgess(1);

                        synchroniseVisites();
                    }
                }

                @Override
                public void onFailure(Call<GlobalResponse> call, Throwable t) {

                }
            });
        }else{
            synchroniseVisitProduct();
        }
    }

    private void synchroniseVisitProduct(){

        if(mBPparcVisiteProducts != null && !mBPparcVisiteProducts.isEmpty()){

            VisitProductRequest visitProductRequest = new VisitProductRequest(mBPparcVisiteProducts.get(0));
            visitProductRequest.setToken(mCurrentToken);
            visitProductRequest.setAdUserId(mCureentUser.getAd_user_id());

            Call<GlobalResponse> globalResponse = mInterface.setProductVisite(visitProductRequest);

            globalResponse.enqueue(new Callback<GlobalResponse>() {
                @Override
                public void onResponse(Call<GlobalResponse> call, Response<GlobalResponse> response) {

                    if(response.body() != null && response.body().getOk().equals(ApiInterface.OK)) {
                        BPparcVisiteProductDAO.getInstance(SynchroniseActivity.this)
                                .setSynchronised( mBPparcVisiteProducts.get(0).getC_bpparc_visit(),
                                        mBPparcVisiteProducts.get(0).getC_bpparc_visit_prod());

                        Log.e("synchroniseVisitProduct", "VisitProduct " + mBPparcVisiteProducts.get(0).getC_bpparc_visit_prod());

                        mBPparcVisiteProducts.remove(0);
                        mParcousVisiteProductSynchronisationview .updateProgess(1);

                        synchroniseVisitProduct();
                    }
                }

                @Override
                public void onFailure(Call<GlobalResponse> call, Throwable t) {

                }
            });
        }
        else{
            synchroniseVisitPicture();
        }
    }

    private void synchroniseVisitPicture(){

        if(mBPparcVisitePictures != null && !mBPparcVisitePictures.isEmpty()){

            VisitPictureRequest visitPictureRequest = new VisitPictureRequest(mBPparcVisitePictures.get(0));
            visitPictureRequest.setToken(mCurrentToken);
            visitPictureRequest.setAdUserId(mCureentUser.getAd_user_id());

            Call<GlobalResponse> globalResponse = mInterface.setvisitPictures(visitPictureRequest);

            globalResponse.enqueue(new Callback<GlobalResponse>() {
                @Override
                public void onResponse(Call<GlobalResponse> call, Response<GlobalResponse> response) {

                    if(response.body() != null && response.body().getOk().equals(ApiInterface.OK)) {

                        BPparcVisitePictureDAO.getInstance(SynchroniseActivity.this)
                                .setSynchronised( mBPparcVisitePictures.get(0).getC_bpparc_visit_id(),
                                        mBPparcVisitePictures.get(0).getC_visit_pic_id());

                        mBPparcVisitePictures.remove(0);
                        mParcousVisitePictureSynchronisationview .updateProgess(1);

                        synchroniseVisitPicture();
                    }
                }

                @Override
                public void onFailure(Call<GlobalResponse> call, Throwable t) {

                }
            });
        }
        else{
            mBPparcVisitePictures = BPparcVisitePictureDAO.getInstance(SynchroniseActivity.this).getPictureToSychronise();
            if(mBPparcVisitePictures != null && !mBPparcVisitePictures.isEmpty()){
                synchroniseVisitPicture();
            }else{
                mResultTextView.setText("Synchronisation terminée.");
            }
        }
    }

    /**
     * Quering database
     */

    public class GetDataTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {

            mBPparcourses = BPparcoursDAO.getInstance(SynchroniseActivity.this).getParcoursToSynchronise(mCureentUser.getAd_user_id());

            mBPparcSubs = BPparcSubDAO.getInstance(SynchroniseActivity.this).getBPparcSubTosSychronise();

            mBPparcVisites = BPparcVisiteDAO.getInstance(SynchroniseActivity.this).getVisitsToSynchronise();

            mBPparcVisiteProducts =  BPparcVisiteProductDAO.getInstance(SynchroniseActivity.this).getProductsToSychronise();

            mBPparcVisitePictures = BPparcVisitePictureDAO.getInstance(SynchroniseActivity.this).getPictureToSychronise();

            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {

            mParcoursSynchronisationview.setMax((mBPparcourses != null ) ? mBPparcourses.size() : 0 );
            mParcousSubPartnerSynchronisationview.setMax((mBPparcSubs != null ) ? mBPparcSubs.size() : 0 );
            mParcousVisiteSynchronisationview.setMax((mBPparcVisites != null ) ? mBPparcVisites.size() : 0 );
            mParcousVisiteProductSynchronisationview.setMax((mBPparcVisiteProducts != null ) ? mBPparcVisiteProducts.size() : 0 );
            mParcousVisitePictureSynchronisationview.setMax((mBPparcVisitePictures != null ) ? mBPparcVisitePictures.size() : 0 );

            synchroniseParcours();
        }

        @Override
        protected void onCancelled() {
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(mCureentUser.getName().equals("admin")){
            getMenuInflater().inflate(R.menu.synchonise_menu, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.action_save_table){
            login();
        }

        if(item.getItemId() == R.id.action_clear_table){
            BPparcoursDAO.getInstance(this).setNonSynchronised();
            BPparcSubDAO.getInstance(this).setNonSynchronised();
            BPparcVisiteDAO.getInstance(this).setNonSynchronised();
            BPparcVisitePictureDAO.getInstance(this).setNonSynchronised();
            BPparcVisiteProductDAO.getInstance(this).setNonSynchronised();
        }
        return super.onOptionsItemSelected(item);
    }

}
