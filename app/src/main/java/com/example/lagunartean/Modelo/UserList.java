package com.example.lagunartean.Modelo;

import java.util.ArrayList;
import java.util.Locale;

public class UserList {
    private ArrayList<User> lUsuarios;
    private int length;

    public UserList(ArrayList<User> pLista){
        if (pLista != null){
            this.lUsuarios = pLista;
            this.length = pLista.size();
        }
        else{
            this.lUsuarios = new ArrayList<User>();
            this.length = 0;
        }
    }


    public void anadir(String pNombre, String pDNI, String pTlf, String pFecha, String pNacionalidad){
        User miUsuario = new User();
        miUsuario.setNombre(pNombre);
        miUsuario.setDNI(pDNI);
        miUsuario.setTlf(pTlf);
        miUsuario.setFNacimiento(pFecha);
        miUsuario.setNacionalidad(pNacionalidad);

        lUsuarios.add(miUsuario);
        length++;
    }

    public void anadirUsuario(User pUsuario){
        lUsuarios.add(pUsuario);
        length++;
    }

    public UserList filtrarNacionalidad(String pNacionalidad){
        UserList resultado = new UserList(new ArrayList<User>());
        User usuarioActual;
        for (int i = 0; i < lUsuarios.size(); i++){
            usuarioActual = lUsuarios.get(i);
            if (usuarioActual.getNacionalidad().equals(pNacionalidad)){
                resultado.anadirUsuario(usuarioActual);
            }
        }

        return resultado;
    }

    public UserList filtrarEdad(String pEdad){
        UserList resultado = new UserList(new ArrayList<User>());
        User usuarioActual;
        for (int i = 0; i < lUsuarios.size(); i++){
            usuarioActual = lUsuarios.get(i);
            if (usuarioActual.getEdad().equals(pEdad)){
                resultado.anadirUsuario(usuarioActual);
            }
        }

        return resultado;
    }

    public UserList filtrarNombre(String pFiltro){
        UserList resultadoBusqueda = new UserList(new ArrayList<User>());
        for (int i=0;i<lUsuarios.size();i++){
            if (lUsuarios.get(i).getNombre().toLowerCase(Locale.ROOT).contains(pFiltro.toLowerCase(Locale.ROOT))) {
                resultadoBusqueda.anadirUsuario(lUsuarios.get(i));
            }
        }
        return resultadoBusqueda;
    }

    public int getLength() {
        return length;
    }

    public User get(int pPos){
        return lUsuarios.get(pPos);
    }
}
