package com.upn.restobarapp;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private int contador = 0;
    private SharedPreferences oflujo = null;
    private static final int REQUEST_CODE_STORAGE_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        solicitarPermisosAlmacenamiento();

        BottomNavigationView botonNavegacion = findViewById(R.id.btnNavegacion);
        botonNavegacion.setOnItemSelectedListener(item -> {
            Intent oIntento = null;
            if (item.getItemId() == R.id.itemInicio) {
                return true;
            }
            if (item.getItemId() == R.id.itemRegistrar) {
                oIntento = new Intent(this, ActividadRegistrar.class);
                startActivity(oIntento);
                return true;
            }
            if (item.getItemId() == R.id.itemListar) {
                oIntento = new Intent(this, ActividadMostrarTarjeta.class);
                startActivity(oIntento);
                return true;
            }
            if (item.getItemId() == R.id.itemCarta) {
                oIntento = new Intent(this, ActividadMostrarCarta.class);
                startActivity(oIntento);
                return true;
            }

            if (item.getItemId() == R.id.itemSalir) {
                finish();
                return true;
            }
            return false;
        });
    }

    private void solicitarPermisosAlmacenamiento() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13+
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.READ_MEDIA_IMAGES,
                        Manifest.permission.READ_MEDIA_VIDEO,
                        Manifest.permission.READ_MEDIA_AUDIO
                }, REQUEST_CODE_STORAGE_PERMISSION);
            }
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, REQUEST_CODE_STORAGE_PERMISSION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_STORAGE_PERMISSION) {
            boolean permisoConcedido = grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
            if (!permisoConcedido) {
                // El usuario denegó los permisos: notificar o cerrar la app
                finish(); // O muestra un diálogo para informar al usuario
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isFinishing()) { // si se cierra la actividad
            contador++;
            SharedPreferences.Editor oEditar = oflujo.edit();
            oEditar.putInt("contador", contador);
            oEditar.commit(); // Guarda los cambios
            oEditar.clear();
        }
    }
}
