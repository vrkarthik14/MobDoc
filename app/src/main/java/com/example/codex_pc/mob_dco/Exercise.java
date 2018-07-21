package com.example.codex_pc.mob_dco;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
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

public class Exercise extends AppCompatActivity {

    private Retrofit.Builder builder,builder1;
    private Retrofit retrofit,retrofit1;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    EditText inputExercise;
    Button complete;
    ImageButton enterexerciseSpeech;
    String calories;
    List<ExercisePOJO> result;
    ListView exerList;
    String tag;
    String user;
    String formattedDate;
    DatabaseReference mreference;
    CustomAdpter adpter;
    double subTot = 0;
    double totalCaloriSpent = 0;
    Button uplaosd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);
        Bundle bundle = getIntent().getExtras();
        tag = bundle.getString("tag");
        user = FirebaseAuth.getInstance().getCurrentUser().getUid();

        exerList = findViewById(R.id.exerList);
        inputExercise = findViewById(R.id.inputexcise);
        complete = findViewById(R.id.Complete);
        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!inputExercise.getText().toString().isEmpty()){
                    calculateCaloriSpentWhileExersize(inputExercise.getText().toString());

                }
            }
        });
        enterexerciseSpeech = findViewById(R.id.enterExSpeechf);
        enterexerciseSpeech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                promtSpeechInput();
            }
        });
        builder1 = new Retrofit.Builder()
                .client(httpClient.build())
                .baseUrl("https://trackapi.nutritionix.com/v2/")
                .addConverterFactory(GsonConverterFactory.create());
        retrofit1 = builder1.build();

        Calendar c = Calendar.getInstance();
        System.out.println("Current time =&gt; "+c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        formattedDate = df.format(c.getTime());

        mreference = FirebaseDatabase.getInstance().getReference().child("Patient").child(user).child("DietFeed").child(formattedDate).child(tag);
        mreference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("total_calori_spent"))
                totalCaloriSpent = dataSnapshot.child("total_calori_spent").getValue(Double.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

       uplaosd = findViewById(R.id.save_exercise);
        uplaosd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(ExercisePOJO exercisePOJO:result){
                    mreference.child("components").push().setValue(exercisePOJO);
                }
                mreference.child("total_calori_spent").setValue(totalCaloriSpent+subTot);
                inputExercise.setText("");
                adpter.clear();
                adpter.notifyDataSetChanged();
                uplaosd.setEnabled(false);
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
                    inputExercise.setText(result.get(0));
                }
                break;
            }

        }
    }
    public void calculateCaloriSpentWhileExersize(String query){

        Nutrition usercleint = retrofit1.create(Nutrition.class);
        ExersiceProfile profile = new ExersiceProfile(query,"male");
        Call<ExersiseResult> call = usercleint.getExersizeReslut("application/json",
                profile,"6097f30a","be5b65a01585e563c8709d0b6295e56b","0");
        call.enqueue(new Callback<ExersiseResult>() {
            @Override
            public void onResponse(Call<ExersiseResult> call, Response<ExersiseResult> response) {
                result = response.body().getExercises();
                uplaosd.setEnabled(true);
                for(ExercisePOJO exersiseResult:result){
                    subTot+=exersiseResult.getNf_calories();
                }
                adpter = new CustomAdpter(getApplicationContext(),R.layout.food_list_item,result);
                exerList.setAdapter(adpter);
            }

            @Override
            public void onFailure(Call<ExersiseResult> call, Throwable t) {

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

    class CustomAdpter extends ArrayAdapter {

        Context context;
        List<ExercisePOJO> exercisePOJOS;
        public CustomAdpter(@NonNull Context context, int resource, @NonNull List objects) {
            super(context, resource, objects);
            this.context = context;
            exercisePOJOS = objects;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


            View vie = convertView;
            if(vie == null)
                vie = LayoutInflater.from(context).inflate(R.layout.food_list_item,parent,false);

            ExercisePOJO ff1 = exercisePOJOS.get(position);
            ImageView img1 = vie.findViewById(R.id.foodImage);
            new DownloadImage(img1).execute(ff1.getPhoto().getThumb());
            TextView t1 = vie.findViewById(R.id.qty);
            t1.setText(ff1.getName());
            TextView t2 = vie.findViewById(R.id.tag_name);
            t2.setText(ff1.getDuration_min()+"");
            TextView t3 = vie.findViewById(R.id.unit);
            t3.setText(ff1.getNf_calories()+"");
            return vie;
        }
    }
}
