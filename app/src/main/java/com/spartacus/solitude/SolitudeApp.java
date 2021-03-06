package com.spartacus.solitude;


import android.app.Application;

import com.crashlytics.android.Crashlytics;

import java.util.concurrent.Executors;

import io.fabric.sdk.android.Fabric;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Scheduler;
import rx.schedulers.Schedulers;

public class SolitudeApp extends Application {
    static SolitudeApp instance;
    private SolitudeService service;
    private Scheduler scheduler;

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
            httpClient = new OkHttpClient();
        }

        // TODO: Only enable crashlytics for release builds once we start distributing release builds.
        Fabric.with(this, new Crashlytics());

        this.scheduler = Schedulers.from(Executors.newSingleThreadExecutor());

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

    public Scheduler getBackgroundScheduler() {
        return scheduler;
    }
}
