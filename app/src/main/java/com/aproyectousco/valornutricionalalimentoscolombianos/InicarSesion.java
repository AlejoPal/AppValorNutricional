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

public class InicarSesion extends AppCompatActivity {

    Button btnIrRegistro;
    EditText correo, contrasena;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicar_sesion);

        mAuth = FirebaseAuth.getInstance();

        correo = findViewById(R.id.Emailinicar);
        contrasena = findViewById(R.id.continiciar);

        btnIrRegistro = findViewById(R.id.iraregitro);
        btnIrRegistro.setOnClickListener(new View.OnClickListener() {
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
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                    Toast.makeText(InicarSesion.this, "Ingreso Adecuado", Toast.LENGTH_SHORT).show();
                    FirebaseUser user = mAuth.getCurrentUser();
                    Intent intent =  new Intent(InicarSesion.this, InformacionGeneral.class);
                    startActivity(intent);
                }else {

                    Toast.makeText(InicarSesion.this, "Usuario o Contraseña invalidas", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
}