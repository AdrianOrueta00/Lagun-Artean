package com.example.lagunartean.Modelo;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lagunartean.Controlador.DatabaseAdapter;
import com.example.lagunartean.Controlador.ServiceAdapter;
import com.example.lagunartean.Controlador.UsersAdapter;
import com.example.lagunartean.Vista.MainActivity;
import com.example.lagunartean.Vista.UserServiceViewHolder;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

public class Application {

    private static Application miApplication;
    private UserList lUsuarios;
    private DatabaseAdapter db;
    private String filtro;

    private Application(Context pContext){
        db = new DatabaseAdapter(pContext.getApplicationContext(), "LagunArtean", null, 3);
        lUsuarios = new UserList(db.cargarUsuarios());
        filtro = "";
    }

    public static Application getMiApplication(Context pContext){
        if (miApplication==null){
            miApplication = new Application(pContext);
        }
        return miApplication;
    }

    public void anadirUsuario(String pNombre, String pDNI, String pTlf, String pFecha, String pNacionalidad){
        db.insertarUsuario(pNombre, pDNI, pTlf, pFecha, pNacionalidad);
    }

    public void actualizarUsuario(int pId, String pNombre, String pDNI, String pTlf, String pFecha, String pNacionalidad){
        db.actualizarUsuario(pId, pNombre, pDNI, pTlf, pFecha, pNacionalidad);
    }

    public void mostrarUsuarios(RecyclerView pLista, String pFiltro, Context pContext, boolean pServices){
        lUsuarios = new UserList(db.cargarUsuarios());
        filtro = pFiltro;

        pLista.setLayoutManager(new LinearLayoutManager(pContext));
        UserList listaFiltrada = lUsuarios.filtrarNombre(filtro);
        if (pServices){
            ServiceAdapter serviceAdapter = new ServiceAdapter(pContext, listaFiltrada);
            pLista.setAdapter(serviceAdapter);
        }
        else {
            UsersAdapter usersAdapter = new UsersAdapter(pContext, listaFiltrada);
            pLista.setAdapter(usersAdapter);
        }
    }

    public ArrayList<String> getDatos(int pId){
        return db.getDatos(pId);
    }

    public void reservar(int pPos, String pServicio, Context ctx, FragmentManager pFM){
        int id = lUsuarios.get(pPos).getId();
        String nombre = lUsuarios.get(pPos).getNombre();
        if (pServicio.equals("ducha")){

            //Generamos un fragment de calendario que restrinja las fechas pasadas
            MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
            builder.setSelection(MaterialDatePicker.todayInUtcMilliseconds());
            builder.setCalendarConstraints(duchasConstraints().build());
            MaterialDatePicker<Long> materialDatePicker = builder.build();
            materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
                @Override
                public void onPositiveButtonClick(Long selection) {
                    Calendar c = Calendar.getInstance();
                    c.setTimeInMillis(selection);
                    String fechaString = formatDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                    db.registrarFechaDucha(id, fechaString);
                    Toast.makeText(ctx, "Se ha registrado a " + nombre + " para ducharse el día " + fechaString, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(ctx.getApplicationContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    ctx.startActivity(intent);
                }
            });
            materialDatePicker.show(pFM, "");
        }
        else if (pServicio.equals("lavanderia")){

            //Consultamos fechas con 7 o mas reservas
            ArrayList<String> fechasOcupadasString = db.getFechasOcupadasLavanderia(id);
            ArrayList<Long> fechasOcupadas = new ArrayList<Long>();
            int dia;
            int mes;
            int anno;
            String fActual;
            Calendar c2 = Calendar.getInstance();
            for (int i=0; i<fechasOcupadasString.size(); i++){
                fActual = fechasOcupadasString.get(i);
                dia = Integer.parseInt(fActual.substring(0, 2));
                mes = Integer.parseInt(fActual.substring(3, 5));
                anno = Integer.parseInt(fActual.substring(6));
                c2.set(Calendar.YEAR, anno);
                c2.set(Calendar.MONTH, mes - 1);
                c2.set(Calendar.DAY_OF_MONTH, dia);
                fechasOcupadas.add(c2.getTimeInMillis());
            }

            //Generamos fragment de calendario con fechas pasadas o completas restringidas
            MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
            builder.setSelection(MaterialDatePicker.todayInUtcMilliseconds());
            builder.setCalendarConstraints(lavanderiaConstraints(fechasOcupadas).build());
            MaterialDatePicker<Long> materialDatePicker = builder.build();
            materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
                @Override
                public void onPositiveButtonClick(Long selection) {
                    Calendar c = Calendar.getInstance();
                    c.setTimeInMillis(selection);
                    String fechaString = formatDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                    db.registrarFechaLavanderia(id, fechaString);
                    Toast.makeText(ctx, "Se ha registrado a " + nombre + " en la lavandería el día " + fechaString, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(ctx.getApplicationContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    ctx.startActivity(intent);
                }
            });
            materialDatePicker.show(pFM, "");
        }
    }
    private CalendarConstraints.Builder lavanderiaConstraints(ArrayList<Long> pFechas) {
        CalendarConstraints.Builder constraintsBuilderRange = new CalendarConstraints.Builder();
        constraintsBuilderRange.setValidator(new ValidadorFechasLavanderia(pFechas));
        return constraintsBuilderRange;
    }
    private CalendarConstraints.Builder duchasConstraints() {
        Calendar c = Calendar.getInstance();
        CalendarConstraints.Builder constraintsBuilderRange = new CalendarConstraints.Builder();
        constraintsBuilderRange.setValidator(DateValidatorPointForward.from(c.getTimeInMillis()));
        return constraintsBuilderRange;
    }

    public String formatDate(int year, int month, int day){
        month++;
        String fecha = "";
        if (day<10) {
            fecha = fecha + "0";
        }
        fecha = fecha + day + "/";
        if (month<10) {
            fecha = fecha + "0";
        }
        fecha = fecha + month + "/";
        fecha = fecha + year;
        return fecha;
    }

    public Integer getPositionOfCheckedUser(){
        Integer posSeleccionada = null;
        User usuarioActual;
        UserList listaFiltrada = lUsuarios.filtrarNombre(filtro);
        for (int i = 0; i < listaFiltrada.getLength(); i++) {
            usuarioActual = listaFiltrada.get(i);
            if (usuarioActual.isChecked()) {
                if (posSeleccionada == null) {
                    posSeleccionada = i;
                } else {
                    posSeleccionada = -1;
                }
            }
        }
        return posSeleccionada;
    }

    public ArrayList<String> getEdades(){
        ArrayList<Integer> edades = new ArrayList<Integer>();
        int edadActual;
        for (int i = 0; i < lUsuarios.getLength(); i++){
            edadActual = Integer.valueOf(lUsuarios.get(i).getEdad());
            if (!edades.contains(edadActual)) {
                edades.add(Integer.valueOf(edadActual));
            }
        }
        Collections.sort(edades);

        ArrayList<String> edadesStrings = new ArrayList<String>();
        edadesStrings.add("Todas");
        for (int i = 0; i < edades.size(); i++){
            edadesStrings.add(edades.get(i).toString());
        }
        return edadesStrings;
    }

    public ArrayList<String> getAnnos(){
        ArrayList<String> annosQuery = db.getAnnos();


        ArrayList<String> annosStrings = new ArrayList<String>();
        annosStrings.add("Todos");
        for (int i = 0; i < annosQuery.size(); i++){
            annosStrings.add(annosQuery.get(i).toString());
        }
        return annosStrings;
    }

    public ArrayList<Integer> getDatosPlot(String pNacionalidad, String pEdad, String pAnno, String pServicio){
        UserList listaFiltrada = lUsuarios;
        if (pNacionalidad != null){
            listaFiltrada = listaFiltrada.filtrarNacionalidad(pNacionalidad);
        }
        if (!pEdad.equals("Todas")){
            listaFiltrada = listaFiltrada.filtrarEdad(pEdad);
        }

        ArrayList<Integer> idsConsulta = new ArrayList<Integer>();
        for (int i = 0; i < listaFiltrada.getLength(); i++){
            idsConsulta.add(listaFiltrada.get(i).getId());
        }

        return db.getDesgloseAnno(idsConsulta, pAnno, pServicio);
    }
}
