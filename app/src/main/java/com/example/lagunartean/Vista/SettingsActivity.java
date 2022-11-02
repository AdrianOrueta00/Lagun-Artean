package com.example.lagunartean.Vista;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lagunartean.Modelo.Application;
import com.example.lagunartean.R;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener{

    private RadioGroup selector;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Application.getMiApplication(getApplicationContext()).setIdiomaAplicacion(getApplicationContext());
        setContentView(R.layout.activity_settings_regular);
        getSupportActionBar().hide();
        selector = findViewById(R.id.radio_idioma_regular);
        String idioma = getIntent().getStringExtra("idioma");
        if (idioma.equals("es")){
            RadioButton c = (RadioButton) findViewById(R.id.radio_castellano_regular);
            c.setChecked(true);
        }
        else if (idioma.equals("eu")){
            RadioButton c = (RadioButton) findViewById(R.id.radio_euskera_regular);
            c.setChecked(true);
        }
    }
    @Override
    public void onClick(View view) {
        int selectedId = selector.getCheckedRadioButtonId();
        switch (selectedId) {
            case R.id.radio_castellano_regular:
                Application.getMiApplication(getApplicationContext()).actualizarIdioma("es", this);
                break;

            case R.id.radio_euskera_regular:
                Application.getMiApplication(getApplicationContext()).actualizarIdioma("eu", this);
                break;
        }
        Intent miIntent = new Intent(SettingsActivity.this, MainActivity.class);
        miIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(miIntent);
        finish();
    }
}
