package com.example.elegituvacuna.Views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.elegituvacuna.Constantes.StaticConstantes;
import com.example.elegituvacuna.Estructuras.EstructuraCentros;
import com.example.elegituvacuna.Estructuras.EstructuraEvento;
import com.example.elegituvacuna.Estructuras.EstructuraToken;
import com.example.elegituvacuna.Presenters.CentrosPresenter;
import com.example.elegituvacuna.Presenters.MainPresenter;
import com.example.elegituvacuna.R;

import java.io.InputStream;
import java.util.ArrayList;

import static com.example.elegituvacuna.Constantes.StaticConstantes.MSJ_NO_INTERNET;
import static com.example.elegituvacuna.Constantes.StaticConstantes.VOLVER_A_LOGUEARSE;

public class Activity_CentroEncontrado extends AppCompatActivity {

    private Spinner CentrosSpinner;
    private Window window;
    private MainPresenter presenter;
    private EstructuraCentros es;
    private Context context=this;
    private EstructuraToken estructuraToken=new EstructuraToken();
    private EstructuraEvento estructuraEvento=new EstructuraEvento();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.centros_encontrados);

        this.window = getWindow();
        presenter=new MainPresenter(this,window);
        presenter.setupSensor();
        presenter.listenerSensor();

        InputStream is = this.getResources().openRawResource(R.raw.centros);
        CentrosPresenter buscar = new CentrosPresenter(is);


        ArrayList<String> CentrosEncontrados = buscar.Busqueda( getIntent().getExtras().getString(StaticConstantes.PALABRA_CLAVE_VACUNA),
                                                                getIntent().getExtras().getString(StaticConstantes.PALABRA_CLAVE_PROVINCIA),
                                                                getIntent().getExtras().getString(StaticConstantes.PALABRA_CLAVE_PARTIDO));

       if(CentrosEncontrados != null){
            CentrosSpinner = (Spinner)findViewById(R.id.spinner);
            ArrayAdapter<String> adapterProv;
            adapterProv = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, CentrosEncontrados);
            CentrosSpinner.setAdapter(adapterProv);

       }else{
            Toast.makeText( Activity_CentroEncontrado.this, "Datos de centros no encontrados", Toast.LENGTH_SHORT).show();
       }

       Button btnVerDetalle = (Button) findViewById(R.id.btnVerDetalle);

       btnVerDetalle.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if(CentrosEncontrados != null){
                    es = buscar.buscarPorNombre(CentrosSpinner.getSelectedItem().toString());

                   Bundle extras = getIntent().getExtras();
                   estructuraToken.setReciboToken(extras.getLong("tokenNow"));

                   if (presenter.checkarConexion(context) == null) {
                       String respuestaDeToken = presenter.compararToken(estructuraToken);
                       switch (respuestaDeToken) {
                           case VOLVER_A_LOGUEARSE:
                               Toast.makeText(context, "Se acabo el tiempo de seccion", Toast.LENGTH_SHORT).show();
                               Intent intentt = new Intent(context, Activity_Desbloqueo.class);
                               context.startActivity(intentt);
                               break;
                           default:

                               estructuraToken.setToken(extras.getString("token"));

                               presenter.ActualizarToken(estructuraToken, context);
                               estructuraEvento.setTipo("Seleccion de Centro");
                               estructuraEvento.setDescripcion("Se selecciono un centro");
                               presenter.RegistrarEvento(estructuraEvento, estructuraToken, context);

                               break;
                       }

                   } else {
                       Toast.makeText(context, MSJ_NO_INTERNET, Toast.LENGTH_SHORT).show();
                   }
               }else{
                   Toast.makeText(context, "No hay centros seleccionados", Toast.LENGTH_SHORT).show();
               }
           }
       });

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

    public void CambiarPantalla(EstructuraToken token, String respuesta){
        Toast.makeText(this, respuesta, Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, Activity_CentroSeleccionado.class);

            intent.putExtra("Centro Seleccionado", es);

            intent.putExtra("token", token.getToken());
            intent.putExtra("tokenRefresh", token.getTokenRefresh());
            intent.putExtra("tokenNow", token.getReciboToken());
            startActivity(intent);
    }
}