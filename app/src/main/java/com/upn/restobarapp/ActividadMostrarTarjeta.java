package com.upn.restobarapp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class ActividadMostrarTarjeta extends AppCompatActivity {


    private RecyclerView rvListaCarta;
    private List<CartaAPI> listaCarta;
    private AdaptadorCarta adaptadorCarta;
    private Button btnEliminar;
    private Button btnEditar;
    private String rutaImagenSeleccionada = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.ly_mostrar_tarjeta);

        btnEliminar = findViewById(R.id.btnEliminar);
        btnEditar = findViewById(R.id.btnEditar);

        if (listaCarta == null) {
            listaCarta = new ArrayList<>();
        }

        rvListaCarta = findViewById(R.id.rvListaCarta);
        rvListaCarta.setLayoutManager(new LinearLayoutManager(this));

        adaptadorCarta = new AdaptadorCarta(ActividadMostrarTarjeta.this, listaCarta, new AdaptadorCarta.OnItemClickListener() {
            @Override
            public void onItemClick(CartaAPI carta) {
                btnEliminar.setVisibility(View.VISIBLE);
                btnEditar.setVisibility(View.VISIBLE);

                btnEliminar.setOnClickListener(v -> eliminarElemento(carta));

                btnEditar.setOnClickListener(v -> editarElemento(carta));
            }
        });

        rvListaCarta.setAdapter(adaptadorCarta);
        MostrarListadoDeCartaApi();
    }

    private void MostrarListadoDeCartaApi() {
        ProgressDialog barraProgreso = new ProgressDialog(this);
        barraProgreso.setMessage("Espere, cargando datos");
        barraProgreso.show();

        // Hacer la llamada a la API
        ApiServicio oServicio = RetrofitCliente.getCliente().create(ApiServicio.class);
        Call<List<CartaAPI>> call = oServicio.GetCartas();

        call.enqueue(new Callback<List<CartaAPI>>() {
            @Override
            public void onResponse(Call<List<CartaAPI>> call, Response<List<CartaAPI>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listaCarta = response.body();
                    Log.d("API Data", "Datos recibidos: " + listaCarta.size());  // Verificar el número de cartas recibidas

                    // Log para verificar cada ID de la carta
                    for (CartaAPI carta : listaCarta) {
                        Log.d("API", "Carta ID: " + carta.getIdCarta());  // Verificar cada ID de carta
                    }

                    // Crear el adaptador después de recibir los datos de la API
                    adaptadorCarta = new AdaptadorCarta(ActividadMostrarTarjeta.this, listaCarta, new AdaptadorCarta.OnItemClickListener() {
                        @Override
                        public void onItemClick(CartaAPI carta) {
                            Log.d("Item Clicked", "Item clicked: " + carta.getDescripcion());
                            btnEliminar.setVisibility(View.VISIBLE);  // Mostrar el botón de eliminar
                            btnEditar.setVisibility(View.VISIBLE);  // Mostrar el botón de editar

                            // Configurar la acción de eliminar
                            btnEliminar.setOnClickListener(v -> {
                                eliminarElemento(carta);  // Llamar a la función de eliminar
                                btnEliminar.setVisibility(View.GONE);  // Ocultar el botón después de la eliminación
                                btnEditar.setVisibility(View.GONE);  // Ocultar el botón después de la eliminación
                            });

                            // Configurar la acción de editar
                            btnEditar.setOnClickListener(v -> {
                                editarElemento(carta);  // Llamar a la función de editar
                                btnEliminar.setVisibility(View.GONE);  // Ocultar el botón después de la edición
                                btnEditar.setVisibility(View.GONE);  // Ocultar el botón después de la edición
                            });
                        }
                    });

                    // Asignar el adaptador al RecyclerView
                    rvListaCarta.setAdapter(adaptadorCarta);
                } else {
                    Log.e("API Error", "Error: " + response.message());
                    Toast.makeText(ActividadMostrarTarjeta.this, "Lista vacía: " + response.message(), Toast.LENGTH_LONG).show();
                }
                barraProgreso.dismiss();  // Desaparece el progress dialog
            }

            @Override
            public void onFailure(Call<List<CartaAPI>> call, Throwable t) {
                Log.e("API Error", "Error al conectar al servidor: " + t.getMessage());
                Toast.makeText(ActividadMostrarTarjeta.this, "Error al conectar al servidor: " + t.getMessage(), Toast.LENGTH_LONG).show();
                barraProgreso.dismiss();
            }
        });
    }


    private void eliminarElemento(CartaAPI carta) {
        // Verificar el ID antes de realizar la solicitud DELETE
        if (carta.getIdCarta() == 0) {
            Toast.makeText(this, "ID de carta no válido", Toast.LENGTH_SHORT).show();
            return;  // Detener la operación si el ID es 0
        }

        // Crear un AlertDialog para confirmar la eliminación
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Eliminar");
        builder.setMessage("¿Estás seguro de eliminar esta carta?");
        builder.setPositiveButton("Sí", (dialog, which) -> {
            // Crear la API servicio
            ApiServicio apiServicio = RetrofitCliente.getCliente().create(ApiServicio.class);

            // Imprimir la URL completa en los logs antes de hacer la llamada DELETE
            Log.d("API", "DELETE URL: " + "http://restobarapi.somee.com/api/Cartas/" + carta.getIdCarta());

            // Usar Retrofit para hacer la solicitud DELETE a la API con el ID de la carta
            Call<Void> call = apiServicio.DeleteCarta(carta.getIdCarta());

            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        // Si la eliminación fue exitosa, eliminar de la lista local
                        listaCarta.remove(carta);
                        adaptadorCarta.notifyDataSetChanged();
                        Toast.makeText(ActividadMostrarTarjeta.this, "Elemento eliminado del servidor", Toast.LENGTH_SHORT).show();
                    } else {
                        // Mostrar código de error si no es exitosa
                        Log.e("API", "Error DELETE: " + response.code() + " - " + response.message());
                        Toast.makeText(ActividadMostrarTarjeta.this, "Error al eliminar del servidor", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Log.e("API", "Error de red DELETE: " + t.getMessage());
                    Toast.makeText(ActividadMostrarTarjeta.this, "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());
        builder.show();
    }



    private void editarElemento(CartaAPI carta) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_edit_item, null);
        builder.setView(dialogView);

        EditText etNombre = dialogView.findViewById(R.id.etNombre);
        EditText etDescripcion = dialogView.findViewById(R.id.etDescripcion);
        EditText etPrecio = dialogView.findViewById(R.id.etPrecio);
        Button btnSeleccionarImagen = dialogView.findViewById(R.id.btnSeleccionarImagen);

        etNombre.setText(carta.getNombre());
        etDescripcion.setText(carta.getDescripcion());
        etPrecio.setText(String.valueOf(carta.getPrecio()));

        final String[] nuevaRutaImagen = {carta.getFoto()};

        btnSeleccionarImagen.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, 100);
        });

        builder.setPositiveButton("Guardar", (dialog, which) -> {
            carta.setNombre(etNombre.getText().toString());
            carta.setDescripcion(etDescripcion.getText().toString());
            carta.setPrecio(Integer.parseInt(etPrecio.getText().toString()));

            if (!nuevaRutaImagen[0].isEmpty()) {
                carta.setFoto(nuevaRutaImagen[0]);
            }

            enviarPutCarta(carta);
        });

        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());

        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            rutaImagenSeleccionada = obtenerRutaDesdeUri(uri);
        }
    }

    private String obtenerRutaDesdeUri(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String filePath = cursor.getString(column_index);
            cursor.close();
            return filePath;
        }
        return null;
    }

    public void enviarPutCarta(CartaAPI carta) {
        // Crear los parámetros que irán en la URL
        RequestBody nombrePart = RequestBody.create(MediaType.parse("text/plain"), carta.getNombre());
        RequestBody descripcionPart = RequestBody.create(MediaType.parse("text/plain"), carta.getDescripcion());
        RequestBody precioPart = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(carta.getPrecio()));
        RequestBody fotoPart = RequestBody.create(MediaType.parse("text/plain"), carta.getFoto());
        RequestBody rutaPart = RequestBody.create(MediaType.parse("text/plain"), carta.getRuta());

        // Crear la parte del archivo de imagen
        MultipartBody.Part body = null;

        if (!carta.getFoto().isEmpty()) {
            File file = new File(carta.getFoto());
            if (file.exists()) {
                RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), file);
                body = MultipartBody.Part.createFormData("Archivo", file.getName(), requestFile);
            }
        }

        // Si no hay archivo, debemos enviar un body vacío
        if (body == null) {
            // Si no se proporciona archivo, creamos un cuerpo vacío para cumplir con la solicitud multipart
            body = MultipartBody.Part.createFormData("Archivo", "");
        }

        // Crear la API
        ApiServicio apiServicio = RetrofitCliente.getCliente().create(ApiServicio.class);

        // Llamar al PUT con los parámetros de la URL y el archivo de imagen
        Call<Void> call = apiServicio.PutCarta(
                carta.getIdCarta(),  // ID de la carta en la URL
                carta.getIdCarta(),
                carta.getNombre(),
                carta.getDescripcion(),
                carta.getPrecio(),
                carta.getFoto(),
                carta.getRuta(),
                body);  // El archivo se envía como parte del cuerpo

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ActividadMostrarTarjeta.this, "Carta actualizada", Toast.LENGTH_SHORT).show();
                    MostrarListadoDeCartaApi();  // Refrescar lista
                } else {
                    Toast.makeText(ActividadMostrarTarjeta.this, "Error al actualizar: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ActividadMostrarTarjeta.this, "Fallo en red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}