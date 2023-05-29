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
        new AsyncTask<Void, Void, List<String>>() {
            @Override
            protected List<String> doInBackground(Void... voids) {
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

                    // Recorrer el arreglo de objetos nutricion y extraer los valores de cada objeto
                    List<String> valuesList = new ArrayList<>();
                    for (int i = 0; i < nutricionArray.length(); i++) {
                        JSONObject nutricionObj = nutricionArray.getJSONObject(i);
                        String nombre = nutricionObj.getString("Nombre");
                        valuesList.add(nombre);
                    }

                    return valuesList;

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(List<String> result) {
                if (result != null) {
                    valuesList = result;

                    // Configurar el ArrayAdapter con la lista de valores
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(AgregarInfo.this, android.R.layout.simple_dropdown_item_1line, valuesList);

                    // Asignar el adaptador al AutoCompleteTextView
                    autoCompleteTextView.setAdapter(adapter);
                }
            }
        }.execute();
    }


    /*
    private void obtenerDatosColumnaDesdeSheets() {
        // Aquí puedes implementar tu lógica para obtener los valores de la columna desde Google Sheets
        // Puedes usar una biblioteca como Sheets API de Google o HTTP Request para realizar la consulta a Google Sheets y obtener los valores

        // En este ejemplo, se agrega manualmente una lista de valores de ejemplo
        valuesList = new ArrayList<>();
        valuesList.add("Valor 1");
        valuesList.add("Valor 2");
        valuesList.add("Valor 3");
        // ...

        // Aquí puedes realizar la consulta a Google Sheets y obtener los valores de la columna deseada
        // Puedes almacenar los valores en la lista valuesList
    }


    private void obtenerDatosColumnaDesdeSheets() throws GeneralSecurityException, IOException {



        // Crea una instancia de Sheets utilizando las credenciales previamente configuradas
        Sheets sheets = new Sheets.Builder(GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(), credential)
                .setApplicationName("Tu aplicación")
                .build();

        // ID de la hoja de cálculo que deseas consultar
        String spreadsheetId = "1bGG7LkQRdh73Y81ZlZ8_Yusb4xuutc3cf_HeJEmkoFc";

        // Rango de la columna que deseas obtener (por ejemplo, "Sheet1!A:A" para obtener todos los valores de la columna A en la hoja Sheet1)
        String range = "nutricion!A:Z";

        try {
            // Realiza la solicitud para obtener los datos de la columna en el rango especificado
            ValueRange response = sheets.spreadsheets().values().get(spreadsheetId, range).execute();

            // Obtiene los valores de la columna en forma de lista
            List<List<Object>> values = response.getValues();

            if (values != null && values.size() > 0) {
                for (List<Object> row : values) {
                    // Accede al valor de la celda en la columna y agrega el valor a la lista de sugerencias
                    String cellValue = row.get(0).toString();
                    suggestionsList.add(cellValue);
                }
            } else {
                // No se encontraron datos en el rango especificado
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    */

}