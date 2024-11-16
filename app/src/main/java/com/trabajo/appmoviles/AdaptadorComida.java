package com.trabajo.appmoviles;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.trabajo.appmoviles.Modelos.Comida;

import java.util.List;

public class AdaptadorComida extends RecyclerView.Adapter<AdaptadorComida.ComidaViewHolder> {

    private Context context;
    private List<Comida> listaComidas;

    // Constructor que recibe el contexto y la lista de comidas
    public AdaptadorComida(Context context, List<Comida> listaComidas) {
        this.context = context;
        this.listaComidas = listaComidas;
    }

    @Override
    public ComidaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.comidass, parent, false);
        return new ComidaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ComidaViewHolder holder, int position) {
        Comida comida = listaComidas.get(position);
        holder.tvNombre.setText(comida.getNombre());
        holder.tvPrecio.setText("s/ " + comida.getPrecio());

        // Cargar la imagen de la comida usando Glide
        Glide.with(context)
                .load(comida.getImagen())
                .into(holder.ivImagenComida);

        // Manejar el clic en el botón de "Agregar al carrito"
        holder.btnAgregarComida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agregarAlCarrito(comida);  // Llamar al método para agregar al carrito
            }
        });
    }

    // Método para agregar la comida al carrito usando SharedPreferences
    private void agregarAlCarrito(Comida comida) {
        // Lógica para agregar al carrito en SharedPreferences
        SharedPreferences prefs = context.getSharedPreferences("Carrito", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();

        // Convertir el objeto comida a JSON para almacenarlo
        String comidaJson = gson.toJson(comida);

        // Guardar la comida usando su nombre como clave (o puedes usar otro identificador)
        editor.putString(comida.getNombre(), comidaJson);
        editor.apply();  // Aplicar los cambios

        Toast.makeText(context, "Comida agregada al carrito", Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getItemCount() {
        return listaComidas.size();
    }

    // Método para actualizar los datos de la lista de comidas
    public void updateData(List<Comida> nuevaListaComidas) {
        this.listaComidas = nuevaListaComidas;
        notifyDataSetChanged();  // Refresca el RecyclerView
    }

    public static class ComidaViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvPrecio;
        ImageView ivImagenComida;
        ImageButton btnAgregarComida;

        public ComidaViewHolder(View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombreProducto);
            tvPrecio = itemView.findViewById(R.id.tvPrecioProducto);
            ivImagenComida = itemView.findViewById(R.id.ivImagenProducto);
            btnAgregarComida = itemView.findViewById(R.id.agregarcomida);
        }
    }
}
