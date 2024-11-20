package com.trabajo.appmoviles.Modelos;

public class MetodoPago {

    private Integer id;
    private String metodoPago;

    public MetodoPago() {
    }

    public MetodoPago(Integer id, String metodoPago) {
        this.id = id;
        this.metodoPago = metodoPago;
    }

    public MetodoPago(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    @Override
    public String toString() {
        return metodoPago;
    }
}
