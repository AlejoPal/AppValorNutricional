package com.aproyectousco.valornutricionalalimentoscolombianos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InicarSesion extends AppCompatActivity {

    EditText correo;
    EditText contrasena;

    TextView IrRegistro;
    private FirebaseAuth mAuth;
    DatabaseReference mRootReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicar_sesion);

        //Direccion base de datos

        // Otras variables
        mAuth = FirebaseAuth.getInstance();
        mRootReference = FirebaseDatabase.getInstance().getReference();

        correo = findViewById(R.id.Emailinicar);
        contrasena = findViewById(R.id.continiciar);

        IrRegistro = findViewById(R.id.textiraregitro);
        IrRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InicarSesion.this, Registrar.class);
                startActivity(intent);
            }
        });

    }
    public void onStart(){
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();

    }
    public void iniciarSesion (View view){


        mAuth.signInWithEmailAndPassword(correo.getText().toString(), contrasena.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            Date date = new Date();
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference mRootReference = database.getReference();

                    // Obtener la referencia al nodo del usuario
                    DatabaseReference usuarioRef = mRootReference.child("Usuario").child(correo.getText().toString().replace(".", ""));


                    //Conseguir fecha
                    SimpleDateFormat fechaC = new SimpleDateFormat("yyyyMMdd");
                    String sfecha = fechaC.format(date);

                    cargarDatosFirebaseI(sfecha, correo.getText().toString().replace(".", ""));

                    //Parte dos
                    Toast.makeText(InicarSesion.this, "Login", Toast.LENGTH_SHORT).show();
                    FirebaseUser user = mAuth.getCurrentUser();
                    Intent intent =  new Intent(InicarSesion.this, InformacionGeneral.class);
                    intent.putExtra("Correo", correo.getText().toString().replace(".", ""));
                    startActivity(intent);
                }else {

                    Toast.makeText(InicarSesion.this, "Usuario o Contraseña invalidas", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    private void cargarDatosFirebaseI(String fecha, String correo) {
        try {
            DatabaseReference usuarioRef = mRootReference.child("Usuario").child(correo.replace(".", ""));
            DatabaseReference fechaRef = usuarioRef.child(fecha);

            fechaRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.exists()) {
                        // La fecha no existe, se crea un nuevo nodo con los datos
                        Map<String, Object> diario = new HashMap<>();
                        diario.put("Desayuno", infoAlimenticia(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0));
                        diario.put("Almuerzo", infoAlimenticia(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0));
                        diario.put("Cena", infoAlimenticia(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0));

                        fechaRef.setValue(diario);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Manejo de errores en caso de fallo en la lectura de la base de datos
                }
            });
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