package com.aproyectousco.valornutricionalalimentoscolombianos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class InformacionGeneral extends AppCompatActivity {

    TextView textonombre;

    Spinner spinner;
    Button btnIragregar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion_general);

        spinner = findViewById(R.id.spinnerfechas);
        btnIragregar = findViewById(R.id.btnIragregar);

        //Variables
        //Se recupera el correo de inicio de sesion o de registrar
        Intent intent = getIntent();
        String Correo = intent.getStringExtra("Correo");



        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mRootReference = database.getReference();

        // Obtener la referencia al nodo del usuario
        DatabaseReference usuarioRef = mRootReference.child("Usuario").child(Correo);


        //Boton Para ir a agregar una comida
        btnIragregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AgregarInfo.class);
                startActivity(intent);
            }
        });

        // Cargar fechas a un spinner
        usuarioRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> fechas = new ArrayList<>();

                for (DataSnapshot fechaSnapshot : dataSnapshot.getChildren()) {
                    if (!fechaSnapshot.getKey().equals("Nombre")) {
                        String fecha = fechaSnapshot.getKey();
                        fechas.add(fecha);
                    }
                }

                // Aquí puedes utilizar la lista de fechas para mostrarlas en el Spinner
                // Por ejemplo, utilizando un ArrayAdapter
                ArrayAdapter<String> adapter = new ArrayAdapter<>(InformacionGeneral.this, android.R.layout.simple_spinner_item, fechas);
                spinner.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Manejo de errores en caso de fallo en la lectura de la base de datos
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String fechaSeleccionada = (String) parent.getItemAtPosition(position);

                // Llama a un método para obtener la información de la fecha seleccionada
                obtenerInformacionComidas(fechaSeleccionada, Correo);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Acciones a realizar cuando no se ha seleccionado ninguna fecha
            }
        });


        // Crear el listener para recuperar el valor del nombre
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String nombre = dataSnapshot.child("Nombre").getValue(String.class);

                    textonombre = findViewById(R.id.NombreUsuario);
                    // Hacer algo con el valor del nombre
                    textonombre.setText(nombre);
                    Log.d("TAG", "Nombre: " + nombre);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Manejar errores de lectura de la base de datos
                Log.d("TAG", "Error al leer la base de datos: " + databaseError.getMessage());
            }
        };

        // Agregar el listener a la referencia del usuario
        usuarioRef.addValueEventListener(valueEventListener);





    }

    private void obtenerInformacionComidas(String fechaSeleccionada, String correo) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mRootReference = database.getReference();

        // Obtener la referencia al nodo del usuario
        DatabaseReference databaseRef = mRootReference.child("Usuario").child(correo).child(fechaSeleccionada);

        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot comidaSnapshot : dataSnapshot.getChildren()) {
                        String comida = comidaSnapshot.getKey();

                        // Obtener la información de la comida actual
                        Double carbohidratos = comidaSnapshot.child("Carbohidratos").getValue(Double.class);
                        Double colesterol = comidaSnapshot.child("Colesterol").getValue(Double.class);
                        Double energia = comidaSnapshot.child("Energia").getValue(Double.class);
                        Double gsat = comidaSnapshot.child("Gsat").getValue(Double.class);
                        Double lipidos = comidaSnapshot.child("Lipidos").getValue(Double.class);
                        Double proteinas = comidaSnapshot.child("Proteina").getValue(Double.class);
                        Double sodio = comidaSnapshot.child("Sodio").getValue(Double.class);

                        // Asignar los valores a los TextView correspondientes
                        if (comida.equals("Desayuno")) {
                            TextView txtCarbohidratosD = findViewById(R.id.txtcarbohidratosD);
                            TextView txtColesterolD = findViewById(R.id.txtcolesterolD);
                            TextView txtEnergiaD = findViewById(R.id.txtenergiaD);
                            TextView txtSaturadasD = findViewById(R.id.txtgsaturadasD);
                            TextView txtLipidosD = findViewById(R.id.txtlipidosD);
                            TextView txtProteinasD = findViewById(R.id.txtproteinasD);
                            TextView txtSodioD = findViewById(R.id.txtsodioD);


                            txtCarbohidratosD.setText(String.valueOf(carbohidratos));
                            txtColesterolD.setText(String.valueOf(colesterol));
                            txtEnergiaD.setText(String.valueOf(energia));
                            txtSaturadasD.setText(String.valueOf(gsat));
                            txtLipidosD.setText(String.valueOf(lipidos));
                            txtProteinasD.setText(String.valueOf(proteinas));
                            txtSodioD.setText(String.valueOf(sodio));

                        } else if (comida.equals("Almuerzo")) {
                            TextView txtCarbohidratosA = findViewById(R.id.txtcarbohidratosA);
                            TextView txtColesterolA = findViewById(R.id.txtcolesterolA);
                            TextView txtEnergiaA = findViewById(R.id.txtenergiaA);
                            TextView txtSaturadasA = findViewById(R.id.txtgsaturadasA);
                            TextView txtLipidosA = findViewById(R.id.txtlipidosA);
                            TextView txtProteinasA = findViewById(R.id.txtproteinasA);
                            TextView txtSodioA = findViewById(R.id.txtsodioA);


                            txtCarbohidratosA.setText(String.valueOf(carbohidratos));
                            txtColesterolA.setText(String.valueOf(colesterol));
                            txtEnergiaA.setText(String.valueOf(energia));
                            txtSaturadasA.setText(String.valueOf(gsat));
                            txtLipidosA.setText(String.valueOf(lipidos));
                            txtProteinasA.setText(String.valueOf(proteinas));
                            txtSodioA.setText(String.valueOf(sodio));

                        } else if (comida.equals("Cena")) {

                            TextView txtCarbohidratosC = findViewById(R.id.txtcarbohidratosC);
                            TextView txtColesterolC = findViewById(R.id.txtcolesterolC);
                            TextView txtEnergiaC = findViewById(R.id.txtenergiaC);
                            TextView txtSaturadasC = findViewById(R.id.txtgsaturadasC);
                            TextView txtLipidosC = findViewById(R.id.txtlipidosC);
                            TextView txtProteinasC = findViewById(R.id.txtproteinasC);
                            TextView txtSodioC = findViewById(R.id.txtsodioC);


                            txtCarbohidratosC.setText(String.valueOf(carbohidratos));
                            txtColesterolC.setText(String.valueOf(colesterol));
                            txtEnergiaC.setText(String.valueOf(energia));
                            txtSaturadasC.setText(String.valueOf(gsat));
                            txtLipidosC.setText(String.valueOf(lipidos));
                            txtProteinasC.setText(String.valueOf(proteinas));
                            txtSodioC.setText(String.valueOf(sodio));
                        }
                    }
                } else {
                    Toast.makeText(InformacionGeneral.this, "No entro a la base de datos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Manejo de errores en caso de fallo en la lectura de la base de datos
            }
        });
    }


}

