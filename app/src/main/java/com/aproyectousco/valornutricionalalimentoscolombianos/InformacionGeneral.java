package com.aproyectousco.valornutricionalalimentoscolombianos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class InformacionGeneral extends AppCompatActivity {

    ViewPager2 viewPager2;

    TextView textonombre;

    Spinner spinner;
    Button btnIragregar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion_general);

        spinner = findViewById(R.id.spinnerfechas);
        btnIragregar = findViewById(R.id.btnIragregar);
        viewPager2 = findViewById(R.id.viewpager);

        // Se recupera el correo de inicio de sesión o de registro
        Intent intent = getIntent();
        String[] comidas = {"Desayuno, Almuerzo, Cena"};
        String correo = intent.getStringExtra("Correo");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mRootReference = database.getReference();

        // Obtener la referencia al nodo del usuario
        DatabaseReference usuarioRef = mRootReference.child("Usuario").child(correo);

        // Botón para ir a agregar una comida
        btnIragregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AgregarInfo.class);
                intent.putExtra("Correo", correo);
                startActivity(intent);
            }
        });

        // Cargar fechas a un spinner
        usuarioRef.addListenerForSingleValueEvent(new ValueEventListener() {
            Date date = new Date();

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

                SimpleDateFormat fechaC = new SimpleDateFormat("yyyyMMdd");
                String fechaHoy= fechaC.format(date);
                // Buscar la posición de la fecha de hoy en la lista
                int position = fechas.indexOf(fechaHoy);

                // Seleccionar la fecha de hoy si está en la lista
                if (position != -1) {
                    spinner.setSelection(position);
                    String fechaSeleccionada = fechas.get(position);
                    obtenerInformacionComidas(fechaSeleccionada, correo);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Manejo de errores en caso de fallo en la lectura de la base de datos
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

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String fechaSeleccionada = (String) parent.getItemAtPosition(position);

                // Llama a un método para obtener la información de la fecha seleccionada
                obtenerInformacionComidas(fechaSeleccionada, correo);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Acciones a realizar cuando no se ha seleccionado ninguna fecha
            }
        });
    }

    private void obtenerInformacionComidas(String fechaSeleccionada, String correo) {
        String[] infoCarbohidratos = new String[3];
        String[] infoColesterol = new String[3];
        String[] infoEnergia = new String[3];
        String[] infoGsat = new String[3];
        String[] infoLipidos = new String[3];
        String[] infoProteina = new String[3];
        String[] infoSodio = new String[3];

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

                        // Asignar los valores a los arrays correspondientes
                        if (comida.equals("Desayuno")) {
                            infoCarbohidratos[0] = String.valueOf(carbohidratos);
                            infoColesterol[0] = String.valueOf(colesterol);
                            infoEnergia[0] = String.valueOf(energia);
                            infoGsat[0] = String.valueOf(gsat);
                            infoLipidos[0] = String.valueOf(lipidos);
                            infoProteina[0] = String.valueOf(proteinas);
                            infoSodio[0] = String.valueOf(sodio);
                        } else if (comida.equals("Almuerzo")) {
                            infoCarbohidratos[1] = String.valueOf(carbohidratos);
                            infoColesterol[1] = String.valueOf(colesterol);
                            infoEnergia[1] = String.valueOf(energia);
                            infoGsat[1] = String.valueOf(gsat);
                            infoLipidos[1] = String.valueOf(lipidos);
                            infoProteina[1] = String.valueOf(proteinas);
                            infoSodio[1] = String.valueOf(sodio);
                        } else if (comida.equals("Cena")) {
                            infoCarbohidratos[2] = String.valueOf(carbohidratos);
                            infoColesterol[2] = String.valueOf(colesterol);
                            infoEnergia[2] = String.valueOf(energia);
                            infoGsat[2] = String.valueOf(gsat);
                            infoLipidos[2] = String.valueOf(lipidos);
                            infoProteina[2] = String.valueOf(proteinas);
                            infoSodio[2] = String.valueOf(sodio);
                        }
                    }

                    // Crear los objetos ViewPagerItem y configurar el adaptador en el ViewPager2
                    ViewPagerItem desayuno = new ViewPagerItem("Desayuno", infoCarbohidratos[0], infoColesterol[0], infoEnergia[0], infoGsat[0], infoLipidos[0], infoProteina[0], infoSodio[0]);
                    ViewPagerItem almuerzo = new ViewPagerItem("Almuerzo", infoCarbohidratos[1], infoColesterol[1], infoEnergia[1], infoGsat[1], infoLipidos[1], infoProteina[1], infoSodio[1]);
                    ViewPagerItem cena = new ViewPagerItem("Cena", infoCarbohidratos[2], infoColesterol[2], infoEnergia[2], infoGsat[2], infoLipidos[2], infoProteina[2], infoSodio[2]);

                    ArrayList<ViewPagerItem> viewPagerItems = new ArrayList<>();
                    viewPagerItems.add(desayuno);
                    viewPagerItems.add(almuerzo);
                    viewPagerItems.add(cena);

                    VPAdapter vpAdapter = new VPAdapter(viewPagerItems);
                    viewPager2.setAdapter(vpAdapter);

                    // Añade esta línea para notificar al adaptador que los datos han cambiado
                    vpAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(InformacionGeneral.this, "No se encontraron datos en la base de datos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Manejo de errores en caso de fallo en la lectura de la base de datos
            }
        });
    }
}
