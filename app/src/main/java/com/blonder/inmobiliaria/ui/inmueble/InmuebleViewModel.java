package com.blonder.inmobiliaria.ui.inmueble;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;
import android.app.Activity.*;
import androidx.activity.result.ActivityResult;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.blonder.inmobiliaria.Models.Inmueble;
import com.blonder.inmobiliaria.Models.Inquilino;
import com.blonder.inmobiliaria.request.ApiClient;
import com.google.android.gms.common.api.ResultTransform;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

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
    public LiveData<Uri> getImagenUri(){

        return mImageUri;
    }
    public LiveData<Inmueble> getInmueble() {
        if (mInmueble == null) {
            mInmueble = new MutableLiveData<>();
        }
        return mInmueble;
    }
    public void setGuardadoExitoso(boolean guardadoExitoso){
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
    public void actualizarDisponible(boolean disponible){
        Inmueble inmuebleExtracted = mInmueble.getValue();
        if(inmuebleExtracted == null){
            return;
        }
        String token = ApiClient.leerToken(context);
        inmuebleExtracted.setDisponible(disponible);
        if( token == null){
            setMessageToView("No hay token");
            return;
        }
        ApiClient.MiServicioInmobiliaria service = ApiClient.getServicio();
        Call<Inmueble> call = service.actualizarInmueble(token, inmuebleExtracted);
        call.enqueue(new Callback<Inmueble>() {
            @Override
            public void onResponse(Call<Inmueble> call, Response<Inmueble> response) {
                if(response.isSuccessful()){
                    Inmueble inmueble = response.body();
                    if(inmueble == null){
                        setMessageToView("El inmueble no existe");
                        return;
                    }
                    setMessageToView("El inmueble se edito correctamente");
                    Log.d("Inmueble", ""+inmueble.isDisponible());
                    mInmueble.postValue(inmueble);
                }else{
                    setMessageToView("Error al editar el inmueble");
                }
            }

            @Override
            public void onFailure(Call<Inmueble> call, Throwable t) {
               setMessageToView("Error al editar el inmueble: "+ t.getMessage());
            }
        });

    }
    public void recibirFotos(ActivityResult result){
        if(result.getResultCode() == Activity.RESULT_OK){
            Intent data = result.getData();
            Uri uri = data.getData();
            mImageUri.setValue(uri);
        }
    }

    public void registrarInmueble(
            String direccion,
            String uso,
            String tipo,
            String ambientes,
            String superficie,
            String latitud,
            String longitud,
            String valor,
            boolean disponible,

            boolean tieneContratoVigente
    ) {
        /// Arreglo que contiene los parametros de los numeros enteros enviados, para que sea mas
        /// legible
        ArrayList<String> numerosEnteros = new ArrayList<>();
        numerosEnteros.add(ambientes);
        numerosEnteros.add(superficie);
        /// Arreglo que contiene los numeros double para que sea mas legible
        ArrayList<String> numerosDouble = new ArrayList<>();
        numerosDouble.add(latitud);
        numerosDouble.add(longitud);
        numerosDouble.add(valor);

        ResultadoValidacion resultadoValidacion = validarCamposNumericos(numerosEnteros,numerosDouble);

        if (    direccion == null
                || direccion.isEmpty()
                || uso == null
                || uso.isEmpty()
                || tipo == null
                || tipo.isEmpty()
                || !resultadoValidacion.isValido()
        ) {
            setMessageToView("Todos los campos son obligatorios");
            setGuardadoExitoso(false);
            return;
        }
//        if(mImageUri.getValue() == null) {
//            setMessageToView("Debe seleccionar una imagen");
//            return;
//        }
        //////////////////////////////////////
        Inmueble inmuebleCreated = new Inmueble();
        inmuebleCreated.setDireccion(direccion);
        inmuebleCreated.setUso(uso);
        inmuebleCreated.setTipo(tipo);
        inmuebleCreated.setDisponible(disponible);
        inmuebleCreated.setTieneContratoVigente(tieneContratoVigente);
        ///////////////////////////////
        /// //SETEAR VALORES INT//////
        ///////////////////////////////
        inmuebleCreated.setAmbientes(resultadoValidacion.getNumerosEnteros().get(0));
        inmuebleCreated.setSuperficie(resultadoValidacion.getNumerosEnteros().get(1));
        ////////////////////////////////
        /////SETEAR VALORES Double//////
        ////////////////////////////////
        inmuebleCreated.setLatitud(resultadoValidacion.getNumerosFlotantes().get(0));
        inmuebleCreated.setLongitud(resultadoValidacion.getNumerosFlotantes().get(1));
        inmuebleCreated.setValor(resultadoValidacion.getNumerosFlotantes().get(2));
        ///////////////////////////////

        String token = ApiClient.leerToken(context);
        if( token == null){
            setMessageToView("No hay token");
            setGuardadoExitoso(false);
            return;
        }
        byte[] foto = transformarImagen(); /// Se obtiene los bytes de la imagen
        String inmuebleJson = new Gson().toJson(inmuebleCreated); /// Se convierte el inmueble a json
        RequestBody inmuebleBody = RequestBody.create(MediaType.parse("application/json; " +
                "charset=utf-8"), inmuebleJson); /// Se crea el request body del inmueble
        RequestBody fotoBody = RequestBody.create(MediaType.parse("image/jpeg"), foto); /// Se crea el request body de la imagen
        MultipartBody.Part imagenPart = MultipartBody.Part.createFormData("imagen", "imagen.jpg",
                fotoBody);/// Se crea el multipart body de la imagen
        ApiClient.MiServicioInmobiliaria service = ApiClient.getServicio(); /// Se crea el servicio
        service.cargarInmueble(token,imagenPart,inmuebleBody).enqueue(new Callback<Inmueble>() {
            @Override
            public void onResponse(Call<Inmueble> call, Response<Inmueble> response) {
                if(response.isSuccessful()){
                    setMessageToView("El inmueble se agrego correctamente");
                    setGuardadoExitoso(true);
                }else{
                    setMessageToView("Error al agregar el inmueble");
                    setGuardadoExitoso(false);
                }
            }

             @Override
             public void onFailure(Call<Inmueble> call, Throwable t) {
                setMessageToView("Error al agregar el inmueble: "+ t.getMessage());
                setGuardadoExitoso(false);
             }
         });
    }
    public ResultadoValidacion validarCamposNumericos(List<String> numerosEnteros, List<String> numerosFlotantes){
        ResultadoValidacion resultadoValidacion = new ResultadoValidacion();
        List<Integer> numerosEnterosValidos = new ArrayList<>();
        List<Double> numerosFlotantesValidos = new ArrayList<>();
        ///Verifica que se pueda parsear a entero y que no sea menor a 0

        for (String numero : numerosEnteros) {
            try {
                int numeroParseado = Integer.parseInt(numero);
                if(numeroParseado < 0) {
                    resultadoValidacion.setValido(false);
                    return resultadoValidacion;
                }
                numerosEnterosValidos.add(numeroParseado);
            } catch (NumberFormatException e) {
                resultadoValidacion.setValido(false);
                return resultadoValidacion;
            }
        }
        /// Verifica que se pueda parsear a float, no verifica menor a 0 porque longitud y latitud
        /// puede ser menores a 0
        /// El condicional que tiene verifica si el atributo "valor" es menor a 0, el mismo se
        /// envia en la posicion 2 del arreglo cuando es asignado por parametro
        int contador = 0;
        for(String numero : numerosFlotantes){
            try {
                if(contador == 2) { ///chequear si funciona bien
                    double numeroParsed = Float.parseFloat(numero);
                    if(numeroParsed < 0) {
                        resultadoValidacion.setValido(false);
                        return resultadoValidacion;
                    }
                    numerosFlotantesValidos.add(numeroParsed);
                    continue;
                }
                numerosFlotantesValidos.add(Double.parseDouble(numero));
                contador++;
            } catch (NumberFormatException e) {
                resultadoValidacion.setValido(false);
                return  resultadoValidacion;
            }
        }
        resultadoValidacion.setValido(true);
        resultadoValidacion.setNumerosEnteros(numerosEnterosValidos);
        resultadoValidacion.setNumerosFlotantes(numerosFlotantesValidos);
        return resultadoValidacion;
    }
    /// Clase para guardar los resultados de la validacion de los campos numericos
    public class ResultadoValidacion{
        private  List<Integer> numerosEnteros = new ArrayList<>();
        private  List<Double> numerosFlotantes = new ArrayList<>();
        private  boolean valido = true;
        public ResultadoValidacion(){}
        public ResultadoValidacion(List<Integer> numerosEnteros, List<Double> numerosFlotantes, boolean valido) {
            this.numerosEnteros = numerosEnteros;
            this.numerosFlotantes = numerosFlotantes;
            this.valido = valido;
        }

        public List<Integer> getNumerosEnteros() {
            return numerosEnteros;
        }

        public void setNumerosEnteros(List<Integer> numerosEnteros) {
            this.numerosEnteros = numerosEnteros;
        }

        public List<Double> getNumerosFlotantes() {
            return numerosFlotantes;
        }

        public void setNumerosFlotantes(List<Double> numerosFlotantes) {
            this.numerosFlotantes = numerosFlotantes;
        }

        public boolean isValido() {
            return valido;
        }

        public void setValido(boolean valido) {
            this.valido = valido;
        }
    }
    ///  Funcion para obtener la imagen de la galeria y convertirla a byte array
//    private byte[] transformarImagen(){
//        try{
//            Uri uri = mImageUri.getValue();
//            //Crea un canal para leer la imagen
//            InputStream inputStream = getApplication().getContentResolver().openInputStream(uri);
//            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
//            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//            bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
//            return byteArrayOutputStream.toByteArray();
//        }catch (FileNotFoundException er){
//            setMessageToView("No ha seleccionado una foto");
//            return new byte[]{};
//        }
//    }
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