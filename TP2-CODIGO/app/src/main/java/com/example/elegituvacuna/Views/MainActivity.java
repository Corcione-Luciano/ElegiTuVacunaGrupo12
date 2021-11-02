package com.example.elegituvacuna.Views;

import androidx.appcompat.app.AppCompatActivity;
import io.paperdb.Paper;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;
import com.example.elegituvacuna.R;

import java.util.List;

public class MainActivity extends AppCompatActivity {


        String save_pat_key="pattern_codigo"; // nos indica donde esta la contraseña
        String final_pattern="";                // aca se guarda el patron finalmente

        PatternLockView mPatternLockView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro_patron);

        Paper.init(this); // inicializamos el paper que es donde guardaremos el patron
        final String save_patt= Paper.book().read(save_pat_key); // guardamos en save_patt lo que lee al ingresar por primera vez

        if(save_patt!=null && !save_patt.equals(null)) // Si no esta vacio save_patt significa que ya registro patron
        {
            setContentView(R.layout.ingreso_patron);  // relaciona clase MainActivity con layout ingreso_patron

            //Hacemos la referencia al Pattern_lock_View
            mPatternLockView=(PatternLockView)findViewById(R.id.pattern_lock_view);  // asocia objeto mPatternLockView al del layout
            mPatternLockView.addPatternLockListener(new PatternLockViewListener()
            {
                // cuando creamos el listener nos crea los metodos automaticamente
                @Override
                public void onStarted() {
                }

                @Override
                public void onProgress(List<PatternLockView.Dot> progressPattern) {
                }

                @Override
                public void onComplete(List<PatternLockView.Dot> pattern) {
                    final_pattern= PatternLockUtils.patternToString(mPatternLockView,pattern);  // guardamos en variable string que va a almacenar los numeros ingresados del patron
                    if(final_pattern.equals(save_patt)){ // lo comparamos con el valor de save_patt para saber si es correcta la clave
                        Toast.makeText(MainActivity.this, "Password Correcto", Toast.LENGTH_SHORT).show();
                        Intent intent= new Intent(MainActivity.this,Activity_Desbloqueo.class); // pasa a la activity_desbloqueada
                        startActivity(intent);
                    }else{
                        // password no correcta
                        Toast.makeText(MainActivity.this, "Password Incorrecto", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCleared() {
                }
            });
        }
        else { // caso contrario aun no registro patron
            setContentView(R.layout.registro_patron);
            mPatternLockView=(PatternLockView)findViewById(R.id.pattern_lock_view);
            mPatternLockView.addPatternLockListener(new PatternLockViewListener() {
                @Override
                public void onStarted() {
                }

                @Override
                public void onProgress(List<PatternLockView.Dot> progressPattern) {
                }

                @Override
                public void onComplete(List<PatternLockView.Dot> pattern) {
                    final_pattern= PatternLockUtils.patternToString(mPatternLockView,pattern);
                }

                @Override
                public void onCleared() {
                }
            });

            Button btnGuardar=(Button) findViewById(R.id.button_Guardar);
            btnGuardar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Paper.book().write(save_pat_key,final_pattern);
                    Toast.makeText(MainActivity.this, "Patron Guardado correctamente", Toast.LENGTH_SHORT).show();
                    Intent intent =new Intent(MainActivity.this,Activity_Ingreso.class);
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}