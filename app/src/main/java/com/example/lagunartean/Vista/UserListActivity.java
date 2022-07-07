package com.example.lagunartean.Vista;

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
        setContentView(R.layout.activity_user_list);
        getSupportActionBar().hide();
        userRecycler = findViewById(R.id.recycler);
        Application.getMiApplication(this).mostrarUsuarios(userRecycler, "",this);


        searchBar = findViewById(R.id.barra_busqueda);
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                Application.getMiApplication(getApplicationContext()).mostrarUsuarios(userRecycler, searchBar.getText().toString(), getApplicationContext());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }


}
