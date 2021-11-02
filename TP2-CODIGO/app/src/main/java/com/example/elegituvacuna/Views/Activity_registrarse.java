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

import com.example.elegituvacuna.R;
import com.example.elegituvacuna.Estructuras.EstructuraToken;
import com.example.elegituvacuna.Estructuras.EstructuraRegistro;
import com.example.elegituvacuna.Presenters.MainPresenter;

import static com.example.elegituvacuna.Constantes.StaticConstantes.CAMPOS_VACIOS;
import static com.example.elegituvacuna.Constantes.StaticConstantes.CANTIDAD_MINIMA_PASSWORD;
import static com.example.elegituvacuna.Constantes.StaticConstantes.COMISION_OTRA_2900;
import static com.example.elegituvacuna.Constantes.StaticConstantes.COMISION_OTRA_3900;
import static com.example.elegituvacuna.Constantes.StaticConstantes.MAIL_INVALIDO;
import static com.example.elegituvacuna.Constantes.StaticConstantes.MSJ_COMISION_INVALIDA;
import static com.example.elegituvacuna.Constantes.StaticConstantes.MSJ_PASSWORD_CORTO;
import static com.example.elegituvacuna.Constantes.StaticConstantes.PATTERN;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Activity_registrarse extends AppCompatActivity {

    private TextView textNombre, textApellido, textDNI, textMail, textPass, textComision, textGrupo;
    private Window window;
    private MainPresenter presenter;
    private EstructuraRegistro registrar = new EstructuraRegistro();
    private String mensajeDeError;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.registrarse);

        context=this;

        textNombre = (EditText) findViewById(R.id.editTextTextNombre);
        textApellido = (EditText) findViewById(R.id.editTextApellido);
        textDNI = (EditText) findViewById(R.id.editTextDNI);
        textMail = (EditText) findViewById(R.id.editTextEmail);
        textPass = (EditText) findViewById(R.id.editTextPass);
        textComision = (EditText) findViewById(R.id.editTextComision);
        textGrupo = (EditText) findViewById(R.id.editTextGrupo);

        this.window = getWindow();
        presenter=new MainPresenter(this,window);
        presenter.setupSensor();
        presenter.listenerSensor();

        Button buttonRegUsu=(Button) findViewById(R.id.buttonRegUsu);

        buttonRegUsu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mensajeDeError=validarCampos();
                if((mensajeDeError)==null){
                    registrar.setName(textNombre.getText().toString());
                    registrar.setLastname(textApellido.getText().toString());
                    registrar.setDni(Integer.parseInt(textDNI.getText().toString()));
                    registrar.setEmail(textMail.getText().toString());
                    registrar.setPassword(textPass.getText().toString());
                    registrar.setCommission(Integer.parseInt(textComision.getText().toString()));
                    registrar.setGroup(Integer.parseInt(textGrupo.getText().toString()));
                    presenter.RegistrarUsuario(registrar,context);
                }else{
                    Toast.makeText(context, mensajeDeError, Toast.LENGTH_SHORT).show();
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

    // Retorno false si alguna validacion no se cumple, true si esta todo bien.
    public String validarCampos() {

        if (    (   textNombre.getText().toString().compareTo("") == 0)
                || (textApellido.getText().toString().compareTo("") == 0)
                || (textDNI.getText().toString().compareTo("") == 0)
                || (textMail.getText().toString().compareTo("") == 0)
                || (textPass.getText().toString().compareTo("") == 0)
                || (textComision.getText().toString().compareTo("") == 0)
                || (textGrupo.getText().toString().compareTo("") == 0)) {
            return CAMPOS_VACIOS;
        }
        if (textPass.length() < CANTIDAD_MINIMA_PASSWORD){
            return MSJ_PASSWORD_CORTO;
        }
        if (!validarMail(textMail.getText().toString())){
            return MAIL_INVALIDO;
        }

        if (textComision.getText().toString().compareTo(COMISION_OTRA_2900) != 0
                && textComision.getText().toString().compareTo(COMISION_OTRA_3900) != 0){
            return MSJ_COMISION_INVALIDA;
        }
        return null;
    }

    public void CambiarPantalla(EstructuraToken token,String respuesta){
        Toast.makeText(this, respuesta, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, Activity_Busqueda.class);
        intent.putExtra("token", token.getToken());
        intent.putExtra("tokenRefresh", token.getTokenRefresh());
        intent.putExtra("tokenNow", token.getReciboToken());
        startActivity(intent);
    }

    // Con este metodo validamos correo ingresado correctamente
    public boolean validarMail (String email){
        Pattern pattern = Pattern.compile(PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}