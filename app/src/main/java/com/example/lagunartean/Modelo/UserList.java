package com.example.lagunartean.Modelo;

import java.util.ArrayList;

public class UserList {
    private ArrayList<User> lUsuarios;

    public UserList(){
        this.lUsuarios = new ArrayList<User>();
    }

    public void anadir(String pNombre, String pDNI, String pTlf, String pFecha, String pNacionalidad){
        User miUsuario = new User();
        miUsuario.setNombre(pNombre);
        miUsuario.setDNI(pDNI);
        miUsuario.setTlf(pTlf);
        miUsuario.setFNacimiento(pFecha);
        miUsuario.setNacionalidad(pNacionalidad);

        lUsuarios.add(miUsuario);
        System.out.println("NUEVO USUARIO:");
        System.out.println("Nombre: " + miUsuario.getNombre());
        System.out.println("DNI: " + miUsuario.getDNI());
        System.out.println("Tlf: " + miUsuario.getTlf());
        System.out.println("Fecha de nacimiento: " + miUsuario.getFNacimiento());
        System.out.println("Nacionalidad: " + miUsuario.getNacionalidad());
    }

    public UserList filtrarNacionalidad(String pNacionalidad){
        //TODO
        return null;
    }

    public UserList filtrarEdad(int pEdad){
        //TODO
        return null;
    }

}
