package com.example.codex_pc.mob_dco;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PatientSignIn extends AppCompatActivity {

    private EditText loginEmail;
    private EditText loginPass;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private Button login;
    private Button signUp;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_sign_in);

        loginEmail = (EditText) findViewById(R.id.loginEmail);
        loginPass = (EditText) findViewById(R.id.loginPass);
        // newly added code
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Patient");

        progressBar = findViewById(R.id.Login_progressBar);
        login = findViewById(R.id.patientlogin);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);

                String email = loginEmail.getText().toString().trim();
                String pass = loginPass.getText().toString().trim();
                if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass)) {
                    mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                progressBar.setVisibility(View.VISIBLE);
                                checkUserExists();
                               // mprogressbar.setVisibility(View.GONE);
                            }else{
                                Toast.makeText(PatientSignIn.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                //mprogressbar.setVisibility(View.GONE);
                            }
                        }
                    });
                }else{
                    Toast.makeText(PatientSignIn.this, "Please fill all details", Toast.LENGTH_SHORT).show();
                }
            }
        });
        signUp = findViewById(R.id.signupbt);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PatientSignIn.this,PatientSignUp.class));
                finish();
            }
        });

    }
    public void checkUserExists() {
        final String user_id = mAuth.getCurrentUser().getUid();
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(user_id)) {
                    Intent loginIntent = new Intent(PatientSignIn.this, fPatientActivity2.class);


                    startActivity(loginIntent);
                    Log.i("entred","ya");
                    finish();
                    progressBar.setVisibility(View.GONE);
                }else{
                    if(FirebaseAuth.getInstance().getCurrentUser() != null){
                        AuthUI.getInstance().signOut(PatientSignIn.this);
                    }
                    Toast.makeText(PatientSignIn.this, "Not Registered, then Please Register.", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }


}
