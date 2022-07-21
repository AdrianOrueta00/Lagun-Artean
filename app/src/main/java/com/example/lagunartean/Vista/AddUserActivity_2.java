package com.example.lagunartean.Vista;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lagunartean.Modelo.Application;
import com.example.lagunartean.R;
import com.hbb20.CountryCodePicker;

public class AddUserActivity_2 extends AppCompatActivity implements View.OnClickListener{

    private DatePicker campoFecha;
    private CountryCodePicker campoNacionalidad;

    private int id;
    private String nombre;
    private String DNI;
    private String tlf;
    private String fNacimiento;
    private String nacionalidad;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_login2);
        getSupportActionBar().hide();

        campoFecha = findViewById(R.id.campo_fecha_nacimiento);
        campoFecha.setOnClickListener(this);
        campoNacionalidad = findViewById(R.id.campo_nacionalidad);
        campoNacionalidad.setDefaultCountryUsingNameCode("ma");
        campoNacionalidad.setCountryPreference("ma, tn, ly");
        this.nombre = getIntent().getStringExtra("nombre");
        this.DNI = getIntent().getStringExtra("DNI");
        this.tlf = getIntent().getStringExtra("tlf");

        //Comprobamos si estamos editando un usuario existente o creando uno nuevo
        this.id = getIntent().getIntExtra("id", -1);
        if (this.id != -1){
            this.fNacimiento = getIntent().getStringExtra("fNacimiento");
            this.nacionalidad = getIntent().getStringExtra("nacionalidad");
            int diaNacimiento = Integer.parseInt(fNacimiento.substring(0, 2));
            int mesNacimiento = Integer.parseInt(fNacimiento.substring(3, 5)) - 1;
            int annoNacimiento = Integer.parseInt(fNacimiento.substring(6));
            campoFecha.updateDate(annoNacimiento, mesNacimiento, diaNacimiento);
            campoNacionalidad.setDefaultCountryUsingNameCode(this.nacionalidad);
            campoNacionalidad.resetToDefaultCountry();
        }

    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btn_registrar_usuario:

                String codPais = campoNacionalidad.getSelectedCountryNameCode();
                int  anno = campoFecha.getYear();
                int mes = campoFecha.getMonth();
                int dia = campoFecha.getDayOfMonth();

                //Damos formato a la fecha de nacimiento
                String fecha = Application.getMiApplication(this).formatDate(anno, mes, dia);

                //Guardamos los cambios
                if (this.id == -1) {
                    Application.getMiApplication(getApplicationContext()).anadirUsuario(nombre, DNI, tlf, fecha, codPais);
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                else{
                    Application.getMiApplication(getApplicationContext()).actualizarUsuario(id, nombre, DNI, tlf, fecha, codPais);
                    Intent intent = new Intent("finish_activity");
                    sendBroadcast(intent);
                    finish();
                }


                break;
        }
    }
}
