package com.skteam.ititest.application;

import com.skteam.ititest.setting.AppConstance;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitService {


    public static Retrofit getRetrofit(){
        return new Retrofit.Builder()
                .baseUrl(AppConstance.API_BASE_URL_RETROFIT)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }


    public static <S> S cteateService(Class<S> serviceClass) {
        return getRetrofit().create(serviceClass);
    }

}