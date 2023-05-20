package com.aproyectousco.valornutricionalalimentoscolombianos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Registrar extends AppCompatActivity {

    private FirebaseAuth mAuth;

    DatabaseReference mRootReference;


    Button btnIrInicio;
    static EditText correo;
    EditText contrasena, confirmar, nombre;
    String idCorreo;
    TextView irIniciar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);

        mAuth = FirebaseAuth.getInstance();

        nombre = findViewById(R.id.nombreRegistro);
        correo = findViewById(R.id.emailpageregistro);
        contrasena = findViewById(R.id.cont1pageregistro);
        confirmar = findViewById(R.id.cont2pageregistro);

        irIniciar= findViewById(R.id.textiraregitro);

        irIniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), InicarSesion.class);
                startActivity(intent);
            }
        });



    }
    public void onStart(){
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();

    }
    public void registrarUsuario(View view){
        if (contrasena.getText().toString().equals(confirmar.getText().toString())){
            mAuth.createUserWithEmailAndPassword(correo.getText().toString(), contrasena.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                Date date = new Date();
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        mRootReference = FirebaseDatabase.getInstance().getReference();

                        //Conseguir fecha
                        SimpleDateFormat fechaC = new SimpleDateFormat("yyyyMMdd");
                        String sfecha = fechaC.format(date);

                        cargarDatosFirebase(nombre.getText().toString(), sfecha, correo.getText().toString().replace(".", ""));

                        //Ir a informacion general cuando inicie Sesion
                        Toast.makeText(Registrar.this, "Login", Toast.LENGTH_SHORT).show();
                        FirebaseUser user = mAuth.getCurrentUser();
                        Intent intent =  new Intent(Registrar.this, InformacionGeneral.class);
                        startActivity(intent);
                    }else {

                        Toast.makeText(Registrar.this, "Authentication failed", Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }else {
            Toast.makeText(this, "Las contrasenas no son iguales", Toast.LENGTH_SHORT).show();
        }

    }

    private void cargarDatosFirebase(String nombre, String fecha, String Correo) {
        //Parte de fechas
        try {
            Map<String, Object> Diario = new HashMap<>();
            Diario.put("Desayuno", infoAlimenticia(0,0,0,0,0));
            Diario.put("Almuerzo", infoAlimenticia(0,0,0,0,0));
            Diario.put("Cena", infoAlimenticia(0,0,0,0,0));

            Map<String, Object> datosUsuario = new HashMap<>();
            datosUsuario.put("Nombre", nombre);
            datosUsuario.put(fecha, Diario );

            if (mRootReference != null) {
                // Verificar que la variable "Correo" contenga un valor válido antes de utilizarla como clave
                if (Correo != null && !Correo.isEmpty()) {
                    mRootReference.child("Usuario").child(Correo).setValue(datosUsuario);
                } else {
                    Toast.makeText(Registrar.this, "Sin correo", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(Registrar.this, "Sin direccion", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e("TAG", "Ocurrió un error: " + e.getMessage());

            // Manejar el error de alguna manera adecuada
        }


    }

    private Object infoAlimenticia(int proteina, int energia, int carbohidratos, int lipidos, int sales){
        Map<String, Object> fechas = new HashMap<>();
        fechas.put("Proteina", proteina);
        fechas.put("Energia", energia);
        fechas.put("Carbohidratos", carbohidratos);
        fechas.put("Lipidos", lipidos);
        fechas.put("Sodio", sales);
        return fechas;
    }


    public class GlobalVariables {
        public static String idCorreo = correo.getText().toString().replace(".", "");
    }


}