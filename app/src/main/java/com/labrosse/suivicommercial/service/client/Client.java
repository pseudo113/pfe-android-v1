package com.labrosse.suivicommercial.service.client;

import com.labrosse.suivicommercial.BuildConfig;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Client {

    private static final String API_URL = "http://192.168.1.240:8080/LBSWS/lbscompws/" ;
   // private static final String API_URL = "http://192.168.43.121:8080/LBSWS/lbscompws/" ;

    private static Client instance = null;
    private Retrofit mRetrofitClient;
    private OkHttpClient mOkHttpClient;

    private ApiInterface exploreService;

    public Client() {

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

        exploreService = mRetrofitClient.create(ApiInterface.class);
    }

    public static Client getInstance() {
        if (instance == null) {
            instance = new Client();
        }

        return instance;
    }

    public ApiInterface getExploreService() {
        return exploreService;
    }

}
