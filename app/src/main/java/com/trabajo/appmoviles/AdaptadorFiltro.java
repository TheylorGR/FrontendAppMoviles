package com.trabajo.appmoviles;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.trabajo.appmoviles.Modelos.Filtro;

import java.util.List;

public class AdaptadorFiltro extends RecyclerView.Adapter<AdaptadorFiltro.FiltroViewHolder> {

    private Context context;
    private List<Filtro> listaFiltros;
    private OnFiltroClickListener listener;

    // Interfaz para manejar el clic en un filtro
    public interface OnFiltroClickListener {
        void onFiltroClick(Filtro filtro);
    }

    // Constructor que recibe el contexto, la lista de filtros y el listener
    public AdaptadorFiltro(Context context, List<Filtro> listaFiltros, OnFiltroClickListener listener) {
        this.context = context;
        this.listaFiltros = listaFiltros;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FiltroViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflar el layout del filtro (CardView con ImageButton)
        View view = LayoutInflater.from(context).inflate(R.layout.filtros, parent, false);
        return new FiltroViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FiltroViewHolder holder, int position) {
        Filtro filtro = listaFiltros.get(position);

        // Cargar la imagen del filtro en el ImageButton usando Glide
        Glide.with(context)
                .load(filtro.getImagen())
                .into(holder.btnFiltro);

        // Manejar clic en el filtro
        holder.btnFiltro.setOnClickListener(v -> listener.onFiltroClick(filtro));
    }

    @Override
    public int getItemCount() {
        return listaFiltros.size();
    }

    // ViewHolder para el filtro
    public static class FiltroViewHolder extends RecyclerView.ViewHolder {
        ImageButton btnFiltro;

        public FiltroViewHolder(@NonNull View itemView) {
            super(itemView);
            btnFiltro = itemView.findViewById(R.id.btnFiltro);
        }
    }
}

