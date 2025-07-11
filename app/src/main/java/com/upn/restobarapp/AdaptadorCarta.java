package com.upn.restobarapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.upn.restobarapp.Model.CartaAPI;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdaptadorCarta extends RecyclerView.Adapter<AdaptadorCarta.CartaViewHolder> {

    private Context contexto;
    private List<CartaAPI> listaCartas; // Lista de CartaAPI
    private OnItemClickListener listener;

    // Constructor del adaptador
    public AdaptadorCarta(Context contexto, List<CartaAPI> listaCartas, OnItemClickListener listener) {
        this.contexto = contexto; // Inicializa el contexto correctamente
        this.listaCartas = listaCartas;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflar el layout de cada tarjeta
        View view = LayoutInflater.from(contexto).inflate(R.layout.ly_tarjeta_carta, parent, false);
        return new CartaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartaViewHolder holder, int position) {
        CartaAPI carta = listaCartas.get(position);

        // Establecer los valores de cada campo de la tarjeta
        holder.lbNombre.setText(carta.getDescripcion());
        holder.lbDescripcion.setText(carta.getDescripcion());
        holder.lbPrecio.setText(String.valueOf(carta.getPrecio()));

        // Cargar foto desde un byte[] (suponiendo que la clase CartaAPI tiene el método getFoto)
        Log.d("FOTO", "Valor de foto: " + carta.getFoto());


        String url = carta.getRuta();  // ruta ya contiene la URL completa
        if (url != null && !url.isEmpty()) {
            Glide.with(contexto)
                    .load(url)
                    .placeholder(R.mipmap.ic_launcher)
                    .into(holder.imgItenFoto);
        } else {
            holder.imgItenFoto.setImageResource(R.mipmap.ic_launcher);
        }


        /*
        byte[] imagenBytes = carta.getFoto();  // Aquí obtenemos directamente el byte[] de la foto
        if (imagenBytes != null && imagenBytes.length > 0) {
            Bitmap oBitmap = BitmapFactory.decodeByteArray(imagenBytes, 0, imagenBytes.length);
            holder.imgItenFoto.setImageBitmap(oBitmap);  // Establecer la imagen en el ImageView
        } else {
            holder.imgItenFoto.setImageResource(R.mipmap.ic_launcher);  // Establecer imagen por defecto si no hay foto
        }

         */

        // Configurar el listener para el clic en el item
        holder.itemView.setOnClickListener(v -> listener.onItemClick(carta));
    }

    @Override
    public int getItemCount() {
        return listaCartas != null ? listaCartas.size() : 0;  // Verificar que la lista no sea null
    }

    // ViewHolder para los ítems de la lista
    public static class CartaViewHolder extends RecyclerView.ViewHolder {
        TextView lbNombre, lbDescripcion, lbPrecio;
        CircleImageView imgItenFoto;

        public CartaViewHolder(@NonNull View itemView) {
            super(itemView);
            lbNombre = itemView.findViewById(R.id.lbTarjetaNombre);
            lbDescripcion = itemView.findViewById(R.id.lbTarjetaDescripcion);
            lbPrecio = itemView.findViewById(R.id.lbTarjetaPrecio);
            imgItenFoto = itemView.findViewById(R.id.imgTarjetaFoto);
        }
    }

    // Definir la interfaz para manejar el clic en el item
    public interface OnItemClickListener {
        void onItemClick(CartaAPI carta);
    }
}
