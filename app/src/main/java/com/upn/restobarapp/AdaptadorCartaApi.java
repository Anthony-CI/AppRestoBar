package com.upn.restobarapp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.upn.restobarapp.Model.CartaAPI;

import java.util.ArrayList;
import java.util.List;

public class AdaptadorCartaApi extends RecyclerView.Adapter<AdaptadorCartaApi.CartaViewHolder>{
    private Context context;
    private List<CartaAPI> listaCarta;
    private List<CartaAPI> cartaSeleccionada; // Lista de cartas seleccionadas

    public AdaptadorCartaApi(Context context, List<CartaAPI> listaCarta, List<CartaAPI> cartaSeleccionada) {
        this.context = context;
        this.listaCarta = listaCarta;
        this.cartaSeleccionada = cartaSeleccionada;
    }

    @Override
    public CartaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.ly_tarjeta_cartas, parent, false);
        return new CartaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CartaViewHolder holder, int position) {
        CartaAPI carta = listaCarta.get(position);

        holder.lbTarjetaNombre.setText(carta.getNombre());
        holder.lbTarjetaPrecio.setText("Precio: " + carta.getPrecio());
        holder.lbTarjetaDescripcion.setText(carta.getDescripcion());

        // Cargar la imagen si hay una URL
        if (carta.getRuta() != null && carta.getRuta().length() > 0) {
            Glide.with(context).load(carta.getRuta()).into(holder.imgCarta);
        } else {
            holder.imgCarta.setImageResource(R.mipmap.ic_launcher);
        }

        // Mostrar el EditText solo cuando la carta está seleccionada
        holder.editCantidad.setVisibility(cartaSeleccionada.contains(carta) ? View.VISIBLE : View.GONE);

        // Manejar el clic en cada CardView
        holder.cardView.setOnClickListener(v -> {
            if (cartaSeleccionada.contains(carta)) {
                cartaSeleccionada.remove(carta);
                holder.cardView.setCardBackgroundColor(Color.WHITE);
                holder.editCantidad.setVisibility(View.GONE); // Ocultar campo cantidad
            } else {
                cartaSeleccionada.add(carta);
                holder.cardView.setCardBackgroundColor(Color.GREEN);
                holder.editCantidad.setVisibility(View.VISIBLE); // Mostrar campo cantidad
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaCarta.size();
    }

    public class CartaViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView lbTarjetaNombre;
        TextView lbTarjetaPrecio;
        TextView lbTarjetaDescripcion;
        ImageView imgCarta;
        EditText editCantidad; // EditText para la cantidad

        public CartaViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);
            lbTarjetaNombre = itemView.findViewById(R.id.lbTarjetaNombre);
            lbTarjetaPrecio = itemView.findViewById(R.id.lbTarjetaPrecio);
            lbTarjetaDescripcion = itemView.findViewById(R.id.lbTarjetaDescripcion);
            imgCarta = itemView.findViewById(R.id.imgTarjetaFoto);
            editCantidad = itemView.findViewById(R.id.editCantidad);  // EditText para la cantidad
            EditText editCantidad;
        }
    }

}
