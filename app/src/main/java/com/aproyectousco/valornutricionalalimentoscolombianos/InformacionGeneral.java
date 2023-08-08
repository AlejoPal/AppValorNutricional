package com.aproyectousco.valornutricionalalimentoscolombianos;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class InformacionGeneral extends AppCompatActivity {

    ViewPager2 viewPager2;

    TextView textonombre;
    TextView textFecha;

    Button btnIragregar;

    String correo, fechahoy;
    DatabaseReference mRootReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion_general);

        textFecha = findViewById(R.id.fecha);
        btnIragregar = findViewById(R.id.btnIragregar);
        viewPager2 = findViewById(R.id.viewpager);

        // Se recupera el correo de inicio de sesión o de registro
        Intent intent = getIntent();
        String[] comidas = {"Desayuno, Almuerzo, Cena"};
        correo = intent.getStringExtra("Correo");


        Toast.makeText(InformacionGeneral.this, "Selecciona la fecha del calendario", Toast.LENGTH_LONG).show();

        textFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int anio = cal.get(Calendar.YEAR);
                int mes = cal.get(Calendar.MONTH);
                int dia = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dpd = new DatePickerDialog(InformacionGeneral.this,R.style.DatePickerDialogTheme, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String fecha = dayOfMonth + "/" + String.format("%02d", (month + 1)) + "/" + year; // Formato de mes con dos dígitos
                        String fechaSeleccionada = "" + year + String.format("%02d", (month + 1)) + String.format("%02d", dayOfMonth); // Formato con ceros iniciales para mes y día

                        obtenerInformacionComidas(fechaSeleccionada, correo);
                        textFecha.setText(fecha);
                    }
                }, anio, mes, dia);
                dpd.show();
            }
        });

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mRootReference = database.getReference();

        // Obtener la referencia al nodo del usuario
        DatabaseReference usuarioRef = mRootReference.child("Usuario").child(correo);

        // Botón para ir a agregar una comida
        btnIragregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AgregarInfo.class);
                intent.putExtra("Correo", correo);
                startActivityForResult(intent, 1); // Usamos el código de solicitud 1

            }
        });


        usuarioRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Verificar si existe el nodo del usuario
                if (dataSnapshot.exists()) {
                    // Obtener el valor del nombre desde el dataSnapshot
                    String nombre = dataSnapshot.child("Nombre").getValue(String.class);

                    // Mostrar el nombre en el TextView correspondiente
                    textonombre = findViewById(R.id.NombreUsuario);
                    textonombre.setText(nombre);
                } else {
                    Toast.makeText(InformacionGeneral.this, "No se encontraron datos en la base de datos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Manejo de errores en caso de fallo en la lectura de la base de datos
                Toast.makeText(InformacionGeneral.this, "Error al leer los datos de la base de datos", Toast.LENGTH_SHORT).show();
            }
        });


        Date date = new Date();
        SimpleDateFormat fechaC = new SimpleDateFormat("yyyyMMdd");
        fechahoy = fechaC.format(date);



        obtenerInformacionComidas(fechahoy, correo);

        SimpleDateFormat fecha2 = new SimpleDateFormat("dd/MM/yyyy");
        String sfecha = fecha2.format(date);

        textFecha.setText(sfecha);



    }

    @Override
    protected void onResume() {
        super.onResume();

        // Actualizar la información cada vez que la actividad se vuelva visible
        obtenerInformacionComidas(fechahoy, correo);

        // Obtener el nombre del usuario y mostrarlo en el TextView correspondiente
        DatabaseReference usuarioRef = mRootReference.child("Usuario").child(correo);
        usuarioRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String nombre = dataSnapshot.child("Nombre").getValue(String.class);
                    textonombre.setText(nombre);
                } else {
                    Toast.makeText(InformacionGeneral.this, "No se encontraron datos en la base de datos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(InformacionGeneral.this, "Error al leer los datos de la base de datos", Toast.LENGTH_SHORT).show();
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

                    // Si no se encontraron datos en la base de datos, crear un ViewPagerItem con valores predeterminados en 0
                    ViewPagerItem desayuno = new ViewPagerItem("Desayuno", "0", "0", "0", "0", "0", "0", "0");
                    ViewPagerItem almuerzo = new ViewPagerItem("Almuerzo", "0", "0", "0", "0", "0", "0", "0");
                    ViewPagerItem cena = new ViewPagerItem("Cena", "0", "0", "0", "0", "0", "0", "0");

                    ArrayList<ViewPagerItem> viewPagerItems = new ArrayList<>();
                    viewPagerItems.add(desayuno);
                    viewPagerItems.add(almuerzo);
                    viewPagerItems.add(cena);

                    VPAdapter vpAdapter = new VPAdapter(viewPagerItems);
                    viewPager2.setAdapter(vpAdapter);

                    // Añade esta línea para notificar al adaptador que los datos han cambiado
                    vpAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Manejo de errores en caso de fallo en la lectura de la base de datos
            }
        });
    }

}
