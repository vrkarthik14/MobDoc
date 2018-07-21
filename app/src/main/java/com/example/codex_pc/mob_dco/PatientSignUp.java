package com.example.codex_pc.mob_dco;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


public class PatientSignUp extends AppCompatActivity {

    EditText email,usernmae,password,height,weight;
    Button signUp,signIn;
    Spinner gender,age;
    FirebaseAuth mauth;
    DatabaseReference databaseReference;
    ImageButton photoPicker;
    FirebaseStorage mStorage;
    StorageReference mStorageReference;
    Uri imageUri;
    String lastSeg;
    UploadTask uploadTask;
    ProgressBar progressBar;

public static final int RC_PHOTO_PICKER = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        progressBar = findViewById(R.id.RegisterProgressBar);
        email = findViewById(R.id.Email);
        password = findViewById(R.id.Password);
        usernmae = findViewById(R.id.Username);
        height = findViewById(R.id.height);
        weight = findViewById(R.id.weight);
        gender = findViewById(R.id.gender);
        photoPicker = findViewById(R.id.photoPicker);
        ArrayAdapter<CharSequence> adt = ArrayAdapter.createFromResource(this,R.array.genders,android.R.layout.simple_spinner_item);
        adt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gender.setAdapter(adt);
        age = findViewById(R.id.age);
        ArrayAdapter<CharSequence> bdt = ArrayAdapter.createFromResource(this,R.array.age,android.R.layout.simple_spinner_item);
        bdt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        age.setAdapter(bdt);
        mauth = FirebaseAuth.getInstance();
        mStorage = FirebaseStorage.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Patient");
        mStorageReference = mStorage.getReference().child("profilepics");
        photoPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);
            }

        });




        signIn = findViewById(R.id.logInBtn);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PatientSignUp.this,PatientSignIn.class));
                finish();
                }
        });
        signUp = findViewById(R.id.signupButton);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);

                if (! usernmae.getText().toString().isEmpty() && !password.getText().toString().isEmpty()&&!email.getText().toString().isEmpty()
                        &&!weight.getText().toString().isEmpty() && !height.getText().toString().isEmpty() && !gender.getSelectedItem().toString().equals("----")
                        && !age.getSelectedItem().toString().equals("-1") && imageUri!= null) {

                    progressBar.setVisibility(View.VISIBLE);
                    StorageReference  storageReference = mStorageReference.child(lastSeg);
                    Log.i("storage ref",storageReference.toString());
                    Log.i("lastseg",lastSeg);
                    Log.i("gtyiyu",imageUri+"");
                    uploadTask = storageReference.putFile(imageUri);



                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            final String downUrl = taskSnapshot.getDownloadUrl().toString();
                            Log.i("downUrl","Downloaded url");

                            final String name_content,password_content,email_content,gend;
                            final int agE;
                            final float weigh,heigh;
                            name_content= usernmae.getText().toString().trim();
                            password_content = password.getText().toString().trim();
                            email_content=  email.getText().toString().trim();
                            weigh = Float.parseFloat(weight.getText().toString());
                            heigh = Float.parseFloat(height.getText().toString());
                            gend= gender.getSelectedItem().toString();
                            agE = Integer.parseInt(age.getSelectedItem().toString());

                            mauth.createUserWithEmailAndPassword(email_content,password_content).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        String user_id = mauth.getCurrentUser().getUid();
                                        DatabaseReference current_user_db = databaseReference.child(user_id);
                                        current_user_db.child("Username").setValue(name_content);
                                        current_user_db.child("Email_id").setValue(email_content);
                                        current_user_db.child("weight").setValue(weigh);
                                        current_user_db.child("height").setValue(heigh);
                                        current_user_db.child("gender").setValue(gend);
                                        current_user_db.child("age").setValue(agE);
                                        current_user_db.child("imageUrl").setValue(downUrl);
                                        startActivity(new Intent(PatientSignUp.this,fPatientActivity2.class));
                                        finish();
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(PatientSignUp.this, "Successfully Registered !", Toast.LENGTH_SHORT).show();

                                    }else {
                                        Toast.makeText(PatientSignUp.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                        }
                    });

                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.i("gvugv",e.toString());
                            progressBar.setVisibility(View.GONE);
                        }
                    });


                }else {
                    Toast.makeText(PatientSignUp.this, "Please enter all details.", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_PHOTO_PICKER){
            if(data != null){
                imageUri = data.getData();
                lastSeg = imageUri.getLastPathSegment();
                Log.i("downUrl",imageUri+"");
            }
        }

    }
}
