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

    public ArrayList<User> cargarUsuarios(){ //Pasa los datos de la base de datos a objetos
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

    public ArrayList<String> getDatos(int pId){ //Consulta los datos de un usuario especifico
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

    public ArrayList<String> getFechasOcupadasLavanderia(){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> fechas = new ArrayList<String>();
        //De todas las fechas en la tabla lavanderia devuelve las que tienen 7 reservas o mas
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

    public ArrayList<String> getAnnos(){ //Devuelve una lista ordenada de todos los annos en los que hay alguna reserva
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Integer> annos = new ArrayList<Integer>();
        ArrayList<String> annosStrings = new ArrayList<String>();
        String annoActual;
        //Todas las fechas no repetidas de la lavanderia
        Cursor cursor1 = db.rawQuery("SELECT DISTINCT " + CAMPO_FECHA_RESERVA + " FROM " + TABLA_LAVANDERIA, null);
        while (cursor1.moveToNext()){
            annoActual = cursor1.getString(0).substring(6);
            if (!annos.contains(Integer.valueOf(annoActual))) { //Si no nos la hemos encontrado ya
                annos.add(Integer.valueOf(annoActual));
            }
        }
        //Repetimos con las duchas
        Cursor cursor2 = db.rawQuery("SELECT DISTINCT " + CAMPO_FECHA_RESERVA + " FROM " + TABLA_DUCHAS, null);
        while (cursor2.moveToNext()){
            annoActual = cursor2.getString(0).substring(6);
            if (!annos.contains(Integer.valueOf(annoActual))) {
                annos.add(Integer.valueOf(annoActual));
            }
        }
        //Ordenamos arraylist con los annos (para ordenar necesitamos integer)
        Collections.sort(annos);

        for (int i = 0; i < annos.size(); i++){ //Pasamos a String
            annosStrings.add(annos.get(i).toString());
        }
        return annosStrings;
    }

    public ArrayList<Integer> getDesgloseAnno(ArrayList<Integer> pIds, String pAnno, String pServicio){
        //Este metodo se usa para los graficos
        //Existen dos tipos de graficos, el anual que desglosa en meses
        //Y el historico de annos, que desglosa en annos
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Integer> datos = new ArrayList<Integer>();
        ArrayList<Boolean> apariciones;
        ArrayList<String> annosStrings = new ArrayList<String>();

        //Damos forma al array de resultados que devolveremos
        if (!pAnno.equals("Todos")){
            for (int j = 0; j < 13; j++){ //12 barras y el total si es anual
                datos.add(0);
            }
        }
        else{
            annosStrings = this.getAnnos();
            for (int j = 0; j < annosStrings.size() + 1; j++){ //Tantas barras como annos haya en la bbdd
                datos.add(0);
            }

        }


        for (int i = 0; i < pIds.size(); i++) {

            //Inicializamos el array de apariciones
            //(Marca las columnas en las que aparece cada usuario)
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


            //Diferenciamos duchas, lavanderia, y duchas y lavanderia por tener queries distintas
            if (pServicio.equals("Duchas")) {
                Cursor cursor = db.rawQuery(queryDuchas, null);
                if (!pAnno.equals("Todos")){ //Si se nos pide un anno concreto

                    int mesActual;
                    while (cursor.moveToNext()) { //Por cada reserva del usuario actual
                        if (cursor.getString(1).substring(6).equals(pAnno)) { //Si el anno coincide
                            //Marcamos a true la posicion del mes correspondiente en las apariciones
                            mesActual = Integer.valueOf(cursor.getString(1).substring(3, 5)) - 1;
                            apariciones.set(mesActual, true);
                            apariciones.set(12, true);
                        }
                    }
                }
                else{ //Si se nos piden todos los annos
                    String annoQuery;
                    String annoListado;
                    while (cursor.moveToNext()) { //Por cada reserva del usuario actual
                        annoQuery = cursor.getString(1).substring(6);
                        for (int j = 0; j < annosStrings.size(); j++){ //Por cada anno en la base de datos (para crear un "indice")
                            annoListado = annosStrings.get(j);
                            if (annoQuery.equals(annoListado)) { //Si el anno de la reserva actual coincide con la posicion del indice
                                //Marcamos a true la posicion del anno correspondiente en las apariciones
                                apariciones.set(j, true);
                                apariciones.set(apariciones.size() - 1, true);
                            }
                        }
                    }
                }
            }
            else if (pServicio.equals("Lavandería")) { //Hacemos lo mismo que en Duchas pero con una query distinta
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
                else{
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
            else{ //Si se piden ambos servicios hay que hacer dos consultas
                Cursor cursor1 = db.rawQuery(queryDuchas, null);
                Cursor cursor2 = db.rawQuery(queryLavanderia, null);
                if (!pAnno.equals("Todos")){ //Si se nos pide un anno en concreto
                    //Bucle de la consulta de duchas
                    int mesActual;
                    while (cursor1.moveToNext()) {
                        if (cursor1.getString(1).substring(6).equals(pAnno)) {
                            mesActual = Integer.valueOf(cursor1.getString(1).substring(3, 5)) - 1;
                            apariciones.set(mesActual, true);
                            apariciones.set(12, true);
                        }
                    }
                    //Bucle de la consulta de lavanderia (sin limpiar array de apariciones)
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
                    //Bucle de la consulta de duchas
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
                    //Bucle de la consulta de lavanderia (sin limpiar array de apariciones)
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
            //(Igual para todas las consultas)
            for (int j = 0; j < apariciones.size(); j++){
                if (apariciones.get(j)){
                    datos.set(j, datos.get(j) + 1);
                }
            }
        }
        return datos;
    }
}
