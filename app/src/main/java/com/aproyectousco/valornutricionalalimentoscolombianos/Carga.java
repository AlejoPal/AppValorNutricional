package com.aproyectousco.valornutricionalalimentoscolombianos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class Carga extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carga);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Obtener el valor de la variable recién capturada
                Intent intent = getIntent();
                String correo = intent.getStringExtra("Correo");

                // Crear el Intent para la nueva vista
                Intent nuevaIntent = new Intent(Carga.this, InformacionGeneral.class);
                nuevaIntent.putExtra("Correo", correo); // Enviar la variable capturada a la nueva vista

                startActivity(nuevaIntent);
                finish();
            }

        }, 1000); // Espera de 2 segundos antes de ir a la vista de información general (ajusta el valor según tus necesidades)

    }
}