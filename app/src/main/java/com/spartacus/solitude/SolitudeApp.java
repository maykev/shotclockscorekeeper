package com.spartacus.solitude;


import android.app.Application;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class SolitudeApp extends Application {
    static SolitudeApp instance;
    private SolitudeService service;

    public static SolitudeApp getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        OkHttpClient httpClient;
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            httpClient = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .build();
        } else {
            Fabric.with(this, new Crashlytics());
            httpClient = new OkHttpClient();
        }

        this.service = new Retrofit.Builder()
                .baseUrl("http://tournamentdirectortest.herokuapp.com")
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build()
                .create(SolitudeService.class);
    }

    public SolitudeService getService() {
        return service;
    }
}
