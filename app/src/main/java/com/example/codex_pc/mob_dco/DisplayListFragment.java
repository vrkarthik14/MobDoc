package com.example.codex_pc.mob_dco;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class DisplayListFragment extends Fragment {

    DatabaseReference databaseReference;
    ListView listView;
    List<AddDignosei> dignoseiList = new ArrayList<>();
    adapter adaptr;

    public DisplayListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_display_list, container, false);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Patient").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Prescriptions");
        listView = rootView.findViewById(R.id.displayDingo);
        adaptr = new adapter(getContext(), R.id.displayDingo, dignoseiList);
        listView.setAdapter(adaptr);

        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                AddDignosei addDignosei = dataSnapshot.getValue(AddDignosei.class);
                dignoseiList.add(addDignosei);
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
        databaseReference.addChildEventListener(childEventListener);
        return rootView;

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
                view = LayoutInflater.from(context).inflate(R.layout.display_previous_presc_item,parent,false);

            TextView doc = view.findViewById(R.id.docto);
            TextView symptoms_problems = view.findViewById(R.id.symptas);
            TextView presdate = view.findViewById(R.id.presdat);
            TextView fev = view.findViewById(R.id.fver);
            TextView bloodPre = view.findViewById(R.id.prescvtion);
            TextView prescv = view.findViewById(R.id.bloodPresr);
            TextView otherinf= view.findViewById(R.id.otherinfor);

            doc.setText(addDignosei.DocName);
            symptoms_problems.setText(addDignosei.symptoms_problems);
            presdate.setText(addDignosei.date);
            fev.setText(addDignosei.fever);
            bloodPre.setText(addDignosei.BP);
            prescv.setText(addDignosei.Prescription);
            otherinf.setText(addDignosei.OtherDetails);
            return view;
        }
    }

}
