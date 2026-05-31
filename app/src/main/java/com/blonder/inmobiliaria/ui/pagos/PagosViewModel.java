package com.blonder.inmobiliaria.ui.pagos;

import android.app.Application;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.blonder.inmobiliaria.Models.Inmueble;
import com.blonder.inmobiliaria.Models.Pago;
import com.blonder.inmobiliaria.request.ApiClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PagosViewModel extends AndroidViewModel {
    private MutableLiveData<List<Pago>> listaPagos;
    private ApiClient.MiServicioInmobiliaria service;


    public PagosViewModel(@NonNull Application application) {
        super(application);
        service= ApiClient.getServicio();
    }

    public LiveData<List<Pago>> getListaPago() {
        if (listaPagos == null) {
            listaPagos = new MutableLiveData<>();
        }
        return listaPagos;
    }

    public void cargarLista( Bundle bundle ){

        if (bundle == null || !bundle.containsKey("idContrato")) {
            return;
        }
        String accessToken = ApiClient.leerToken(getApplication());
        if (accessToken == null) {
            return;
        }

        Call<List<Pago>> call = service.obtenerPagosPorContrato(accessToken, bundle.getInt("idContrato"));
        call.enqueue(new Callback<List<Pago>>() {
            @Override
            public void onResponse(Call<List<Pago>> call, Response<List<Pago>> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(getApplication(), "Error al obtener los pagos", Toast.LENGTH_SHORT).show();
                }

                List<Pago> pagos = response.body();

                if (pagos == null || pagos.isEmpty()){
                    Toast.makeText(getApplication(), "No hay inmuebles", Toast.LENGTH_SHORT).show();
                    return;
                }
                listaPagos.postValue(pagos);
            }

            @Override
            public void onFailure(Call<List<Pago>> call, Throwable t) {
                Toast.makeText(getApplication(), "Error al obtener los pagos", Toast.LENGTH_SHORT).show();

            }
        });
    }
}