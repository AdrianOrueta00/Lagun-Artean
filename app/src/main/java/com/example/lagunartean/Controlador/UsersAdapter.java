package com.example.lagunartean.Controlador;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lagunartean.Modelo.Application;
import com.example.lagunartean.R;
import com.example.lagunartean.Vista.UserViewHolder;

import java.util.ArrayList;

public class UsersAdapter extends RecyclerView.Adapter<UserViewHolder>
        implements View.OnClickListener {

    private Context ctx;
    private ArrayList<String> nombres;
    private ArrayList<String> telefonos;
    private ArrayList<String> edades;

    public UsersAdapter(Context pContexto, ArrayList<String> pNombres, ArrayList<String> pTelefonos, ArrayList<String> pEdades){
        ctx = pContexto;
        nombres = pNombres;
        telefonos = pTelefonos;
        edades = pEdades;
    }

    @Override
    public void onClick(View view) {

    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_user, parent, false);
        view.setOnClickListener(this);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.txtNombre.setText(nombres.get(position));
        holder.txtTlf.setText(telefonos.get(position));
        holder.txtEdad.setText(edades.get(position));
    }

    @Override
    public int getItemCount() {
        return nombres.size();
    }
}
