package com.example.lagunartean.Controlador;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;


public class DatabaseAdapter extends SQLiteOpenHelper {

    private static final String TABLA_USUARIO = "usuarios";
    private static final String CAMPO_ID = "id";
    private static final String CAMPO_NOMBRE = "nombre";
    private static final String CAMPO_DNI = "dni";
    private static final String CAMPO_TLF = "telefono";
    private static final String CAMPO_FECHA = "fnacimiento";
    private static final String CAMPO_NACIONALIDAD = "nacionalidad";

    private static final String CREAR_TABLA_USUARIO = "CREATE TABLE IF NOT EXISTS " + TABLA_USUARIO +
            "(" + CAMPO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            CAMPO_NOMBRE + " TEXT, " +
            CAMPO_DNI + " TEXT, " +
            CAMPO_TLF + " TEXT, " +
            CAMPO_FECHA + " TEXT, " +
            CAMPO_NACIONALIDAD + " TEXT)";


    public DatabaseAdapter(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREAR_TABLA_USUARIO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLA_USUARIO);
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
}
