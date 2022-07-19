package com.example.lagunartean.Controlador;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lagunartean.Modelo.Application;
import com.example.lagunartean.Modelo.UserList;
import com.example.lagunartean.R;
import com.example.lagunartean.Vista.AddUserActivity_1;
import com.example.lagunartean.Vista.UserViewHolder;

import java.util.ArrayList;

public class UsersAdapter extends RecyclerView.Adapter<UserViewHolder> {

    private Context ctx;
    private UserList usuarios;

    public UsersAdapter(Context pContexto, UserList pUsusarios){
        ctx = pContexto;
        usuarios = pUsusarios;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_user, parent, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecyclerView r = parent.findViewById(R.id.recycler);
                int pos = r.getChildAdapterPosition(view);
                Intent miIntent = new Intent(ctx, AddUserActivity_1.class);
                miIntent.putExtra("id", usuarios.get(pos).getId());
                ctx.startActivity(miIntent);
            }
        });
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.txtNombre.setText(usuarios.get(position).getNombre());
        holder.txtTlf.setText(usuarios.get(position).getTlf());
        holder.txtEdad.setText(usuarios.get(position).getEdad());
    }

    @Override
    public int getItemCount() {
        return usuarios.getLength();
    }
}
