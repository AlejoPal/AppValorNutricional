package com.aproyectousco.valornutricionalalimentoscolombianos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class InformacionGeneral extends AppCompatActivity {

    ViewPager2 viewPager2;
    private ArrayList<ViewPagerItem> viewPagerItemsList; // Agrega una lista de ViewPagerItem


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

        viewPagerItemsList = new ArrayList<>(); // Inicializa la lista de ViewPagerItem


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
        // Crear una lista vacía para cada comida
        List<ViewPagerItem> listaDesayuno = new ArrayList<>();
        List<ViewPagerItem> listaAlmuerzo = new ArrayList<>();
        List<ViewPagerItem> listaCena = new ArrayList<>();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mRootReference = database.getReference();

        // Obtener la referencia al nodo del usuario
        DatabaseReference databaseRef = mRootReference.child("Usuario").child(correo).child(fechaSeleccionada);

        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Recorrer las comidas del día
                    for (DataSnapshot comidaSnapshot : dataSnapshot.getChildren()) {
                        String comida = comidaSnapshot.getKey();

                        // Obtener la información de la comida actual
                        List<TablaItem> alimentosList = new ArrayList<>();
                        Double carbohidratos = comidaSnapshot.child("Carbohidratos").getValue(Double.class);
                        Double colesterol = comidaSnapshot.child("Colesterol").getValue(Double.class);
                        Double energia = comidaSnapshot.child("Energia").getValue(Double.class);
                        Double gsat = comidaSnapshot.child("Gsat").getValue(Double.class);
                        Double lipidos = comidaSnapshot.child("Lipidos").getValue(Double.class);
                        Double proteinas = comidaSnapshot.child("Proteina").getValue(Double.class);
                        Double sodio = comidaSnapshot.child("Sodio").getValue(Double.class);

                        // Formatea los valores con un máximo de dos decimales
                        String carbohidratosFormatted = String.format("%.2f", carbohidratos);
                        String colesterolFormatted = String.format("%.2f", colesterol);
                        String energiaFormatted = String.format("%.2f", energia);
                        String gsatFormatted = String.format("%.2f", gsat);
                        String lipidosFormatted = String.format("%.2f", lipidos);
                        String proteinasFormatted = String.format("%.2f", proteinas);
                        String sodioFormatted = String.format("%.2f", sodio);

                        // Crea un nuevo objeto TablaItem usando los valores formateados
                        TablaItem tablaItemR = new TablaItem(fechaSeleccionada,correo,"Resumen", "Resumen", carbohidratosFormatted, colesterolFormatted, energiaFormatted, gsatFormatted, lipidosFormatted, proteinasFormatted, sodioFormatted);
                        alimentosList.add(tablaItemR);


                        // Recorrer los alimentos de esta comida y agregarlos a la lista correspondiente
                        DataSnapshot alimentosSnapshot = comidaSnapshot.child("Alimentos");
                        for (DataSnapshot alimentoSnapshot : alimentosSnapshot.getChildren()) {
                            String alimento = alimentoSnapshot.getKey();
                            Double carbohidratosA = alimentoSnapshot.child("Carbohidratos").getValue(Double.class);
                            Double colesterolA = alimentoSnapshot.child("Colesterol").getValue(Double.class);
                            Double energiaA = alimentoSnapshot.child("Energia").getValue(Double.class);
                            Double gsatA = alimentoSnapshot.child("Gsat").getValue(Double.class);
                            Double lipidosA = alimentoSnapshot.child("Lipidos").getValue(Double.class);
                            Double proteinasA = alimentoSnapshot.child("Proteina").getValue(Double.class);
                            Double sodioA = alimentoSnapshot.child("Sodio").getValue(Double.class);

                            // Formatea los valores con un máximo de dos decimales
                            String carbohidratosFormatted1 = String.format("%.2f", carbohidratosA);
                            String colesterolFormatted1 = String.format("%.2f", colesterolA);
                            String energiaFormatted1 = String.format("%.2f", energiaA);
                            String gsatFormatted1 = String.format("%.2f", gsatA);
                            String lipidosFormatted1 = String.format("%.2f", lipidosA);
                            String proteinasFormatted1 = String.format("%.2f", proteinasA);
                            String sodioFormatted1 = String.format("%.2f", sodioA);

                            // Crea un objeto TablaItem con los valores formateados
                            TablaItem tablaItem = new TablaItem(fechaSeleccionada,correo,comida,alimento, carbohidratosFormatted1, colesterolFormatted1, energiaFormatted1, gsatFormatted1, lipidosFormatted1, proteinasFormatted1, sodioFormatted1);
                            alimentosList.add(tablaItem);
                        }

                        // Crear el objeto ViewPagerItem correspondiente a esta comida
                        ViewPagerItem viewPagerItem = new ViewPagerItem(alimentosList, comida);

                        // Agregar el objeto ViewPagerItem a la lista correspondiente
                        if (comida.equals("Desayuno")) {
                            listaDesayuno.add(viewPagerItem);
                        } else if (comida.equals("Almuerzo")) {
                            listaAlmuerzo.add(viewPagerItem);
                        } else if (comida.equals("Cena")) {
                            listaCena.add(viewPagerItem);
                        }
                    }

                    // Crear la lista final de objetos ViewPagerItem para cada comida
                    ArrayList<ViewPagerItem> viewPagerItems = new ArrayList<>();
                    viewPagerItems.addAll(listaDesayuno);
                    viewPagerItems.addAll(listaAlmuerzo);
                    viewPagerItems.addAll(listaCena);

                    // Configurar el adaptador del ViewPager2
                    VPAdapter vpAdapter = new VPAdapter(viewPagerItems);
                    viewPager2.setAdapter(vpAdapter);
                    // Notificar al adaptador que los datos han cambiado
                    vpAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(InformacionGeneral.this, "No se encontraron datos en la base de datos", Toast.LENGTH_SHORT).show();
                    // Si no se encontraron datos en la base de datos, crear ViewPagerItem con valores predeterminados en 0
                    // (esto ya se hace dentro del adaptador, por lo que no es necesario repetirlo aquí)
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Manejo de errores en caso de fallo en la lectura de la base de datos
            }
        });
    }


}
