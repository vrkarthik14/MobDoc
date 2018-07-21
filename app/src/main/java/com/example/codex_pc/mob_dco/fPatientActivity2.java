package com.example.codex_pc.mob_dco;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

public class fPatientActivity2 extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    SectionPageAdapter msectionPageAdapter;
    Button calorie;
    Button showQr;
    Bitmap bitmap;
    ImageView imageView;
    public final static int QRcodeWidth = 500;
    String uid;
    String url;
    View headerView;
    private ViewPager mviewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_f_patient2);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        headerView = navigationView.getHeaderView(0);



        FirebaseDatabase.getInstance().getReference().child("Patient").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("imageUrl")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        url = dataSnapshot.getValue(String.class);
                        ImageView image = headerView.findViewById(R.id.profileImage);
                        Log.i("vfuy",image.toString());
                        Glide.with(image.getContext()).load(url).into(image);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        FirebaseDatabase.getInstance().getReference().child("Patient").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Username")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        TextView t1 = headerView.findViewById(R.id.pName);
                        t1.setText(dataSnapshot.getValue(String.class));
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        FirebaseDatabase.getInstance().getReference().child("Patient").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("age")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        TextView t2 = headerView.findViewById(R.id.page);
                        t2.setText("age:"+dataSnapshot.getValue(Long.class)+"");
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        FirebaseDatabase.getInstance().getReference().child("Patient").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("gender")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        TextView t3 = headerView.findViewById(R.id.Pgender);
                        t3.setText(dataSnapshot.getValue(String.class));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        Log.i("asdfafw",url+"");



//        DisplayListFragment displayListFragment = new DisplayListFragment();
//        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager()
//                .beginTransaction();
//        transaction.replace(R.id.fragment_container,displayListFragment);
//        transaction.commit();






        final FloatingActionButton showQr;

        showQr = (FloatingActionButton) findViewById(R.id.show_qr);
        try {
            bitmap = TextToImageEncode(uid);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        showQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertadd = new AlertDialog.Builder(fPatientActivity2.this);
                LayoutInflater factory = LayoutInflater.from(fPatientActivity2.this);
                final View viw = factory.inflate(R.layout.dialog, null);
                imageView = viw.findViewById(R.id.dialog_imageview);
                imageView.setImageBitmap(bitmap);
                alertadd.setView(viw);
                alertadd.setNeutralButton("close", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dlg, int sumthin) {

                    }
                });

                alertadd.show();
            }
        });




        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);




        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        msectionPageAdapter = new SectionPageAdapter(getSupportFragmentManager());

        mviewPager = findViewById(R.id.nutri_container);
        mviewPager.setAdapter(msectionPageAdapter);

        TabLayout tabLayout = findViewById(R.id.patient_tabs);
        tabLayout.setupWithViewPager(mviewPager);

        mviewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        showQr.show();
                        break;
                    default:
                        showQr.hide();
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

                Log.i("afasfasfd",url+"");
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.f_patient_activity2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.logout) {
            // Handle the camera action
            AuthUI.getInstance().signOut(this);
            startActivity(new Intent(fPatientActivity2.this,MainActivity.class));
            Toast.makeText(this, "Successfully Logged Out! ", Toast.LENGTH_SHORT).show();
            finish();
            return true;

        }
        if (id == R.id.about) {
            startActivity(new Intent(fPatientActivity2.this,About.class));
            


        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    Bitmap TextToImageEncode(String Value) throws WriterException {
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(
                    Value,
                    BarcodeFormat.DATA_MATRIX.QR_CODE,
                    QRcodeWidth, QRcodeWidth, null
            );

        } catch (IllegalArgumentException Illegalargumentexception) {

            return null;
        }
        int bitMatrixWidth = bitMatrix.getWidth();

        int bitMatrixHeight = bitMatrix.getHeight();

        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        for (int y = 0; y < bitMatrixHeight; y++) {
            int offset = y * bitMatrixWidth;

            for (int x = 0; x < bitMatrixWidth; x++) {

                pixels[offset + x] = bitMatrix.get(x, y) ?
                        getResources().getColor(R.color.QRCodeBlackColor) : getResources().getColor(R.color.QRCodeWhiteColor);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);

        bitmap.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
    }

    public class SectionPageAdapter extends FragmentPagerAdapter{

        public SectionPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position){
                case 0:
                    DisplayListFragment display = new DisplayListFragment();
                    return display;
                case 1:
                    NutrientCalculate nutri = new NutrientCalculate();
                    return nutri;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case 0:
                    return "My Diagnosis";
                case 1:
                    return "Diet Feed";

            }
            return null;
        }
    }
}
