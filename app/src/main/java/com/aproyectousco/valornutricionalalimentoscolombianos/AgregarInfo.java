package com.aproyectousco.valornutricionalalimentoscolombianos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class AgregarInfo extends AppCompatActivity {

    TextView verificar;
    private AutoCompleteTextView autoCompleteTextView;
    private List<String> valuesList;

    private FirebaseAuth mAuth;
    DatabaseReference mRootReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_info);

        mAuth = FirebaseAuth.getInstance();
        mRootReference = FirebaseDatabase.getInstance().getReference();

        // Obtén una referencia al AutoCompleteTextView desde el layout
        autoCompleteTextView = findViewById(R.id.txtBusqueda);


        // Obtiene los valores de la columna desde Google Sheets
        obtenerDatosColumnaDesdeSheets();

        // Configura el ArrayAdapter con la lista de valores
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, valuesList);

        // Asigna el adaptador al AutoCompleteTextView
        autoCompleteTextView.setAdapter(adapter);



    }






    @SuppressLint("StaticFieldLeak")
    private void obtenerDatosColumnaDesdeSheets() {

        Button desayunoButton = findViewById(R.id.btnAgregarDesayuno);
        Button almuerzoButton = findViewById(R.id.btnAgregarAlmuerzo);
        Button cenaButton = findViewById(R.id.btnAgregarCena);

        new AsyncTask<Void, Void, List<Alimento>>() {
            @Override
            protected List<Alimento> doInBackground(Void... voids) {
                // URL de la API con el spreadsheetId y sheet
                String apiUrl = "https://script.google.com/macros/s/AKfycbyDpgPAZrRnAlK-HnO0mRbjxo8BscDDX1td3Z9tyUpkl-9ZMdlwGd7PmCLrtMBt7o2k/exec?spreadsheetId=1bGG7LkQRdh73Y81ZlZ8_Yusb4xuutc3cf_HeJEmkoFc&sheet=nutricion";

                try {
                    // Realizar solicitud HTTP GET a la API
                    URL url = new URL(apiUrl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.connect();

                    // Leer la respuesta de la API
                    InputStream inputStream = connection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        response.append(line);
                    }

                    // Analizar la respuesta JSON
                    JSONObject jsonResponse = new JSONObject(response.toString());
                    JSONArray nutricionArray = jsonResponse.getJSONArray("nutricion");

                    // Recorrer el arreglo de objetos nutricion y crear instancias de Alimento
                    List<Alimento> alimentosList = new ArrayList<>();
                    for (int i = 0; i < nutricionArray.length(); i++) {
                        JSONObject nutricionObj = nutricionArray.getJSONObject(i);
                        String nombre = nutricionObj.getString("Nombre");
                        String energia = nutricionObj.getString("Energia");
                        String proteina = nutricionObj.getString("Proteina");
                        String carbohidratos = nutricionObj.getString("Carbohidratos");
                        String colesterol = nutricionObj.getString("Colesterol");
                        String lipidos = nutricionObj.getString("Lipidos");
                        String gsat = nutricionObj.getString("Gsat");
                        String sodio = nutricionObj.getString("Sodio");

                        // Crear una instancia de Alimento y asignar los valores
                        Alimento alimento = new Alimento();
                        alimento.setNombre(nombre);
                        alimento.setEnergia(energia);
                        alimento.setProteina(proteina);
                        alimento.setCarbohidratos(carbohidratos);
                        alimento.setColesterol(colesterol);
                        alimento.setLipidos(lipidos);
                        alimento.setGsat(gsat);
                        alimento.setSodio(sodio);

                        alimentosList.add(alimento);
                    }

                    return alimentosList;

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(List<Alimento> result) {
                if (result != null) {
                    // Configurar el ArrayAdapter con la lista de valores (nombres de los alimentos)
                    valuesList = new ArrayList<>();
                    for (Alimento alimento : result) {
                        valuesList.add(alimento.getNombre());
                    }

                    // Configurar el ArrayAdapter con la lista de alimentos
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(AgregarInfo.this, android.R.layout.simple_dropdown_item_1line, valuesList);

                    // Asignar el adaptador al AutoCompleteTextView
                    autoCompleteTextView.setAdapter(adapter);

                    // Manejar la selección del AutoCompleteTextView
                    autoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> {
                        // Obtener el alimento seleccionado
                        Alimento alimentoSeleccionado = result.get(position);

                        // Aquí puedes acceder a los demás atributos del alimento y hacer lo que necesites
                        String nombre = alimentoSeleccionado.getNombre();
                        String energia = alimentoSeleccionado.getEnergia();
                        String proteina = alimentoSeleccionado.getProteina();
                        String carbohidratos = alimentoSeleccionado.getCarbohidratos();
                        String colesterol = alimentoSeleccionado.getColesterol();
                        String lipidos = alimentoSeleccionado.getLipidos();
                        String gsat = alimentoSeleccionado.getGsat();
                        String sodio = alimentoSeleccionado.getSodio();
                        // Hacer algo con los datos obtenidos
/*
                        // Conseguir fecha y correo
                        Date date = new Date();
                        SimpleDateFormat fechaC = new SimpleDateFormat("yyyyMMdd");
                        String sfecha = fechaC.format(date);
                        Intent intent = getIntent();
                        String correo = intent.getStringExtra("Correo");

                        // Obtener la referencia al nodo del usuario
                        DatabaseReference databaseRef = mRootReference.child("Usuario").child(correo).child(sfecha).child("Desayuno");
                        Log.d("AgregarInfo", "Referencia de la base de datos: " + databaseRef.toString());

                        // Crear un mapa con los datos del alimento
                        Map<String, Object> alimentoMap = new HashMap<>();
                        alimentoMap.put("energia", energia);
                        alimentoMap.put("proteina", proteina);
                        alimentoMap.put("carbohidratos", carbohidratos);
                        alimentoMap.put("colesterol", colesterol);
                        alimentoMap.put("lipidos", lipidos);
                        alimentoMap.put("gsat", gsat);
                        alimentoMap.put("sodio", sodio);

                        // Guardar los datos en la base de datos
                        databaseRef.setValue(alimentoMap)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            // Los datos se guardaron exitosamente
                                            Toast.makeText(AgregarInfo.this, "Datos guardados en Firebase", Toast.LENGTH_SHORT).show();
                                        } else {
                                            // Se produjo un error al guardar los datos
                                            Toast.makeText(AgregarInfo.this, "Error al guardar los datos en Firebase", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                        */
                        // Conseguir fecha y correo
                        Date date = new Date();
                        SimpleDateFormat fechaC = new SimpleDateFormat("yyyyMMdd");
                        String fechaHoy= fechaC.format(date);

                        //Se recupera el correo de inicio de sesion o de registrar
                        Intent intent = getIntent();
                        String Correo = intent.getStringExtra("Correo");


                        desayunoButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                cargarDatosFirebaseI(fechaHoy, Correo, "Desayuno", Double.parseDouble(proteina), Double.parseDouble(energia), Double.parseDouble(carbohidratos), Double.parseDouble(lipidos), Double.parseDouble(sodio), Double.parseDouble(gsat), Double.parseDouble(colesterol));
                                Toast.makeText(AgregarInfo.this, "Desayuno agregado", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(AgregarInfo.this, InformacionGeneral.class);
                                startActivity(intent);
                            }
                        });

                        almuerzoButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                cargarDatosFirebaseI(fechaHoy, Correo, "Almuerzo", Double.parseDouble(proteina), Double.parseDouble(energia), Double.parseDouble(carbohidratos), Double.parseDouble(lipidos), Double.parseDouble(sodio), Double.parseDouble(gsat), Double.parseDouble(colesterol));
                                Toast.makeText(AgregarInfo.this, "Almuerzo agregado", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(AgregarInfo.this, InformacionGeneral.class);
                                startActivity(intent);
                            }
                        });

                        cenaButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                cargarDatosFirebaseI(fechaHoy, Correo, "Cena", Double.parseDouble(proteina), Double.parseDouble(energia), Double.parseDouble(carbohidratos), Double.parseDouble(lipidos), Double.parseDouble(sodio), Double.parseDouble(gsat), Double.parseDouble(colesterol));
                                Toast.makeText(AgregarInfo.this, "Cena agregada", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(AgregarInfo.this, InformacionGeneral.class);
                                startActivity(intent);
                            }
                        });
                    });



                }
            }

        }.execute();
    }

    private void cargarDatosFirebaseI(String fecha, String correo, String comida, double proteina, double energia, double carbohidratos, double lipidos, double sales, double gsat, double colesterol) {
        try {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference mRootReference = database.getReference();
            // Obtener la referencia al nodo del usuario
            DatabaseReference fechaRef = mRootReference.child("Usuario").child(correo).child(fecha).child(comida);

            fechaRef.addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        fechaRef.setValue(infoAlimenticia(proteina, energia, carbohidratos, lipidos, sales, gsat, colesterol));
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