package com.example.elegituvacuna.Models;

import android.content.Context;
import android.os.AsyncTask;

import com.example.elegituvacuna.Estructuras.EstructuraLogin;
import com.example.elegituvacuna.Estructuras.EstructuraToken;
import com.example.elegituvacuna.Presenters.MainPresenter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;

import static com.example.elegituvacuna.Constantes.StaticConstantes.MSJ_ERROR_TOKEN;
import static com.example.elegituvacuna.Constantes.StaticConstantes.MSJ_TOKEN_ACTUALIZADO;
import static com.example.elegituvacuna.Constantes.StaticConstantes.REQUEST_PUT;
import static com.example.elegituvacuna.Constantes.StaticConstantes.TIMEOUT_SOLICITUD;

public class AsyntaskActualizarToken extends AsyncTask<String, Void, String> {
    private Context context;
    private int CodigoRespuesta;
    private EstructuraToken estructuraToken;
    private MainPresenter presenter;
    private Exception Excepcion = null;
    private String msj_error;

    public AsyntaskActualizarToken(Context context, EstructuraToken estructuraToken) {

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
            urlConnection.setRequestProperty("Authorization", "Bearer " + this.estructuraToken.getTokenRefresh());
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setConnectTimeout(TIMEOUT_SOLICITUD);
            urlConnection.setRequestMethod(REQUEST_PUT);


            urlConnection.connect();


            CodigoRespuesta = urlConnection.getResponseCode();

        } catch (Exception e) {
            Excepcion = e;
        }

        try {
            if (CodigoRespuesta != HttpURLConnection.HTTP_OK) {
                String resultResponse = InputStreamToString(new InputStreamReader(urlConnection.getErrorStream())).toString();
                JSONObject jsonResponse = new JSONObject(resultResponse);
                msj_error = jsonResponse.getString("msg");
                return MSJ_ERROR_TOKEN;
            }

            String resultResponse = InputStreamToString(new InputStreamReader(urlConnection.getInputStream())).toString();
            JSONObject jsonResponse = new JSONObject(resultResponse);
            this.estructuraToken.setReciboToken(new Timestamp(System.currentTimeMillis()).getTime());
            this.estructuraToken.setExito(jsonResponse.getString("success"));
            this.estructuraToken.setToken(jsonResponse.getString("token"));
            this.estructuraToken.setTokenRefresh(jsonResponse.getString("token_refresh"));
        } catch  (JSONException | IOException e)  {
            e.printStackTrace();
        }
        return MSJ_TOKEN_ACTUALIZADO;
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
        presenter.respuestaDeActualizarToken(this.estructuraToken,this.context,s);
    }
}
