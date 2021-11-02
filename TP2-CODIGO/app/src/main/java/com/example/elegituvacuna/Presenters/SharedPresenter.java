package com.example.elegituvacuna.Presenters;

import android.app.Activity;

import com.example.elegituvacuna.Models.SharedPreferenceModel;

import java.util.ArrayList;

public class SharedPresenter {

    private SharedPreferenceModel sh;

    public void StartSharedVacuna(Activity activity){
        sh = new SharedPreferenceModel(activity);

        sh.IniciarSharedVacuna();
    }

    public void GuardarMetrica(String key){
        sh.GuardarMetrica(key);
    }

    public ArrayList<String> MostrarMetricas(){
        ArrayList<String> res = new ArrayList<String>();

        res.add("Busquedas Realizadas: " + sh.mostrar("contador_metricas_realizadas_Busquedas"));
        res.add("Cantidad usos del Shacke: " + sh.mostrar("contador_metricas_Usa_Shacke"));

        return res;
    }
}
