package com.upn.restobarapp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.upn.restobarapp.Access.DAOCartaBD;
import com.upn.restobarapp.Model.CartaAPI;
import com.upn.restobarapp.Model.CartaDB;
import com.upn.restobarapp.Network.ApiServicio;
import com.upn.restobarapp.Network.RetrofitCliente;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class ActividadMostrarTarjeta extends AppCompatActivity {


    private RecyclerView rvListaCarta;
    private List<CartaAPI> listaCarta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.ly_mostrar_tarjeta);
        Toolbar toolbar = findViewById(R.id.tbMostrar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        rvListaCarta = findViewById(R.id.rvListaCarta);
        rvListaCarta.setLayoutManager(new LinearLayoutManager(this));
        MostrarListadoDeEmergenciaApi();
    }

    private void MostrarListadoDeEmergenciaApi() {
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
                    oAdaptador = new AdaptadorCartaApi(ActividadMostrarTarjeta.this,listaCarta);
                    rvListaCarta.setAdapter(oAdaptador);

                }else {
                    Toast.makeText(ActividadMostrarTarjeta.this, "lista vacia: " +response.message(), Toast.LENGTH_LONG);
                }
                barraProgreso.dismiss();
            }

            @Override
            public void onFailure(Call<List<CartaAPI>> call, Throwable t) {
                Toast.makeText(ActividadMostrarTarjeta.this, "Error al conectar al servidor:" +t.getMessage(), Toast.LENGTH_LONG);
                barraProgreso.dismiss();
            }
        });
    }


}