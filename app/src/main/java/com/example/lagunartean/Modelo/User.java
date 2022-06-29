package com.example.lagunartean.Modelo;

public class User {

    private String nombre;
    private String DNI;
    private String tlf;
    private String fNacimiento;
    private String nacionalidad;

    public User(){
        nombre = "";
        DNI = "";
        tlf = "";
        fNacimiento = "";
        nacionalidad = "";
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDNI() {
        return DNI;
    }

    public void setDNI(String DNI) {
        this.DNI = DNI;
    }

    public String getTlf() {
        return tlf;
    }

    public void setTlf(String tlf) {
        this.tlf = tlf;
    }

    public String getFNacimiento() {
        return fNacimiento;
    }

    public void setFNacimiento(String fNacimiento) {
        this.fNacimiento = fNacimiento;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }
}
