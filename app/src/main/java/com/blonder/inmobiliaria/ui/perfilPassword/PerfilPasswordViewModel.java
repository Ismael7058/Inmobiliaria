package com.blonder.inmobiliaria.ui.perfilPassword;

import android.app.Application;
import android.content.Context;
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

public class PerfilPasswordViewModel extends AndroidViewModel {
    private Context context;
    private MutableLiveData<Boolean> ok;

    public PerfilPasswordViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
        ok = new MutableLiveData<>(false);
    }

    public LiveData<Boolean> getOk() {
        return ok;
    }

    public void cambiarContra(String anterior, String nueva) {
        String accessToken = ApiClient.leerToken(context);
        if (accessToken == null) {
            Toast.makeText(context, "Vuelva a iniciar sesión", Toast.LENGTH_SHORT).show();
            return;
        }
        if (anterior == null || anterior.isEmpty() || nueva == null || nueva.isEmpty()) {
            Toast.makeText(context, "Ingrese ambas contraseñas", Toast.LENGTH_SHORT).show();
            return;
        }
        ApiClient.MiServicioInmobiliaria service = ApiClient.getServicio();
        Call<Void> call = service.cambiarContrasena(accessToken, anterior, nueva);
        call.enqueue(new Callback<Void>() {

            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(context, "Intente de nuevo ", Toast.LENGTH_SHORT).show();
                    ok.setValue(false);
                    return;
                }

                Toast.makeText(context, "Contraseña cambiada correctamente", Toast.LENGTH_SHORT).show();
                ok.setValue(true);

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "Error al cambiar la contraseña", Toast.LENGTH_SHORT).show();
                ok.setValue(false);
            }
        });
    }

}
