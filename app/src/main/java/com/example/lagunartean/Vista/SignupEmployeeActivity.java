package com.example.lagunartean.Vista;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lagunartean.Modelo.Application;
import com.example.lagunartean.R;

public class SignupEmployeeActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText campoNombre;
    private EditText campoContrasena;
    private EditText campoRepetirContrasena;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_employee_signup);
        getSupportActionBar().hide();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_registro_empleado:
                campoNombre = (EditText) findViewById(R.id.campo_nombre_registro_empleado);
                campoContrasena = (EditText) findViewById(R.id.campo_contrasena_registro1);
                campoRepetirContrasena = (EditText) findViewById(R.id.campo_contrasena_registro2);
                String nombre = campoNombre.getText().toString();
                String contrasena = campoContrasena.getText().toString();
                String contrasena2 = campoRepetirContrasena.getText().toString();
                if (!contrasena.equals(contrasena2)) {
                    Toast.makeText(this, "La contraseña no coincide", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (!Application.getMiApplication(this).existeEmpleado(nombre, contrasena)) {
                        Application.getMiApplication(this).anadirEmpleado(nombre, contrasena);
                        Intent miIntent1 = new Intent(SignupEmployeeActivity.this, MainActivity.class);

                        startActivity(miIntent1);
                        finish();
                    }
                    else{
                        Toast.makeText(this, "El usuario ya existe", Toast.LENGTH_SHORT).show();
                        boolean foo = Application.getMiApplication(this).esAdmin(nombre, contrasena);
                        System.out.println(foo);
                    }
                }
                break;

            case R.id.btn_ir_login:

                Intent miIntent2 = new Intent(SignupEmployeeActivity.this, LoginEmployeeActivity.class);

                startActivity(miIntent2);
                finish();
                break;
        }
    }
}
