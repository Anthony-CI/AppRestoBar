package com.upn.restobarapp;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


import com.upn.restobarapp.Model.PedidoDB;

public class AdaptadorPedido extends RecyclerView.Adapter<AdaptadorPedido.PedidoViewHolder> {

    private List<PedidoDB> listaPedidos;

    // Constructor que recibe solo la lista de pedidos
    public AdaptadorPedido(List<PedidoDB> listaPedidos) {
        this.listaPedidos = listaPedidos;
    }

    @Override
    public PedidoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflar el layout para cada elemento de la lista
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ly_mostar_pedido, parent, false);
        return new PedidoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PedidoViewHolder holder, int position) {
        PedidoDB pedido = listaPedidos.get(position);

        // Establecer los valores en los elementos del layout
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
    }

    @Override
    public int getItemCount() {
        return listaPedidos.size();
    }

    // ViewHolder que mantiene las vistas para cada elemento
    public static class PedidoViewHolder extends RecyclerView.ViewHolder {
        public TextView nombrePedido;
        public TextView descripcionPedido;
        public TextView cantidadPedido;
        public TextView mesaPedido;
        public TextView mozoPedido;

        public PedidoViewHolder(View itemView) {
            super(itemView);
            nombrePedido = itemView.findViewById(R.id.nombrePedido);
            descripcionPedido = itemView.findViewById(R.id.descripcionPedido);
            cantidadPedido = itemView.findViewById(R.id.cantidadPedido);
            mesaPedido = itemView.findViewById(R.id.mesaPedido);
            mozoPedido = itemView.findViewById(R.id.mozoPedido);
        }
    }
}
