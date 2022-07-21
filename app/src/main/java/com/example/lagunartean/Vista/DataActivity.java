package com.example.lagunartean.Vista;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.lagunartean.Modelo.Application;
import com.example.lagunartean.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.slider.LabelFormatter;
import com.hbb20.CountryCodePicker;

import java.util.ArrayList;

public class DataActivity extends AppCompatActivity implements View.OnClickListener{

    private CheckBox campoActivarNacionalidad;
    private CountryCodePicker campoNacionalidad;
    private Spinner campoEdad;
    private Spinner campoAnno;
    private Spinner campoServicio;
    private BarChart campoPlot;

    private String nacionalidad;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_data);
        getSupportActionBar().hide();

        campoActivarNacionalidad = findViewById(R.id.checkbox_data);
        campoNacionalidad = findViewById(R.id.selector_pais_data);
        campoEdad = findViewById(R.id.spinner_edad);
        campoAnno = findViewById(R.id.spinner_anno);
        campoServicio = findViewById(R.id.spinner_servicio);
        campoPlot = findViewById(R.id.barchart_data);

        //Activar y desactivar filtro de nacionalidad
        campoActivarNacionalidad.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    campoNacionalidad.setContentColor(-16777216);
                    campoNacionalidad.setCcpClickable(true);
                    nacionalidad = campoNacionalidad.getSelectedCountryNameCode();
                }
                else{
                    campoNacionalidad.setContentColor(-8355712);
                    campoNacionalidad.setCcpClickable(false);
                    nacionalidad = null;
                }
            }
        });
        //Necesario para que se vea en gris al entrar
        campoActivarNacionalidad.setChecked(true);
        campoActivarNacionalidad.setChecked(false);

        //Guardar nacionalidad cuando el usuario la selecciona
        campoNacionalidad.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                nacionalidad = campoNacionalidad.getSelectedCountryNameCode();
            }
        });

        //Llenar spinner de edades
        ArrayList<String> edades = Application.getMiApplication(this).getEdades();
        ArrayAdapter<String> spinnerArrayAdapter1 = new ArrayAdapter<String>(this, com.google.android.material.R.layout.support_simple_spinner_dropdown_item, edades);
        campoEdad.setAdapter(spinnerArrayAdapter1);

        //Llenar spinner de annos
        ArrayList<String> annos = Application.getMiApplication(this).getAnnos();
        ArrayAdapter<String> spinnerArrayAdapter2 = new ArrayAdapter<String>(this, com.google.android.material.R.layout.support_simple_spinner_dropdown_item, annos);
        campoAnno.setAdapter(spinnerArrayAdapter2);

        ArrayList<String> opcionesServicios = new ArrayList<String>();
        opcionesServicios.add("Todos");
        opcionesServicios.add("Duchas");
        opcionesServicios.add("Lavandería");
        ArrayAdapter<String> spinnerArrayAdapter3 = new ArrayAdapter<String>(this, com.google.android.material.R.layout.support_simple_spinner_dropdown_item, opcionesServicios);
        campoServicio.setAdapter(spinnerArrayAdapter3);

        findViewById(R.id.btn_consultar_datos).callOnClick();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_consultar_datos:
                //Conseguimos datos
                ArrayList<Integer> r = Application.getMiApplication(this).getDatosPlot(nacionalidad, campoEdad.getSelectedItem().toString(), campoAnno.getSelectedItem().toString(), campoServicio.getSelectedItem().toString());
                //Ponemos los datos en un formato adecuado para la libreria de graficos
                ArrayList<BarEntry> s = new ArrayList<BarEntry>();
                ArrayList<String> annos = Application.getMiApplication(this).getAnnos();
                String[] annosArray = new String[r.size()];
                for (int i = 0; i < r.size(); i++){
                    BarEntry entrada = new BarEntry( i, r.get(i));
                    s.add(entrada);
                    if (campoAnno.getSelectedItem().toString().equals("Todos")) {
                        if (i != 0) {
                            annosArray[i - 1] = annos.get(i);
                        } else {
                            annosArray[r.size() - 1] = annos.get(i);
                        }
                    }
                }
                //Introducimos los datos en el grafico y hacemos otras inicializaciones
                BarDataSet dataset = new BarDataSet(s, null);
                dataset.setColor(ContextCompat.getColor(this, R.color.LA_blue));
                BarData barData = new BarData(dataset);
                barData.setValueTextSize(10f);
                campoPlot.setData(barData);
                Description desc = new Description();
                desc.setEnabled(false);
                campoPlot.setDescription(desc);
                campoPlot.getXAxis().setDrawGridLines(false);
                campoPlot.getAxisLeft().setDrawAxisLine(false);
                campoPlot.getAxisRight().setDrawAxisLine(false);
                campoPlot.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                campoPlot.getXAxis().setLabelCount(dataset.getEntryCount());
                campoPlot.getAxisRight().setAxisMinValue(0);
                campoPlot.getAxisLeft().setAxisMinValue(0);
                if (campoAnno.getSelectedItem().toString().equals("Todos")) {
                    campoPlot.getXAxis().setValueFormatter(new IndexAxisValueFormatter(annosArray));
                }
                else{
                    campoPlot.getXAxis().setValueFormatter(new IndexAxisValueFormatter(new String[]{"Ene", "Feb", "Mar", "Abr", "May", "Jun", "Jul", "Ago", "Sep", "Oct", "Nov", "Dic", "Todos"}));
                }
                campoPlot.getXAxis().setGranularity(1f);
                campoPlot.getLegend().setEnabled(false);
                //Representamos grafico
                campoPlot.invalidate();

                break;
        }
    }
    public class LabelFormatter implements IAxisValueFormatter{
        private final String [] mLabels;

        public LabelFormatter(String[] labels){
            mLabels = labels;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis){
            return mLabels[(int) value];
        }
    }
}
