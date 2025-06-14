package com.upn.restobarapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private int contador=0;
    private SharedPreferences oflujo=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        BottomNavigationView botonNavegacion =findViewById(R.id.btnNavegacion);
        botonNavegacion.setOnItemSelectedListener(item -> {
            Intent oIntento = null;
            if(item.getItemId()==R.id.itemInicio){
                return true;
            }
            if(item.getItemId()==R.id.itemRegistrar){
                oIntento = new Intent(this, ActividadRegistrar.class);
                startActivity(oIntento);
                return true;
            }

            if(item.getItemId()==R.id.itemListar){
                //oIntento = new Intent(this, ActividadMostrar.class);
                oIntento = new Intent(this, ActividadMostrarTarjeta.class);
                startActivity(oIntento);
                return true;
            }

            if(item.getItemId()==R.id.itemSalir){
                finish();
                return true;
            }
            return false;
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(isFinishing()){ // si se cierra la actividad
            contador++;
            SharedPreferences.Editor oEditar = oflujo.edit();
            oEditar.putInt("contador",contador);
            oEditar.commit();//Guarda los cambios
            oEditar.clear();
        }
    }
}