package com.example.lagunartean.Vista;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lagunartean.Modelo.Application;
import com.example.lagunartean.R;

public class SettingsAdminActivity extends AppCompatActivity implements View.OnClickListener{

    private RadioGroup selector;
    private CheckBox activarMaxLavanderia;
    private EditText campoMaxLavanderia;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Application.getMiApplication(getApplicationContext()).setIdiomaAplicacion(getApplicationContext());
        setContentView(R.layout.activity_settings_admin);
        getSupportActionBar().hide();
        selector = findViewById(R.id.radio_idioma_admin);
        String idioma = getIntent().getStringExtra("idioma");
        if (idioma.equals("es")){
            RadioButton c = (RadioButton) findViewById(R.id.radio_castellano_admin);
            c.setChecked(true);
        }
        else if (idioma.equals("eu")){
            RadioButton c = (RadioButton) findViewById(R.id.radio_euskera_admin);
            c.setChecked(true);
        }

        activarMaxLavanderia = findViewById(R.id.activar_max_lavanderia);
        campoMaxLavanderia = findViewById(R.id.campo_max_lavanderia);
        if (Application.getMiApplication(this).hayMaxLavanderia()){
            activarMaxLavanderia.setChecked(true);
        }
        int max = Application.getMiApplication(this).getMaxLavanderia();
        campoMaxLavanderia.setText(Integer.toString(max));

    }

    @Override
    public void onClick(View view) {

        //Gestionamos las opciones de idioma
        int selectedId = selector.getCheckedRadioButtonId();
        switch (selectedId) {
            case R.id.radio_castellano_admin:
                Application.getMiApplication(getApplicationContext()).actualizarIdioma("es", this);
                break;

            case R.id.radio_euskera_admin:
                Application.getMiApplication(getApplicationContext()).actualizarIdioma("eu", this);

                break;

        }

        //Gestionamos las opciones de la lavanderia
        Application.getMiApplication(this).setActivarMaxLavanderia(activarMaxLavanderia.isChecked());
        try {
            Application.getMiApplication(this).setMaxLavanderia(new Integer(campoMaxLavanderia.getText().toString()));
        }
        catch (Exception e){
            e.printStackTrace();
            Toast.makeText(this, R.string.error_max_lavanderia, Toast.LENGTH_SHORT).show();
        }

        Intent miIntent = new Intent(SettingsAdminActivity.this, MainActivity.class);
        miIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(miIntent);
        finish();
    }
}
