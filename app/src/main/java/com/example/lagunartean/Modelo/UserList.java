package com.example.lagunartean.Modelo;

import java.util.ArrayList;

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

    public UserList filtrarNacionalidad(String pNacionalidad){
        //TODO
        return null;
    }

    public UserList filtrarEdad(int pEdad){
        //TODO
        return null;
    }

    public int getLength() {
        return length;
    }

    public User get(int pPos){
        return lUsuarios.get(pPos);
    }
}
