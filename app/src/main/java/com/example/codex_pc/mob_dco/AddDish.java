package com.example.codex_pc.mob_dco;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddDish extends AppCompatActivity {

    private EditText speech;
    private ImageButton mic;
    public TextView protien;
    public TextView fat;
    public TextView carb;
    public TextView chol;
    public TextView cal;
    private Retrofit.Builder builder,builder1;
    private Retrofit retrofit,retrofit1;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    ListView component;
    List<FoodComponents> foodComponents1 = new ArrayList<>();
    List<String> compo = new ArrayList<>();
    ArrayAdapter<String> adapter;
    CustomAdpter customAdapter;
    Button load_nutrient;
    NutrientResult1 n2;
    ImageView img1;
    DatabaseReference mreference;
    String user;
    NutrientResult1 result;
    String tag;
    String formattedDate;
    NutrientResult1 n1= new NutrientResult1();
    String key1;
    Button summary;
    LinearLayout listhead;
    LinearLayout caldisplay;
    public String calories;
    public Float calories1;

    List<PieEntry1> af;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_add_dish );

        final Bundle bundle = getIntent().getExtras();
        tag = bundle.getString("tag");
        Log.i("tagere",tag);
        user = FirebaseAuth.getInstance().getCurrentUser().getUid();
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);
        load_nutrient = findViewById(R.id.loadNutri);
        speech = (EditText ) findViewById( R.id.speech );
        protien = (TextView ) findViewById( R.id.protiens );
        carb =(TextView ) findViewById( R.id.carb );
        fat=(TextView ) findViewById( R.id.fat);
        chol=(TextView ) findViewById( R.id.chol );
        cal=(TextView ) findViewById( R.id.cal );
        summary=(Button ) findViewById( R.id.summary );
        listhead = (LinearLayout ) findViewById( R.id.listhead );
        caldisplay=(LinearLayout ) findViewById( R.id.caldisplay );
        summary.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//               calories1=Float.parseFloat(  cal.getText().toString() ) ;

                Intent intent =  new Intent( AddDish.this,PieChart.class);
                Bundle bundle1 = new Bundle();
                bundle1.putParcelableArrayList("mlist",(ArrayList<? extends Parcelable>)af);
                intent.putExtra("bundle",bundle1);
                startActivity(intent);

            }
        } );
        builder1 = new Retrofit.Builder()
                .client(httpClient.build())
                .baseUrl("https://trackapi.nutritionix.com/v2/")
                .addConverterFactory(GsonConverterFactory.create());
        retrofit1 = builder1.build();

        mic = (ImageButton)findViewById(R.id.mic);

        // getActionBar().hide();

        mic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                promtSpeechInput();
            }
        });
        component = findViewById(R.id.componentsf);
        adapter =new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,compo);
        component.setVisibility(View.GONE);
        load_nutrient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //adapter.notifyDataSetChanged();
                if (!speech.getText().toString().isEmpty()) {
                    loadNutritionFormNutrinixdotcom(speech.getText().toString());
                    getFoodComposition(speech.getText().toString());
                }
            }
        });
        Calendar c = Calendar.getInstance();
        System.out.println("Current time =&gt; "+c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
         formattedDate = df.format(c.getTime());
// Now formattedDate have current date/time
//        Toast.makeText(this, formattedDate, Toast.LENGTH_SHORT).show();

        mreference = FirebaseDatabase.getInstance().getReference().child("Patient").child(user).child("DietFeed").child(formattedDate).child(tag);
        Button upload = findViewById(R.id.upload);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(FoodComponents foodCompo:foodComponents1){
                    mreference.child("components").push().setValue(foodCompo);
                }
                //mreference.child("components").setValue(foodComponents1);
                n2 = new NutrientResult1();
                if(n1!=null && result!=null) {
                    n2.setNf_calories(n1.getNf_calories() + result.getNf_calories());
                    n2.setNf_cholesterol(n1.getNf_cholesterol() + result.getNf_cholesterol());
                    n2.setNf_protein(n1.getNf_protein() + result.getNf_protein());
                    n2.setNf_sugars(n1.getNf_sugars() + result.getNf_sugars());
                    n2.setNf_total_carbohydrate(n1.getNf_total_carbohydrate() + result.getNf_total_carbohydrate());
                    n2.setNf_total_fat(n1.getNf_total_fat() + result.getNf_total_fat());

                }
                mreference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild("totalCalories")){
                            mreference.child("totalCalories").removeValue();
                            Log.i("asdfqer",n1.getNf_calories()+"");
                            mreference.child("totalCalories").setValue(n2);
                        }else {
                            mreference.child("totalCalories").setValue(result);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
        mreference.child("totalCalories").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                n1 = dataSnapshot.getValue(NutrientResult1.class);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    private void promtSpeechInput(){
        Intent intent = new Intent( RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    speech.setText(result.get(0));
                }
                break;
            }

        }
    }

    public void loadNutritionFormNutrinixdotcom(String query){
        Nutrition usercleint = retrofit1.create(Nutrition.class);
        Nutriquery_reto1 queryClass = new Nutriquery_reto1(query,1);
        Call<NutrintionResultList> call = usercleint.loadNutrion1("application/json",
                queryClass,"6097f30a","be5b65a01585e563c8709d0b6295e56b","0");
        call.enqueue(new Callback<NutrintionResultList>() {
            @Override
            public void onResponse(Call<NutrintionResultList> call, Response<NutrintionResultList> response) {
                if(response.isSuccessful()){
                    result = response.body().getFoods().get(0);
                    if(result!= null){
                        Log.i("calories",result.getNf_calories()+"");

                   protien.setText( result.getNf_protein()+"g" );
                   fat.setText( result.getNf_total_fat()+"g" );
                   carb.setText( result.getNf_total_carbohydrate()+"g" );
                   chol.setText( result.getNf_cholesterol()+"mg" );
                   cal.setText( result.getNf_calories()+" calories" );
                   af = new ArrayList<>();
                   af.add(new PieEntry1("Cholestrol",result.getNf_cholesterol()/1000));
                   af.add(new PieEntry1("Protiens",result.getNf_protein()));
                   af.add(new PieEntry1("Carbohydtrates",result.getNf_total_carbohydrate()));
                   af.add(new PieEntry1("Fats",result.getNf_total_fat()));
                   Log.i("qasdf",af.get(0).getXdata());
//                   calories = cal.getText().toString();
//                   calories1=Float.parseFloat( calories );

                    }
                }else {
                    Toast.makeText( AddDish.this, "Sorry! Result Not Found", Toast.LENGTH_SHORT ).show();

                }
            }

            @Override
            public void onFailure(Call<NutrintionResultList> call, Throwable t) {

            }
        });
    }

    public void getFoodComposition(String Query1){
        Nutrition usercleint = retrofit1.create(Nutrition.class);
        Nutriquery_reto1 queryClass = new Nutriquery_reto1(Query1,1);
        Call<List<FoodComponents>> call = usercleint.getFoodComponents("application/json",
                queryClass,"6097f30a","be5b65a01585e563c8709d0b6295e56b","0");
        call.enqueue(new Callback<List<FoodComponents>>() {
            @Override
            public void onResponse(Call<List<FoodComponents>> call, Response<List<FoodComponents>> response) {
                if(response.isSuccessful()){
                    listhead.setVisibility( View.VISIBLE );
                    component.setVisibility(View.VISIBLE);
                    caldisplay.setVisibility( View.VISIBLE );
                    foodComponents1 = response.body();
                        Log.i("food asd",foodComponents1.toString());




                        CustomAdpter customAdpter = new CustomAdpter(getApplicationContext(),R.layout.food_list_item,foodComponents1);
                        component.setAdapter(customAdpter);

                        for(FoodComponents fdc:foodComponents1) {
                            compo.add(fdc.getQUANTITY() + "     " + fdc.getMEASURE() + "     " + fdc.getITEM());
                        }

                    Log.i("adptertri","yea");
                }
                else {

                }
            }

            @Override
            public void onFailure(Call<List<FoodComponents>> call, Throwable t) {

            }
        });
    }

    @Override
    public void onBackPressed() {


        super.onBackPressed();

    }

    class DownloadImage extends AsyncTask<String,Void,Bitmap>{
        ImageView img;
        public DownloadImage(ImageView imageView) {
            img = imageView;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(String... strings) {

            try {
                return BitmapFactory.decodeStream((InputStream)new URL(strings[0]).getContent());
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            img.setImageBitmap(bitmap);
        }
    }

   class CustomAdpter extends ArrayAdapter{

        Context context;
        List<FoodComponents> foodComponentsList;
       public CustomAdpter(@NonNull Context context, int resource, @NonNull List objects) {
           super(context, resource, objects);
           this.context = context;
           foodComponentsList = objects;
       }

       @NonNull
       @Override
       public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


           View vie = convertView;
                   if(vie == null)
                   vie = LayoutInflater.from(context).inflate(R.layout.food_list_item,parent,false);

           FoodComponents ff1 = foodComponentsList.get(position);
           ImageView img1 = vie.findViewById(R.id.foodImage);
           new DownloadImage(img1).execute(ff1.getTAG_IMAGE());
           TextView t1 = vie.findViewById(R.id.qty);
            t1.setText(ff1.getQUANTITY());
            TextView t2 = vie.findViewById(R.id.tag_name);
            t2.setText(ff1.getTAG_NAME());
            TextView t3 = vie.findViewById(R.id.unit);
            t3.setText(ff1.getMEASURE());
           return vie;
       }
   }



}

//
//key1 = dataSnapshot.getKey();
//        NutrientResult1 n1 = dataSnapshot.getValue(NutrientResult1.class);
//        n2 = new NutrientResult1();
//        Log.i("previousd", String.valueOf(n1.getNf_calories())+n1.getNf_total_fat());
//        n2.setNf_calories(n1.getNf_calories()+result.getNf_calories());
//        n2.setNf_cholesterol(n1.getNf_cholesterol()+result.getNf_cholesterol());
//        n2.setNf_protein(n1.getNf_protein()+result.getNf_protein());
//        n2.setNf_sugars(n1.getNf_sugars()+result.getNf_sugars());
//        n2.setNf_total_carbohydrate(n1.getNf_total_carbohydrate()+result.getNf_total_carbohydrate());
//        n2.setNf_total_fat(n1.getNf_total_fat()+result.getNf_total_fat());




//                            String key = dataSnapshot.getKey();
//                            Map<String,Object> newN2 = new HashMap<>();
//                            newN2.put("nf_calories",n2.getNf_calories());
//                            newN2.put("nf_total_fat",n2.getNf_total_fat());
//                            newN2.put("nf_cholesterol",n2.getNf_cholesterol());
//                            newN2.put("nf_total_carbohydrate",n2.getNf_total_carbohydrate());
//                            newN2.put("nf_sugars",n2.getNf_sugars());
//                            newN2.put("nf_protein",n2.getNf_protein());
//                             Map<String,Object> update = new HashMap<>();
//                            update.put(key,newN2);
//                            mreference.updateChildren(update);