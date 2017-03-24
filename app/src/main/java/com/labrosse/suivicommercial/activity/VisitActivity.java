package com.labrosse.suivicommercial.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.labrosse.suivicommercial.R;
import com.labrosse.suivicommercial.adapter.VisitProductDataAdapter;
import com.labrosse.suivicommercial.database.dao.BPparcVisiteDAO;
import com.labrosse.suivicommercial.database.dao.BPparcVisitePictureDAO;
import com.labrosse.suivicommercial.database.dao.BPparcVisiteProductDAO;
import com.labrosse.suivicommercial.database.dao.BPparcoursDAO;
import com.labrosse.suivicommercial.database.dao.ProductDAO;
import com.labrosse.suivicommercial.model.database.BPparcVisite;
import com.labrosse.suivicommercial.model.database.BPparcVisitePicture;
import com.labrosse.suivicommercial.model.database.BPparcours;
import com.labrosse.suivicommercial.model.database.ExtendedBPparcVisiteProduct;
import com.labrosse.suivicommercial.model.database.ExtendedParcoursSubPartner;
import com.labrosse.suivicommercial.model.database.MProduct;
import com.labrosse.suivicommercial.service.response.webservice.ProductResponse;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class VisitActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;

    public final DateFormat mDateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private RecyclerView mRecyclerView;
    private VisitProductDataAdapter mAdapter;

    private ArrayList<ExtendedBPparcVisiteProduct> mProductList;
    private ArrayList<BPparcVisitePicture> mImagesList;
    private ExtendedParcoursSubPartner mCurrentBPparcSub;
    private boolean mIsNewVisite;
    private BPparcVisite mCurrentVisite;
    private Button mBtnShow;
    private RelativeLayout mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit);

        // Get sub partner for new visite
        Intent extras =  getIntent();
        mIsNewVisite = extras.getBooleanExtra("IS_NEW_VISIT", false);
        mCurrentBPparcSub = getIntent().getParcelableExtra("SUB_PARTNER");

        mLoadingIndicator = (RelativeLayout) findViewById(R.id.loading_indicator);

        if(mCurrentBPparcSub == null){
            Toast.makeText(this, "mCurrentBPparcSub is null" , Toast.LENGTH_LONG ).show();
            finish();
        }

        setTitle(mCurrentBPparcSub.getName());

        if(mIsNewVisite){
            String location = extras.getStringExtra("SUB_PARTNER_LOCATION");
            BPparcours parcours = BPparcoursDAO.getInstance(VisitActivity.this).getParcours(mCurrentBPparcSub.getC_bpparcours_id());
            long id = BPparcVisiteDAO.getInstance(this).addBPparcVisite(parcours, mCurrentBPparcSub.getC_bpsub_id(), location);
            mCurrentBPparcSub.setC_bpparc_visit_id(id);
        }

        mCurrentVisite = BPparcVisiteDAO.getInstance(this).getBPparcVisite(mCurrentBPparcSub.getC_bpparc_visit_id());

        mRecyclerView = (RecyclerView)findViewById(R.id.visit_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // create an Object for Adapter
        mAdapter = new VisitProductDataAdapter(this);
        mAdapter.setmIsEditMod(true);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE:
                        mAdapter.setmIsEditMod(true);
                        break;
                    case RecyclerView.SCROLL_STATE_DRAGGING:
                        mAdapter.setmIsEditMod(false);
                        break;
                    case RecyclerView.SCROLL_STATE_SETTLING:
                        break;

                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        // set the adapter object to the Recyclerview
        mRecyclerView.setAdapter(mAdapter);

        mBtnShow = (Button) findViewById(R.id.btnShow);

        mBtnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SaveToDataBaseTask().execute();
            }
        });

        new GetProudctTask().execute();
    }

    /**
     *  JSON
     */

    private void getProducts() {

        if(mIsNewVisite){
            mProductList = BPparcVisiteProductDAO.getInstance(this).getProductsForVisit(mCurrentBPparcSub.getC_bpsub_id(), -1,  true);

            ArrayList<ExtendedBPparcVisiteProduct> products = new ArrayList<>();

            if(mProductList !=  null && !mProductList.isEmpty()){

                for(ExtendedBPparcVisiteProduct product : mProductList){

                    int update = BPparcVisiteProductDAO.getInstance(this).updateProduct(product);
                    if(update == 0) {
                        long add = BPparcVisiteProductDAO.getInstance(this).addBPparcVisiteProduct(product, mCurrentVisite);

                        if(add > 0){
                            product.setC_bpparc_visit_prod(add);
                            product.setC_bpparc_visit(mCurrentBPparcSub.getC_bpparc_visit_id());
                        }
                    }

                    products.add(product);
                }

                mProductList = products;
            }

        }else{

            mProductList = BPparcVisiteProductDAO.getInstance(this).getProductsForVisit(mCurrentBPparcSub.getC_bpsub_id(), mCurrentBPparcSub.getC_bpparc_visit_id(), false);

            if(mProductList == null || mProductList.isEmpty()){
                mProductList = BPparcVisiteProductDAO.getInstance(this).getProductsForVisit(mCurrentBPparcSub.getC_bpsub_id(), -1,  true);
            }
        }

        if(mProductList == null || mProductList.isEmpty()){

            // TODO comment this
            /*String jsonString =  loadJSONFromAsset();
            Gson gson = new GsonBuilder().create();
            ProductResponse r = gson.fromJson(jsonString, ProductResponse.class);

            if( r != null && !r.getProducts().isEmpty()){
                ArrayList<MProduct>  products = r.getProducts();

                for(MProduct product : products){
                    long l = ProductDAO.getInstance(this).addProduct(product);
                }
            }

           mProductList = BPparcVisiteProductDAO.getInstance(this).getProductsForVisit(mCurrentBPparcSub.getC_bpsub_id(), -1 , mIsNewVisite);*/
        }


    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open("product.json");
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

    private boolean saveProducts(){

        ArrayList<ExtendedBPparcVisiteProduct> products = mAdapter.getProductList();

        if( products != null && !products.isEmpty()){
            int added = 1;

            for (ExtendedBPparcVisiteProduct product : products) {

                int update = BPparcVisiteProductDAO.getInstance(this).updateProduct(product);
                if (update == 0) {
                    long add = BPparcVisiteProductDAO.getInstance(this).addBPparcVisiteProduct(product, mCurrentVisite);

                    if (add > 0) {
                        added++;
                    }

                } else {
                    added++;
                }
            }

            if (added == 1 || added < products.size()) {
                return false;
            } else {
                return true;
            }
        }else {
            return true;
        }
    }

    private boolean saveImages(){
        if(mImagesList != null && !mImagesList.isEmpty()){
            int added = 1;

            for(BPparcVisitePicture picture : mImagesList){
               int updated =  BPparcVisitePictureDAO.getInstance(VisitActivity.this).updatePicture(picture);

                if(updated == 0){
                   long l =  BPparcVisitePictureDAO.getInstance(VisitActivity.this).addBPparcVisitePicture(picture);
                   if(l > -1){
                        added ++;
                    }
                }else{
                   added ++;
                }
            }

            if(added == 1 || added < mImagesList.size()){
                return false;
            }else {
                return true;
            }
        }else{
            return true;
        }
    }

    private boolean saveVisite(){
        return BPparcVisiteDAO.getInstance(this).stopVisite(mCurrentBPparcSub.getC_bpparc_visit_id());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.visit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.action_take_picture){
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            /*Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, imageBitmap.getWidth(), stream);
            byte[] byteArray = stream.toByteArray();*/

            Uri uri = intent.getData();
            byte[] inputData = null;
            try {
                InputStream iStream =   getContentResolver().openInputStream(uri);
                inputData = getBytes(iStream);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            if(mImagesList ==  null){
                mImagesList = new ArrayList<>();
            }

            BPparcVisitePicture picture = new BPparcVisitePicture();
            picture.setPicture(inputData);
            picture.setPictureDate(getSynchroniseDate(Calendar.getInstance().getTime()));
            picture.setPictureName("VISISTE_" + Calendar.getInstance().getTimeInMillis());
            picture.setC_bpparc_visit_id(mCurrentBPparcSub.getC_bpparc_visit_id());
            picture.setVisitValue(mCurrentVisite.getValue());

            long id = BPparcVisitePictureDAO.getInstance(VisitActivity.this).addBPparcVisitePicture(picture);
            picture.setC_visit_pic_id(id);
            mImagesList.add(picture);

            Toast.makeText(VisitActivity.this, mImagesList.size() + "", Toast.LENGTH_SHORT).show();
        }
    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    public class GetProudctTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showLoadingDialog();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            getProducts();
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            hideLoadingDialog();
            if(mProductList !=  null){
                mAdapter.setProductList(mProductList);
            }else{
                Toast.makeText(VisitActivity.this, "Pas de produits trouvés pour ce magasin", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() {
            hideLoadingDialog();
        }
    }

    public String getSynchroniseDate(java.util.Date time) {
        return mDateFormatter.format(time);
    }

    private void showLoadingDialog() {
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    private void hideLoadingDialog() {
        mLoadingIndicator.setVisibility(View.GONE);
    }

    public class SaveToDataBaseTask extends AsyncTask<Void, Void, Boolean> {

        private boolean mProductValid = false;
        private boolean mImageValid = false;
        private boolean mVisitValid = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showLoadingDialog();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            mProductValid = saveProducts();
            mImageValid = saveImages();
            mVisitValid = saveVisite();
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            hideLoadingDialog();

            if(!mProductValid){
                Toast.makeText(VisitActivity.this, "Problème d'enregistements des produits", Toast.LENGTH_SHORT).show();
            }else if(!mImageValid){
                Toast.makeText(VisitActivity.this, "Problème d'enregistements des images", Toast.LENGTH_SHORT).show();
            }else  if(!mVisitValid){
                Toast.makeText(VisitActivity.this, "Problème d'enregistements de la visite", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(VisitActivity.this, "Enregistement terminée", Toast.LENGTH_SHORT).show();
            }
            finish();
        }

        @Override
        protected void onCancelled() {
            hideLoadingDialog();
        }
    }

}
