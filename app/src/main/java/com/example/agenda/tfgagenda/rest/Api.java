package com.example.agenda.tfgagenda.rest;


import com.example.agenda.tfgagenda.model.Empty;
import com.example.agenda.tfgagenda.model.Event;
import com.example.agenda.tfgagenda.model.User;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.*;

/**
 * Created by Nonis123 on 24/05/2018.
 */

public interface Api {

    @POST("users/register")
    Call<User> login(@Body User login);

    @GET("users/")
    Call<List<User>> getUsers();

    @GET("events/{name}")
    Call<List<Event>> getEventsByUsername(@Path("name") String name);

    @POST("events/")
    Call<ResponseBody> createEvent(@Body Event event);

    @DELETE("events/{id}")
    Call<Empty> deleteEvent(@Path("id") Long id);
}
