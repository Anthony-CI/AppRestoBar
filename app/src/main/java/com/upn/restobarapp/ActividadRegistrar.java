package com.upn.restobarapp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.radiobutton.MaterialRadioButton;
import com.google.android.material.textfield.TextInputEditText;
import com.upn.restobarapp.Access.DAOCartaBD;
import com.upn.restobarapp.Model.CartaAPI;
import com.upn.restobarapp.Model.CartaDB;
import com.upn.restobarapp.Network.ApiServicio;
import com.upn.restobarapp.Network.RetrofitCliente;

import java.io.ByteArrayOutputStream;
import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActividadRegistrar extends AppCompatActivity {

    private TextInputEditText txtNombre;
    private TextInputEditText txtDescripcion;
    private TextInputEditText txtPrecio;
    private ImageView imgFoto;
    private Uri uriFoto=null; //almacena la ruta donde està ubicada la imagen
    private MaterialButton btnSelecionarFoto;
    //Declarar una variable para ir a la actividad del sistema de galeria de fotos
    private ActivityResultLauncher<Intent> irActividadGaleriaFotos;
    private FloatingActionButton btnGrabar;
    private byte[] imagenSeleccionada=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.ly_registrar);

        uriFoto=null;
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        txtNombre = findViewById(R.id.txtNombre);
        txtDescripcion = findViewById(R.id.txtDescripcion);
        txtPrecio = findViewById(R.id.txtPrecio);
        imgFoto = findViewById(R.id.imgFoto);
        btnSelecionarFoto = findViewById(R.id.btnSeleccionarFoto);
        btnGrabar = findViewById(R.id.FAB);

        btnSelecionarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Definir un objeto intento de tipo genérico
                Intent oIntento = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                //startActivity(oIntento);
                irActividadGaleriaFotos.launch(oIntento);
            }
        });
        irActividadGaleriaFotos = registerForActivityResult( new ActivityResultContracts.StartActivityForResult(),
                result->{
                    if(result.getResultCode()==RESULT_OK && result.getData()!=null){
                        uriFoto = result.getData().getData();
                        imgFoto.setImageURI(uriFoto);
                        /*
                        //Convertir de URI a byte[]
                        imgFoto.buildDrawingCache();
                        Bitmap oGrafico = imgFoto.getDrawingCache();
                        ByteArrayOutputStream oFlujo = new ByteArrayOutputStream();
                        oGrafico.compress(Bitmap.CompressFormat.PNG,0,oFlujo);
                        imagenSeleccionada = oFlujo.toByteArray();
                        */
                    }
                });
        btnGrabar.setOnClickListener(v->{
            grabarObjetoCarta();
        });

    }

    private void grabarObjetoCarta() {
        //validar


        if(txtNombre.getText().toString().isEmpty()){
            txtNombre.setError("Campo nombre es obligatorio");
            txtNombre.requestFocus();
            return;
        }

        if(txtDescripcion.getText().toString().isEmpty()){
            txtDescripcion.setError("Campo descripción es obligatorio");
            txtDescripcion.requestFocus();
            return;
        }

        if(txtPrecio.getText().toString().isEmpty()){
            txtPrecio.setError("Campo precio es obligatorio");
            txtPrecio.requestFocus();
            return;
        }

        if(uriFoto==null){
            Toast.makeText(this,"Cargar una imagen",Toast.LENGTH_LONG).show();
            return;
        }
        //Capturar en variables los datos ingresados por el usuario
        String nombre = txtNombre.getText().toString();
        String descripcion = txtDescripcion.getText().toString();
        int precio = Integer.parseInt(txtPrecio.getText().toString());

        /*
        //Crear un objeto carta en BD SQLite
        CartaDB oE = new CartaDB(nombre, descripcion,precio,
                imagenSeleccionada);
        DAOCartaBD oBDCarta = new DAOCartaBD(this);
        String rpta = oBDCarta.addCartaBD(oE);
        if(rpta=="OK")
            Toast.makeText(this,"Registro aceptado",Toast.LENGTH_LONG).show();
        else
            Toast.makeText(this,rpta,Toast.LENGTH_LONG).show();
        CuadroDialogo();

         */

        CartaAPI oE = new CartaAPI(nombre,descripcion,precio,"nulo");
        enviarPost(oE);

    }

    private void enviarPost(CartaAPI oE) {
        //Declarar un objeto de tipo progreso
        ProgressDialog oProgreso = new ProgressDialog(this);
        oProgreso.setMessage("Enviando datos de Carta ...");
        oProgreso.setCancelable(false); // Inmobilizar el aplicativo
        oProgreso.show();
        ApiServicio apiServicio = RetrofitCliente.getCliente().create(ApiServicio.class);
        //Enviar cada valor del atributo EmergenciaAPI a una parte del cuerpo del formulario
        RequestBody IdCartaPart = RequestBody.
                create(MediaType.parse("text/plain"),"0");
        RequestBody nombrePart = RequestBody.
                create(MediaType.parse("text/plain"),oE.getNombre());
        RequestBody descripcionPart = RequestBody.
                create(MediaType.parse("text/plain"),oE.getDescripcion());
        RequestBody precioPart = RequestBody.
                create(MediaType.parse("text/plain"),String.valueOf(oE.getPrecio()));
        RequestBody fotoPart = RequestBody.
                create(MediaType.parse("text/plain"),"no");
        RequestBody rutaPart = RequestBody.
                create(MediaType.parse("text/plain"),"no");
        //convertir la imagen del cliente uri a multipart
        MultipartBody.Part archivoPart = prepararFilePart(uriFoto,"Archivo");
        //Recien se puede llamar al metodo POSt
        Call<CartaAPI> call = apiServicio.PostCartas(
                IdCartaPart,
                nombrePart,
                descripcionPart,
                precioPart,
                fotoPart,
                rutaPart,
                archivoPart
        );
        //llamar al servio web(api)
        call.enqueue(new Callback<CartaAPI>() {
            @Override
            public void onResponse(Call<CartaAPI> call, Response<CartaAPI> response) {
                if(response.isSuccessful()){
                    Toast.makeText(ActividadRegistrar.this,"Carta registrado",
                            Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(ActividadRegistrar.this,"Error en enviar datos "+response.message(),
                            Toast.LENGTH_SHORT).show();
                }
                oProgreso.dismiss();// desactiva el objeto progreso
            }

            @Override
            public void onFailure(Call<CartaAPI> call, Throwable t) {
                Toast.makeText(ActividadRegistrar.this,"Error conexión ip "+t.getMessage(),
                        Toast.LENGTH_SHORT).show();
                oProgreso.dismiss();
            }
        });
    }

    private MultipartBody.Part prepararFilePart(Uri uri,String nomFoto) {
        File archivo = new File(getArchivoMovil(uri));
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"),archivo);
        return MultipartBody.Part.createFormData(nomFoto,archivo.getName(),requestBody);
    }

    private String getArchivoMovil(Uri uri) {
        String[] cadenaFoto = {MediaStore.Images.Media.DATA};
        Cursor curso = getContentResolver().
                query(uri,cadenaFoto,null,null,null);
        if(curso!=null){
            //obtener indice de la cadenafoto
            int indice = curso.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            //cursor se mueva al primer registro
            curso.moveToFirst();
            String ruta = curso.getString(indice);
            curso.close();
            return ruta;
        }
        return null;
    }





    private void CuadroDialogo() {
        AlertDialog.Builder oDialogo = new AlertDialog.Builder(this);
        oDialogo.setTitle("Aviso");
        oDialogo.setMessage("¿Desea seguir registrando Cartas?");
        oDialogo.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                ActividadRegistrar.this.finish();
            }
        });
        oDialogo.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                limpar();
            }
        });
        oDialogo.show();
    }

    private void limpar() {
        txtNombre.setText("");
        txtDescripcion.setText("");
        txtPrecio.setText("");
        uriFoto=null;
        imgFoto.setImageResource(R.drawable.faltafoto);
        txtNombre.requestFocus();
    }
}