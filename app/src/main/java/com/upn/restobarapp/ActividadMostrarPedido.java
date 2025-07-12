package com.upn.restobarapp;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.upn.restobarapp.Access.DAOPedidoBD;
import com.upn.restobarapp.Model.PedidoDB;

import java.util.List;

public class ActividadMostrarPedido extends AppCompatActivity {
    private RecyclerView recyclerViewPedidos;
    private AdaptadorPedido adaptadorPedido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ly_mostar_pedido);

        // Inicializar RecyclerView
        recyclerViewPedidos = findViewById(R.id.recyclerViewPedidos);
        recyclerViewPedidos.setLayoutManager(new LinearLayoutManager(this));

        // Obtener los datos de la base de datos
        DAOPedidoBD daoPedidoBD = new DAOPedidoBD(this);
        daoPedidoBD.open();
        List<PedidoDB> pedidos = daoPedidoBD.obtenerPedidos();
        daoPedidoBD.close();

        // Configurar el adaptador
        adaptadorPedido = new AdaptadorPedido(pedidos);
        recyclerViewPedidos.setAdapter(adaptadorPedido);
    }
}