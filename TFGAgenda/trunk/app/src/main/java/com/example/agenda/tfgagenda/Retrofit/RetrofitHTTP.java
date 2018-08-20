package com.example.agenda.tfgagenda.Retrofit;
import android.content.Context;

import com.example.agenda.tfgagenda.rest.Api;
import com.example.agenda.tfgagenda.util.Global;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.OkHttpClient;
import persistentcookiejar.ClearableCookieJar;
import persistentcookiejar.PersistentCookieJar;
import persistentcookiejar.cache.SetCookieCache;
import persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import okhttp3.logging.HttpLoggingInterceptor;
/**
 * Created by Nonis123 on 25/05/2018.
 */
public class RetrofitHTTP {
    public Api setup(Context ctx) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient(); //create OKHTTPClient

        ClearableCookieJar cookieJar =
                new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(ctx));

        OkHttpClient httpClient = new OkHttpClient.Builder()
                .cookieJar(cookieJar)
                .addInterceptor(interceptor)
                .cache(null)
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

        return retrofit.create(Api.class);
    }
}