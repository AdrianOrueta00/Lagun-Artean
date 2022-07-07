package com.example.lagunartean.Vista;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lagunartean.R;

public class UserViewHolder extends RecyclerView.ViewHolder{

    public TextView txtNombre;
    public TextView txtEdad;
    public TextView txtTlf;

    public UserViewHolder(@NonNull View itemView) {
        super(itemView);
        txtNombre = itemView.findViewById(R.id.txt_nombre);
        txtEdad = itemView.findViewById(R.id.txt_edad);
        txtTlf = itemView.findViewById(R.id.txt_tlf);
    }
}
