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
    private List<CartaAPI> cartaSeleccionada = new ArrayList<>(); // Lista de cartas seleccionadas
    private DAOPedidoBD daoPedidoBD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ly_mostrar_carta);

        Toolbar toolbar = findViewById(R.id.tbMostrar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Inicializar RecyclerView de cartas
        rvListaCarta = findViewById(R.id.rvListaCarta);
        rvListaCarta.setLayoutManager(new LinearLayoutManager(this));
        MostrarListadoDeCartaApi();  // Mostrar cartas desde la API

        // Inicializar RecyclerView de pedidos
        rvListaPedidos = findViewById(R.id.rvListaPedidos);
        rvListaPedidos.setLayoutManager(new LinearLayoutManager(this));  // Mostrar los pedidos

        // Inicializar DAO para pedidos
        daoPedidoBD = new DAOPedidoBD(this);

        // Botón para enviar pedidos
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

        // Llamar a la API para obtener las cartas
        ApiServicio oServicio = RetrofitCliente.getCliente().create(ApiServicio.class);
        Call<List<CartaAPI>> call = oServicio.GetCartas();
        call.enqueue(new Callback<List<CartaAPI>>() {
            @Override
            public void onResponse(Call<List<CartaAPI>> call, Response<List<CartaAPI>> response) {
                if(response.isSuccessful() && response != null){
                    listaCarta = response.body();
                    AdaptadorCartaApi oAdaptador = new AdaptadorCartaApi(ActividadMostrarCarta.this, listaCarta, cartaSeleccionada);
                    rvListaCarta.setAdapter(oAdaptador);
                } else {
                    Toast.makeText(ActividadMostrarCarta.this, "Lista vacía: " + response.message(), Toast.LENGTH_LONG).show();
                }
                barraProgreso.dismiss();
            }

            @Override
            public void onFailure(Call<List<CartaAPI>> call, Throwable t) {
                Toast.makeText(ActividadMostrarCarta.this, "Error al conectar al servidor: " + t.getMessage(), Toast.LENGTH_LONG).show();
                barraProgreso.dismiss();
            }
        });
    }

    private void mostrarDialogoRegistrarPedido() {
        final View customLayout = getLayoutInflater().inflate(R.layout.dialogo_mesa_mozo, null);
        final EditText editTextMesa = customLayout.findViewById(R.id.editTextMesa);
        final EditText editTextMozo = customLayout.findViewById(R.id.editTextMozo);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Registrar Pedido");
        builder.setMessage("Ingresa el número de la mesa y el nombre del mozo");

        builder.setView(customLayout);

        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String mesaNumero = editTextMesa.getText().toString();
                String mozoNombre = editTextMozo.getText().toString();

                // Guardar los pedidos seleccionados
                for (CartaAPI carta : cartaSeleccionada) {
                    // Obtener la cantidad desde el EditText de la carta seleccionada
                    int cantidad = 0;
                    // Acceder al ViewHolder para obtener la cantidad
                    // Aquí buscamos el editCantidad correspondiente
                    for (CartaAPI holder : listaCarta) {
                        if (carta == holder) {
                            EditText editCantidad = holder.editCantidad;
                            if (editCantidad != null && !editCantidad.getText().toString().isEmpty()) {
                                cantidad = Integer.parseInt(editCantidad.getText().toString());  // Obtener la cantidad
                            }
                        }
                    }

                    // Crear un nuevo pedido con los datos
                    PedidoDB nuevoPedido = new PedidoDB(
                            carta.getNombre(),
                            carta.getDescripcion(),
                            cantidad,  // Asignar la cantidad desde el EditText
                            Integer.parseInt(mesaNumero), // Número de mesa
                            mozoNombre, // Nombre del mozo
                            0 // Estado inicial (pendiente)
                    );

                    // Insertar el pedido en la base de datos
                    DAOPedidoBD daoPedidoBD = new DAOPedidoBD(ActividadMostrarCarta.this);
                    daoPedidoBD.open();
                    long idInsertado = daoPedidoBD.insertarPedido(nuevoPedido);
                    daoPedidoBD.close();

                    // Mostrar un mensaje de confirmación
                    Toast.makeText(ActividadMostrarCarta.this, "Pedido registrado: " + nuevoPedido.getNombre(), Toast.LENGTH_SHORT).show();
                }

                // Mostrar el diálogo de confirmación final
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