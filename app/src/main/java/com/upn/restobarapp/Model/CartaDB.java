package com.upn.restobarapp.Model;

import androidx.annotation.NonNull;

public class CartaDB {
    private int IdEmergencia;
    private String nombre;
    private String descripcion;
    private int precio;
    private byte[] foto;

    public CartaDB(String nombre, String descripcion, int precio, byte[] foto) {
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

    public byte[] getFoto() {
        return foto;
    }
    @NonNull
    @Override
    public String toString() {
        return nombre +": " + " "+descripcion +": "+" " + precio + ": " +" ";

    }
}
