package com.upn.restobarapp.Access;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.upn.restobarapp.Model.PedidoDB;

import java.util.ArrayList;
import java.util.List;

public class DAOPedidoBD {
    private SQLiteDatabase db;
    private BDOpenHelper dbHelper;

    // Constructor modificado para proporcionar los parámetros correctos
    public DAOPedidoBD(Context context) {
        dbHelper = new BDOpenHelper(context, "BDRestoBar", null, 1);
    }

    // Abrir la base de datos en modo lectura/escritura
    public void open() throws SQLException {
        db = dbHelper.getWritableDatabase();
    }

    // Cerrar la base de datos
    public void close() {
        dbHelper.close();
    }

    // Obtener todos los pedidos
    public List<PedidoDB> obtenerPedidos() {
        // Asegurarnos de que la base de datos esté abierta antes de ejecutar la consulta
        if (db == null || !db.isOpen()) {
            open(); // Abre la base de datos si no está abierta
        }

        List<PedidoDB> pedidos = new ArrayList<>();
        // Realizamos la consulta
        Cursor cursor = db.query("pedido", null, null, null, null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    PedidoDB pedido = new PedidoDB();
                    pedido.setId(cursor.getInt(cursor.getColumnIndex("id")));
                    pedido.setNombre(cursor.getString(cursor.getColumnIndex("nombre")));
                    pedido.setDescripcion(cursor.getString(cursor.getColumnIndex("descripcion")));
                    pedido.setCantidad(cursor.getInt(cursor.getColumnIndex("cantidad")));
                    pedido.setMesaNumero(cursor.getInt(cursor.getColumnIndex("mesaNumero")));
                    pedido.setNombreMozo(cursor.getString(cursor.getColumnIndex("nombreMozo")));
                    pedido.setEstado(cursor.getInt(cursor.getColumnIndex("estado")));
                    pedidos.add(pedido);
                } while (cursor.moveToNext());
            }
            cursor.close(); // Cerrar el cursor
        }

        return pedidos;
    }

    public long insertarPedido(PedidoDB pedido) {
        ContentValues values = new ContentValues();
        values.put("nombre", pedido.getNombre());
        values.put("descripcion", pedido.getDescripcion());
        values.put("cantidad", pedido.getCantidad());
        values.put("mesaNumero", pedido.getMesaNumero());
        values.put("nombreMozo", pedido.getNombreMozo());
        values.put("estado", pedido.getEstado());  // Estado (pendiente o completado)

        // Insertamos el pedido en la base de datos y devolvemos el ID generado
        return db.insert("pedido", null, values);
    }

    // Cambiar el estado del pedido
    public void cambiarEstadoPedido(int id, int estado) {
        ContentValues values = new ContentValues();
        values.put("estado", estado);
        db.update("pedido", values, "id = ?", new String[]{String.valueOf(id)});
    }
}
