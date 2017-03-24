package com.labrosse.suivicommercial.service.client;

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

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by ahmedhammami on 01/01/2017.
 */

public interface ApiInterface {

    /**
     * Global
     */

    String SUCESS = "OK";
    String ERROR = "ERROR";
    String OK = "Y";

    /**
     * Dowload
     */

    @POST("login")
    Call<LoginResponse> login(@Body LoginRequest request);


    @POST("getusers")
    Call<AdUserResponse> getUser(@Body GlobalRequest request);

    @Headers({
            "Content-Type: application/json;charset=utf-8",
            "Accept: application/json"
    })
    @POST("getcities")
    Call<CityResponse> getcities(@Body GlobalRequest request);

    @Headers({
            "Content-Type: application/json;charset=utf-8",
            "Accept: application/json"
    })
    @POST("getmagasins")
    Call<SubBPartnersResponse> getMagasins(@Body GlobalRequest request);

    @Headers({
            "Content-Type: application/json;charset=utf-8",
            "Accept: application/json"
    })
    @POST("getproducts")
    Call<ProductResponse> getProducts(@Body GlobalRequest request);

    /**
     * Upload
     */

    @POST("setparcours")
    Call<GlobalResponse> setParcours(@Body ParcoursRequest request);

    @POST("setmagasinschoisis")
    Call<GlobalResponse> setMagasinsChoisis(@Body SelectedSubBPartnerRequest request);

    @POST("setvisites")
    Call<GlobalResponse> setVisite(@Body VisitRequest request);

    @POST("setvisitProducts")
    Call<GlobalResponse> setProductVisite(@Body VisitProductRequest request);

    @POST("setvisitPictures")
    Call<GlobalResponse> setvisitPictures(@Body VisitPictureRequest request);


}
