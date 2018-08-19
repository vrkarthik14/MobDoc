package com.example.codex_pc.mob_dco;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    public static final int RC_PHOTO_PICKER=1;



    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button doc = findViewById(R.id.doc);
        progressBar = findViewById(R.id.main_progressbaR);
        doc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,DoctorLogin.class));

            }
        });

        Button button = findViewById(R.id.symptomasd);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,SymptomCheckingActivity.class));
            }
        });

        Button patient = findViewById(R.id.patient);
        patient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user != null){
                    Intent studentevent = new Intent(MainActivity.this,fPatientActivity2.class);
                    startActivity(studentevent);

                }
                else{
                    Intent studentevent = new Intent(MainActivity.this,PatientSignIn.class);
                    startActivity(studentevent);
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        Log.i("onatrsumr","ya");
        progressBar.setVisibility(View.GONE);

    }
}


