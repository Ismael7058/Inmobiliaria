package com.blonder.inmobiliaria.ui.inmueble;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import androidx.activity.result.ActivityResult;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.blonder.inmobiliaria.Models.Inmueble;
import com.blonder.inmobiliaria.request.ApiClient;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InmuebleViewModel extends AndroidViewModel {
    private MutableLiveData<Uri> mImageUri = new MutableLiveData<>();
    private MutableLiveData<Inmueble> mInmueble = new MutableLiveData<>();
    private MutableLiveData<String> messageToView = new MutableLiveData<>();
    private MutableLiveData<Boolean> guardadoExitoso = new MutableLiveData<>();
    private Context context;
    public InmuebleViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
        guardadoExitoso.setValue(false);
    }

    public LiveData<Uri> getImagenUri() {
        return mImageUri;
    }

    public LiveData<Inmueble> getInmueble() {
        if (mInmueble == null) {
            mInmueble = new MutableLiveData<>();
        }
        return mInmueble;
    }

    public void setGuardadoExitoso(boolean guardadoExitoso) {
        this.guardadoExitoso.setValue(guardadoExitoso);
    }

    public LiveData<Boolean> getGuardadoExitoso() {
        return guardadoExitoso;
    }

    public void cargarInmueble(Inmueble inmueble) {
        mInmueble.setValue(inmueble);
    }

    public LiveData<String> getMessageToView() {
        return messageToView;
    }

    public void setMessageToView(String message) {
        messageToView.setValue(message);
    }

    public void actualizarDisponible(boolean disponible) {
        Inmueble inmuebleExtracted = mInmueble.getValue();
        if (inmuebleExtracted == null) {
            return;
        }
        String token = ApiClient.leerToken(context);
        inmuebleExtracted.setDisponible(disponible);
        if (token == null) {
            setMessageToView("No hay token");
            return;
        }
        ApiClient.MiServicioInmobiliaria service = ApiClient.getServicio();
        Call<Inmueble> call = service.actualizarInmueble(token, inmuebleExtracted);
        call.enqueue(new Callback<Inmueble>() {
            @Override
            public void onResponse(Call<Inmueble> call, Response<Inmueble> response) {
                if (response.isSuccessful()) {
                    Inmueble inmueble = response.body();
                    if (inmueble == null) {
                        setMessageToView("El inmueble no existe");
                        return;
                    }
                    setMessageToView("El inmueble se edito correctamente");
                    Log.d("Inmueble", "" + inmueble.isDisponible());
                    mInmueble.postValue(inmueble);
                } else {
                    setMessageToView("Error al editar el inmueble");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Inmueble> call, @NonNull Throwable t) {
                setMessageToView("Error al editar el inmueble: " + t.getMessage());
            }
        });
    }

    public void recibirFotos(ActivityResult result) {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent data = result.getData();
            if (data != null) {
                Uri uri = data.getData();
                mImageUri.setValue(uri);
            }
        }
    }

    public void registrarInmueble(String direccion, String uso, String tipo, String ambientes, String superficie, String latitud, String longitud, String valor, boolean disponible) {
        if (direccion == null || direccion.trim().isEmpty() || uso == null || uso.trim().isEmpty() || tipo == null || tipo.trim().isEmpty()) {
            setErrorValidacion();
            return;
        }

        Integer nAmbientes = parseInteger(ambientes);
        Integer nSuperficie = parseInteger(superficie);
        Double dLatitud = parseDouble(latitud, true);
        Double dLongitud = parseDouble(longitud, true);
        Double dValor = parseDouble(valor, false);

        if (nAmbientes == null || nSuperficie == null || dLatitud == null || dLongitud == null || dValor == null) {
            setErrorValidacion();
            return;
        }

        Inmueble inmuebleCreated = new Inmueble();
        inmuebleCreated.setDireccion(direccion);
        inmuebleCreated.setUso(uso);
        inmuebleCreated.setTipo(tipo);
        inmuebleCreated.setAmbientes(nAmbientes);
        inmuebleCreated.setSuperficie(nSuperficie);
        inmuebleCreated.setLatitud(dLatitud);
        inmuebleCreated.setLongitud(dLongitud);
        inmuebleCreated.setValor(dValor);
        inmuebleCreated.setDisponible(disponible);

        String token = ApiClient.leerToken(context);
        if (token == null) {
            setMessageToView("No hay token");
            setGuardadoExitoso(false);
            return;
        }

        byte[] foto = transformarImagen();
        String inmuebleJson = new Gson().toJson(inmuebleCreated);
        RequestBody inmuebleBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), inmuebleJson);
        RequestBody fotoBody = RequestBody.create(MediaType.parse("image/jpeg"), foto);
        MultipartBody.Part imagenPart = MultipartBody.Part.createFormData("imagen", "imagen.jpg", fotoBody);

        ApiClient.getServicio().cargarInmueble(token, imagenPart, inmuebleBody).enqueue(new Callback<Inmueble>() {
            @Override
            public void onResponse(Call<Inmueble> call, Response<Inmueble> response) {
                if (!response.isSuccessful()) {
                    setMessageToView("Error al agregar el inmueble");
                    setGuardadoExitoso(false);
                    return;
                }

                setMessageToView("El inmueble se agrego correctamente");
                setGuardadoExitoso(true);
            }

            @Override
            public void onFailure(@NonNull Call<Inmueble> call, Throwable t) {
                setMessageToView("Error al agregar el inmueble: " + t.getMessage());
                setGuardadoExitoso(false);
            }
        });
    }

    private void setErrorValidacion() {
        setMessageToView("Complete todos los campos correctamente");
        setGuardadoExitoso(false);
    }

    private Integer parseInteger(String value) {
        try {
            int n = Integer.parseInt(value);
            if (n >= 0) {
                return n;
            } else {
                return null;
            }
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Double parseDouble(String value, boolean permitirNegativo) {
        try {
            double d = Double.parseDouble(value);
            if (permitirNegativo || d >= 0) {
                return d;
            } else {
                return null;
            }
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private byte[] transformarImagen() {
        try {
            Uri uri = mImageUri.getValue();
            Bitmap bitmap;
            if (uri != null) {
                InputStream inputStream = getApplication().getContentResolver().openInputStream(uri);
                bitmap = BitmapFactory.decodeStream(inputStream);
            } else {
                bitmap = BitmapFactory.decodeResource(getApplication().getResources(), com.blonder.inmobiliaria.R.drawable.inmueble_default);
            }
            if (bitmap == null) {
                return new byte[]{};
            }
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } catch (FileNotFoundException er) {
            setMessageToView("Error al procesar la imagen del inmueble");
            return new byte[]{};
        }
    }
}