package com.aproyectousco.valornutricionalalimentoscolombianos;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class InformacionGeneral extends AppCompatActivity {

    private FirebaseAuth mAuth;
    DatabaseReference mRootReference;
    Button enviar;
    String Correo = Registrar.GlobalVariables.idCorreo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion_general);
        enviar = findViewById(R.id.envio);

        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                mRootReference = FirebaseDatabase.getInstance().getReference();

                DatabaseReference myRef = database.getReference("message");
                myRef.setValue(Correo);
                cargarDatosFirebase("Alejandro", "Palacios", 301, "Casa");
                Toast.makeText(InformacionGeneral.this, "Enviando algo", Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void cargarDatosFirebase(String nombre, String apellido, int telefono, String direccion) {

        Map<String, Object> datosUsuario = new HashMap<>();
        datosUsuario.put("nombre", nombre);
        datosUsuario.put("apellido", apellido);
        datosUsuario.put("telefono", telefono);
        datosUsuario.put("direccion", direccion);

        mRootReference.child("Usuarios").push().setValue(datosUsuario);
    }
}