package com.trabajo.appmoviles;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.trabajo.appmoviles.Modelos.Pedido;

import java.util.ArrayList;
import java.util.List;

public class AdaptadorPedido extends RecyclerView.Adapter<AdaptadorPedido.PedidoViewHolder> {

    private List<Pedido> pedidos;
    private OnPedidoClickListener listener;

    public interface OnPedidoClickListener {
        void onPedidoClick(Pedido pedido);
    }

    public AdaptadorPedido(List<Pedido> pedidos, OnPedidoClickListener listener) {
        this.pedidos = new ArrayList<>(pedidos); // Inicializar con una nueva lista para evitar problemas con referencias
        this.listener = listener;
    }

    @NonNull
    @Override
    public PedidoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pedido_lista, parent, false);
        return new PedidoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PedidoViewHolder holder, int position) {
        Pedido pedido = pedidos.get(position);

        holder.tvPedidoId.setText("Pedido ID: " + pedido.getId());
        holder.tvPedidoTotal.setText("Total: s/" + pedido.getTotalCompra());
        holder.tvMetodoPago.setText("Método de pago: " + pedido.getMetodoPago().getMetodoPago());
        holder.tvDireccion.setText("Dirección: " + pedido.getDireccion().getNombre_ubi());
        holder.tvTipoEntrega.setText("Tipo de entrega: " + pedido.getTipoEntrega().getTipo_entrega());
        holder.tvEstado.setText("Estado: " + pedido.getEstado().getEstado());

        // Manejo de clics
        holder.cardView.setOnClickListener(v -> listener.onPedidoClick(pedido));
    }

    @Override
    public int getItemCount() {
        return pedidos.size();
    }

    public void actualizarPedidos(List<Pedido> nuevosPedidos) {
        this.pedidos.clear(); // Limpiar la lista actual
        this.pedidos.addAll(nuevosPedidos); // Agregar los nuevos datos
        notifyDataSetChanged(); // Notificar al adaptador que los datos han cambiado
    }

    public static class PedidoViewHolder extends RecyclerView.ViewHolder {

        TextView tvPedidoId, tvPedidoTotal, tvMetodoPago, tvDireccion, tvTipoEntrega, tvEstado;
        CardView cardView;

        public PedidoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPedidoId = itemView.findViewById(R.id.tvPedidoId);
            tvPedidoTotal = itemView.findViewById(R.id.tvPedidoTotal);
            tvMetodoPago = itemView.findViewById(R.id.tvMetodoPago);
            tvDireccion = itemView.findViewById(R.id.tvDireccion);
            tvTipoEntrega = itemView.findViewById(R.id.tvTipoEntrega);
            tvEstado = itemView.findViewById(R.id.tvEstado);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}