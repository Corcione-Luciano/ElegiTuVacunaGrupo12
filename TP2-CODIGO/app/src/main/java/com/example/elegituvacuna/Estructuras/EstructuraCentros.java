package com.example.elegituvacuna.Estructuras;

import java.io.Serializable;

public class EstructuraCentros implements Serializable {

    private String provincia;
    private String partido;
    private String direccion;
    private String web;
    private String nombre;
    private String descripcion;
    private String vacuna;

    public EstructuraCentros(String provincia, String partido, String direccion, String web, String nombre, String descripcion, String vacuna) {
        this.provincia = provincia;
        this.partido = partido;
        this.direccion = direccion;
        this.web = web;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.vacuna = vacuna;
    }

    public String getProvincia() {
        return provincia;
    }



    public String getPartido() {
        return partido;
    }



    public String getDireccion() {
        return direccion;
    }



    public String getWeb() {
        return web;
    }



    public String getNombre() {
        return nombre;
    }



    public String getDescripcion() {
        return descripcion;
    }



    public String getVacuna() {
        return vacuna;
    }






}
