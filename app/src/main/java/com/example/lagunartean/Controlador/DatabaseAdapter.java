package com.example.lagunartean.Controlador;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.lagunartean.Modelo.User;

import java.util.ArrayList;
import java.util.Collections;


public class DatabaseAdapter extends SQLiteOpenHelper {

    private static final String TABLA_USUARIO = "usuarios";
    private static final String TABLA_DUCHAS = "duchas";
    private static final String TABLA_LAVANDERIA = "lavanderia";
    private static final String CAMPO_ID = "id";
    private static final String CAMPO_NOMBRE = "nombre";
    private static final String CAMPO_DNI = "dni";
    private static final String CAMPO_TLF = "telefono";
    private static final String CAMPO_FECHA = "fnacimiento";
    private static final String CAMPO_NACIONALIDAD = "nacionalidad";
    private static final String CAMPO_FECHA_RESERVA = "freserva";

    private static final String MAX_USUARIOS_LAVANDERIA = "7";

    private static final String CREAR_TABLA_USUARIO = "CREATE TABLE IF NOT EXISTS " + TABLA_USUARIO +
            "(" + CAMPO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            CAMPO_NOMBRE + " TEXT, " +
            CAMPO_DNI + " TEXT, " +
            CAMPO_TLF + " TEXT, " +
            CAMPO_FECHA + " TEXT, " +
            CAMPO_NACIONALIDAD + " TEXT)";

    private static final String CREAR_TABLA_DUCHAS = "CREATE TABLE IF NOT EXISTS " + TABLA_DUCHAS +
            "(" + CAMPO_ID + " INTEGER, " +
            CAMPO_FECHA_RESERVA + " TEXT, " +
            "PRIMARY KEY(" + CAMPO_ID + ", " + CAMPO_FECHA_RESERVA + "), " +
            "FOREIGN KEY(" + CAMPO_ID + ") " +
            "REFERENCES " + TABLA_USUARIO + "(" + CAMPO_ID + "))";


    private static final String CREAR_TABLA_LAVANDERIA = "CREATE TABLE IF NOT EXISTS " + TABLA_LAVANDERIA +
            "(" + CAMPO_ID + " INTEGER , " +
            CAMPO_FECHA_RESERVA + " TEXT, " +
            "PRIMARY KEY(" + CAMPO_ID + ", " + CAMPO_FECHA_RESERVA + "), " +
            "FOREIGN KEY(" + CAMPO_ID + ") " +
            "REFERENCES " + TABLA_USUARIO + "(" + CAMPO_ID + "))";


    public DatabaseAdapter(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREAR_TABLA_USUARIO);
        sqLiteDatabase.execSQL(CREAR_TABLA_DUCHAS);
        sqLiteDatabase.execSQL(CREAR_TABLA_LAVANDERIA);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLA_USUARIO);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLA_DUCHAS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLA_LAVANDERIA);
        onCreate(sqLiteDatabase);
    }

    public void insertarUsuario(String pNombre, String pDNI, String pTlf, String pFecha, String pNacionalidad){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CAMPO_NOMBRE, pNombre);
        values.put(CAMPO_DNI, pDNI);
        values.put(CAMPO_TLF, pTlf);
        values.put(CAMPO_FECHA, pFecha);
        values.put(CAMPO_NACIONALIDAD, pNacionalidad);
        db.insert(TABLA_USUARIO, null, values);
        //db.execSQL("DELETE FROM " + TABLA_USUARIO);
        db.close();
    }

    public void actualizarUsuario(int pId, String pNombre, String pDNI, String pTlf, String pFecha, String pNacionalidad){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + TABLA_USUARIO + " SET " +
                CAMPO_NOMBRE + " = '" + pNombre + "', " +
                CAMPO_DNI + " = '" + pDNI + "', " +
                CAMPO_TLF + " = '" + pTlf + "', " +
                CAMPO_FECHA + " = '" + pFecha + "', " +
                CAMPO_NACIONALIDAD + " = '" + pNacionalidad +
                "' WHERE " + CAMPO_ID + " = " + pId);
    }

    public ArrayList<User> cargarUsuarios(){
        SQLiteDatabase db = this.getReadableDatabase();
        User usuario = null;
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLA_USUARIO, null);

        ArrayList<User> lUsuarios = new ArrayList<User>();

        while (cursor.moveToNext()) {
            usuario = new User();
            usuario.setId(cursor.getInt(0));
            usuario.setNombre(cursor.getString(1));
            usuario.setDNI(cursor.getString(2));
            usuario.setTlf(cursor.getString(3));
            usuario.setFNacimiento(cursor.getString(4));
            usuario.setNacionalidad(cursor.getString(5));

            lUsuarios.add(usuario);
        }
        db.close();
        return lUsuarios;
    }

    public ArrayList<String> getDatos(int pId){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> datos = new ArrayList<String>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLA_USUARIO + " WHERE " + CAMPO_ID + " = " + pId, null);

        if (cursor.moveToNext()){
            datos.add(cursor.getString(1));
            datos.add(cursor.getString(2));
            datos.add(cursor.getString(3));
            datos.add(cursor.getString(4));
            datos.add(cursor.getString(5));
        }
        return datos;
    }

    public ArrayList<String> getFechasOcupadasLavanderia(int pId){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> fechas = new ArrayList<String>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLA_LAVANDERIA + " GROUP BY " + CAMPO_FECHA_RESERVA + " HAVING COUNT(" + CAMPO_FECHA_RESERVA + ") >= " + MAX_USUARIOS_LAVANDERIA, null);

        while (cursor.moveToNext()){
            fechas.add(cursor.getString(1));
        }
        return fechas;
    }

    public void registrarFechaLavanderia(int pId, String pFecha){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CAMPO_ID, pId);
        values.put(CAMPO_FECHA_RESERVA, pFecha);
        db.insert(TABLA_LAVANDERIA, null, values);
        db.close();
    }

    public void registrarFechaDucha(int pId, String pFecha){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CAMPO_ID, pId);
        values.put(CAMPO_FECHA_RESERVA, pFecha);
        db.insert(TABLA_DUCHAS, null, values);
        db.close();
    }

    public ArrayList<String> getAnnos(){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Integer> annos = new ArrayList<Integer>();
        ArrayList<String> annosStrings = new ArrayList<String>();
        String annoActual;
        Cursor cursor1 = db.rawQuery("SELECT DISTINCT " + CAMPO_FECHA_RESERVA + " FROM " + TABLA_LAVANDERIA, null);
        while (cursor1.moveToNext()){
            annoActual = cursor1.getString(0).substring(6);
            if (!annos.contains(Integer.valueOf(annoActual))) {
                annos.add(Integer.valueOf(annoActual));
            }
        }
        Cursor cursor2 = db.rawQuery("SELECT DISTINCT " + CAMPO_FECHA_RESERVA + " FROM " + TABLA_DUCHAS, null);
        while (cursor2.moveToNext()){
            annoActual = cursor2.getString(0).substring(6);
            if (!annos.contains(Integer.valueOf(annoActual))) {
                annos.add(Integer.valueOf(annoActual));
            }
        }
        Collections.sort(annos);

        for (int i = 0; i < annos.size(); i++){
            annosStrings.add(annos.get(i).toString());
        }
        return annosStrings;
    }

    public ArrayList<Integer> getDesgloseAnno(ArrayList<Integer> pIds, String pAnno, String pServicio){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Integer> datos = new ArrayList<Integer>();
        ArrayList<Boolean> apariciones;
        ArrayList<String> annosStrings = new ArrayList<String>();
        if (!pAnno.equals("Todos")){
            for (int j = 0; j < 13; j++){
                datos.add(0);
            }
        }
        else{
            annosStrings = this.getAnnos();
            for (int j = 0; j < annosStrings.size() + 1; j++){
                datos.add(0);
            }

        }




        for (int i = 0; i < pIds.size(); i++) {

            //Damos forma al arraylist que devolveremos como resultado
            apariciones = new ArrayList<Boolean>();
            if (!pAnno.equals("Todos")){
                for (int j = 0; j < 13; j++){
                    apariciones.add(false);
                }
            }
            else{
                annosStrings = this.getAnnos();
                for (int j = 0; j < annosStrings.size() + 1; j++){
                    apariciones.add(false);
                }

            }

            String baseQuery = "SELECT " + TABLA_USUARIO + "." + CAMPO_ID + ", " + CAMPO_FECHA_RESERVA + " FROM " + TABLA_USUARIO;

            String queryDuchas = baseQuery + " INNER JOIN " + TABLA_DUCHAS + " ON " + TABLA_USUARIO + "." + CAMPO_ID + "=" + TABLA_DUCHAS + "." + CAMPO_ID;
            String queryLavanderia = baseQuery + " INNER JOIN " + TABLA_LAVANDERIA + " ON " + TABLA_USUARIO + "." + CAMPO_ID + "=" + TABLA_LAVANDERIA + "." + CAMPO_ID;

            queryDuchas = queryDuchas + " WHERE " + TABLA_USUARIO + "." + CAMPO_ID + "=" + pIds.get(i);
            queryLavanderia = queryLavanderia + " WHERE " + TABLA_USUARIO + "." + CAMPO_ID + "=" + pIds.get(i);



            if (pServicio.equals("Duchas")) {
                Cursor cursor = db.rawQuery(queryDuchas, null);
                if (!pAnno.equals("Todos")){ //Si se nos pide un anno concreto

                    int mesActual;
                    while (cursor.moveToNext()) {
                        if (cursor.getString(1).substring(6).equals(pAnno)) {
                            mesActual = Integer.valueOf(cursor.getString(1).substring(3, 5)) - 1;
                            apariciones.set(mesActual, true);
                            apariciones.set(12, true);
                        }
                    }
                }
                else{ //Si se nos piden todos los annos
                    String annoQuery;
                    String annoListado;
                    while (cursor.moveToNext()) {
                        annoQuery = cursor.getString(1).substring(6);
                        for (int j = 0; j < annosStrings.size(); j++){
                            annoListado = annosStrings.get(j);
                            if (annoQuery.equals(annoListado)) {
                                apariciones.set(j, true);
                                apariciones.set(apariciones.size() - 1, true);
                            }
                        }
                    }
                }
            }
            else if (pServicio.equals("Lavandería")) {
                Cursor cursor = db.rawQuery(queryLavanderia, null);
                if (!pAnno.equals("Todos")){

                    int mesActual;
                    while (cursor.moveToNext()) {
                        if (cursor.getString(1).substring(6).equals(pAnno)) {
                            mesActual = Integer.valueOf(cursor.getString(1).substring(3, 5)) - 1;
                            apariciones.set(mesActual, true);
                            apariciones.set(12, true);
                        }
                    }
                }
                else{ //Si se nos piden todos los annos
                    String annoQuery;
                    String annoListado;
                    while (cursor.moveToNext()) {
                        annoQuery = cursor.getString(1).substring(6);
                        for (int j = 0; j < annosStrings.size(); j++){
                            annoListado = annosStrings.get(j);
                            if (annoQuery.equals(annoListado)) {
                                apariciones.set(j, true);
                                apariciones.set(apariciones.size() - 1, true);
                            }
                        }
                    }
                }
            }
            else{
                Cursor cursor1 = db.rawQuery(queryDuchas, null);
                Cursor cursor2 = db.rawQuery(queryLavanderia, null);
                if (!pAnno.equals("Todos")){

                    int mesActual;
                    while (cursor1.moveToNext()) {
                        if (cursor1.getString(1).substring(6).equals(pAnno)) {
                            mesActual = Integer.valueOf(cursor1.getString(1).substring(3, 5)) - 1;
                            apariciones.set(mesActual, true);
                            apariciones.set(12, true);
                        }
                    }
                    while (cursor2.moveToNext()) {
                        if (cursor2.getString(1).substring(6).equals(pAnno)) {
                            mesActual = Integer.valueOf(cursor2.getString(1).substring(3, 5)) - 1;
                            apariciones.set(mesActual, true);
                            apariciones.set(12, true);
                        }
                    }
                }
                else{ //Si se nos piden todos los annos
                    String annoQuery;
                    String annoListado;
                    while (cursor1.moveToNext()) {
                        annoQuery = cursor1.getString(1).substring(6);
                        for (int j = 0; j < annosStrings.size(); j++){
                            annoListado = annosStrings.get(j);
                            if (annoQuery.equals(annoListado)) {
                                apariciones.set(j, true);
                                apariciones.set(apariciones.size() - 1, true);
                            }
                        }
                    }
                    while (cursor2.moveToNext()) {
                        annoQuery = cursor2.getString(1).substring(6);
                        for (int j = 0; j < annosStrings.size(); j++){
                            annoListado = annosStrings.get(j);
                            if (annoQuery.equals(annoListado)) {
                                apariciones.set(j, true);
                                apariciones.set(apariciones.size() - 1, true);
                            }
                        }
                    }
                }
            }


            //Sumamos 1 a cada posicion del vector resultado si hay al menos una aparicion
            for (int j = 0; j < apariciones.size(); j++){
                if (apariciones.get(j)){
                    datos.set(j, datos.get(j) + 1);
                }
            }
        }
        return datos;
    }
}
