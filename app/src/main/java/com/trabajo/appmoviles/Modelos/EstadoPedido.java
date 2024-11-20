package com.trabajo.appmoviles.Modelos;

public class EstadoPedido {

    private Integer id;
    private String estado;

    public EstadoPedido() {}

    public EstadoPedido(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

}
