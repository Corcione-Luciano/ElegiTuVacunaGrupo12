package com.example.elegituvacuna.Views;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.elegituvacuna.Estructuras.EstructuraCentros;
import com.example.elegituvacuna.Estructuras.EstructuraEvento;
import com.example.elegituvacuna.Estructuras.EstructuraToken;
import com.example.elegituvacuna.Presenters.CentroEncontradoPresenter;
import com.example.elegituvacuna.Presenters.MainPresenter;
import com.example.elegituvacuna.R;

import static com.example.elegituvacuna.Constantes.StaticConstantes.MSJ_NO_INTERNET;
import static com.example.elegituvacuna.Constantes.StaticConstantes.VOLVER_A_LOGUEARSE;

public class Activity_CentroSeleccionado extends AppCompatActivity{
    private Spinner CentrosSpinner;
    private Window window;
    private MainPresenter presenter;
    private CentroEncontradoPresenter centroEncontradoPresenter;
    private Uri link;
    private Context context=this;
    private EstructuraToken estructuraToken=new EstructuraToken();
    private EstructuraEvento estructuraEvento=new EstructuraEvento();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.centro_seleccionado);

        this.window = getWindow();
        presenter = new MainPresenter(this, window);
        presenter.setupSensor();
        presenter.listenerSensor();

        EstructuraCentros es = (EstructuraCentros)getIntent().getSerializableExtra("Centro Seleccionado");
        centroEncontradoPresenter = new CentroEncontradoPresenter(this, window, es);
        centroEncontradoPresenter.setupSensor();
        centroEncontradoPresenter.listenerSensor();

        TextView nombreCentro = (TextView)findViewById(R.id.textNombreCentro);
        nombreCentro.setText(es.getNombre());

        TextView direccionCentro = (TextView)findViewById(R.id.textDireccionCentro);
        direccionCentro.setText(es.getDireccion());

        TextView descripcionCentro = (TextView)findViewById(R.id.textDescripcionCentro);
        descripcionCentro.setText(es.getDescripcion());

        Button btnAccederWeb = (Button) findViewById(R.id.btnAccederWeb);

        btnAccederWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                link = Uri.parse(es.getWeb());

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
                            estructuraToken.setToken(extras.getString("token"));
                            presenter.ActualizarToken(estructuraToken,context);
                            estructuraEvento.setTipo("Ingreso a la web");
                            estructuraEvento.setDescripcion("Ingreso a la web del centro");
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
        presenter.listenerSensor();
        centroEncontradoPresenter.listenerSensor();
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.unlistenerSensor();
        centroEncontradoPresenter.unlistenerSensor();
    }

    public void CambiarPantalla(EstructuraToken token, String respuesta){
        Toast.makeText(this, respuesta, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Intent.ACTION_VIEW, link);
        startActivity(intent);
    }
}





