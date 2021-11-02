package com.example.elegituvacuna.Presenters;

import android.app.Activity;

import com.example.elegituvacuna.Constantes.StaticConstantes;
import com.example.elegituvacuna.Estructuras.EstructuraCentros;
import com.example.elegituvacuna.Models.SharedPreferenceModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class CentrosPresenter {

    private ArrayList<EstructuraCentros> ListaCentros;
    private InputStream Archivo;

    public CentrosPresenter(InputStream archivo) {

        Archivo = archivo;
    }

    private void CargarCentrosDesdeArchivo() throws IOException {
        ListaCentros = new ArrayList<EstructuraCentros>();
        String linea;
        BufferedReader reader = new BufferedReader(new InputStreamReader(Archivo));

        if (Archivo != null) {
            //recorre cada linea del archivo
            while ((linea = reader.readLine()) != null) {
                EstructuraCentros centro = new EstructuraCentros(   linea.split(",")[0],
                                                linea.split(",")[1],
                                                linea.split(",")[2],
                                                linea.split(",")[3],
                                                linea.split(",")[4],
                                                linea.split(",")[5],
                                                linea.split(",")[6] );
                ListaCentros.add(centro);
            }
        }
    }

    public ArrayList<String> Busqueda(String Vacuna, String Provincia, String Partido){
        ArrayList<String> res = new ArrayList<String>();
        try {
            this.CargarCentrosDesdeArchivo();
            Archivo.close();
        } catch (IOException e) {
            e.printStackTrace();
            res = null;
            return res;
        }

        for (EstructuraCentros x : ListaCentros ) {
            if(x.getVacuna().equals(Vacuna) && x.getProvincia().equals(Provincia) && x.getPartido().equals(Partido)) {
                res.add(x.getNombre());
            }else{
                if(Provincia.equals(StaticConstantes.CAPITAL_FEDERAL) && x.getVacuna().equals(Vacuna) && x.getProvincia().equals(Provincia)) {
                    res.add(x.getNombre());
                }

            }
        }

        if(res.size() == 0){
            res = null;
        }

        return res;
    }

    public EstructuraCentros buscarPorNombre(String nombre){
        EstructuraCentros res = null;

        for (EstructuraCentros x : ListaCentros ) {
            if (x.getNombre().equals(nombre)) {
                return x;
            }
        }
        return res;
    }




}


