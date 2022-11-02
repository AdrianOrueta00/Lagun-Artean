package com.example.lagunartean.Vista;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lagunartean.Modelo.Application;
import com.example.lagunartean.R;

public class UserListActivity extends AppCompatActivity {

    RecyclerView userRecycler;
    EditText searchBar;

    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        inicializar();
    }

    private void inicializar(){
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Application.getMiApplication(getApplicationContext()).setIdiomaAplicacion(getApplicationContext());
        setContentView(R.layout.activity_user_list);
        getSupportActionBar().hide();
        userRecycler = findViewById(R.id.recycler);
        //Construimos recyclerview
        Application.getMiApplication(this).mostrarUsuarios(userRecycler, "",this, false);


        searchBar = findViewById(R.id.barra_busqueda);
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //Volvemos a construir el recyclerview cada vez que se edita el texto de la barra de busqueda, pasando como parametro filtro el texto
                Application.getMiApplication(getApplicationContext()).mostrarUsuarios(userRecycler, searchBar.getText().toString(), getApplicationContext(), false);
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
}
