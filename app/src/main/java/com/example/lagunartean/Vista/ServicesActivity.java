package com.example.lagunartean.Vista;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lagunartean.Modelo.Application;
import com.example.lagunartean.R;

import java.util.ArrayList;

public class ServicesActivity extends AppCompatActivity implements View.OnClickListener{

    RecyclerView serviceRecycler;
    EditText searchBar;
    RadioGroup selector;
    Button btnSiguiente;

    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        inicializar();
    }

    private void inicializar(){
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Application.getMiApplication(getApplicationContext()).setIdiomaAplicacion(getApplicationContext());
        setContentView(R.layout.activity_services);
        getSupportActionBar().hide();
        selector = findViewById(R.id.radio_servicios);
        btnSiguiente = findViewById(R.id.btn_ver_fechas);
        serviceRecycler = findViewById(R.id.recycler_servicios);
        //Construimos recyclerview
        Application.getMiApplication(this).mostrarUsuarios(serviceRecycler, "",this, true);


        searchBar = findViewById(R.id.barra_busqueda_servicios);
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //Volvemos a construir el recyclerview cada vez que se edita el texto de la barra de busqueda, pasando como parametro filtro el texto
                Application.getMiApplication(getApplicationContext()).mostrarUsuarios(serviceRecycler, searchBar.getText().toString(), getApplicationContext(), true);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        inicializar();
    }

    public void onRadioButtonClicked(View view){

    }


    @Override
    public void onClick(View view) {
        Integer posSeleccionada = Application.getMiApplication(this).getPositionOfCheckedUser();
        //Miramos que usuario esta seleccionado

        if (posSeleccionada == null) {
            Toast.makeText(this, R.string.al_menos_un_usuario, Toast.LENGTH_SHORT).show();
        } else if (posSeleccionada == -1) {
            Toast.makeText(this, R.string.selecciona_solo_un_usuario, Toast.LENGTH_SHORT).show();
        } else {
            //Poppear calendario con fechas grises pasando por Application
            int selectedId = selector.getCheckedRadioButtonId();
            switch (selectedId) {
                case R.id.radio_ducha:
                    Application.getMiApplication(this).reservar(posSeleccionada, "ducha", this, getSupportFragmentManager());
                    break;

                case R.id.radio_lavanderia:
                    Application.getMiApplication(this).reservar(posSeleccionada, "lavanderia", this, getSupportFragmentManager());
                    break;

                case -1:
                    Toast.makeText(this, R.string.selecciona_un_servicio, Toast.LENGTH_SHORT).show();
                    break;
            }

        }
    }
}