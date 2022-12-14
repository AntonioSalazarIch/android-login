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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignUpActivity extends AppCompatActivity {

    private EditText etName;
    private EditText etPassword;
    private EditText etEmail;

    private Button btSignUp;
    private TextView tvLogin;

    private Profesor profesor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        setUpView();
    }

    private void setUpView(){
        etName = findViewById( R.id.idEtPerson );
        etEmail = findViewById( R.id.idEtEmail );
        etPassword = findViewById( R.id.idEtPassword );

        btSignUp = findViewById( R.id.idBtnSignUp );
        tvLogin = findViewById( R.id.tvLogin );

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

    private void userSignUp(){
        String email = etEmail.getText().toString().trim();
        String name = etName.getText().toString().trim();
        String password = etPassword.getText().toString();

        if( name.isEmpty() ){
            etName.setError( getResources().getString( R.string.name_error ) );
            etName.requestFocus();
            return;
        }

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
        profesor.setNombre( name );
        profesor.setEmail( email );
        profesor.setPassword( password );
        crearProfesor( profesor );
    }

    private void crearProfesor( Profesor profesor){
        String BASE_URL = "http://10.241.2.217:8080/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl( BASE_URL )
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WebServiceApi api = retrofit.create( WebServiceApi.class );
        Call<Void> call = api.registrarProfesor( profesor );
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if( response.code() == 201 ){
                    Log.d(  "TAG1", "Profesor guardado correctamente" );
                } else if ( response.code() == 409 ){
                    Log.d(  "TAG1", "Profesor ya existe" );
                } else {
                    Log.d(  "TAG1", "Error no definido" );
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d(  "TAG1 Error: ", t.getMessage().toString() );
            }
        });
    }
}