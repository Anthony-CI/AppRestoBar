package com.upn.restobarapp.Model;

import androidx.annotation.NonNull;

public class CartaAPI {
    private int IdCarta;
    private String nombre;
    private String descripcion;
    private int precio;
    private String foto;
    private String ruta;


    public CartaAPI(String nombre, String descripcion, int precio, String foto) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.foto = foto;
    }


    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getPrecio() {
        return precio;
    }

    public String getFoto() {
        return foto;
    }

    public String getRuta() {
        return ruta;
    }

    @NonNull
    @Override
    public String toString() {
        return nombre +": " + " "+descripcion +": "+" " + precio + ": " +" ";

    }
}

