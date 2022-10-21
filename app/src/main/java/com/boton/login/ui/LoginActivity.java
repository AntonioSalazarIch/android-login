package com.boton.login.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.boton.login.R;
import com.boton.login.api.WebServiceApi;
import com.boton.login.model.Profesor;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    private EditText etPassword;
    private EditText etEmail;
    private Button btLogin;
    private TextView tvSignUp;

    private Profesor profesor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setUpView();
    }

    private void setUpView(){

        etEmail = findViewById( R.id.idEtEmail );
        etPassword = findViewById( R.id.idEtPassword );

        
        btSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userSignUp();
            }
        });

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent( getApplicationContext(), LoginActivity.class ) );
            }
        });
    }

    private void setUpView(){
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString();

        if( email.isEmpty() ){
            etEmail.setError( getResources().getString( R.string.email_error ) );
            etEmail.requestFocus();
            return;
        }

        if( !Patterns.EMAIL_ADDRESS.matcher( email ).matches() ){
            etEmail.setError( getResources().getString( R.string.email_doesnt_match ) );
            etEmail.requestFocus();
            return;
        }

        if( password.isEmpty() ){
            etPassword.setError( getResources().getString( R.string.password_error ) );
            etPassword.requestFocus();
            return;
        }

        if( password.length() < 4 ){
            etPassword.setError( getResources().getString( R.string.password_error_less_than ) );
            etPassword.requestFocus();
            return;
        }

        profesor = new Profesor();
        profesor.setEmail( email );
        profesor.setPassword( password );

        login( );
    }

    private void login(){
        String BASE_URL = "http://10.241.2.217:8080/";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl( BASE_URL )
                .addConverterFactory(GsonConverterFactory.create() )
                .build();

        WebServiceApi api = retrofit.create( WebServiceApi.class);
        Call<List<Profesor>> call = api.login( profesor );

        call.enqueue(new Callback<List<Profesor>>() {
            @Override
            public void onResponse(Call<List<Profesor>> call, Response<List<Profesor>> response) {
                if( response.code() == 200 ){
                    Log.d( "TAG1", "Profesor logueado con exito" );
                } else  if( response.code() == 404 ){
                    Log.d( "TAG1", "Profesor no existe" );
                } else {
                    Log.d( "TAG1", "Error desconocido" );
                }
            }

            @Override
            public void onFailure(Call<List<Profesor>> call, Throwable t) {

            }
        });
    }
}