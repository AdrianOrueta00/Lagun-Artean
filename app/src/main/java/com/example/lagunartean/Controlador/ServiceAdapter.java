package com.example.lagunartean.Controlador;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lagunartean.Modelo.User;
import com.example.lagunartean.Modelo.UserList;
import com.example.lagunartean.R;
import com.example.lagunartean.Vista.AddUserActivity_1;
import com.example.lagunartean.Vista.UserServiceViewHolder;
import com.example.lagunartean.Vista.UserViewHolder;

import java.util.ArrayList;

public class ServiceAdapter extends RecyclerView.Adapter<UserServiceViewHolder> {

    private Context ctx;
    private UserList usuarios;

    public ServiceAdapter(Context pContexto, UserList pUsuarios){
        ctx = pContexto;
        usuarios = pUsuarios;
    }

    @NonNull
    @Override
    public UserServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_user_service, parent, false);

        return new UserServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserServiceViewHolder holder, int position) {
        holder.txtNombre.setText(usuarios.get(position).getNombre());
        holder.txtTlf.setText(usuarios.get(position).getTlf());
        holder.txtEdad.setText(usuarios.get(position).getEdad());
        holder.selectorUsuario.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                //Cambia el atributo checked del usuario correspondiente en la lista de
                //Reserva de los servicios
                usuarios.get(holder.getAdapterPosition()).setChecked(b);
            }
        });
    }

    @Override
    public int getItemCount() {
        return usuarios.getLength();
    }

}
