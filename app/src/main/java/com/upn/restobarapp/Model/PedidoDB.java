package com.upn.restobarapp.Model;

public class PedidoDB {
    private int id;
    private String nombre;
    private String descripcion;
    private int cantidad;
    private int mesaNumero;
    private String nombreMozo;
    private int estado; // 0 para pendiente, 1 para completado

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getMesaNumero() {
        return mesaNumero;
    }

    public void setMesaNumero(int mesaNumero) {
        this.mesaNumero = mesaNumero;
    }

    public String getNombreMozo() {
        return nombreMozo;
    }

    public void setNombreMozo(String nombreMozo) {
        this.nombreMozo = nombreMozo;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }
}
