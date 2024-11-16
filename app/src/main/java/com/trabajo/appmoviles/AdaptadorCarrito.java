package com.trabajo.appmoviles;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.trabajo.appmoviles.Modelos.Comida;

import java.util.List;

public class AdaptadorCarrito extends RecyclerView.Adapter<AdaptadorCarrito.CarritoViewHolder> {

    private Context context;
    private List<Comida> listaCarrito;
    private Runnable actualizarTotalFinalCallback;  // Callback para actualizar el total final

    // Constructor que recibe el contexto, la lista de productos del carrito y el callback para actualizar el total
    public AdaptadorCarrito(Context context, List<Comida> listaCarrito, Runnable actualizarTotalFinalCallback) {
        this.context = context;
        this.listaCarrito = listaCarrito;
        this.actualizarTotalFinalCallback = actualizarTotalFinalCallback;  // Callback para actualizar el total
    }

    @Override
    public CarritoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.carrito, parent, false);
        return new CarritoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CarritoViewHolder holder, int position) {
        Comida comida = listaCarrito.get(holder.getAdapterPosition());

        holder.tvNombre.setText(comida.getNombre());
        holder.tvPrecio.setText("s/ " + calcularPrecioTotal(comida));
        holder.tvCantidadProducto.setText(String.valueOf(comida.getCantidad()));

        // Cargar la imagen de la comida usando Glide
        Glide.with(context)
                .load(comida.getImagen())
                .into(holder.ivImagenComida);

        // Lógica para aumentar la cantidad
        holder.btnAumentarCantidad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition == RecyclerView.NO_POSITION) return;  // Evitar problemas si la posición no es válida

                Comida comida = listaCarrito.get(adapterPosition);
                int cantidadActual = comida.getCantidad();
                int stockDisponible = comida.getStock();

                if (cantidadActual < stockDisponible) {
                    comida.setCantidad(cantidadActual + 1);
                    holder.tvCantidadProducto.setText(String.valueOf(comida.getCantidad()));
                    holder.tvPrecio.setText("s/ " + calcularPrecioTotal(comida));

                    notifyDataSetChanged();
                    actualizarTotalFinalCallback.run();  // Llamar al callback para actualizar el total final
                } else {
                    Toast.makeText(context, "Stock máximo alcanzado", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Lógica para disminuir la cantidad
        holder.btnDisminuirCantidad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition == RecyclerView.NO_POSITION) return;

                Comida comida = listaCarrito.get(adapterPosition);
                int cantidadActual = comida.getCantidad();

                if (cantidadActual > 1) {
                    comida.setCantidad(cantidadActual - 1);
                    holder.tvCantidadProducto.setText(String.valueOf(comida.getCantidad()));
                    holder.tvPrecio.setText("s/ " + calcularPrecioTotal(comida));

                    notifyDataSetChanged();
                    actualizarTotalFinalCallback.run();  // Llamar al callback para actualizar el total final
                } else {
                    Toast.makeText(context, "No puedes tener menos de 1 unidad", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Lógica para eliminar el producto de la lista local (sin eliminar de la base de datos)
        holder.btnEliminarProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition == RecyclerView.NO_POSITION) return;

                listaCarrito.remove(adapterPosition);
                notifyItemRemoved(adapterPosition);
                notifyItemRangeChanged(adapterPosition, listaCarrito.size());

                Toast.makeText(context, "Producto eliminado del carrito", Toast.LENGTH_SHORT).show();
                actualizarTotalFinalCallback.run();  // Llamar al callback para actualizar el total final
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaCarrito.size();
    }

    // Método para calcular el precio total del producto en el carrito (cantidad * precio unitario)
    private double calcularPrecioTotal(Comida comida) {
        return comida.getPrecio() * comida.getCantidad();
    }

    // ViewHolder del carrito
    public static class CarritoViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvPrecio, tvCantidadProducto;
        ImageView ivImagenComida;
        ImageButton btnAumentarCantidad, btnDisminuirCantidad, btnEliminarProducto;

        public CarritoViewHolder(View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombreProducto);
            tvPrecio = itemView.findViewById(R.id.tvPrecioProducto);
            ivImagenComida = itemView.findViewById(R.id.ivImagenProducto);
            tvCantidadProducto = itemView.findViewById(R.id.tvCantidadProducto);
            btnAumentarCantidad = itemView.findViewById(R.id.btnAumentarCantidad);
            btnDisminuirCantidad = itemView.findViewById(R.id.btnDisminuirCantidad);
            btnEliminarProducto = itemView.findViewById(R.id.btnEliminarProducto);
        }
    }
}
