package com.boton.login.api;

import com.boton.login.model.Profesor;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface WebServiceApi {

    @POST( "/api/sign_up" )
    Call<Void> registrarProfesor(@Body Profesor profesor );

    @POST( "/api/login" )
    Call<List<Profesor>> login( @Body Profesor profesor );
}
