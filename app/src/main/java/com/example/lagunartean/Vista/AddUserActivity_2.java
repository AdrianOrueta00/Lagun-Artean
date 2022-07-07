package com.example.lagunartean.Vista;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lagunartean.Modelo.Application;
import com.example.lagunartean.R;
import com.example.lagunartean.Modelo.User;
import com.example.lagunartean.Vista.fragments.DatePickerFragment;
import com.hbb20.CountryCodePicker;

public class AddUserActivity_2 extends AppCompatActivity implements View.OnClickListener{

    private EditText campoFecha;
    private CountryCodePicker campoNacionalidad;

    private String nombre;
    private String DNI;
    private String tlf;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        getSupportActionBar().hide();

        campoFecha = findViewById(R.id.campo_fecha_nacimiento);
        campoFecha.setOnClickListener(this);
        campoNacionalidad = findViewById(R.id.campo_nacionalidad);
        //campoNacionalidad.setOnClickListener(this);
        this.nombre = getIntent().getStringExtra("nombre");
        this.DNI = getIntent().getStringExtra("DNI");
        this.tlf = getIntent().getStringExtra("tlf");

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.campo_fecha_nacimiento:
                DatePickerFragment newFragment = new DatePickerFragment();
                newFragment.setCampo(campoFecha);
                newFragment.show(getSupportFragmentManager(), "Fecha de nacimiento");
                break;

            case R.id.btn_registrar_usuario:

                String codPais = campoNacionalidad.getSelectedCountryNameCode();
                String fecha = campoFecha.getText().toString();

                Application.getMiApplication(getApplicationContext()).anadirUsuario(nombre, DNI, tlf, fecha, codPais);

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

                break;
        }
    }
}
