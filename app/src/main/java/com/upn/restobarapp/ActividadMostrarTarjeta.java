package com.upn.restobarapp;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.upn.restobarapp.Access.DAOCartaBD;
import com.upn.restobarapp.Model.CartaDB;

import java.util.List;

public class ActividadMostrarTarjeta extends AppCompatActivity {


    private RecyclerView rvListaCarta;
    private AdaptadorCarta oAdaptador;
    private List<CartaDB> listaCarta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.ly_mostrar_tarjeta);
        Toolbar toolbar = findViewById(R.id.tbMostrar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        rvListaCarta = findViewById(R.id.rvListaCarta);
        DAOCartaBD oBD= new DAOCartaBD(this);
        listaCarta = oBD.getListadoCarta();
        rvListaCarta.setLayoutManager(new LinearLayoutManager(this));
        oAdaptador = new AdaptadorCarta(this,listaCarta);
        rvListaCarta.setAdapter(oAdaptador);
    }
}