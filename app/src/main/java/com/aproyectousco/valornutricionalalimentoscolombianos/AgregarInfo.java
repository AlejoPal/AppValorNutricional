package com.aproyectousco.valornutricionalalimentoscolombianos;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import java.util.ArrayList;
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

public class AgregarInfo extends AppCompatActivity {

    private AutoCompleteTextView autoCompleteTextView;
    private List<String> valuesList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_info);

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
                        // ...
                    });
                }
            }
        }.execute();
    }




}