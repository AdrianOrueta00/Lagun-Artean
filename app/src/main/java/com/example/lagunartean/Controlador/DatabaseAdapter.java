package com.example.lagunartean.Controlador;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.lagunartean.Modelo.User;

import java.util.ArrayList;


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
}
