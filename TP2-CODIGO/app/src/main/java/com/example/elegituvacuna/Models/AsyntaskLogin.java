package com.example.elegituvacuna.Models;


import android.content.Context;
import android.os.AsyncTask;
import com.example.elegituvacuna.Estructuras.EstructuraLogin;
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
import static com.example.elegituvacuna.Constantes.StaticConstantes.EMAIL;
import static com.example.elegituvacuna.Constantes.StaticConstantes.MSJ_LOGIN_CORRECTO;
import static com.example.elegituvacuna.Constantes.StaticConstantes.MSJ_LOGIN_INCORRECTO;
import static com.example.elegituvacuna.Constantes.StaticConstantes.PASSWORD;
import static com.example.elegituvacuna.Constantes.StaticConstantes.REQUEST_POST;
import static com.example.elegituvacuna.Constantes.StaticConstantes.TIMEOUT_SOLICITUD;

public class AsyntaskLogin extends AsyncTask<String, Void, String> {
    private int CodigoRespuesta;
    private Exception Excepcion = null;
    private Context context;
    private EstructuraToken estructuraToken;
    private String msj_error;
    private EstructuraLogin estructuraLogin;
    private MainPresenter presenter;



    public AsyntaskLogin(Context context,EstructuraLogin estructuraLogin) {
        this.context = context;
        this.estructuraLogin=estructuraLogin;
        this.estructuraToken= new EstructuraToken();
        presenter=new MainPresenter();
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
            urlConnection.setReadTimeout(TIMEOUT_SOLICITUD);
            urlConnection.setRequestMethod(REQUEST_POST);

            JSONObject jsonObject = new JSONObject();

            jsonObject.put(EMAIL, estructuraLogin.getEmail());
            jsonObject.put(PASSWORD, estructuraLogin.getPassword());

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
            if (CodigoRespuesta != HttpURLConnection.HTTP_OK) {
                String resultResponse = InputStreamToString(new InputStreamReader(urlConnection.getErrorStream())).toString();
                JSONObject jsonResponse = new JSONObject(resultResponse);
                msj_error = jsonResponse.getString("msg");
                return MSJ_LOGIN_INCORRECTO;
            }



            String resultResponse = InputStreamToString(new InputStreamReader(urlConnection.getInputStream())).toString();
            JSONObject jsonResponse = new JSONObject(resultResponse);
            this.estructuraToken.setExito(jsonResponse.getString("success"));
            this.estructuraToken.setToken(jsonResponse.getString("token"));
            this.estructuraToken.setTokenRefresh(jsonResponse.getString("token_refresh"));
            this.estructuraToken.setReciboToken(new Timestamp(System.currentTimeMillis()).getTime());

        } catch (JSONException | IOException e) {
            return MSJ_LOGIN_INCORRECTO;
        }


        return MSJ_LOGIN_CORRECTO;
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
        presenter.respuestaDeLogin(this.estructuraToken,this.context,s);

    }
}
