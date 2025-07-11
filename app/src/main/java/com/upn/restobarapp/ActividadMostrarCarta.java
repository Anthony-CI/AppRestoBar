package com.upn.restobarapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.upn.restobarapp.Access.DAOPedidoBD;
import com.upn.restobarapp.Model.CartaAPI;
import com.upn.restobarapp.Model.PedidoDB;
import com.upn.restobarapp.Network.ApiServicio;
import com.upn.restobarapp.Network.RetrofitCliente;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActividadMostrarCarta extends AppCompatActivity {

    private RecyclerView rvListaCarta;
    private RecyclerView rvListaPedidos;
    private List<CartaAPI> listaCarta;
    private ListView listView;
    private AdaptadorPedido adaptadorPedido;
    private List<PedidoDB> listaPedidos;
    private DAOPedidoBD daoPedidoBD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.ly_mostrar_carta);


        Toolbar toolbar = findViewById(R.id.tbMostrar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // Inicializar el RecyclerView de las cartas
        rvListaCarta = findViewById(R.id.rvListaCarta);
        rvListaCarta.setLayoutManager(new LinearLayoutManager(this));
        MostrarListadoDeCartaApi();  // Mostrar las cartas desde la API

        // Inicializar el RecyclerView de los pedidos
        rvListaPedidos = findViewById(R.id.rvListaPedidos);  // Referencia al RecyclerView de pedidos
        rvListaPedidos.setLayoutManager(new LinearLayoutManager(this));  // Mostrar los pedidos en una lista

        // Obtener los pedidos de la base de datos
        listaPedidos = new ArrayList<>();
        daoPedidoBD = new DAOPedidoBD(this);

        // Abrir la base de datos antes de obtener los pedidos
        daoPedidoBD.open();  // Asegúrate de abrir la base de datos antes de realizar la consulta
        listaPedidos = daoPedidoBD.obtenerPedidos();
        daoPedidoBD.close();  // Cierra la base de datos después de usarla

        // Adaptador para los pedidos
        adaptadorPedido = new AdaptadorPedido(listaPedidos);  // Solo pasamos la lista de pedidos
        rvListaPedidos.setAdapter(adaptadorPedido);






        // Botón flotante para enviar los pedidos
        findViewById(R.id.btnEnviarPedidos).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDialogoRegistrarPedido();
            }
        });



    }




    private void MostrarListadoDeCartaApi() {
        ProgressDialog barraProgreso = new ProgressDialog(this);
        barraProgreso.setMessage("Espere, cargando datos");
        barraProgreso.show();
        //leer la api de tipo get de nuestro servicio web
        ApiServicio oServicio = RetrofitCliente.getCliente().create(ApiServicio.class);
        //donde se va ha cargar
        Call<List<CartaAPI>> call = oServicio.GetCartas();
        call.enqueue(new Callback<List<CartaAPI>>() {
            @Override
            public void onResponse(Call<List<CartaAPI>> call, Response<List<CartaAPI>> response) {
                if(response.isSuccessful() && response!=null){
                    AdaptadorCartaApi oAdaptador = null;
                    listaCarta = response.body();
                    oAdaptador = new AdaptadorCartaApi(ActividadMostrarCarta.this,listaCarta);
                    rvListaCarta.setAdapter(oAdaptador);

                }else {
                    Toast.makeText(ActividadMostrarCarta.this, "lista vacia: " +response.message(), Toast.LENGTH_LONG);
                }
                barraProgreso.dismiss();
            }

            @Override
            public void onFailure(Call<List<CartaAPI>> call, Throwable t) {
                Toast.makeText(ActividadMostrarCarta.this, "Error al conectar al servidor:" +t.getMessage(), Toast.LENGTH_LONG);
                barraProgreso.dismiss();
            }
        });
    }


    private void mostrarDialogoRegistrarPedido() {
        // Mostrar un diálogo para ingresar el número de mesa y nombre del mozo
        final View customLayout = getLayoutInflater().inflate(R.layout.dialogo_mesa_mozo, null);
        final EditText editTextMesa = customLayout.findViewById(R.id.editTextMesa);
        final EditText editTextMozo = customLayout.findViewById(R.id.editTextMozo);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Registrar Pedido");
        builder.setMessage("Ingresa el número de la mesa y el nombre del mozo");

        builder.setView(customLayout);

        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Guardar la información de mesa y mozo
                String mesaNumero = editTextMesa.getText().toString();
                String mozoNombre = editTextMozo.getText().toString();

                // Aquí deberías guardar los pedidos y pasar a la siguiente actividad
                Toast.makeText(ActividadMostrarCarta.this, "Pedido registrado en mesa " + mesaNumero + " por " + mozoNombre, Toast.LENGTH_SHORT).show();

                // Después de esto, mostrar el diálogo de confirmación final
                mostrarDialogoConfirmarPedido();
            }
        });

        builder.setNegativeButton("Cancelar", null);
        builder.create().show();
    }

    private void mostrarDialogoConfirmarPedido() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmar Pedido");
        builder.setMessage("¿Deseas confirmar este pedido?");

        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Confirmar el pedido
                Toast.makeText(ActividadMostrarCarta.this, "Pedido confirmado", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("No", null);
        builder.create().show();
    }
}