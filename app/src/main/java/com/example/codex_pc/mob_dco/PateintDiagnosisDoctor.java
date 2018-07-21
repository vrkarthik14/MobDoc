package com.example.codex_pc.mob_dco;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PateintDiagnosisDoctor extends AppCompatActivity {

    FloatingActionButton addDigno;
    String UidPatient;
    TextView patient_name, patient_age, patient_weight, patient_height, blood_group, patient_gener;
    DatabaseReference mFirebaseRef;
    DatabaseReference fref;
    List<AddDignosei> patDia = new ArrayList<>();
    ListView listView;
    adapter adaptr;
    String doc;
    ChildEventListener childEventListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patientlayotu);

        UidPatient = getIntent().getExtras().get("uid").toString();
        listView = findViewById(R.id.prescrtionD);
        adaptr = new adapter(this,R.id.prescrtionD,patDia);
        listView.setAdapter(adaptr);
        addDigno = findViewById(R.id.addDigo);
        patient_name = findViewById(R.id.patient_name);
        patient_age = findViewById(R.id.patient_age);
        patient_weight = findViewById(R.id.patient_weight);
        patient_height = findViewById(R.id.patient_height);
        patient_gener = findViewById(R.id.patient_gender);
        blood_group = findViewById(R.id.blood_group);
        addDigno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertadd = new AlertDialog.Builder(PateintDiagnosisDoctor.this);
                LayoutInflater factory = LayoutInflater.from(PateintDiagnosisDoctor.this);
                final View viw = factory.inflate(R.layout.write_diagnose_details, null);
                final EditText ferve = viw.findViewById(R.id.fever);
                final EditText symtpoms_and_problems = viw.findViewById(R.id.symptoms_problems);
                final EditText Bp = viw.findViewById(R.id.BP);
                final EditText Extrain = viw.findViewById(R.id.otehrInfo);
                final EditText pres = viw.findViewById(R.id.Prescription);
                final EditText date = viw.findViewById(R.id.date);
                doc = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Docter").child(doc);
                db.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        doc = dataSnapshot.getValue().toString();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                alertadd.setView(viw);
                alertadd.setNeutralButton("DONE", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dlg, int sumthin) {
                        if(!ferve.getText().toString().equals("")&&!Bp.getText().toString().equals("")&&!pres.getText().toString().equals("")
                                && !date.getText().toString().equals("")) {
                            String fev = ferve.getText().toString();
                            String bp = Bp.getText().toString();
                            String other = Extrain.getText().toString();
                            String prescr = pres.getText().toString();
                            final String dat = date.getText().toString();
                            String symptomsProbl = symtpoms_and_problems.getText().toString();
                            AddDignosei add = new AddDignosei(symptomsProbl,dat,fev,bp,other,doc,prescr);
                            fref = FirebaseDatabase.getInstance().getReference().child("Patient").child(UidPatient).child("Prescriptions");
                            fref.push().setValue(add);
                            Toast.makeText(PateintDiagnosisDoctor.this, "Prescription added..", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(PateintDiagnosisDoctor.this, "Please fill all fields...", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                alertadd.show();
            }
        });


        mFirebaseRef = FirebaseDatabase.getInstance().getReference().child("Patient").child(UidPatient);
        mFirebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                PatientDetails patientDetails = dataSnapshot.getValue(PatientDetails.class);
                Log.i("patewdf",(patDia!=null)?"true":"false");
                patient_name.setText("Name: "+patientDetails.Username);
                patient_age.setText("Age: "+patientDetails.age + "");
                patient_weight.setText("Weight: "+patientDetails.weight + "");
                patient_height.setText("Height: "+patientDetails.height + "");
                patient_gener.setText("Gender:"+patientDetails.gender);
                blood_group.setText("Blood Group: "+patientDetails.blood_group);
                loadPatientPrescition();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    public void loadPatientPrescition(){
        if(childEventListener == null) {
            childEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    AddDignosei addDignosei = dataSnapshot.getValue(AddDignosei.class);
                    patDia.add(addDignosei);
                    adaptr.notifyDataSetChanged();

                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };

            FirebaseDatabase.getInstance().getReference().child("Patient").child(UidPatient).child("Prescriptions").addChildEventListener(childEventListener);
        }
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

            TextView symptoms_problems = view.findViewById(R.id.symptoms_p);
            TextView presdate = view.findViewById(R.id.presdate);
            TextView fev = view.findViewById(R.id.fev);
            TextView bloodPre = view.findViewById(R.id.bloodPre);
            TextView prescv = view.findViewById(R.id.prescv);
            TextView otherinf= view.findViewById(R.id.otherinf);
            TextView doc = view.findViewById(R.id.doctorname);

            symptoms_problems.setText("Symptoms:\n"+addDignosei.symptoms_problems);
            presdate.setText(addDignosei.date);
            fev.setText("Fever: "+addDignosei.fever);
            bloodPre.setText("Blood Pressure: "+addDignosei.BP);
            prescv.setText("Prescription:\n"+addDignosei.Prescription);
            otherinf.setText("Other Info:\n"+addDignosei.OtherDetails);
            doc.setText("Diagnosed By:  "+addDignosei.DocName);

            return view;
        }
    }
}
