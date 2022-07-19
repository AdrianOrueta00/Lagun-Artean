package com.example.lagunartean.Vista;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lagunartean.R;

public class UserServiceViewHolder extends RecyclerView.ViewHolder{

    public TextView txtNombre;
    public TextView txtEdad;
    public TextView txtTlf;
    public CheckBox selectorUsuario;

    public UserServiceViewHolder(@NonNull View itemView) {
        super(itemView);
        txtNombre = itemView.findViewById(R.id.txt_nombre_service);
        txtEdad = itemView.findViewById(R.id.txt_edad_service);
        txtTlf = itemView.findViewById(R.id.txt_tlf_service);
        selectorUsuario = itemView.findViewById(R.id.user_selection);
    }
}
