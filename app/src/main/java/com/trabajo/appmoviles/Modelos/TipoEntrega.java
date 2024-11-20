package com.trabajo.appmoviles.Modelos;

public class TipoEntrega {

    private Integer id;
    private String tipo_entrega;
    private double costo;

    public TipoEntrega() {
    }

    public TipoEntrega(Integer id, String tipo_entrega, double costo) {
        this.id = id;
        this.tipo_entrega = tipo_entrega;
        this.costo = costo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTipo_entrega() {
        return tipo_entrega;
    }

    public void setTipo_entrega(String tipo_entrega) {
        this.tipo_entrega = tipo_entrega;
    }

    public double getCosto() {
        return costo;
    }

    public void setCosto(double costo) {
        this.costo = costo;
    }

    @Override
    public String toString() {
        return tipo_entrega;
    }
}
