package com.example.agenda.tfgagenda.rest;


import com.example.agenda.tfgagenda.model.User;

import retrofit2.Call;
import retrofit2.http.*;

/**
 * Created by Nonis123 on 24/05/2018.
 */

public interface Api {

    @POST("users/register")
    Call<User> login(@Body User login);
}
