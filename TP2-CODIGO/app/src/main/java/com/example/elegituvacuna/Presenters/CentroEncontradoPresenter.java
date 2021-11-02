package com.example.elegituvacuna.Presenters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.view.Window;

import com.example.elegituvacuna.Estructuras.EstructuraCentros;

public class CentroEncontradoPresenter implements SensorEventListener {

    private SensorManager sensorManagerA;


    private Activity activity;
    private Window window;
    private EstructuraCentros centro;
    private int whip;
    private SharedPresenter shp;


    public CentroEncontradoPresenter(Activity activity, Window window, EstructuraCentros centro){
        this.activity=activity;
        this.window=window;
        this.centro=centro;
        this.whip = 0;
        shp = new SharedPresenter();
        shp.StartSharedVacuna(activity);
    }

    public void setupSensor(){
        sensorManagerA = (SensorManager) this.activity.getSystemService(Context.SENSOR_SERVICE);
    }

    public void listenerSensor(){
        Sensor sensorAcelerometro = sensorManagerA.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManagerA.registerListener(this,sensorAcelerometro,sensorManagerA.SENSOR_DELAY_FASTEST);
    }

    public void unlistenerSensor(){
        sensorManagerA.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];

        if(x<-5 && whip==0){
            whip++;
        }else if(x>5 && whip==1){
            whip++;
        }

        if(whip==2){
            Uri link = Uri.parse(centro.getWeb());
            shp.GuardarMetrica("contador_metricas_Usa_Shacke");
            Intent intent = new Intent(Intent.ACTION_VIEW, link);

            this.activity.startActivity(intent);
            whip=0;
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
