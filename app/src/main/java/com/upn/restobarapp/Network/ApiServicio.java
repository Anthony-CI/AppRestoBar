package com.upn.restobarapp.Network;

import com.upn.restobarapp.Model.CartaAPI;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

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

    /*
    @Multipart
    @PUT("Cartas/{id}")
    Call<Void> PutCarta(
            @Path("id") int id,
                        @Part("Nombre") RequestBody nombre,
                        @Part("Descripcion") RequestBody descripcion,
                        @Part("Precio") RequestBody precio,
                        @Part("Foto") RequestBody foto,
                        @Part("Ruta") RequestBody ruta,
                        @Part MultipartBody.Part archivo);

     */
    @Multipart
    @PUT("api/Cartas/{id}")
    Call<Void> PutCarta(
            @Path("id") int idCarta,  // El ID de la carta va como Path
            @Query("IdCarta") int id,
            @Query("Nombre") String nombre,
            @Query("Descripcion") String descripcion,
            @Query("Precio") int precio,
            @Query("Foto") String foto,
            @Query("Ruta") String ruta,
            @Part MultipartBody.Part archivo // El archivo de imagen va como @Part
    );

    @DELETE("Cartas/{id}")
    Call<Void> DeleteCarta(@Path("id") int id);


}
