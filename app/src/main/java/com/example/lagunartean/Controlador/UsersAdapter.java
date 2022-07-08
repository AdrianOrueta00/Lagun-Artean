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
import com.example.lagunartean.R;
import com.example.lagunartean.Vista.AddUserActivity_1;
import com.example.lagunartean.Vista.UserViewHolder;

import java.util.ArrayList;

public class UsersAdapter extends RecyclerView.Adapter<UserViewHolder> {

    private Context ctx;
    private ArrayList<String> nombres;
    private ArrayList<String> telefonos;
    private ArrayList<String> edades;
    private ArrayList<Integer> ids;

    public UsersAdapter(Context pContexto, ArrayList<String> pNombres, ArrayList<String> pTelefonos, ArrayList<String> pEdades, ArrayList<Integer> pIds){
        ctx = pContexto;
        nombres = pNombres;
        telefonos = pTelefonos;
        edades = pEdades;
        ids = pIds;
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
                miIntent.putExtra("id", ids.get(pos));
                ctx.startActivity(miIntent);
            }
        });
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
