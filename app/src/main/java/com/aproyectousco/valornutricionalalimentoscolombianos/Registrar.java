package com.aproyectousco.valornutricionalalimentoscolombianos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
    static EditText correoR;
    EditText contrasena, confirmar, nombre;
    String idCorreo;
    TextView irIniciar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);

        mAuth = FirebaseAuth.getInstance();

        nombre = findViewById(R.id.nombreRegistro);
        correoR = findViewById(R.id.emailpageregistro);
        contrasena = findViewById(R.id.cont1pageregistro);
        confirmar = findViewById(R.id.cont2pageregistro);

        irIniciar= findViewById(R.id.textiraregitro);

        irIniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), InicarSesion.class);
                startActivity(intent);
                finish();
            }
        });



    }
    public void onStart(){
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();

    }
    public void registrarUsuario(View view) {
        String nombreText = nombre.getText().toString().trim();
        String correoText = correoR.getText().toString().trim();
        String contrasenaText = contrasena.getText().toString().trim();
        String confirmarText = confirmar.getText().toString().trim();

        // Verificar que los campos no estén vacíos
        if (nombreText.isEmpty() || correoText.isEmpty() || contrasenaText.isEmpty() || confirmarText.isEmpty()) {
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Verificar la longitud mínima de la contraseña
        if (contrasenaText.length() < 6 || confirmarText.length() < 6) {
            Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
            return;
        }

        if (contrasenaText.equals(confirmarText)) {
            mAuth.createUserWithEmailAndPassword(correoText, contrasenaText).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                Date date = new Date();

                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        mRootReference = FirebaseDatabase.getInstance().getReference();

                        // Obtener la fecha actual
                        SimpleDateFormat fechaC = new SimpleDateFormat("yyyyMMdd");
                        String sfecha = fechaC.format(date);

                        cargarDatosFirebase(nombreText, sfecha, correoText.replace(".", ""));

                        // Ir a InformacionGeneral cuando inicie sesión



                        Toast.makeText(Registrar.this, "Inicio de sesión", Toast.LENGTH_SHORT).show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                FirebaseUser user = mAuth.getCurrentUser();
                                Intent intent = new Intent(Registrar.this, InformacionGeneral.class);
                                intent.putExtra("Correo", correoText.replace(".", ""));
                                startActivity(intent);
                                finish();
                            }
                        }, 200);

                    } else {
                        Toast.makeText(Registrar.this, "Error en la autenticación", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
        }
    }


    private void cargarDatosFirebase(String nombre, String fecha, String Correo) {
        //Parte de fechas
        try {
            Map<String, Object> Diario = new HashMap<>();
            Diario.put("Desayuno", infoAlimenticia(0.0,0.0,0.0,0.0,0.0,0.0,0.0));
            Diario.put("Almuerzo", infoAlimenticia(0.0,0.0,0.0,0.0,0.0,0.0,0.0));
            Diario.put("Cena", infoAlimenticia(0.0,0.0,0.0,0.0,0.0,0.0,0.0));

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

    private Object infoAlimenticia(double proteina, double energia, double carbohidratos, double lipidos, double sales, double gsat, double colesterol){
        Map<String, Object> fechas = new HashMap<>();
        fechas.put("Proteina", proteina);
        fechas.put("Energia", energia);
        fechas.put("Carbohidratos", carbohidratos);
        fechas.put("Lipidos", lipidos);
        fechas.put("Sodio", sales);
        fechas.put("Gsat", gsat);
        fechas.put("Colesterol", colesterol);
        return fechas;
    }



}