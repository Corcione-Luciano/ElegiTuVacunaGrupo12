package com.example.elegituvacuna.Presenters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.view.Window;
import android.widget.Toast;

import com.example.elegituvacuna.Estructuras.EstructuraEvento;
import com.example.elegituvacuna.Estructuras.EstructuraRegistro;
import com.example.elegituvacuna.Estructuras.EstructuraLogin;
import com.example.elegituvacuna.Estructuras.EstructuraToken;
import com.example.elegituvacuna.Models.AsyntaskActualizarToken;
import com.example.elegituvacuna.Models.AsyntaskLogin;
import com.example.elegituvacuna.Models.AsyntaskRegistrarEvento;
import com.example.elegituvacuna.Models.AsyntaskRegistro;
import com.example.elegituvacuna.Views.Activity_Busqueda;
import com.example.elegituvacuna.Views.Activity_CentroEncontrado;
import com.example.elegituvacuna.Views.Activity_CentroSeleccionado;
import com.example.elegituvacuna.Views.Activity_Desbloqueo;
import com.example.elegituvacuna.Views.Activity_registrarse;

import java.sql.Timestamp;

import static com.example.elegituvacuna.Constantes.StaticConstantes.ACTUALIZAR_TOKEN_Y_REGISTRAR_EVENTO;
import static com.example.elegituvacuna.Constantes.StaticConstantes.COLOR_CON_LUZ;
import static com.example.elegituvacuna.Constantes.StaticConstantes.COLOR_POCA_LUZ;
import static com.example.elegituvacuna.Constantes.StaticConstantes.MSJ_LOGIN_CORRECTO;
import static com.example.elegituvacuna.Constantes.StaticConstantes.MSJ_NO_INTERNET;
import static com.example.elegituvacuna.Constantes.StaticConstantes.MSJ_REGISTRO_CORRECTO;
import static com.example.elegituvacuna.Constantes.StaticConstantes.MSJ_TOKEN_ACTUALIZADO;
import static com.example.elegituvacuna.Constantes.StaticConstantes.URL_ACTUALIZAR_TOKEN;
import static com.example.elegituvacuna.Constantes.StaticConstantes.URL_EVENTOS;
import static com.example.elegituvacuna.Constantes.StaticConstantes.URL_LOGIN;
import static com.example.elegituvacuna.Constantes.StaticConstantes.VALOR_LUZ_CAMBIO_FONDO;
import static com.example.elegituvacuna.Constantes.StaticConstantes.VENCIMIENTO_TOKEN;
import static com.example.elegituvacuna.Constantes.StaticConstantes.VOLVER_A_LOGUEARSE;

public class MainPresenter implements SensorEventListener {


    private Activity activity;
    private SensorManager sensorManagerL;
    float valor_sensor;
    private Window window;
    private EstructuraLogin estructuraLogin;
    private EstructuraRegistro estructuraRegistro;
    private EstructuraEvento estructuraEvento;
    private EstructuraToken estructuraToken;
    private Context context;
    private AsyntaskLogin asyntaskLogin;
    private AsyntaskRegistro asyntaskRegistro;
    private AsyntaskRegistrarEvento asyntaskRegistrarEvento;
    private AsyntaskActualizarToken asyntaskActualizarToken;
    private boolean cambio=false;


    //private Sensor sensorLuz;
    public MainPresenter(){
    }
    public MainPresenter(Activity activity,Window window){
        this.activity=activity;
        this.window=window;
    }

    public void setupSensor(){
        sensorManagerL = (SensorManager) this.activity.getSystemService(Context.SENSOR_SERVICE);
    }



    public void listenerSensor(){
        Sensor sensorLuz = sensorManagerL.getDefaultSensor(Sensor.TYPE_LIGHT);
        sensorManagerL.registerListener(this,sensorLuz,sensorManagerL.SENSOR_DELAY_FASTEST);
    }
    public void unlistenerSensor(){
        sensorManagerL.unregisterListener(this);
    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        valor_sensor = sensorEvent.values[0];

        if (valor_sensor > VALOR_LUZ_CAMBIO_FONDO) {
            CambiarColorDePantalla(COLOR_CON_LUZ);
            if(cambio==false){
                cambio=true;

            }

        }else{
            CambiarColorDePantalla(COLOR_POCA_LUZ);
            if(cambio==true){
                cambio=false;

            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public void ConectarLogin(EstructuraLogin login, Context context){
            this.estructuraLogin=login;
            this.context=context;
            asyntaskLogin = new AsyntaskLogin(this.context, this.estructuraLogin);
            asyntaskLogin.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,URL_LOGIN);


    }

    public void respuestaDeLogin(EstructuraToken token, Context context, String respuesta) {
        if (respuesta == MSJ_LOGIN_CORRECTO) {

            estructuraEvento=new EstructuraEvento();
            estructuraEvento.setTipo("Login");
            estructuraEvento.setDescripcion("Se loggeo");
            estructuraToken=token;
            RegistrarEvento(estructuraEvento,token,context);
            ((Activity_Desbloqueo)context).CambiarPantalla(token,respuesta);

            return;
        }else{
            Toast.makeText(context, respuesta, Toast.LENGTH_SHORT).show();
        }

    }



    public void RegistrarUsuario(EstructuraRegistro registro, Context context){
        this.estructuraRegistro=registro;
        this.context=context;
        asyntaskRegistro = new AsyntaskRegistro(this.context,this.estructuraRegistro);
        asyntaskRegistro.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "http://so-unlam.net.ar/api/api/register");
    }

    public void respuestaDeRegistro(EstructuraToken token, Context context, String respuesta) {
        if (respuesta == MSJ_REGISTRO_CORRECTO) {
            ((Activity_registrarse)context).CambiarPantalla(token,respuesta);

            return;
        }else{
            Toast.makeText(context, respuesta, Toast.LENGTH_SHORT).show();
        }



    }



    public void RegistrarEvento(EstructuraEvento evento,EstructuraToken token , Context context){
        this.estructuraEvento=evento;
        this.estructuraToken=token;
        this.context=context;
        asyntaskRegistrarEvento = new AsyntaskRegistrarEvento(this.context, this.estructuraEvento, this.estructuraToken);
        asyntaskRegistrarEvento.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,URL_EVENTOS);
    }

    public void respuestaDeRegistrarEvento(Context context, String respuesta) {
         Toast.makeText(context, respuesta, Toast.LENGTH_SHORT).show();
    }


    public void ActualizarToken(EstructuraToken token, Context context){
        this.estructuraToken=token;
        this.context=context;
        asyntaskActualizarToken = new AsyntaskActualizarToken(this.context, this.estructuraToken);
        asyntaskActualizarToken.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,URL_ACTUALIZAR_TOKEN);
    }

    public void respuestaDeActualizarToken(EstructuraToken token, Context context, String respuesta) {
        if (respuesta == MSJ_TOKEN_ACTUALIZADO) {
            if(context.getClass().equals(Activity_Busqueda.class))
                ((Activity_Busqueda)context).CambiarPantalla(token,respuesta);
            if(context.getClass().equals(Activity_CentroEncontrado.class))
                ((Activity_CentroEncontrado)context).CambiarPantalla(token,respuesta);
            if(context.getClass().equals(Activity_CentroSeleccionado.class))
                ((Activity_CentroSeleccionado)context).CambiarPantalla(token,respuesta);
            return;
        }else{
            Toast.makeText(context, respuesta, Toast.LENGTH_SHORT).show();
        }

    }


    public String compararToken(EstructuraToken token) {
        long tiempoActual = new Timestamp(System.currentTimeMillis()).getTime();
        if (tiempoActual < (token.getReciboToken() + VENCIMIENTO_TOKEN)) {
            return ACTUALIZAR_TOKEN_Y_REGISTRAR_EVENTO;
        } else {
            //Perdiste el registrar evento por inactividad, volvete a loguear.
            return VOLVER_A_LOGUEARSE;
        }
    }


    public String checkarConexion(Context contexto) {
        ConnectivityManager cm = (ConnectivityManager) contexto.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo n = cm.getActiveNetworkInfo();
        if (n == null || !n.isAvailable() || !n.isConnected())
            return MSJ_NO_INTERNET;
        return null;
    }






    private void CambiarColorDePantalla(String colorBack){
        //modifica el color de fondo
        this.window.setBackgroundDrawable(new ColorDrawable(Color.parseColor(colorBack)));
    }




}
