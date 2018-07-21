package com.example.codex_pc.mob_dco;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.List;

public class DocterActivity extends AppCompatActivity {

    IntentIntegrator qrscan;
    Button scacn;
    String UidPatient;
    TextView logo;
    Point size;
    TextView DocWelcomeText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pateint_diagnosis_doctor);
        final Activity activity = this;
        size = new Point();
        //Get scree size
        Display display = getWindowManager().getDefaultDisplay();
        display.getSize(size);
        DocWelcomeText = findViewById(R.id.docWelcomeText);
        FirebaseDatabase.getInstance().getReference().child("Docter").child(FirebaseAuth.getInstance().getCurrentUser()
                .getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DocWelcomeText.setText("Welcome "+dataSnapshot.getValue(String.class)+"You can now start diagnosis. ");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        scacn = findViewById(R.id.scanQr);
        scacn.animate().translationX(size.x*-.5f).withStartAction(new Runnable() {
            @Override
            public void run() {
                scacn.animate().translationX(size.x/-12);
                scacn.animate().translationX(size.x/-10);
                scacn.animate().translationX(size.x/-8);
                scacn.animate().translationX(size.x/-6);
                scacn.animate().translationX(size.x/-4);
            }
        });

        logo = findViewById(R.id.logo);



        logo.animate().translationX(size.x/2).withStartAction(new Runnable() {
            @Override
            public void run() {
                logo.animate().translationX(size.x/10);
                logo.animate().translationX(size.x/8);
                logo.animate().translationX(size.x/6).alpha(0.5f);
            }
        });

        scacn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qrscan = new IntentIntegrator(activity);
                qrscan.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                qrscan.setPrompt("Scan");
                qrscan.setCameraId(0);
                qrscan.setBeepEnabled(true);
                qrscan.setOrientationLocked(true);
                qrscan.setBarcodeImageEnabled(false);
                qrscan.setCaptureActivity(CaptureActivit.class);
                qrscan.initiateScan();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_SHORT).show();
            } else {
                UidPatient = result.getContents();
                Intent intent = new Intent(DocterActivity.this,PateintDiagnosisDoctor.class);
                intent.putExtra("uid",UidPatient);
                startActivity(intent);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AuthUI.getInstance().signOut(this);
        startActivity(new Intent(DocterActivity.this,MainActivity.class));
        Toast.makeText(this, "Successfully! Logged you Out ! ", Toast.LENGTH_SHORT).show();
        finish();
    }

    class adapter extends ArrayAdapter {
        Context context;
        List<AddDignosei> list;
        public adapter(@NonNull Context context, int resource, @NonNull List objects) {
            super(context, resource, objects);
            this.context = context;
            list = objects;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View view = convertView;
            AddDignosei addDignosei = list.get(position);
            if(view == null)
                view = LayoutInflater.from(context).inflate(R.layout.disply_presc,parent,false);
            TextView presdate = view.findViewById(R.id.presdate);
            TextView fev = view.findViewById(R.id.fev);
            TextView bloodPre = view.findViewById(R.id.bloodPre);
            TextView prescv = view.findViewById(R.id.prescv);
            TextView otherinf= view.findViewById(R.id.otherinf);
            presdate.setText(addDignosei.date);
            fev.setText(addDignosei.fever);
            bloodPre.setText(addDignosei.BP);
            prescv.setText(addDignosei.Prescription);
            otherinf.setText(addDignosei.OtherDetails);
            return view;
        }
    }


}

