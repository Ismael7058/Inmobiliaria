package com.blonder.inmobiliaria;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.blonder.inmobiliaria.request.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends AndroidViewModel {

    private MutableLiveData<String> msj;
    private Context context;


    public LoginViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
    }


    public LiveData<String> getMsj() {
        if (msj == null) {
            msj = new MutableLiveData<>();
        }
        return msj;
    }

    public void recuperarDatos(String email, String pass) {
        if (email.isEmpty() || pass.isEmpty()) {
            msj.setValue("Complete los campos");
            return;
        }
        ApiClient.MiServicioInmobiliaria servicio = ApiClient.getServicio();
        Call<String> call = servicio.login(email, pass);
        call.enqueue(new Callback<String>() {


            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (!response.isSuccessful()) {
                    msj.setValue("Usuario o contraseña incorrectos");
                    return;
                }
                String token = response.body();
                if (token == null || token.isEmpty()) {
                    msj.setValue("El servidor no envio el token");
                    return;
                }
                ApiClient.saveToken(context, token);
                Intent intent = new Intent(getApplication(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                msj.setValue("Vuelva a intentar mas tarde");
            }
        });
    }


}