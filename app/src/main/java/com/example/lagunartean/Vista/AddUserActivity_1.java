package com.example.lagunartean.Vista;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.lagunartean.R;

public class AddUserActivity_1 extends AppCompatActivity implements View.OnClickListener {

    private EditText campoNombre;
    private EditText campoDNI;
    private EditText campoTlf;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        campoNombre = (EditText) findViewById(R.id.campo_nombre);
        campoDNI = (EditText) findViewById(R.id.campo_dni);
        campoTlf = (EditText) findViewById(R.id.campo_tlf);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_siguiente:

                Intent miIntent = new Intent(AddUserActivity_1.this, AddUserActivity_2.class);
                miIntent.putExtra("nombre", campoNombre.getText().toString());
                miIntent.putExtra("DNI", campoDNI.getText().toString());
                miIntent.putExtra("tlf", campoTlf.getText().toString());

                startActivity(miIntent);
        }
    }
}