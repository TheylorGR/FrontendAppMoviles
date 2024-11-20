package com.trabajo.appmoviles.Modelos;

import java.util.List;

public class Pedido {

    private int id;
    private double totalCompra;
    private double total;
    private Usuarios usuario;
    private EstadoPedido estado;
    private MetodoPago metodoPago;
    private TipoEntrega tipoEntrega;
    private List<Comida> comidas;
    private Direccion direccion;

    public Pedido() {
    }

    public Pedido(int id, double totalCompra, double total, Usuarios usuario, EstadoPedido estado, MetodoPago metodoPago, TipoEntrega tipoEntrega, List<Comida> comidas, Direccion direccion) {
        this.id = id;
        this.totalCompra = totalCompra;
        this.total = total;
        this.usuario = usuario;
        this.estado = estado;
        this.metodoPago = metodoPago;
        this.tipoEntrega = tipoEntrega;
        this.comidas = comidas;
        this.direccion = direccion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getTotalCompra() {
        return totalCompra;
    }

    public void setTotalCompra(double totalCompra) {
        this.totalCompra = totalCompra;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public Usuarios getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuarios usuario) {
        this.usuario = usuario;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public void setEstado(EstadoPedido estado) {
        this.estado = estado;
    }

    public MetodoPago getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(MetodoPago metodoPago) {
        this.metodoPago = metodoPago;
    }

    public TipoEntrega getTipoEntrega() {
        return tipoEntrega;
    }

    public void setTipoEntrega(TipoEntrega tipoEntrega) {
        this.tipoEntrega = tipoEntrega;
    }

    public List<Comida> getComidas() {
        return comidas;
    }

    public void setComidas(List<Comida> comidas) {
        this.comidas = comidas;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }
}
