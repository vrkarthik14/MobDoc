package com.example.codex_pc.mob_dco;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class NutrientCalculate extends Fragment {

    private Retrofit.Builder builder,builder1;
    private Retrofit retrofit,retrofit1;
    String measurURi;
    String rawingredient;
    String app_id = "396ac60a";
    String app_key = "f8dabb6991b0005254e587daff28d50b";
    String foodURi;
    String ingredient;
    NutrientResult1 btot,ltot,dtot;
    double etot;

    Calendar c;
    SimpleDateFormat df;

    ListView breakfast;
    ListView lunch;
    ListView dinner,exerciseList;
    List<FoodComponents> foodComponents1= new ArrayList<>();
    List<FoodComponents> foodComponents2= new ArrayList<>();
    List<FoodComponents> foodComponents3 = new ArrayList<>();
    List<String> compo = new ArrayList<>();
    ArrayAdapter<String> adapter;

    ImageButton brekInfo,luchIfo,dinneInfo;
    String formattedDate;
    EditText input;
    DatabaseReference mreference1,mreference2,mreference3,mexxercise;
    List<PieEntry1> af,af2,af3;
    List<ExercisePOJO> exList = new ArrayList<>();
    double totalIntake;
    TextView remaining;
    TextView totalcalin;
    TextView totalcalburn;
    ImageButton beforeDate,afterDate;

    public NutrientCalculate() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_nutrient_calculate, container, false);
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);
        Button btnb = rootView.findViewById( R.id.btnb );
        Button btnl = rootView.findViewById( R.id.btnl );
        Button btnd = rootView.findViewById( R.id.btnd );
        brekInfo = rootView.findViewById(R.id.brekInfo);
        luchIfo = rootView.findViewById(R.id.luchInfo);
        dinneInfo = rootView.findViewById(R.id.dinneInfo);
        exerciseList = rootView.findViewById(R.id.exersiselis);
        final CustomAdpter1 adpter4 = new CustomAdpter1(getContext(),R.layout.food_list_item,exList);
        exerciseList.setAdapter(adpter4);
        Button btnexer = rootView.findViewById(R.id.btnex);
   //
        //
        //
        breakfast= (ListView)rootView.findViewById(R.id.breakfastdish);

        final CustomAdpter customAdp = new CustomAdpter(getContext(),R.layout.food_list_item,foodComponents1);
        breakfast.setAdapter(customAdp);
        lunch= (ListView)rootView.findViewById(R.id.lunchdish);
        final CustomAdpter customAdpt2 = new CustomAdpter(getContext(),R.layout.food_list_item,foodComponents2);
        lunch.setAdapter(customAdpt2);

        c = Calendar.getInstance();
        System.out.println("Current time =&gt; "+c.getTime());

        df = new SimpleDateFormat("dd-MM-yyyy");
        formattedDate = df.format(c.getTime());
        dinner = rootView.findViewById(R.id.dinnerlist);

//        beforeDate = rootView.findViewById(R.id.before);
//
//        beforeDate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                c.add(Calendar.DATE,-1);
//                Log.i("prevas",df.format(c.getTime()));
//
//            }
//        });

        final CustomAdpter customAdpter3 = new CustomAdpter(getContext(),R.layout.food_list_item,foodComponents3);
        dinner.setAdapter(customAdpter3);


        mreference1 = FirebaseDatabase.getInstance().getReference().child("Patient").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("DietFeed").child(formattedDate).child("BreakFast");


        brekInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =  new Intent(getActivity(),PieChart.class);
                Bundle bundle1 = new Bundle();
                bundle1.putParcelableArrayList("mlist",(ArrayList<? extends Parcelable>)af);
                intent.putExtra("bundle",bundle1);
                startActivity(intent);
            }
        });
        luchIfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =  new Intent(getActivity(),PieChart.class);
                Bundle bundle1 = new Bundle();
                bundle1.putParcelableArrayList("mlist",(ArrayList<? extends Parcelable>)af2);
                intent.putExtra("bundle",bundle1);
                startActivity(intent);
            }
        });
        dinneInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =  new Intent(getActivity(),PieChart.class);
                Bundle bundle1 = new Bundle();
                bundle1.putParcelableArrayList("mlist",(ArrayList<? extends Parcelable>)af3);
                intent.putExtra("bundle",bundle1);
                startActivity(intent);
            }
        });

        mreference1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                btot = dataSnapshot.getValue(NutrientResult1.class);
                updateIntake();
                Log.i("aqwrwfas",btot.getNf_calories()+"");
                af = new ArrayList<>();
                af.add(new PieEntry1("Cholestrol",btot.getNf_cholesterol()/1000));
                af.add(new PieEntry1("Protiens",btot.getNf_protein()));
                af.add(new PieEntry1("Carbohydtrates",btot.getNf_total_carbohydrate()));
                af.add(new PieEntry1("Fats",btot.getNf_total_fat()));

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
        });
        mreference1.child("components").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                FoodComponents foodComp = dataSnapshot.getValue(FoodComponents.class);
                foodComponents1.add(foodComp);
                customAdp.notifyDataSetChanged();
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
        });



        mreference2 = FirebaseDatabase.getInstance().getReference().child("Patient").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("DietFeed").child(formattedDate).child("Lunch");
        mreference2.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ltot = dataSnapshot.getValue(NutrientResult1.class);
                updateIntake();
                Log.i("aqwrwfas",btot.getNf_calories()+"");
                af2 = new ArrayList<>();
                af2.add(new PieEntry1("Cholestrol",ltot.getNf_cholesterol()/1000));
                af2.add(new PieEntry1("Protiens",ltot.getNf_protein()));
                af2.add(new PieEntry1("Carbohydtrates",ltot.getNf_total_carbohydrate()));
                af2.add(new PieEntry1("Fats",ltot.getNf_total_fat()));

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
        });
        mreference2.child("components").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                FoodComponents foodComp = dataSnapshot.getValue(FoodComponents.class);
                foodComponents2.add(foodComp);
                customAdpt2.notifyDataSetChanged();
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
        });


        mreference3 = FirebaseDatabase.getInstance().getReference().child("Patient").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("DietFeed").child(formattedDate).child("Dinner");
        mreference3.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                dtot = dataSnapshot.getValue(NutrientResult1.class);
                updateIntake();
                Log.i("aqwrwfas",btot.getNf_calories()+"");
                af3 = new ArrayList<>();
                af3.add(new PieEntry1("Cholestrol",dtot.getNf_cholesterol()/1000));
                af3.add(new PieEntry1("Protiens",dtot.getNf_protein()));
                af3.add(new PieEntry1("Carbohydtrates",dtot.getNf_total_carbohydrate()));
                af3.add(new PieEntry1("Fats",dtot.getNf_total_fat()));

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
        });
        mreference3.child("components").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                FoodComponents foodComp = dataSnapshot.getValue(FoodComponents.class);
                foodComponents3.add(foodComp);
                customAdpter3.notifyDataSetChanged();
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
        });
        mexxercise = FirebaseDatabase.getInstance().getReference().child("Patient").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("DietFeed").child(formattedDate).child("Exercise");

        mexxercise.child("components").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ExercisePOJO exercisePOJO = dataSnapshot.getValue(ExercisePOJO.class);
                exList.add(exercisePOJO);
                adpter4.notifyDataSetChanged();


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
        });
        mexxercise.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("total_calori_spent")){
                    etot = dataSnapshot.child("total_calori_spent").getValue(Double.class);
                    updateIntake();
                    Log.i("asfqfwerqwerfas",etot+"");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




         remaining = rootView.findViewById( R.id.remaining );
         totalcalin = rootView.findViewById( R.id.totalcalin );
         totalcalburn =rootView.findViewById( R.id.totalcalburn );
//
//        String burn = totalcalburn.toString();
//        String in = totalcalin.toString();
//        int cal = (Integer.parseInt(  totalcalin.toString())-Integer.parseInt( totalcalburn.toString() ));

//        ImageButton calendar = rootView.findViewById( R.id.calendar );
        btnb.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity( new Intent( getActivity().getApplication(), AddDish.class )
                .putExtra("tag","BreakFast"));
            }
        } );
        btnl.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity( new Intent( getActivity().getApplication(),AddDish.class )
                .putExtra("tag","Lunch"));
            }
        } );
        btnd.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity( new Intent( getActivity().getApplication(),AddDish.class )
                .putExtra("tag","Dinner"));
            }
        } );
//        calendar.setOnClickListener( new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                addEvent("Diet Stats:\n Set Exercise Time","Home");
//            }
//        } );
        btnexer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity().getApplication(),Exercise.class)
                .putExtra("tag","Exercise"));
            }
        });
//        input = rootView.findViewById(R.id.input);
        builder = new Retrofit.Builder()
                .client(httpClient.build())
                .baseUrl("https://api.edamam.com/api/food-database/")
                .addConverterFactory(GsonConverterFactory.create());
        retrofit = builder.build();

        builder1 = new Retrofit.Builder()
                .client(httpClient.build())
                .baseUrl("https://trackapi.nutritionix.com/v2/")
                .addConverterFactory(GsonConverterFactory.create());
        retrofit1 = builder1.build();

        rawingredient = "one large red apple";
        rawingredient = "1 medium burger";
        ingredient = rawingredient.replaceAll(" ","%20");
        Log.i("rsultecnos",ingredient);


        return rootView;
    }

//    public void LoadData(){
//        Nutrition userCleint= retrofit.create(Nutrition.class);
//        Call<FoodData> call = userCleint.loadFoodData(ingredient,app_id,app_key);
//        call.enqueue(new Callback<FoodData>() {
//            @Override
//            public void onResponse(Call<FoodData> call, Response<FoodData> response) {
//                if(response.isSuccessful()){
//                    FoodData foodData = response.body();
//                    foodURi = foodData.getParsed().get(0).getFood().getUri();
//                    measurURi = foodData.getParsed().get(0).getMeasure().getUri();
//                    Log.i("gotitm",foodData.getParsed().get(0).getFood().getUri());
//                    LoadNutrition(1,measurURi,foodURi);
//                }else {
//                    Log.i("failedre","yes");
//                }
//            }
//
//            @Override
//            public void onFailure(Call<FoodData> call, Throwable t) {
//                Log.i("failedre",t.getMessage());
//            }
//        });
//    }
//
//    public void LoadNutrition(int quatity,String measureURI,String foodURI){
//        Nutrition usercleint = retrofit.create(Nutrition.class);
//        RawBody body = new RawBody();
//        body.setYield(1);
//        RawBody.ingredients ingredients = body.new ingredients(quatity,measureURI,foodURI);
//        List<RawBody.ingredients> ingredientsList = new ArrayList<>();
//        ingredientsList.add(ingredients);
//        body.setIngredients(ingredientsList);
//
//        Call<NutrientResult> call = usercleint.loadNutritionResult("application/json",body,"396ac60a","f8dabb6991b0005254e587daff28d50b");
//        call.enqueue(new Callback<NutrientResult>() {
//            @Override
//            public void onResponse(Call<NutrientResult> call, Response<NutrientResult> response) {
//
//                if(response.isSuccessful()){
//                    NutrientResult result = response.body();
//                    Log.i("resultitem",result.getCalories());
//                }else {
//
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<NutrientResult> call, Throwable t) {
//                Log.i("asdfava",t.getMessage());
//            }
//        });
//    }


   public void updateIntake(){
       double d = 0;
        if(btot!=null&&ltot!=null&&dtot!=null)
             d = btot.getNf_calories()+ltot.getNf_calories()+dtot.getNf_calories();
       totalcalin.setText(Math.round(d)+"    ");
       totalcalburn.setText(Math.round(etot)+"   ");
       remaining.setText(Math.round(d-etot)+"   ");
   }

    public void addEvent(String title, String location) {
        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData( CalendarContract.Events.CONTENT_URI)
                .putExtra( CalendarContract.Events.TITLE, title)
                .putExtra( CalendarContract.Events.EVENT_LOCATION, location);
        startActivity(intent);

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
                    NutrientResult1 result = response.body().getFoods().get(0);
                    if(result!= null){
                        Log.i("calories",result.getNf_calories()+"");
                        Toast.makeText(getContext(), "You gained "+result.getNf_calories()+" calories", Toast.LENGTH_SHORT).show();
                    }
                }else {


                }
            }

            @Override
            public void onFailure(Call<NutrintionResultList> call, Throwable t) {

            }
        });
    }

    class DownloadImage extends AsyncTask<String,Void,Bitmap> {
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

    class CustomAdpter1 extends ArrayAdapter {

        Context context;
        List<ExercisePOJO> exercisePOJOS;

        public CustomAdpter1(@NonNull Context context, int resource, @NonNull List objects) {
            super(context, resource, objects);
            this.context = context;
            exercisePOJOS = objects;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


            View vie = convertView;
            if (vie == null)
                vie = LayoutInflater.from(context).inflate(R.layout.food_list_item, parent, false);

            ExercisePOJO ff1 = exercisePOJOS.get(position);
            ImageView img1 = vie.findViewById(R.id.foodImage);
            new DownloadImage(img1).execute(ff1.getPhoto().getThumb());
            TextView t1 = vie.findViewById(R.id.qty);
            t1.setText(ff1.getName());
            TextView t2 = vie.findViewById(R.id.tag_name);
            t2.setText(ff1.getDuration_min() + "");
            TextView t3 = vie.findViewById(R.id.unit);
            t3.setText(ff1.getNf_calories() + "");
            return vie;
        }
    }

}
