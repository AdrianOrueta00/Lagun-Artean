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

public class LoginEmployeeActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText campoNombre;
    private EditText campoContrasena;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_employee_login);
        getSupportActionBar().hide();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_inicio_sesion:
                campoNombre = (EditText) findViewById(R.id.campo_nombre_empleado_login);
                campoContrasena = (EditText) findViewById(R.id.campo_contrasena_login);
                String nombre = campoNombre.getText().toString();
                String contrasena = campoContrasena.getText().toString();
                if (Application.getMiApplication(this).existeEmpleado(nombre, contrasena)) {
                    Application.getMiApplication(this).iniciarSesion(nombre, contrasena);
                    Intent miIntent1 = new Intent(LoginEmployeeActivity.this, MainActivity.class);
                    startActivity(miIntent1);
                    finish();
                }
                else{
                    Toast.makeText(this, "El usuario o la contraseña son incorrectos", Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.btn_ir_registro:

                Intent miIntent2 = new Intent(LoginEmployeeActivity.this, SignupEmployeeActivity.class);

                startActivity(miIntent2);
                finish();
                break;
        }
    }
}
