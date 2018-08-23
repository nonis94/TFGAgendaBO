package com.example.agenda.tfgagenda.Retrofit;
import android.app.Application;

import com.example.agenda.tfgagenda.rest.Api;
import com.example.agenda.tfgagenda.util.Global;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import persistentcookiejar.ClearableCookieJar;
import persistentcookiejar.PersistentCookieJar;
import persistentcookiejar.cache.SetCookieCache;
import persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitHTTP extends Application {
    private Api api;

    @Override
    public void onCreate() {
        super.onCreate();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        ClearableCookieJar cookieJar =
                new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(this));

        OkHttpClient httpClient = new OkHttpClient.Builder()
                .cookieJar(cookieJar)
                .addInterceptor(interceptor)
                .build();

        Gson gson = new GsonBuilder()
                .setLenient()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .client(httpClient)
                .baseUrl(Global.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        //retrofit.requestBodyConverter();

        api = retrofit.create(Api.class);
    }

    public Api getAPI() {
        return api;
    }
}