package com.example.elegituvacuna.Views;

import android.os.Bundle;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.elegituvacuna.Presenters.MainPresenter;
import com.example.elegituvacuna.R;

import java.util.ArrayList;

public class Activity_MostrarMetricas extends AppCompatActivity {

    private Window window;
    private ListView listview;
    private MainPresenter presenter;
    private ArrayList<String> lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.mostrar_metricas);

        this.window = getWindow();
        presenter=new MainPresenter(this,window);
        presenter.setupSensor();
        presenter.listenerSensor();


        listview = (ListView) findViewById(R.id.list);
        lista = (ArrayList<String>) getIntent().getStringArrayListExtra("Datos");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, lista);
        listview.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.listenerSensor();
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.unlistenerSensor();
    }
}