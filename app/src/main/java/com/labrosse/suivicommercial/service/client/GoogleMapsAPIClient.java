package com.labrosse.suivicommercial.service.client;

import com.labrosse.suivicommercial.BuildConfig;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ahmedhammami on 03/01/2017.
 */

public class GoogleMapsAPIClient {
    private static final String API_URL = "https://maps.googleapis.com/maps/api/" ;

    private static GoogleMapsAPIClient instance = null;
    private Retrofit mRetrofitClient;
    private OkHttpClient mOkHttpClient;

    private GoogleMapsAPIInterface exploreService;

    public GoogleMapsAPIClient() {

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder();

        if (BuildConfig.DEBUG) {
            okHttpBuilder.addInterceptor(loggingInterceptor);
        }

        mOkHttpClient = okHttpBuilder.build();

        mRetrofitClient = new Retrofit.Builder().baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(mOkHttpClient)
                .build();

        exploreService = mRetrofitClient.create(GoogleMapsAPIInterface.class);
    }

    public static GoogleMapsAPIClient getInstance() {
        if (instance == null) {
            instance = new GoogleMapsAPIClient();
        }

        return instance;
    }

    public GoogleMapsAPIInterface getExploreService() {
        return exploreService;
    }
}
