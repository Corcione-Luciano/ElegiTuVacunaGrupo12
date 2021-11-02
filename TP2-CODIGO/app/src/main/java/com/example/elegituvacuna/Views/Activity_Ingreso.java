package com.example.elegituvacuna.Views;

import androidx.appcompat.app.AppCompatActivity;
import io.paperdb.Paper;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;
import com.example.elegituvacuna.R;

import java.util.List;

public class Activity_Ingreso extends AppCompatActivity {

    String save_pat_key="pattern_codigo";
    String final_pattern="";

    PatternLockView mPatternLockView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ingreso_patron);

        Paper.init(this);

        final String save_patt= Paper.book().read(save_pat_key);

        if(save_patt!=null && !save_patt.equals(null)){
            setContentView(R.layout.ingreso_patron);
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
                    if(final_pattern.equals(save_patt)){
                        Toast.makeText(Activity_Ingreso.this, "Password Correcto", Toast.LENGTH_SHORT).show();
                        Intent intent= new Intent(Activity_Ingreso.this,Activity_Desbloqueo.class);
                        startActivity(intent);
                    }else{
                        Toast.makeText(Activity_Ingreso.this, "Password Incorrecto", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCleared() {
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