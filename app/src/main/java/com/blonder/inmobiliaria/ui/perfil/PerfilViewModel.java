package com.blonder.inmobiliaria.ui.perfil;

import android.app.Application;
import android.content.Context;
import android.util.Patterns;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.blonder.inmobiliaria.Models.Propietario;
import com.blonder.inmobiliaria.request.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerfilViewModel extends AndroidViewModel {
    private MutableLiveData<Propietario> propietarioMutable;
    private Context context;

    public PerfilViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
    }


    public LiveData<Propietario> getPropietario() {
        if (propietarioMutable == null) {
            propietarioMutable = new MutableLiveData<>();
        }
        return propietarioMutable;
    }

    public void cargarPropietario() {
        String accessToken = ApiClient.leerToken(context);
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
                    propietarioMutable.postValue(propietario);
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

    public void editarPropietario(String nombre, String apellido, String dni, String telefono, String email) {

        if (nombre == null || nombre.isEmpty() || apellido == null || apellido.isEmpty() || dni == null || dni.isEmpty() || telefono == null || telefono.isEmpty() || email == null || email.isEmpty()) {
            Toast.makeText(getApplication(), "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        int dniInt;
        try {
            dniInt = Integer.parseInt(dni);
        } catch (NumberFormatException e) {
            Toast.makeText(getApplication(), "El DNI solo acepta numeros", Toast.LENGTH_SHORT).show();
            return;

        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(getApplication(), "El email no es válido", Toast.LENGTH_SHORT).show();
            return;
        }

        Propietario propietarioEditado = new Propietario();
        propietarioEditado.setNombre(nombre);
        propietarioEditado.setApellido(apellido);
        propietarioEditado.setDni(dniInt);
        propietarioEditado.setTelefono(telefono);
        propietarioEditado.setEmail(email);

        Propietario actual = propietarioMutable.getValue();
        if (actual == null || actual.equals(propietarioEditado)) {
            Toast.makeText(getApplication(), "No hay cambios", Toast.LENGTH_SHORT).show();
            return;
        }

        String accessToken = ApiClient.leerToken(context);
        if (accessToken == null) {
            Toast.makeText(getApplication(), "No hay token", Toast.LENGTH_SHORT).show();
            return;
        }
        ApiClient.MiServicioInmobiliaria service = ApiClient.getServicio();
        Call<Propietario> call = service.actualizarPropietario(accessToken, propietarioEditado);
        call.enqueue(new Callback<Propietario>() {
            @Override
            public void onResponse(Call<Propietario> call, Response<Propietario> response) {
                if (response.isSuccessful()) {
                    Propietario propietario = response.body();
                    if (propietario == null) {
                        Toast.makeText(getApplication(), "El propietario no existe", Toast.LENGTH_LONG).show();
                        return;
                    }
                    Toast.makeText(getApplication(), "El propietario se edito correctamente", Toast.LENGTH_LONG).show();
                    propietarioMutable.postValue(propietario);
                } else {
                    Toast.makeText(getApplication(), "Error al editar el propietario", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Propietario> call, Throwable t) {
                Toast.makeText(getApplication(), "Error al editar el propietario", Toast.LENGTH_LONG).show();
            }
        });
    }
}