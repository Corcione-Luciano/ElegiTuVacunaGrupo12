package com.example.elegituvacuna.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.elegituvacuna.Estructuras.EstructuraLogin;
import com.example.elegituvacuna.Estructuras.EstructuraToken;
import com.example.elegituvacuna.Presenters.MainPresenter;
import com.example.elegituvacuna.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.elegituvacuna.Constantes.StaticConstantes.CAMPOS_VACIOS;
import static com.example.elegituvacuna.Constantes.StaticConstantes.MAIL_INVALIDO;
import static com.example.elegituvacuna.Constantes.StaticConstantes.PATTERN;

public class Activity_Desbloqueo extends AppCompatActivity {

    private TextView mail, password;

    private Window window;
    private EstructuraLogin estructuraLogin;

    private MainPresenter presenter;
    private Context context;
    private String mensajeValidacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        estructuraLogin=new EstructuraLogin();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desbloqueo);

        context=this;

        this.window = getWindow();
        presenter=new MainPresenter(this,window);
        presenter.setupSensor();
        presenter.listenerSensor();

        mail = (EditText) findViewById(R.id.editTextEmailLogin);
        password = (EditText) findViewById(R.id.editTextPasswordLogin);

        Button btnRegistrarse=(Button) findViewById(R.id.btnRegistrarse);

        btnRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(Activity_Desbloqueo.this,Activity_registrarse.class);
                startActivity(intent);
            }
        });

        Button btnLogin=(Button) findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               mensajeValidacion=validacionDeCampos();
                if((mensajeValidacion)==null){
                    estructuraLogin.setEmail(mail.getText().toString());
                    estructuraLogin.setPassword(password.getText().toString());
                    presenter.ConectarLogin(estructuraLogin,context);
                }else{
                    Toast.makeText(context, mensajeValidacion, Toast.LENGTH_SHORT).show();
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

    private String validacionDeCampos(){
        if ((mail.getText().toString().compareTo("") == 0)
                || (password.getText().toString().compareTo("") == 0)) {
            return CAMPOS_VACIOS;
        }
        Pattern pattern = Pattern.compile(PATTERN);
        Matcher matcher = pattern.matcher(mail.getText().toString());

        if (!matcher.matches()) {
            return MAIL_INVALIDO;
        }
        return null;
    }

    public void CambiarPantalla(EstructuraToken token, String respuesta){
        Toast.makeText(this, respuesta, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, Activity_Busqueda.class);
        intent.putExtra("token", token.getToken());
        intent.putExtra("tokenRefresh", token.getTokenRefresh());
        intent.putExtra("tokenNow", token.getReciboToken());
        startActivity(intent);
    }
}