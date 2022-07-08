package com.example.lagunartean.Modelo;

import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lagunartean.Controlador.DatabaseAdapter;
import com.example.lagunartean.Controlador.UsersAdapter;

import java.util.ArrayList;
import java.util.Locale;

public class Application {

    private static Application miApplication;
    private UserList lUsuarios;
    private DatabaseAdapter db;

    private Application(Context pContext){
        db = new DatabaseAdapter(pContext.getApplicationContext(), "LagunArtean", null, 1);
        lUsuarios = new UserList(db.cargarUsuarios());
    }

    public static Application getMiApplication(Context pContext){
        if (miApplication==null){
            miApplication = new Application(pContext);
        }
        return miApplication;
    }

    public void anadirUsuario(String pNombre, String pDNI, String pTlf, String pFecha, String pNacionalidad){
        db.insertarUsuario(pNombre, pDNI, pTlf, pFecha, pNacionalidad);
    }

    public void actualizarUsuario(int pId, String pNombre, String pDNI, String pTlf, String pFecha, String pNacionalidad){
        db.actualizarUsuario(pId, pNombre, pDNI, pTlf, pFecha, pNacionalidad);
    }

    public void mostrarUsuarios(RecyclerView pLista, String pFiltro, Context pContext){
        lUsuarios = new UserList(db.cargarUsuarios());

        pLista.setLayoutManager(new LinearLayoutManager(pContext));
        ArrayList<String> nombres = new ArrayList<String>();
        ArrayList<String> telefonos = new ArrayList<String>();
        ArrayList<String> edades = new ArrayList<String>();
        ArrayList<Integer> ids = new ArrayList<Integer>();
        for (int i=0;i<lUsuarios.getLength();i++){
            if (lUsuarios.get(i).getNombre().toLowerCase(Locale.ROOT).contains(pFiltro.toLowerCase(Locale.ROOT))) {
                nombres.add(lUsuarios.get(i).getNombre());
                telefonos.add(lUsuarios.get(i).getTlf());
                edades.add(lUsuarios.get(i).getEdad());
                ids.add(lUsuarios.get(i).getId());
            }
        }
        UsersAdapter usersAdapter = new UsersAdapter(pContext, nombres, telefonos, edades, ids);
        pLista.setAdapter(usersAdapter);
    }

    public ArrayList<String> getDatos(int pId){
        return db.getDatos(pId);
    }
}
