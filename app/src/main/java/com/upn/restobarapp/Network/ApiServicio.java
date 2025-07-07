package com.upn.restobarapp.Network;

import com.upn.restobarapp.Model.CartaAPI;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiServicio {
    @GET("Cartas")
    Call<List<CartaAPI>>GetCartas();
    //@POST("Cartas")
    //Call<CartaAPI>PostEmergencia(@Body EmergenciaAPI emergencia);
    @Multipart
    @POST("Cartas")
    Call<CartaAPI> PostCartas(
            @Part("IdCarta") RequestBody IdCarta,
            @Part("Nombre") RequestBody nombre,
            @Part("Descripcion") RequestBody descripcion,
            @Part("Precio") RequestBody precio,
            @Part("Foto") RequestBody foto,
            @Part("Ruta") RequestBody ruta,
            @Part MultipartBody.Part archivo
    );
}
