package com.upn.restobarapp;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


import com.upn.restobarapp.Model.PedidoDB;

public class AdaptadorPedido extends RecyclerView.Adapter<AdaptadorPedido.PedidoViewHolder> {

    private List<PedidoDB> listaPedidos;
    private int selectedPosition = -1; // Para manejar la posición seleccionada

    public AdaptadorPedido(List<PedidoDB> listaPedidos) {
        this.listaPedidos = listaPedidos;
    }

    @Override
    public PedidoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflar el layout de cada item
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ly_mostar_pedido, parent, false);
        return new PedidoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PedidoViewHolder holder, int position) {
        PedidoDB pedido = listaPedidos.get(position);

        // Establecer los valores en los TextViews
        holder.nombrePedido.setText(pedido.getNombre());
        holder.descripcionPedido.setText(pedido.getDescripcion());
        holder.cantidadPedido.setText("Cantidad: " + pedido.getCantidad());
        holder.mesaPedido.setText("Mesa: " + pedido.getMesaNumero());
        holder.mozoPedido.setText("Mozo: " + pedido.getNombreMozo());

        // Cambiar el color de fondo según el estado
        if (pedido.getEstado() == 1) {
            holder.itemView.setBackgroundColor(Color.GREEN);
        } else {
            holder.itemView.setBackgroundColor(Color.RED);
        }

        // Si el item está seleccionado, cambiar el borde y mostrar el campo de cantidad
        if (position == selectedPosition) {
            holder.itemView.setBackgroundColor(Color.LTGRAY);  // Cambiar color de selección
            holder.cantidadEditText.setVisibility(View.VISIBLE);  // Mostrar el campo de cantidad
            holder.cantidadEditText.setText(String.valueOf(pedido.getCantidad()));  // Mostrar la cantidad actual
        } else {
            holder.itemView.setBackgroundColor(Color.WHITE);  // Color normal
            holder.cantidadEditText.setVisibility(View.GONE);  // Ocultar el campo de cantidad
        }

        // Listener para seleccionar un item
        holder.itemView.setOnClickListener(v -> {
            selectedPosition = position;  // Marcar el item como seleccionado
            notifyDataSetChanged();  // Notificar que se ha actualizado la vista
        });

        // Listener para editar la cantidad cuando el campo tiene el foco
        holder.cantidadEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                // Actualizar la cantidad cuando el campo obtiene el foco
                pedido.setCantidad(Integer.parseInt(holder.cantidadEditText.getText().toString()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaPedidos.size();
    }

    // ViewHolder que mantiene las vistas para cada item
    public static class PedidoViewHolder extends RecyclerView.ViewHolder {
        public TextView nombrePedido;
        public TextView descripcionPedido;
        public TextView cantidadPedido;
        public TextView mesaPedido;
        public TextView mozoPedido;
        public EditText cantidadEditText;  // EditText para ingresar la cantidad

        public PedidoViewHolder(View itemView) {
            super(itemView);
            nombrePedido = itemView.findViewById(R.id.nombrePedido);
            descripcionPedido = itemView.findViewById(R.id.descripcionPedido);
            cantidadPedido = itemView.findViewById(R.id.cantidadPedido);
            mesaPedido = itemView.findViewById(R.id.mesaPedido);
            mozoPedido = itemView.findViewById(R.id.mozoPedido);
            cantidadEditText = itemView.findViewById(R.id.cantidadEditText); // Campo para ingresar cantidad
        }
    }
}
