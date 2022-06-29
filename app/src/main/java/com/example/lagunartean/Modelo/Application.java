package com.example.lagunartean.Modelo;

import android.content.Context;

import com.example.lagunartean.Controlador.DatabaseAdapter;

public class Application {

    private static Application miApplication;
    private UserList lUsuarios;
    private DatabaseAdapter db;

    private Application(Context pContext){
        lUsuarios = new UserList();
        db = new DatabaseAdapter(pContext.getApplicationContext(), "LagunArtean", null, 1);
    }

    public static Application getMiApplication(Context pContext){
        if (miApplication==null){
            miApplication = new Application(pContext);
        }
        return miApplication;
    }

    public void anadirUsuario(String pNombre, String pDNI, String pTlf, String pFecha, String pNacionalidad){

        lUsuarios.anadir(pNombre, pDNI, pTlf, pFecha, pNacionalidad);
        db.insertarUsuario(pNombre, pDNI, pTlf, pFecha, pNacionalidad);
    }
}
