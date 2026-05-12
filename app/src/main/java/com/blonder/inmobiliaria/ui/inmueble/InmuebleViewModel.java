package com.blonder.inmobiliaria.ui.inmueble;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.blonder.inmobiliaria.Models.Inmueble;
import com.blonder.inmobiliaria.request.ApiClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InmuebleViewModel extends AndroidViewModel {
    private MutableLiveData<List<Inmueble>> listaInmuebles;
    private MutableLiveData<Boolean> listaDisponibles;
    private ApiClient.MiServicioInmobiliaria service;



    public InmuebleViewModel(@NonNull Application application) {
        super(application);
        service= ApiClient.getServicio();
    }

    public LiveData<Boolean> getListaDisponibles() {
        if (listaDisponibles == null) {
            listaDisponibles = new MutableLiveData<>();
        }
        return listaDisponibles;
    }

    public LiveData<List<Inmueble>> getListaInmuebles() {
        if (listaInmuebles == null) {
            listaInmuebles = new MutableLiveData<>();
        }
        return listaInmuebles;
    }

    public void setListaDisponibles(boolean disponibles){
        listaDisponibles.postValue(disponibles);
    }

    public void recargarLista(){
        String accessToken = ApiClient.leerToken(getApplication());
        if (accessToken == null) {
            return;
        }
        if (listaDisponibles.getValue()){
            consultarTodosLosInmuebles(accessToken);
        } else {
            consultarInmueblesAlquilados(accessToken);
        }
    }

    public void cargarLista(boolean disponibles){
        String accessToken = ApiClient.leerToken(getApplication());
        if (accessToken == null) {
            return;
        }
        if (disponibles){
            consultarTodosLosInmuebles(accessToken);
        } else {
            consultarInmueblesAlquilados(accessToken);
        }
    }

    private void consultarTodosLosInmuebles(String accessToken) {
        Call<List<Inmueble>> call = service.obtenerTodosLosInmuebles(accessToken);
        call.enqueue(new Callback<List<Inmueble>>() {
            @Override
            public void onResponse(Call<List<Inmueble>> call, Response<List<Inmueble>> response) {
                if (response.isSuccessful()){
                    List<Inmueble> inmuebles = response.body();
                    if (inmuebles == null){
                        Toast.makeText(getApplication(), "No hay inmuebles", Toast.LENGTH_LONG).show();
                        return;
                    }
                    listaInmuebles.postValue(inmuebles);

                } else {
                    Toast.makeText(getApplication(), "Error al obtener los inmuebles", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Inmueble>> call, Throwable t) {
                Toast.makeText(getApplication(), "Error al obtener los inmuebles", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void consultarInmueblesAlquilados(String accessToken) {
        Call<List<Inmueble>> call = service.obtenerInmueblesAlquilados(accessToken);
        call.enqueue(new Callback<List<Inmueble>>() {
            @Override
            public void onResponse(Call<List<Inmueble>> call, Response<List<Inmueble>> response) {
                if (response.isSuccessful()){
                    List<Inmueble> inmuebles = response.body();
                    if (inmuebles == null){
                        Toast.makeText(getApplication(), "No hay inmuebles", Toast.LENGTH_LONG).show();
                        return;
                    }
                    listaInmuebles.postValue(inmuebles);

                } else {
                    Toast.makeText(getApplication(), "Error al obtener los inmuebles con contratos", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Inmueble>> call, Throwable t) {
                Toast.makeText(getApplication(), "Error al obtener los inmuebles con contratos", Toast.LENGTH_LONG).show();
            }
        });
    }

}