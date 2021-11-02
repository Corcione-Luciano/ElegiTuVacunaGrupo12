package com.example.elegituvacuna.Models;

import android.content.Context;
import android.os.AsyncTask;
import com.example.elegituvacuna.Estructuras.EstructuraEvento;
import com.example.elegituvacuna.Estructuras.EstructuraToken;
import com.example.elegituvacuna.Presenters.MainPresenter;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.DataOutputStream;
import static com.example.elegituvacuna.Constantes.StaticConstantes.ENTORNO;
import static com.example.elegituvacuna.Constantes.StaticConstantes.ENTORNO_PROD;
import static com.example.elegituvacuna.Constantes.StaticConstantes.ENTORNO_TEST;
import static com.example.elegituvacuna.Constantes.StaticConstantes.REQUEST_POST;
import static com.example.elegituvacuna.Constantes.StaticConstantes.TIMEOUT_SOLICITUD;

public class AsyntaskRegistrarEvento extends AsyncTask<String, Void, String> {

    private Context context;
    private int CodigoRespuesta;
    private EstructuraToken estructuraToken;
    private MainPresenter presenter;
    private Exception Excepcion = null;
    private String msj_error;
    private EstructuraEvento estructuraEvento;

    public AsyntaskRegistrarEvento(Context context,EstructuraEvento estructuraEvento, EstructuraToken estructuraToken){
        this.estructuraEvento = estructuraEvento;
        this.estructuraToken = estructuraToken;
        this.context = context;
        presenter=new MainPresenter();
    }


    @Override
    protected String doInBackground(String... strings) {
        HttpURLConnection urlConnection = null;

        try {

            URL Uri = new URL(strings[0]);


            urlConnection = (HttpURLConnection) Uri.openConnection();
            urlConnection.setRequestProperty("Content-type", "application/json; charset = UTF-8");
            urlConnection.setRequestProperty("Authorization", "Bearer " + this.estructuraToken.getToken());
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setConnectTimeout(TIMEOUT_SOLICITUD);
            urlConnection.setRequestMethod(REQUEST_POST);

            JSONObject jsonObject = new JSONObject();

            jsonObject.put(ENTORNO, ENTORNO_PROD);
            jsonObject.put("type_events", this.estructuraEvento.getTipo());
            jsonObject.put("description", this.estructuraEvento.getDescripcion());

            DataOutputStream dataOutputStreamPost = new DataOutputStream(urlConnection.getOutputStream());
            dataOutputStreamPost.write((jsonObject.toString().getBytes("UTF-8")));
            dataOutputStreamPost.flush();
            dataOutputStreamPost.close();


            urlConnection.connect();

            CodigoRespuesta = urlConnection.getResponseCode();

        } catch (Exception e) {
            Excepcion = e;
        }

        try {
            if (!(CodigoRespuesta == HttpURLConnection.HTTP_OK
                    || CodigoRespuesta == HttpURLConnection.HTTP_CREATED)) {
                String resultResponse = InputStreamToString(new InputStreamReader(urlConnection.getErrorStream())).toString();
                JSONObject jsonResponse = new JSONObject(resultResponse);
                msj_error = jsonResponse.getString("msj");
                return "Error al registrar el evento";
            }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
        return "Evento Registrado";
    }

    public StringBuilder InputStreamToString(InputStreamReader inputStream) throws IOException {
        BufferedReader br = new BufferedReader(inputStream);
        StringBuilder result = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            result.append(line + "\n");
        }
        br.close();
        return result;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        presenter.respuestaDeRegistrarEvento(this.context,s);
    }
}
