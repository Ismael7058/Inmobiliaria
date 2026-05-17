package com.blonder.inmobiliaria;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.blonder.inmobiliaria.Models.Propietario;
import com.blonder.inmobiliaria.request.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivityViewModel extends AndroidViewModel {

    private MutableLiveData<Propietario> propietarioMutableLiveData = new MutableLiveData<>();
    public MainActivityViewModel(@NonNull Application application) {
        super(application);
    }
    public void setPropietarioMutableLiveData(Propietario propietario){
        propietarioMutableLiveData.setValue(propietario);
    }
    public LiveData<Propietario> getPropietarioMutableLiveData(){
        return propietarioMutableLiveData;
    }
    public void cargarPropietario() {
        String accessToken = ApiClient.leerToken(getApplication());
        if (accessToken == null) {
            return;
        }
        ApiClient.MiServicioInmobiliaria service = ApiClient.getServicio();
        Call<Propietario> call = service.obtenerPropietario(accessToken);
        call.enqueue(new Callback<Propietario>() {
            @Override
            public void onResponse(Call<Propietario> call, Response<Propietario> response) {
                if (response.isSuccessful()) {

                    Propietario propietario = response.body();
                    if (propietario == null) {
                        Toast.makeText(getApplication(), "El propietario no existe", Toast.LENGTH_LONG).show();
                        return;
                    }
                    propietarioMutableLiveData.postValue(propietario);
                } else {
                    Toast.makeText(getApplication(), "Error al obtener el propietario", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Propietario> call, Throwable t) {
                Toast.makeText(getApplication(), "Error al obtener el propietario", Toast.LENGTH_LONG).show();
            }
        });
    }
}
