package com.example.codex_pc.mob_dco;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DoctorLogin extends AppCompatActivity {

    private EditText email,password;
    private FirebaseAuth mAuth;
    private Button signUpbtn,signInbtn,btnReset;
    private DatabaseReference mDatabase;
    private ProgressBar mprogressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_login);
        Firebase.setAndroidContext(this);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Docter");
        mprogressBar = (ProgressBar)findViewById(R.id.AutherProgessBar);

        email = (EditText)findViewById(R.id.Autheremail);
        password = (EditText)findViewById(R.id.password);
        signInbtn = (Button)findViewById(R.id.signinbtn);
        signUpbtn = (Button)findViewById(R.id.signupbtn);

        signUpbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DoctorLogin.this);
                builder.setTitle("Get a official Doctor account");
                builder.setMessage("Are you a Qualified Doctor? ");

                builder.setPositiveButton("Yes ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent i = new Intent(Intent.ACTION_SEND);
                        i.setType("message/rfc822");
                        i.putExtra(Intent.EXTRA_EMAIL,new String[]{"vrkarthik14@gmail.com"});
                        i.putExtra(Intent.EXTRA_SUBJECT,"Get a posting account");
                        i.putExtra(Intent.EXTRA_TEXT,"Please provide your official email id with proper document for your qualification and delete this content before sending email.We will contact you soon.");

                        startActivity(Intent.createChooser(i,"Send mail.."));

                        dialog.dismiss();

                    }
                });


                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            }
        });

        signInbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email = email.getText().toString().trim();
                String pass = password.getText().toString().trim();

                mprogressBar.setVisibility(View.VISIBLE);
                InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);

                if (!TextUtils.isEmpty(Email) && !TextUtils.isEmpty(pass)) {
                    mAuth.signInWithEmailAndPassword(Email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                checkUserExists();
                                Log.i("success","yes");
                                mprogressBar.setVisibility(View.GONE);
                            }else{
                                Toast.makeText(DoctorLogin.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                mprogressBar.setVisibility(View.GONE);
                                Log.i("success","No");
                            }
                        }
                    });
                }else{
                    Toast.makeText(DoctorLogin.this, "Please Enter All Fields ", Toast.LENGTH_SHORT).show();
                    mprogressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    public void checkUserExists() {
        final String user_id = mAuth.getCurrentUser().getUid();
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(user_id)) {
                    Intent loginIntent = new Intent(DoctorLogin.this, DocterActivity.class);
                    startActivity(loginIntent);
                }
                else{
                    Log.i("accessf",user_id);
                    Toast.makeText(DoctorLogin.this, "Sorry ! You don't have access ", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

}

