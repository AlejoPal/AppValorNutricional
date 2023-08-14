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
    private TextView peso;
    private List<String> valuesList;

    private FirebaseAuth mAuth;
    DatabaseReference mRootReference;


    Map<String, Object> MapaAlimentos = new HashMap<>();
    final double[] energia = {0.0};
    final double[] proteina = {0.0};
    final double[] carbohidratos = {0.0};
    final double[] colesterol = {0.0};
    final double[] lipidos = {0.0};
    final double[] gsat = {0.0};
    final double[] sodio = {0.0};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_info);

        mAuth = FirebaseAuth.getInstance();
        mRootReference = FirebaseDatabase.getInstance().getReference();

        // Obtén una referencia al AutoCompleteTextView desde el layout
        autoCompleteTextView = findViewById(R.id.txtBusqueda);
        peso = findViewById(R.id.txtpeso);


        // Obtiene los valores de la columna desde Google Sheets
        obtenerDatosColumnaDesdeSheets();

        // Configura el ArrayAdapter con la lista de valores
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, valuesList);

        // Asigna el adaptador al AutoCompleteTextView
        autoCompleteTextView.setAdapter(adapter);



    }






    @SuppressLint("StaticFieldLeak")
    private void obtenerDatosColumnaDesdeSheets() {

        Button agregarButton = findViewById(R.id.btnAgregar);
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

                        Log.d("CLASE", "Nombre: " + nombre);
                        Log.d("CLASE", "Energia: " + energia);
                        Log.d("CLASE", "Proteina: " + proteina);
                        Log.d("CLASE", "Carbohidratos: " + carbohidratos);
                        Log.d("CLASE", "Colesterol: " + colesterol);
                        Log.d("CLASE", "Lipidos: " + lipidos);
                        Log.d("CLASE", "Gsat: " + gsat);
                        Log.d("CLASE", "Sodio: " + sodio);

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
                        String Venergia = alimentoSeleccionado.getEnergia();
                        String Vproteina = alimentoSeleccionado.getProteina();
                        String Vcarbohidratos = alimentoSeleccionado.getCarbohidratos();
                        String Vcolesterol = alimentoSeleccionado.getColesterol();
                        String Vlipidos = alimentoSeleccionado.getLipidos();
                        String Vgsat = alimentoSeleccionado.getGsat();
                        String Vsodio = alimentoSeleccionado.getSodio();

                        Log.d("INFOALIMENTOS", "Nombre: " + nombre);
                        Log.d("INFOALIMENTOS", "Energia: " + Venergia);
                        Log.d("INFOALIMENTOS", "Proteina: " + Vproteina);
                        Log.d("INFOALIMENTOS", "Carbohidratos: " + Vcarbohidratos);
                        Log.d("INFOALIMENTOS", "Colesterol: " + Vcolesterol);
                        Log.d("INFOALIMENTOS", "Lipidos: " + Vlipidos);
                        Log.d("INFOALIMENTOS", "Gsat: " + Vgsat);
                        Log.d("INFOALIMENTOS", "Sodio: " + Vsodio);


                        // Hacer algo con los datos obtenidos
                        Log.d("INFOALIMENTOS", "Nombre: " + nombre);
                        Log.d("INFOALIMENTOS", "Energia: " + energia);
                        Log.d("INFOALIMENTOS", "Proteina: " + proteina);
                        Log.d("INFOALIMENTOS", "Carbohidratos: " + carbohidratos);
                        Log.d("INFOALIMENTOS", "Colesterol: " + colesterol);
                        Log.d("INFOALIMENTOS", "Lipidos: " + lipidos);
                        Log.d("INFOALIMENTOS", "Gsat: " + gsat);
                        Log.d("INFOALIMENTOS", "Sodio: " + sodio);
                        // Conseguir fecha y correo
                        Date date = new Date();
                        SimpleDateFormat fechaC = new SimpleDateFormat("yyyyMMdd");
                        String fechaHoy= fechaC.format(date);

                        //Se recupera el correo de inicio de sesion o de registrar
                        Intent intent = getIntent();
                        String Correo = intent.getStringExtra("Correo");


                        agregarButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                String pesoText = peso.getText().toString().trim();
                                String autoCompleteText = autoCompleteTextView.getText().toString().trim();

                                // Verificar que ambos campos tengan valores
                                if (pesoText.isEmpty() || autoCompleteText.isEmpty()) {
                                    Toast.makeText(AgregarInfo.this, "Por favor ingresa el peso y selecciona un alimento", Toast.LENGTH_SHORT).show();
                                    return; // Salir del método si no se cumplen los requisitos
                                }

                                double pesoValue = Double.parseDouble(peso.getText().toString())/100;

                                // Convertir los valores de tipo String a double
                                double energiaValue = Double.parseDouble(Venergia) * pesoValue;
                                double proteinaValue = Double.parseDouble(Vproteina) * pesoValue;
                                double carbohidratosValue = Double.parseDouble(Vcarbohidratos) * pesoValue;
                                double colesterolValue = Double.parseDouble(Vcolesterol) * pesoValue;
                                double lipidosValue = Double.parseDouble(Vlipidos) * pesoValue;
                                double gsatValue = Double.parseDouble(Vgsat) * pesoValue;
                                double sodioValue = Double.parseDouble(Vsodio) * pesoValue;

                                MapaAlimentos.put(nombre, infoAlimenticiaIndividual(energiaValue,proteinaValue,carbohidratosValue,colesterolValue,lipidosValue,gsatValue,sodioValue));

                                // Sumar los valores convertidos a las variables
                                energia[0] += energiaValue;
                                proteina[0] += proteinaValue;
                                carbohidratos[0] += carbohidratosValue;
                                colesterol[0] += colesterolValue;
                                lipidos[0] += lipidosValue;
                                gsat[0] += gsatValue;
                                sodio[0] += sodioValue;

                                Toast.makeText(AgregarInfo.this, "Informacion Guardada", Toast.LENGTH_SHORT).show();
                                autoCompleteTextView.setText("");  // Borra el valor ingresado
                                autoCompleteTextView.setHint("Buscar");  // Restaura el hint
                                peso.setText("");
                                peso.setHint("Peso(gr)");

                            }

                        });
                        desayunoButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                cargarDatosFirebaseI(fechaHoy, Correo, "Desayuno",MapaAlimentos, proteina[0], energia[0], carbohidratos[0], lipidos[0], sodio[0], gsat[0], colesterol[0]);
                                Toast.makeText(AgregarInfo.this, "Desayuno agregado", Toast.LENGTH_SHORT).show();
                                volverAInformacionGeneral(Correo);
                            }
                        });

                        almuerzoButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                cargarDatosFirebaseI(fechaHoy, Correo, "Almuerzo",MapaAlimentos, proteina[0], energia[0], carbohidratos[0], lipidos[0], sodio[0], gsat[0], colesterol[0]);
                                Toast.makeText(AgregarInfo.this, "Almuerzo agregado", Toast.LENGTH_SHORT).show();
                                volverAInformacionGeneral(Correo);
                                }
                        });

                        cenaButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                cargarDatosFirebaseI(fechaHoy, Correo, "Cena", MapaAlimentos, proteina[0], energia[0], carbohidratos[0], lipidos[0], sodio[0], gsat[0], colesterol[0]);
                                Toast.makeText(AgregarInfo.this, "Cena agregada", Toast.LENGTH_SHORT).show();
                                volverAInformacionGeneral(Correo);
                                }
                        });
                    });



                }
            }

        }.execute();
    }

    private void cargarDatosFirebaseI(String fecha, String correo, String comida ,Map<String, Object> alimentos, double proteina, double energia, double carbohidratos, double lipidos, double sales, double gsat, double colesterol) {
        try {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference mRootReference = database.getReference();

            // Obtener la referencia al nodo del usuario
            DatabaseReference fechaRef = mRootReference.child("Usuario").child(correo).child(fecha).child(comida);

            fechaRef.setValue(infoAlimenticia(alimentos, proteina, energia, carbohidratos, lipidos, sales, gsat, colesterol))
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                // Los datos se guardaron exitosamente
                                Toast.makeText(AgregarInfo.this, "Datos guardados en Firebase", Toast.LENGTH_SHORT).show();
                                //volverAInformacionGeneral(correo);
                            } else {
                                // Se produjo un error al guardar los datos
                                Toast.makeText(AgregarInfo.this, "Error al guardar los datos en Firebase", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } catch (Exception e) {
            Log.e("TAG", "Ocurrió un error: " + e.getMessage());
            // Manejar el error de alguna manera adecuada
        }
    }



    private Object infoAlimenticia(Map<String, Object> alimentos,double proteina, double energia, double carbohidratos, double lipidos, double sales, double gsat, double colesterol){
        Map<String, Object> fechas = new HashMap<>();
        fechas.put("Alimentos", alimentos);
        fechas.put("Proteina", proteina);
        fechas.put("Energia", energia);
        fechas.put("Carbohidratos", carbohidratos);
        fechas.put("Lipidos", lipidos);
        fechas.put("Sodio", sales);
        fechas.put("Gsat", gsat);
        fechas.put("Colesterol", colesterol);
        return fechas;
    }

    private void volverAInformacionGeneral(String Correo) {
        Intent intent = new Intent(AgregarInfo.this, InformacionGeneral.class);
        intent.putExtra("Correo", Correo);
        setResult(RESULT_OK, intent); // Establecemos el resultado como RESULT_OK

        finish();
    }


    private Object infoAlimenticiaIndividual(double proteina, double energia, double carbohidratos, double lipidos, double sales, double gsat, double colesterol){
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