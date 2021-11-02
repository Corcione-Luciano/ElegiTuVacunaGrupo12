package com.example.elegituvacuna.Models;
import android.content.Context;

import android.os.AsyncTask;

import com.example.elegituvacuna.Estructuras.EstructuraRegistro;
import com.example.elegituvacuna.Estructuras.EstructuraToken;
import com.example.elegituvacuna.Presenters.MainPresenter;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;


import static com.example.elegituvacuna.Constantes.StaticConstantes.ENTORNO;
import static com.example.elegituvacuna.Constantes.StaticConstantes.ENTORNO_PROD;
import static com.example.elegituvacuna.Constantes.StaticConstantes.MSJ_REGISTRO_CORRECTO;
import static com.example.elegituvacuna.Constantes.StaticConstantes.MSJ_REGISTRO_INCORRECTO;
import static com.example.elegituvacuna.Constantes.StaticConstantes.REQUEST_POST;
import static com.example.elegituvacuna.Constantes.StaticConstantes.TIMEOUT_SOLICITUD;
import static com.example.elegituvacuna.Constantes.StaticConstantes.APELLIDO;
import static com.example.elegituvacuna.Constantes.StaticConstantes.COMISION;
import static com.example.elegituvacuna.Constantes.StaticConstantes.DNI;
import static com.example.elegituvacuna.Constantes.StaticConstantes.EMAIL;
import static com.example.elegituvacuna.Constantes.StaticConstantes.GRUPO;
import static com.example.elegituvacuna.Constantes.StaticConstantes.NOMBRE;
import static com.example.elegituvacuna.Constantes.StaticConstantes.PASSWORD;


////////////////////////////


public class AsyntaskRegistro extends AsyncTask<String, Void, String> {


    private Exception excepcion = null;
    private Context context;
    private EstructuraToken estructuraToken;
    private EstructuraRegistro estructuraRegistro;
    private String msj_error;
    private int codigoRespuesta;
    private MainPresenter presenter;

    public AsyntaskRegistro(Context context,EstructuraRegistro estructuraRegistro) {
        this.context = context;
        this.estructuraRegistro= estructuraRegistro;
        this.estructuraToken= new EstructuraToken();
        presenter = new MainPresenter();
    }



    @Override
    protected String doInBackground(String... strings) {


        HttpURLConnection urlConnection = null;
        try {

            URL Uri = new URL(strings[0]);

            urlConnection = (HttpURLConnection) Uri.openConnection();
            urlConnection.setRequestProperty("Content-type", "application/json; charset = UTF-8");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setConnectTimeout(TIMEOUT_SOLICITUD);
            urlConnection.setRequestMethod(REQUEST_POST);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put(ENTORNO, ENTORNO_PROD);
            jsonObject.put(NOMBRE, this.estructuraRegistro.getName());
            jsonObject.put(APELLIDO, this.estructuraRegistro.getLastname());
            jsonObject.put(DNI, this.estructuraRegistro.getDni());
            jsonObject.put(EMAIL, this.estructuraRegistro.getEmail());
            jsonObject.put(PASSWORD, this.estructuraRegistro.getPassword());
            jsonObject.put(COMISION, this.estructuraRegistro.getCommission());
            jsonObject.put(GRUPO, this.estructuraRegistro.getGroup());

            DataOutputStream dataOutputStreamPost = new DataOutputStream(urlConnection.getOutputStream());
            dataOutputStreamPost.write((jsonObject.toString().getBytes("UTF-8")));
            dataOutputStreamPost.flush();
            dataOutputStreamPost.close();

            urlConnection.connect();

            codigoRespuesta = urlConnection.getResponseCode();

        } catch (Exception e) {
            excepcion = e;
        }

        try {
            if (codigoRespuesta != HttpURLConnection.HTTP_OK) {
                String resultResponse = convertInputStreamToString(new InputStreamReader(urlConnection.getErrorStream())).toString();
                JSONObject jsonResponse = new JSONObject(resultResponse);
                this.msj_error = jsonResponse.getString("msg");
                return MSJ_REGISTRO_INCORRECTO;
            }

            String resultResponse = convertInputStreamToString(new InputStreamReader(urlConnection.getInputStream())).toString();
            JSONObject jsonResponse = new JSONObject(resultResponse);
            this.estructuraToken.setReciboToken(new Timestamp(System.currentTimeMillis()).getTime());
            this.estructuraToken.setExito(jsonResponse.getString("success"));
            this.estructuraToken.setToken(jsonResponse.getString("token"));
            this.estructuraToken.setTokenRefresh(jsonResponse.getString("token_refresh"));
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
        return  MSJ_REGISTRO_CORRECTO;


    }

    @Override
    protected void onPostExecute(String result) {

        super.onPostExecute(result);

        presenter.respuestaDeRegistro(this.estructuraToken,this.context,result);



    }


    public StringBuilder convertInputStreamToString(InputStreamReader inputStream) throws IOException {
        BufferedReader br = new BufferedReader(inputStream);
        StringBuilder result = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            result.append(line + "\n");
        }
        br.close();
        return result;
    }
}
