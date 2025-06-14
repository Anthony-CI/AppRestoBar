package com.upn.restobarapp.Access;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.upn.restobarapp.Model.CartaDB;

import java.util.ArrayList;
import java.util.List;

public class DAOCartaBD {
    private String nombreBD;
    private int version;
    private BDOpenHelper oBDConstructor;

    public DAOCartaBD(Activity contexto) {
        this.nombreBD = "BDECartas";
        this.version = 1;
        this.oBDConstructor = new BDOpenHelper(contexto,nombreBD,null,version);
    }

    public String addCartaBD(CartaDB oE){
        String rpta="";
        SQLiteDatabase oBD = oBDConstructor.getWritableDatabase();
        ContentValues oColumnas = new ContentValues();
        oColumnas.put("nombre",oE.getNombre());
        oColumnas.put("descripcion",oE.getDescripcion());
        oColumnas.put("precio",oE.getPrecio());
        oColumnas.put("foto",oE.getFoto());
        long fila=oBD.insert("Carta",null,oColumnas);
        if(fila>0)
            rpta="OK";
        else
            rpta="Error: Registro inválido";
        oBD.close();
        return rpta;
    }
    public List<CartaDB> getListadoCarta(){
        List<CartaDB> lista = new ArrayList<CartaDB>();
        SQLiteDatabase oBD = oBDConstructor.getReadableDatabase();
        String sql = "SELECT * FROM Carta";
        Cursor oRegistros = oBD.rawQuery(sql,null);
        if(oRegistros.moveToFirst()){
            do {
                String nombre = oRegistros.getString(1);
                String descripcion = oRegistros.getString(2);
                int precio = Integer.parseInt(oRegistros.getString(3));
                byte[] foto = oRegistros.getBlob(4);
                CartaDB oE = new CartaDB(nombre,descripcion,precio,foto);
                lista.add(oE);
            }while (oRegistros.moveToNext());
            oBD.close();
            oRegistros.close();
        }
        return lista;
    }
}
