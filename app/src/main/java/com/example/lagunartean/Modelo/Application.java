package com.example.lagunartean.Modelo;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lagunartean.Controlador.DatabaseAdapter;
import com.example.lagunartean.Controlador.ServiceAdapter;
import com.example.lagunartean.Controlador.UsersAdapter;
import com.example.lagunartean.R;
import com.example.lagunartean.Vista.MainActivity;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Locale;

public class Application {

    private static Application miApplication;
    private UserList lUsuarios;
    private DatabaseAdapter db;
    private String filtro;
    private String nombreSesion;
    private String contrasenaSesion;
    private String idioma;

    private Application(Context pContext){ //Patron Singleton
        db = new DatabaseAdapter(pContext.getApplicationContext(), "LagunArtean", null, 10);
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
        //Representar en el recycler los usuarios correspondientes
        //Filtrados por texto
        lUsuarios = new UserList(db.cargarUsuarios());
        filtro = pFiltro;

        pLista.setLayoutManager(new LinearLayoutManager(pContext));
        UserList listaFiltrada = lUsuarios.filtrarNombre(filtro);
        if (pServices){ //Si se llama a la funcion desde la pantalla de servicios
            //(Tienen adaptadores y viewholders distintos)
            ServiceAdapter serviceAdapter = new ServiceAdapter(pContext, listaFiltrada);
            pLista.setAdapter(serviceAdapter);
        }
        else { //Si se llama a la funcion desde la pantalla de listado
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
            //Si queremos reservar una ducha
            //Generamos un fragment de calendario que restrinja las fechas pasadas
            MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
            builder.setSelection(MaterialDatePicker.todayInUtcMilliseconds());
            builder.setCalendarConstraints(duchasConstraints().build());
            MaterialDatePicker<Long> materialDatePicker = builder.build();
            materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
                @Override
                public void onPositiveButtonClick(Long selection) {
                    //Registramos la reserva
                    Calendar c = Calendar.getInstance();
                    c.setTimeInMillis(selection);
                    String fechaString = formatDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                    db.registrarFechaDucha(id, fechaString);
                    String texto = ctx.getString(R.string.mensaje_ducha_registrada);
                    texto = String.format(texto, nombre, fechaString);
                    Toast.makeText(ctx, texto, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(ctx.getApplicationContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    ctx.startActivity(intent);
                }
            });
            materialDatePicker.show(pFM, "");
        }
        else if (pServicio.equals("lavanderia")){
            //Si queremos reservar en la lavanderia
            //Consultamos fechas con 7 o mas reservas
            ArrayList<String> fechasOcupadasString = db.getFechasOcupadasLavanderia();
            ArrayList<Long> fechasOcupadas = new ArrayList<Long>();
            int dia;
            int mes;
            int anno;
            String fActual;
            Calendar c2 = Calendar.getInstance();

            //Generamos un arraylist con todas las fechas ocupadas en formato Long
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
                    //Registramos reserva de lavanderia
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
        //Clase necesaria para personalizar restricciones del fragment calendario
        CalendarConstraints.Builder constraintsBuilderRange = new CalendarConstraints.Builder();
        constraintsBuilderRange.setValidator(new ValidadorFechasLavanderia(pFechas));
        return constraintsBuilderRange;
    }
    private CalendarConstraints.Builder duchasConstraints() {
        //Clase necesaria para personalizar restricciones del fragment calendario
        Calendar c = Calendar.getInstance();
        CalendarConstraints.Builder constraintsBuilderRange = new CalendarConstraints.Builder();
        constraintsBuilderRange.setValidator(DateValidatorPointForward.from(c.getTimeInMillis()));
        return constraintsBuilderRange;
    }

    public String formatDate(int year, int month, int day){
        //Pasar de numeros a un String
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
        //Comprueba el atributo checkado de los usuarios que se muestran en el recyclerview
        Integer posSeleccionada = null;
        User usuarioActual;
        UserList listaFiltrada = lUsuarios.filtrarNombre(filtro);
        for (int i = 0; i < listaFiltrada.getLength(); i++) {
            usuarioActual = listaFiltrada.get(i);
            if (usuarioActual.isChecked()) {
                if (posSeleccionada == null) { //Si es la primera casilla marcada que encuentra
                    posSeleccionada = i;
                } else { //Si hay mas de una casilla marcada
                    posSeleccionada = -1;
                }
            }
        }
        return posSeleccionada;
    }

    public ArrayList<String> getEdades(Context pContexto){ //Devuelve las edades existentes entre todos los usuarios
                                          //Ordenadas y sin repetir
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
        edadesStrings.add(pContexto.getString(R.string.todas));
        for (int i = 0; i < edades.size(); i++){
            edadesStrings.add(edades.get(i).toString());
        }
        return edadesStrings;
    }

    public ArrayList<String> getAnnos(Context pContexto){ //Devuelve los annos en los que existen reservas
                                         //Ordenados y sin repetir
        ArrayList<String> annosQuery = db.getAnnos();


        ArrayList<String> annosStrings = new ArrayList<String>();
        annosStrings.add(pContexto.getString(R.string.todos));
        for (int i = 0; i < annosQuery.size(); i++){
            annosStrings.add(annosQuery.get(i).toString());
        }
        return annosStrings;
    }

    public ArrayList<Integer> getDatosPlot(String pNacionalidad, String pEdad, String pAnno, String pServicio){
        UserList listaFiltrada = lUsuarios;

        //filtramos por nacionalidad y edad
        if (pNacionalidad != null){
            listaFiltrada = listaFiltrada.filtrarNacionalidad(pNacionalidad);
        }
        if (!pEdad.equals("Todas")&&!pEdad.equals("Guztiak")){
            listaFiltrada = listaFiltrada.filtrarEdad(pEdad);
        }

        //Conseguimos ids y se los pasamos a DatabaseAdapter para que obtenga los datos
        //Teniendo en cuenta el anno y el servicio seleccionados
        ArrayList<Integer> idsConsulta = new ArrayList<Integer>();
        for (int i = 0; i < listaFiltrada.getLength(); i++){
            idsConsulta.add(listaFiltrada.get(i).getId());
        }

        return db.getDesgloseAnno(idsConsulta, pAnno, pServicio);
    }

    public boolean existeEmpleado(String pNombre, String pContrasena){
        return db.comprobarEmpleado(pNombre, pContrasena, db.getReadableDatabase());
    }

    public boolean esAdmin(){
        return db.isAdmin(nombreSesion, contrasenaSesion);
    }

    public void anadirEmpleado(String pNombre, String pContrasena){
        db.insertarEmpleado(pNombre, pContrasena, false, "es", db.getWritableDatabase());
    }

    public void iniciarSesion(String pNombre, String pContrasena){
        String idioma = db.getIdioma(pNombre, pContrasena);
        if (idioma==null){
            idioma = "es";
        }
        this.nombreSesion = pNombre;
        this.contrasenaSesion = pContrasena;
        this.idioma = idioma;
    }

    public void actualizarIdioma(String pIdioma, Context pContexto){
        db.setIdioma(nombreSesion, contrasenaSesion, pIdioma);
        idioma=pIdioma;
        setIdiomaAplicacion(pContexto);
    }

    public void setIdiomaAplicacion(Context pContexto){
        Locale locale = new Locale(idioma);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        pContexto.getResources().updateConfiguration(config, pContexto.getResources().getDisplayMetrics());
    }

    public String getIdioma(){
        return idioma;
    }

    public int getMaxLavanderia(){
        int max = db.getMaxUsuariosLavanderia();
        return max;
    }

    public void setMaxLavanderia(int pMax){
        db.setMaxUsuariosLavanderia(pMax);
    }

    public boolean hayMaxLavanderia(){
        return db.maxUsuariosLavanderiaActivado();
    }

    public void setActivarMaxLavanderia(boolean pActivado){
        db.setMaxUsuariosLavanderiaActivado(pActivado);
    }
}
