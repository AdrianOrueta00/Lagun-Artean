package com.example.lagunartean.Vista;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lagunartean.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_registro:

                Intent miIntent = new Intent(MainActivity.this, AddUserActivity_1.class);

                startActivity(miIntent);
        }
    }
}
