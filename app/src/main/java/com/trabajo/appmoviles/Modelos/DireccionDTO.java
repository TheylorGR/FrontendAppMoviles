package com.trabajo.appmoviles.Modelos;

public class DireccionDTO {

    private Integer id;
    private Double latitud;
    private Double longitud;
    private String nombre_ubi;


    public DireccionDTO(Integer id, Double latitud, Double longitud, String nombre_ubi) {
        this.id = id;
        this.latitud = latitud;
        this.longitud = longitud;
        this.nombre_ubi = nombre_ubi;
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
}
