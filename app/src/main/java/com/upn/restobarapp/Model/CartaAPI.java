package com.upn.restobarapp.Model;

import android.widget.EditText;

import androidx.annotation.NonNull;

public class CartaAPI {
    public EditText editCantidad;
    private int idCarta;
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

    public int getIdCarta() {
        return idCarta;
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


    public void setIdCarta(int idCarta) {
        this.idCarta = idCarta;  // Correcto
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    @NonNull
    @Override
    public String toString() {
        return nombre +": " + " "+descripcion +": "+" " + precio + ": " +" ";

    }
}

