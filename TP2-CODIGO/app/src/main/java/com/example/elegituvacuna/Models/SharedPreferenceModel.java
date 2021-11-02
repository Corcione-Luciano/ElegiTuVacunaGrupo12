package com.example.elegituvacuna.Models;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceModel {

    private Activity activity;
    private SharedPreferences.Editor editor;
    private SharedPreferences sharedPreferences;

    public SharedPreferenceModel(Activity activity){
        this.activity = activity;
    }

    public void IniciarSharedVacuna(){

        sharedPreferences = this.activity.getSharedPreferences("preferencias",Context.MODE_PRIVATE);

    }


    public void GuardarMetrica(String key){
        int valor = sharedPreferences.getInt(key,0);
        valor ++;
        sharedPreferences.edit().putInt(key,valor).commit();

    }

    public int mostrar(String key){
        return sharedPreferences.getInt(key,0);
    }

}
