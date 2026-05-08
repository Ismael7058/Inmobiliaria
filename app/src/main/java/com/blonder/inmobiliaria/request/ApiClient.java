package com.blonder.inmobiliaria.request;

import android.content.Context;
import android.content.SharedPreferences;


import com.blonder.inmobiliaria.Models.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.*;

public class ApiClient {
    public final static String BASE_URL = "https://capacitacion.alwaysdata.ne9t/";

    public static MiServicioInmobiliaria getServicio() {
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create(gson)).build();
        return retrofit.create(MiServicioInmobiliaria.class);
    }

    public interface MiServicioInmobiliaria {
        @FormUrlEncoded
        @POST("api/Propietarios/login")
        Call<String> login(@Field("Usuario") String usuario, @Field("Clave") String clave);

        /// Devuelve los datos del propietario autenticado.
        @GET("api/Propietarios")
        Call<Propietario> obtenerPropietario(@Header("Authorization") String token);

        /// Actualiza la información del propietario.
        @PUT("api/Propietarios/actualizar")
        Call<Propietario> obtenerInmuebles(@Header("Authorization") String token, @Body Propietario propietario);

        /// Envía un correo para reestablecer la contraseña.
        @FormUrlEncoded
        @POST("/api/Propietarios/email")
        Call<String> resetearContrasena(@Field("email") String email);

        /// Cambia la contraseña del propietario autenticado.
        @FormUrlEncoded
        @PUT("/api/Propietarios/changePassword")
        Call<Void> cambiarContrasena(@Header("Authorization") String token, @Field("currentPassword") String currentPassword, @Field("newPassword") String newPassword);

        /// Obtiene todos los inmuebles registrados del propietario autenticado.
        @GET("/api/Inmuebles")
        Call<List<Inmueble>> obtenerTodosLosInmuebles(@Header("Authorization") String token);

        /// Devuelve los inmuebles que se encuentran actualmente alquilados.
        @GET("/api/Inmuebles/GetContratoVigente")
        Call<List<Inmueble>> obtenerInmueblesAlquilados(@Header("Authorization") String token);

        /// Registra un nuevo inmueble con imagen.
        @Multipart
        @POST("/api/Inmuebles/cargar")
        Call<Inmueble> cargarInmueble(@Header("Authorization") String token, @Part MultipartBody.Part imagen, @Part("inmueble") RequestBody inmuebleJson);//queda ver si esta bien este asi

        /// Actualiza la información de un inmueble existente.
        @PUT("/api/Inmuebles/actualizar")
        Call<Inmueble> actualizarInmueble(@Header("Authorization") String token, @Body Inmueble inmueble);

        /// Devuelve el contrato asociado a un inmueble específico.
        @GET("/api/contratos/inmueble/{id}")
        Call<Contrato> obtenerContratoPorInmueble(@Header("Authorization") String token, @Path("id") int idInmueble);

        /// Devuelve todos los pagos realizados correspondientes a un contrato.
        @GET("/api/pagos/contrato/{id}")
        Call<List<Pago>> obtenerPagosPorContrato(@Header("Authorization") String token, @Path("id") int idContrato);


    }

    public static void saveToken(Context context, String token) {
        SharedPreferences sp = context.getSharedPreferences("token.xml", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("token", "Bearer " + token);
        editor.apply();

    }

    public static String leerToken(Context context) {
        SharedPreferences sp = context.getSharedPreferences("token.xml", Context.MODE_PRIVATE);
        return sp.getString("token", null);
    }
}
