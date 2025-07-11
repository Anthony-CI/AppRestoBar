package com.upn.restobarapp.Network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitCliente {
    private static Retrofit retrofit = null;


    private static final String URL="http://restobarapi.somee.com/api/";

    public static Retrofit getCliente(){
        //si el objeto de retrofit nulo que creo una instancia de retrofit
        if(retrofit==null){
            retrofit = new Retrofit.Builder().
                    baseUrl(URL).
                    addConverterFactory(GsonConverterFactory.create()).
                    build();

        }
        return retrofit;
    }
}
