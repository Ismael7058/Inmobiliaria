package com.blonder.inmobiliaria.ui.contrato;

import android.app.Application;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.blonder.inmobiliaria.Models.Contrato;
import com.blonder.inmobiliaria.request.ApiClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContratoViewModel extends AndroidViewModel {
    MutableLiveData<String> errorMessage = new MutableLiveData<>();
    MutableLiveData<Contrato> contratoMutableLiveData = new MutableLiveData<>();
    public ContratoViewModel(@NonNull Application application) {
        super(application);
    }

    public void setErrorMessage(String errorMessage){
        this.errorMessage.setValue(errorMessage);
    }
    public LiveData<String> getErrorMessage(){
        return errorMessage;
    }
    public LiveData<Contrato> getContratoMutableLiveData(){
        return contratoMutableLiveData;
    }
    public void cargarContrato(Bundle bundle){
        if (bundle == null || !bundle.containsKey("idInmueble")) {
            setErrorMessage("Error al cargar el contrato");
            return;
        }
        int idInmueble = bundle.getInt("idInmueble");
        String token = ApiClient.leerToken(getApplication());
        if (token == null) {
            setErrorMessage("Error al cargar el contrato");
            return;
        }
        ApiClient.MiServicioInmobiliaria service = ApiClient.getServicio();
        Call<Contrato> call = service.obtenerContratoPorInmueble(token, idInmueble);
        call.enqueue(new Callback<Contrato>() {
            @Override
            public void onResponse(Call<Contrato> call, Response<Contrato> response) {
                if(response.isSuccessful()){
                    Contrato contratoFinded = response.body();
                    contratoMutableLiveData.setValue(contratoFinded);
                    Log.d("Contrato", contratoFinded.toString());
                }else{
                    setErrorMessage("Error al cargar el contrato");
                }
            }

            @Override
            public void onFailure(Call<Contrato> call, Throwable t) {
                setErrorMessage("Error al cargar el contrato");
            }
        });
    }

    public Integer getIdContrato(){
        if (contratoMutableLiveData.getValue() == null){
            return null;
        }
        return contratoMutableLiveData.getValue().getIdContrato();
    }
}