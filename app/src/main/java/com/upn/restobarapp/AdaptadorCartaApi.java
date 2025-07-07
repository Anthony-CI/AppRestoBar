package com.upn.restobarapp;

import android.content.Context;
import android.graphics.Typeface;
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

public class AdaptadorCartaApi extends RecyclerView.Adapter<AdaptadorCartaApi.CartaViewHolder>{
    private Context contexto;
    private List<CartaAPI> listaCarta;

    public AdaptadorCartaApi(Context contexto, List<CartaAPI> listaCarta) {
        this.contexto = contexto;
        this.listaCarta = listaCarta;
    }

    @NonNull
    @Override
    public AdaptadorCartaApi.CartaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(contexto).
                inflate(R.layout.ly_tarjeta_carta,parent,false);

        return new CartaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorCartaApi.CartaViewHolder holder, int position) {
        CartaAPI oE = listaCarta.get(position);
        holder.lbNombre.setText(oE.getNombre());
        holder.lbDescripcion.setText(oE.getDescripcion());
        holder.lbPrecio.setText(String.valueOf(oE.getPrecio()));
        /*
        //cambiar propiedades en tiempo real
        if(oE.getUrgencia()==2){
            holder.lbUrgencia.setTextColor(contexto.getResources().getColor(R.color.rojo_500));
            holder.lbUrgencia.setTypeface(null, Typeface.BOLD);
        }else {
            holder.lbUrgencia.setTextColor(contexto.getResources().getColor(R.color.azul));
            holder.lbUrgencia.setTypeface(null, Typeface.ITALIC);
        }

         */

        //Cargar foto remotamente

        if (oE.getRuta()!=null && oE.getRuta().length()>0){
            Glide.with(contexto).load(oE.getRuta()).into(holder.imgItemFoto);
        }else {
            //en caso la emergencia no tenga foto
            holder.imgItemFoto.setImageResource(R.mipmap.ic_launcher);
        }




    }

    @Override
    public int getItemCount() {
        return listaCarta.size();
    }

    static class CartaViewHolder extends RecyclerView.ViewHolder {
        TextView lbNombre,lbDescripcion,lbPrecio;
        CircleImageView imgItemFoto;

        public CartaViewHolder(@NonNull View itemView) {
            super(itemView);
            lbNombre = itemView.findViewById(R.id.lbTarjetaNombre);
            lbDescripcion = itemView.findViewById(R.id.lbTarjetaDescripcion);
            lbPrecio=itemView.findViewById(R.id.lbTarjetaPrecio);
            imgItemFoto = itemView.findViewById(R.id.imgTarjetaFoto);
        }
    }
}
