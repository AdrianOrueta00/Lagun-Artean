package com.example.lagunartean.Modelo;

import java.util.Calendar;

public class User {

    private int id;
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

    public String getEdad() {
        int diaNacimiento = Integer.parseInt(fNacimiento.substring(0, 2));
        int mesNacimiento = Integer.parseInt(fNacimiento.substring(3, 5));
        int annoNacimiento = Integer.parseInt(fNacimiento.substring(6));

        Calendar c = Calendar.getInstance();
        int diaActual = c.get(Calendar.DAY_OF_MONTH);
        int mesActual = c.get(Calendar.MONTH) + 1;
        int annoActual = c.get(Calendar.YEAR);

        int edad = annoActual - annoNacimiento;
        if (mesActual<mesNacimiento){
            edad--;
        }
        else if (mesActual==mesNacimiento && diaActual<diaNacimiento){
            edad--;
        }

        return String.valueOf(edad);
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
