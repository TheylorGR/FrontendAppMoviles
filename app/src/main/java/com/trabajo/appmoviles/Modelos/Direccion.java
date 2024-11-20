package com.trabajo.appmoviles.Modelos;

public class Direccion {

    private Integer id;
    private Double latitud;
    private Double longitud;
    private String nombre_ubi;
    private Integer usuarioId;

    public Direccion() {}

    public Direccion(Integer id, Double latitud, Double longitud, String nombre_ubi, Integer usuarioId) {
        this.id = id;
        this.latitud = latitud;
        this.longitud = longitud;
        this.nombre_ubi = nombre_ubi;
        this.usuarioId = usuarioId;
    }

    public Direccion(double latitud, double longitud, String nombre_ubi, int usuarioId) {
        this.latitud = latitud;
        this.longitud = longitud;
        this.nombre_ubi = nombre_ubi;
        this.usuarioId = usuarioId;
    }

    public Direccion(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public String getNombre_ubi() {
        return nombre_ubi;
    }

    public void setNombre_ubi(String nombre_ubi) {
        this.nombre_ubi = nombre_ubi;
    }

    public Integer getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Integer usuarioId) {
        this.usuarioId = usuarioId;
    }

    @Override
    public String toString() {
        return nombre_ubi;
    }

}
