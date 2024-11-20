package com.trabajo.appmoviles.Modelos;

public class Comida {
    private Integer id;
    private String nombre;
    private Integer precio;
    private String descripcion;
    private String imagen;
    private Integer stock;
    private Filtro filtro;
    private Integer cantidadSolicitada;

    public Comida() {this.cantidadSolicitada = 1;}

    public Comida(Integer id, String nombre, Integer precio, String descripcion, String imagen, Integer stock, Filtro filtro) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.descripcion = descripcion;
        this.imagen = imagen;
        this.stock = stock;
        this.filtro = filtro;
        this.cantidadSolicitada = 1;
    }

    public Comida(Integer id, Integer cantidadSolicitada) {
        this.id = id;
        this.cantidadSolicitada = cantidadSolicitada;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getPrecio() {
        return precio;
    }

    public void setPrecio(Integer precio) {
        this.precio = precio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Filtro getFiltro() {
        return filtro;
    }

    public void setFiltro(Filtro filtro) {
        this.filtro = filtro;
    }

    public Integer getCantidadSolicitada() {
        return cantidadSolicitada;
    }

    public void setCantidadSolicitada(Integer cantidadSolicitada) {
        this.cantidadSolicitada = cantidadSolicitada;
    }

}
