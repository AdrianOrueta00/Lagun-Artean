package com.example.lagunartean;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.lagunartean.fragments.DatePickerFragment;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText campoNombre;
    private EditText campoDNI;
    private EditText campoTlf;
    private EditText campoFecha;
    private EditText campoNacionalidad;

    private Button btnSiguiente;
    private Button btnRegistrarse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.campo_fecha_nacimiento:
                DatePickerFragment newFragment = new DatePickerFragment();
                newFragment.setCampo(campoFecha);
                newFragment.show(getSupportFragmentManager(), "Fecha de nacimiento");
                break;
            case R.id.btn_siguiente:
                setContentView(R.layout.activity_login2);
                campoFecha = findViewById(R.id.campo_fecha_nacimiento);
                campoFecha.setOnClickListener(this);
        }
    }
}