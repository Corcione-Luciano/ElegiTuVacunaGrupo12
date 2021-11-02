package com.example.elegituvacuna.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import static com.example.elegituvacuna.Constantes.StaticConstantes.MSJ_NO_INTERNET;
import static com.example.elegituvacuna.Constantes.StaticConstantes.VOLVER_A_LOGUEARSE;

import com.example.elegituvacuna.Estructuras.EstructuraEvento;
import com.example.elegituvacuna.Estructuras.EstructuraToken;
import com.example.elegituvacuna.Presenters.MainPresenter;
import com.example.elegituvacuna.Presenters.SharedPresenter;
import com.example.elegituvacuna.Constantes.StaticConstantes;
import com.example.elegituvacuna.R;
import android.widget.Toast;

public class Activity_Busqueda extends AppCompatActivity {

    private Spinner Provincias;
    private Spinner Vacunas;
    private Spinner Partido;
    private SharedPresenter shp;
    private boolean botonUno;

    private Window window;
    private MainPresenter presenter;
    private Context context=this;

    private EstructuraToken estructuraToken=new EstructuraToken();
    private EstructuraEvento estructuraEvento=new EstructuraEvento();

    TextView nivel;

    //PARA MOSTRAR BATERIA
    BroadcastReceiver bateriaReciever = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {


            int currentLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL,-1);
            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE,-1);
            int level = -1;
            if (currentLevel >=0 && scale > 0){
                level= (currentLevel * 100)/scale;
            }
            nivel.setText("Batería restante : "+level+"%");

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.principal);

        //NECESARIO PARA  MOSTRAR BATERIA
        nivel =(TextView) findViewById(R.id.txtNivel);

        this.window = getWindow();
        presenter=new MainPresenter(this,window);
        presenter.setupSensor();
        presenter.listenerSensor();

        shp = new SharedPresenter();
        shp.StartSharedVacuna(this);

        Provincias = (Spinner)findViewById(R.id.spinnerProvincia);
        ArrayAdapter<CharSequence> adapterProv;
        adapterProv = ArrayAdapter.createFromResource(Activity_Busqueda.this, R.array.Provincias, android.R.layout.simple_spinner_dropdown_item);
        Provincias.setAdapter(adapterProv);

        Vacunas = (Spinner)findViewById(R.id.spinnerVacuna);
        ArrayAdapter<CharSequence> adapterVac;
        adapterVac = ArrayAdapter.createFromResource(Activity_Busqueda.this, R.array.Vacunas, android.R.layout.simple_spinner_dropdown_item);
        Vacunas.setAdapter(adapterVac);

        Partido = (Spinner) findViewById(R.id.spinnerPartido);
        ArrayAdapter<CharSequence> adapterPar;
        adapterPar = ArrayAdapter.createFromResource(Activity_Busqueda.this, R.array.Partidos, android.R.layout.simple_spinner_dropdown_item);
        Partido.setAdapter(adapterPar);
        Partido.setVisibility(View.INVISIBLE);

        Provincias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(Provincias.getSelectedItem().equals(StaticConstantes.PROVINCIA_BUENOS_AIRES)) {
                    Partido.setVisibility(View.VISIBLE);
                }
                if(Provincias.getSelectedItem().equals(StaticConstantes.CAPITAL_FEDERAL)){
                    Partido.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                Partido.setVisibility(View.INVISIBLE);
            }
        });

        Button btnBuscar = (Button) findViewById(R.id.btnBuscar);
        Button btnMostrarMetricas = (Button) findViewById(R.id.btnMetricas);

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Aca se hace el actualizar token y registra el evento
                Bundle extras = getIntent().getExtras();
                estructuraToken.setReciboToken(extras.getLong("tokenNow"));

                if(presenter.checkarConexion(context) == null){
                    String respuestaDeToken=presenter.compararToken(estructuraToken);
                    switch (respuestaDeToken) {
                        case VOLVER_A_LOGUEARSE:
                            Toast.makeText(context, "Se acabo el tiempo de seccion", Toast.LENGTH_SHORT).show();
                            Intent intentt = new Intent(context, Activity_Desbloqueo.class);
                            context.startActivity(intentt);
                            break;
                            default :
                                botonUno=true;
                                estructuraToken.setToken(extras.getString("token"));

                                presenter.ActualizarToken(estructuraToken,context);
                                estructuraEvento.setTipo("Busqueda");
                                estructuraEvento.setDescripcion("Se hizo una busqueda de vacuna");
                                presenter.RegistrarEvento(estructuraEvento,estructuraToken,context);

                                shp.GuardarMetrica("contador_metricas_realizadas_Busquedas");
                                break;
                    }
                }else{
                    Toast.makeText(context, MSJ_NO_INTERNET, Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnMostrarMetricas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Aca se hace el actualizar token y registra el evento
                Bundle extras = getIntent().getExtras();
                estructuraToken.setReciboToken(extras.getLong("tokenNow"));

                if(presenter.checkarConexion(context) == null){
                    String respuestaDeToken=presenter.compararToken(estructuraToken);
                    switch (respuestaDeToken) {
                        case VOLVER_A_LOGUEARSE:
                            Toast.makeText(context, "Se acabo el tiempo de seccion", Toast.LENGTH_SHORT).show();
                            Intent intentt = new Intent(context, Activity_Desbloqueo.class);
                            context.startActivity(intentt);
                            break;
                        default :
                            botonUno=false;
                            estructuraToken.setToken(extras.getString("token"));

                            presenter.ActualizarToken(estructuraToken,context);
                            estructuraEvento.setTipo("Metricas");
                            estructuraEvento.setDescripcion("Se hizo un muestreo de las metricas");
                            presenter.RegistrarEvento(estructuraEvento,estructuraToken,context);
                            break;
                    }
                }else{
                    Toast.makeText(context, MSJ_NO_INTERNET, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter batteryFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(bateriaReciever,batteryFilter);
        presenter.listenerSensor();
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.unlistenerSensor();
        unregisterReceiver(bateriaReciever);
    }

    public void CambiarPantalla(EstructuraToken token,String respuesta){
        Toast.makeText(this, respuesta, Toast.LENGTH_SHORT).show();
        if(botonUno){
            Intent intent = new Intent(this, Activity_CentroEncontrado.class);
            intent.putExtra(StaticConstantes.PALABRA_CLAVE_VACUNA, Vacunas.getSelectedItem().toString());
            intent.putExtra(StaticConstantes.PALABRA_CLAVE_PROVINCIA, Provincias.getSelectedItem().toString());
            intent.putExtra(StaticConstantes.PALABRA_CLAVE_PARTIDO, Partido.getSelectedItem().toString());
            intent.putExtra("token", token.getToken());
            intent.putExtra("tokenRefresh", token.getTokenRefresh());
            intent.putExtra("tokenNow", token.getReciboToken());
            startActivity(intent);
        }else{
            Intent intent = new Intent(this, Activity_MostrarMetricas.class);
            intent.putStringArrayListExtra("Datos", shp.MostrarMetricas());
            intent.putExtra("token", token.getToken());
            intent.putExtra("tokenRefresh", token.getTokenRefresh());
            intent.putExtra("tokenNow", token.getReciboToken());
            startActivity(intent);
        }
    }
}