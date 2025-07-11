package com.upn.restobarapp;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.upn.restobarapp.Model.CartaAPI;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdaptadorCartaApi extends RecyclerView.Adapter<AdaptadorCartaApi.CartaViewHolder>{
    private Context context;
    private List<CartaAPI> listaCarta;
    private List<CartaAPI> cartaSeleccionada = new ArrayList<>(); // Lista para las cartas seleccionadas

    public AdaptadorCartaApi(Context context, List<CartaAPI> listaCarta) {
        this.context = context;
        this.listaCarta = listaCarta;
    }

    @Override
    public CartaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.ly_tarjeta_cartas, parent, false);
        return new CartaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CartaViewHolder holder, int position) {
        CartaAPI carta = listaCarta.get(position);

        // Aquí puedes agregar la información de la carta en los TextViews
        holder.lbTarjetaNombre.setText(carta.getNombre());  // Asume que "nombre" es un campo de la carta
        holder.lbTarjetaPrecio.setText("Precio: " + carta.getPrecio());  // Muestra el precio
        holder.lbTarjetaDescripcion.setText(carta.getDescripcion());  // Muestra la descripción

        if (carta.getRuta()!=null && carta.getRuta().length()>0){
            Glide.with(context).load(carta.getRuta()).into(holder.imgCarta);
        }else {
            //en caso la emergencia no tenga foto
            holder.imgCarta.setImageResource(R.mipmap.ic_launcher);
        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cambiar el color del borde cuando se selecciona
                if (cartaSeleccionada.contains(carta)) {
                    cartaSeleccionada.remove(carta);
                    holder.cardView.setCardBackgroundColor(Color.WHITE);  // Restaurar el color
                    holder.editCantidad.setVisibility(View.GONE);  // Ocultar campo cantidad
                } else {
                    cartaSeleccionada.add(carta);
                    holder.cardView.setCardBackgroundColor(Color.GREEN);  // Color de selección
                    holder.editCantidad.setVisibility(View.VISIBLE);  // Mostrar campo cantidad
                }
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
        TextView lbTarjetaPrecio;  // TextView para el precio
        TextView lbTarjetaDescripcion;  // TextView para la descripción
        ImageView imgCarta;  // ImageView para la imagen
        EditText editCantidad;

        public CartaViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);
            lbTarjetaNombre = itemView.findViewById(R.id.lbTarjetaNombre);
            lbTarjetaPrecio = itemView.findViewById(R.id.lbTarjetaPrecio);  // Inicializa el TextView para el precio
            lbTarjetaDescripcion = itemView.findViewById(R.id.lbTarjetaDescripcion);  // Inicializa el TextView para la descripción
            imgCarta = itemView.findViewById(R.id.imgTarjetaFoto);
            editCantidad = itemView.findViewById(R.id.editCantidad);
        }
    }
}
