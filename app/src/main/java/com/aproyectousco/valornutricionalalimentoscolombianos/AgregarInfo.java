package com.aproyectousco.valornutricionalalimentoscolombianos;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class AgregarInfo extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_info);

        Intent intent = getIntent();
        String Correo = intent.getStringExtra("Correo");

        Button desayunoButton = findViewById(R.id.btnAgregarDesayuno);
        Button almuerzoButton = findViewById(R.id.btnAgregarAlmuerzo);
        Button cenaButton = findViewById(R.id.btnAgregarCena);
        Button historialButton = findViewById(R.id.btnInformacionGeneral);





        desayunoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IrAlimentos("Desayuno", Correo);
            }
        });

        almuerzoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IrAlimentos("Almuerzo", Correo);
            }
        });

        cenaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IrAlimentos("Cena", Correo);
            }
        });

        historialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AgregarInfo.this, InformacionGeneral.class);
                intent.putExtra("Correo", Correo);
                startActivity(intent);
                finish();
            }
        });




    }

    @Override
    public void onBackPressed() {
        // No hagas nada para anular el comportamiento del botón de retroceso
    }


    private void IrAlimentos(String Alimento, String correo) {


        Intent intentIrAlimentos = new Intent(AgregarInfo.this, AgregarAlimento.class);
        intentIrAlimentos.putExtra("Correo", correo);
        intentIrAlimentos.putExtra("Alimento", Alimento);
        startActivity(intentIrAlimentos);
    }

}