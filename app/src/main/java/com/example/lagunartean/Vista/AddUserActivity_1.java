package com.example.lagunartean.Vista;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.lagunartean.Modelo.Application;
import com.example.lagunartean.R;

import java.util.ArrayList;

public class AddUserActivity_1 extends AppCompatActivity implements View.OnClickListener {

    private EditText campoNombre;
    private EditText campoDNI;
    private EditText campoTlf;

    private int id;
    private ArrayList<String> datos;

    //Necesario para cerrar la actividad una vez AddUserActivity_2 haga finish();
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context arg0, Intent intent) {
            String action = intent.getAction();
            if (action.equals("finish_activity")) {
                finish();
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Application.getMiApplication(getApplicationContext()).setIdiomaAplicacion(getApplicationContext());
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        campoNombre = (EditText) findViewById(R.id.campo_nombre);
        campoDNI = (EditText) findViewById(R.id.campo_dni);
        campoTlf = (EditText) findViewById(R.id.campo_tlf);

        //Comprobamos si se ha abierto la actividad para registrar un nuevo usuario o editar uno existente
        this.id = getIntent().getIntExtra("id", -1);
        if (this.id != -1){
            datos = Application.getMiApplication(this).getDatos(this.id);
            campoNombre.setText(datos.get(0));
            campoDNI.setText(datos.get(1));
            campoTlf.setText(datos.get(2));
        }

        registerReceiver(broadcastReceiver, new IntentFilter("finish_activity"));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_siguiente:

                //Pasamos los datos a la segunda actividad de AddUser
                Intent miIntent = new Intent(AddUserActivity_1.this, AddUserActivity_2.class);
                miIntent.putExtra("id", this.id);
                miIntent.putExtra("nombre", campoNombre.getText().toString());
                miIntent.putExtra("DNI", campoDNI.getText().toString());
                miIntent.putExtra("tlf", campoTlf.getText().toString());
                if (this.id != -1) {
                    miIntent.putExtra("fNacimiento", datos.get(3));
                    miIntent.putExtra("nacionalidad", datos.get(4));
                }
                startActivity(miIntent);

        }
    }
}