package com.upn.restobarapp;

import android.app.Activity;
import android.app.AlertDialog;
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
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pedido, parent, false);  // Aquí usas item_pedido.xml
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
            holder.itemView.setBackgroundColor(Color.GREEN);  // Si el estado es "Sí", fondo verde
        } else {
            holder.itemView.setBackgroundColor(Color.RED);  // Si no es "Sí", fondo rojo
        }

        // Listener para seleccionar un item
        holder.itemView.setOnClickListener(v -> {
            // Mostrar el diálogo al hacer clic en el item
            mostrarDialogoCambioEstado(holder.itemView, position);
        });
    }

    @Override
    public int getItemCount() {
        return listaPedidos.size();
    }

    // Clase ViewHolder para mantener las vistas de cada item
    public static class PedidoViewHolder extends RecyclerView.ViewHolder {
        public TextView nombrePedido;
        public TextView descripcionPedido;
        public TextView cantidadPedido;
        public TextView mesaPedido;
        public TextView mozoPedido;
        public EditText cantidadEditText;

        public PedidoViewHolder(View itemView) {
            super(itemView);
            nombrePedido = itemView.findViewById(R.id.nombrePedido);
            descripcionPedido = itemView.findViewById(R.id.descripcionPedido);
            cantidadPedido = itemView.findViewById(R.id.cantidadPedido);
            mesaPedido = itemView.findViewById(R.id.mesaPedido);
            mozoPedido = itemView.findViewById(R.id.mozoPedido);
        }
    }

    private void mostrarDialogoCambioEstado(View itemView, int position) {
        // Crear el diálogo de confirmación
        new AlertDialog.Builder(itemView.getContext())
                .setTitle("Cambiar estado")
                .setMessage("¿Deseas cambiar el estado del pedido?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    // Si el usuario selecciona "Sí", cambiar el estado y el color de fondo
                    listaPedidos.get(position).setEstado(1);  // Establecer el estado a "Sí"
                    itemView.setBackgroundColor(Color.GREEN);  // Cambiar el fondo a verde
                    notifyItemChanged(position);  // Notificar que el ítem ha cambiado
                })
                .setNegativeButton("No", (dialog, which) -> {
                    // Si el usuario selecciona "No", no hacer nada
                    dialog.dismiss();
                })
                .create()
                .show();
    }
}
