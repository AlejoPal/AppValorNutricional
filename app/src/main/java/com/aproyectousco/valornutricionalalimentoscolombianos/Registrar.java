package com.aproyectousco.valornutricionalalimentoscolombianos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Registrar extends AppCompatActivity {

    private FirebaseAuth mAuth;

    Button btnIrInicio;
    static EditText correo;
    EditText contrasena;
    EditText confirmar;
    String idCorreo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);

        mAuth = FirebaseAuth.getInstance();

        correo = findViewById(R.id.emailpageregistro);
        contrasena = findViewById(R.id.cont1pageregistro);
        confirmar = findViewById(R.id.cont2pageregistro);

        btnIrInicio = findViewById(R.id.btniriniciar);
        btnIrInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Registrar.this, InicarSesion.class);
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
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        idCorreo = correo.getText().toString();
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = database.getReference("Usuarios");
                        myRef.setValue(idCorreo);

                        Toast.makeText(Registrar.this, "autenticacion correcta", Toast.LENGTH_SHORT).show();
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

    public class GlobalVariables {
        public static String idCorreo = correo.getText().toString();
    }


}