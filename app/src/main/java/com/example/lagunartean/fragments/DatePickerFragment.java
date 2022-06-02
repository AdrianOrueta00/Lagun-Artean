package com.example.lagunartean.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;

import com.example.lagunartean.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    private EditText campo;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        c.getTime();


        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year-18, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {


        Calendar c = Calendar.getInstance();
        c.set(year, month, day);
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
/*
        SimpleDateFormat date = new SimpleDateFormat(fecha);
        fecha = date.format(c.getTime());*/
        campo.setText(fecha);
    }

    public void setCampo(EditText pCampo){
        campo = pCampo;
    }

}