package com.upn.restobarapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.upn.restobarapp.Model.CartaDB;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdaptadorCarta extends RecyclerView.Adapter<AdaptadorCarta.CartaViewHolder>{

    public AdaptadorCarta(Context contexto, List<CartaDB> listaCarta) {
        this.contexto = contexto;
        this.listaCarta = listaCarta;
    }

    private Context contexto;
    private List<CartaDB> listaCarta;
    public  AdaptadorCarta(){

    }
    @NonNull
    @Override
    public AdaptadorCarta.CartaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(contexto).inflate(R.layout.ly_tarjeta_carta,parent,false);
        return new CartaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorCarta.CartaViewHolder holder, int position) {
        CartaDB oE= listaCarta.get(position);
        holder.lbNombre.setText(oE.getDescripcion());
        holder.lbDescripcion.setText(oE.getDescripcion());
        holder.lbPrecio.setText(oE.getPrecio()+"");

        //cargar foto de sql lite
        byte[] imagenBytes = oE.getFoto();
        if(imagenBytes != null && imagenBytes.length>0){
            Bitmap oBitmap = BitmapFactory.decodeByteArray(imagenBytes,0,imagenBytes.length);
            holder.imgItenFoto.setImageBitmap(oBitmap);
        }else {
            holder.imgItenFoto.setImageResource(R.mipmap.ic_launcher);
        }
    }

    @Override
    public int getItemCount() {
        return listaCarta.size();
    }


    //Traer todos los objetos de la tarjeta
    static class CartaViewHolder extends RecyclerView.ViewHolder{
        TextView lbNombre, lbDescripcion,lbPrecio;
        CircleImageView imgItenFoto;

        public CartaViewHolder(@NonNull View itemView) {
            super(itemView);
            lbNombre=itemView.findViewById(R.id.lbTarjetaNombre);
            lbDescripcion = itemView.findViewById(R.id.lbTarjetaDescripcion);
            lbPrecio=itemView.findViewById(R.id.lbTarjetaPrecio);
            imgItenFoto= itemView.findViewById(R.id.imgTarjetaFoto);
        }
    }
}
