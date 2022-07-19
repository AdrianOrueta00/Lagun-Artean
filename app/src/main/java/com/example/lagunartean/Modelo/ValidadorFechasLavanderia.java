package com.example.lagunartean.Modelo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.material.datepicker.CalendarConstraints;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ValidadorFechasLavanderia implements CalendarConstraints.DateValidator {

    private ArrayList<Long> fechas;

    ValidadorFechasLavanderia(ArrayList<Long> pFechas) {
        this.fechas = pFechas;
    }

    ValidadorFechasLavanderia(Parcel parcel) {
    }

    @Override
    public boolean isValid(long date) {
        int i = 0;
        boolean valid = true;
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        while (valid && i<fechas.size()){
            start.setTimeInMillis(fechas.get(i));
            start.add(Calendar.DATE, -1);
            end.setTimeInMillis(fechas.get(i));
            valid = valid && !(date>start.getTimeInMillis() && date<end.getTimeInMillis());
            i++;
        }
        //Comprobamos que no sea un dia pasado
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, -1);
        valid = valid && (date > c.getTimeInMillis());
        return valid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }

    public static final Parcelable.Creator<ValidadorFechasLavanderia> CREATOR = new Parcelable.Creator<ValidadorFechasLavanderia>() {

        @Override
        public ValidadorFechasLavanderia createFromParcel(Parcel parcel) {
            return new ValidadorFechasLavanderia(parcel);
        }

        @Override
        public ValidadorFechasLavanderia[] newArray(int size) {
            return new ValidadorFechasLavanderia[size];
        }
    };
}
