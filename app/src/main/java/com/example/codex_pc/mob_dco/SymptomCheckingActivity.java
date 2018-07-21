package com.example.codex_pc.mob_dco;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SymptomCheckingActivity extends AppCompatActivity {

    String isRedFlag="";
    private Button clearInput;
    private Button btn_getDignosis;
    private List<HelthItem> symptoms;
    private Button btn;
    private String computedHashString = "";
    private String token;

    private Retrofit.Builder builder;
    private Retrofit retrofit;
    private AutoCompleteTextView sympotm_seletion;
    List<HealthDiagnosis> diagnosesResponse;
    String a[] = {"indis","afgan","paki"};
    HashMap<String,Integer> sympsHm = new HashMap<>();
    HashMap<String ,Integer> proposedHm = new HashMap<>();
    HashMap<String ,Integer> issues = new HashMap<>();
    ArrayAdapter<String> arrayAdapter;
    List<Integer> sympIds = new ArrayList<>();
    ListView display_selected_sympoms;
    List<String> proposeSYm;
    List<String> display = new ArrayList<>();
    ListView displayProposedSymptoms;
    Adapter_display adapter;
    ArrayAdapter adapter1;
    ProgressBar overallProgress;
    LinearLayout progressLayout;
    TextView progressText;
    Spinner gender;
    Spinner yob;
    String gen;
    int year_of_birth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symptom_checking);
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);
        overallProgress = findViewById(R.id.progress);
        progressLayout = findViewById(R.id.progressDisplay);
        progressText = findViewById(R.id.progressText);
        gender = findViewById(R.id.gender);
        ArrayAdapter<CharSequence> adt = ArrayAdapter.createFromResource(this,R.array.genders,android.R.layout.simple_spinner_item);
        adt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gender.setAdapter(adt);

        yob = findViewById(R.id.year_of_birth);
        ArrayAdapter<CharSequence> adr = ArrayAdapter.createFromResource(this,R.array.yob,android.R.layout.simple_spinner_item);
        adr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yob.setAdapter(adr);

        final InputMethodManager imm = (InputMethodManager) getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
        btn_getDignosis =(Button) findViewById(R.id.get_dignosis);
        clearInput = findViewById(R.id.clearInput);
        builder = new Retrofit.Builder()
                .baseUrl("https://healthservice.priaid.ch/")
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create());
        retrofit = builder.build();
        sympotm_seletion = findViewById(R.id.Select_symptoms);
        display_selected_sympoms = findViewById(R.id.display_symptom);
        displayProposedSymptoms = (ListView) findViewById(R.id.proposedSym);
        adapter = new Adapter_display(this,R.id.proposedSym,display);
        display_selected_sympoms.setAdapter(adapter);
        try {
            LoadToken("vr_karthik","XEQtVNyeUmdM5E8j","https://authservice.priaid.ch/login");
        } catch (Exception e) {
            e.printStackTrace();
        }
        gettoken();
        btn =(Button) findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gen = gender.getSelectedItem().toString();
                year_of_birth =  Integer.parseInt(yob.getSelectedItem().toString());
                String selsy = sympotm_seletion.getText().toString();
                if (!gen.equals("---")&&year_of_birth != 0000) {
                    if (!selsy.equals("") && sympsHm.containsKey(selsy)) {
                        sympIds.add(sympsHm.get(selsy));
                        display.add(selsy);
                        sympotm_seletion.setText("");
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        adapter.notifyDataSetChanged();
                        propossedSymptoms(gen, year_of_birth);
                        IsRedFlagSet(sympsHm.get(selsy));
                    } else
                        Toast.makeText(SymptomCheckingActivity.this, "Please enter valid symptoms(Search for related symptoms).", Toast.LENGTH_SHORT).show();
                }else {
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    Toast.makeText(SymptomCheckingActivity.this, "Select gender and year of birth.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn_getDignosis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!sympIds.isEmpty()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(SymptomCheckingActivity.this);
                    builder.setMessage("I understand that this app does not replace in any case a medical consultation. The information in the application " +
                            "is supplement, not substitute for, the expertise and judgemental health care professionals.")
                            .setPositiveButton("ACCEPT", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    load_diagnosis();
                                }
                            })
                            .setNegativeButton("INGNORE", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
                else
                    Toast.makeText(SymptomCheckingActivity.this, "Symptoms Not Selected.Please select symptoms for any query.", Toast.LENGTH_SHORT).show();
            }
        });
        clearInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.clear();
                adapter.notifyDataSetChanged();
                if(adapter1!=null)
                {
                    adapter1.clear();
                    adapter1.notifyDataSetChanged();
                }
                sympIds.clear();
                display.clear();
                if(proposeSYm!=null)
                    proposeSYm.clear();
                sympotm_seletion.setText("");
            }
        });
    }

    private void LoadToken(String username, String password, String url) throws Exception {

        SecretKeySpec keySpec = new SecretKeySpec(
                password.getBytes(),
                "HmacMD5");
        try {
            Mac mac = Mac.getInstance("HmacMD5");
            mac.init(keySpec);
            byte[] result = mac.doFinal(url.getBytes());
            computedHashString = android.util.Base64.encodeToString(result, android.util.Base64.URL_SAFE);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new Exception("Can not create token (NoSuchAlgorithmException)");
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            throw new Exception("Can not create token (InvalidKeyException)");
        }
    }

    public void gettoken(){
        progressLayout.setVisibility(View.VISIBLE);
        overallProgress.setVisibility(View.VISIBLE);
        progressText.setText("Loading Symptoms              Please wait...");
        sympotm_seletion.setCursorVisible(false);
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://authservice.priaid.ch/")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        retro userClient = retrofit.create(retro.class);

        String base= "Bearer vr_karthik:" + computedHashString;
        Log.i("bearer",base);
        String authHeader = base.trim();
        Call<AccessToken> call = userClient.getToken(authHeader);
        call.enqueue(new Callback<AccessToken>() {
            @Override
            public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                token = response.body().getToken();
                loadsymp();
            }
            @Override
            public void onFailure(Call<AccessToken> call, Throwable t) {
                gettoken();
            }
        });
    }

    public void loadsymp(){
        String extraArgs = token;
        retro userClient = retrofit.create(retro.class);
        Call<List<HelthItem>> call = userClient.loadSymptoms(extraArgs,"en-gb","json");
        call.enqueue(new Callback<List<HelthItem>>() {
            @Override
            public void onResponse(Call<List<HelthItem>> call, Response<List<HelthItem>> response) {
                if (response.isSuccessful()) {
                    symptoms = response.body();
                    for (HelthItem a : symptoms) {
                        sympsHm.put(a.getName(), a.getID());
                    }
                    arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, new ArrayList<String>(sympsHm.keySet()));
                    sympotm_seletion.setAdapter(arrayAdapter);
                    progressLayout.setVisibility(View.GONE);
                    sympotm_seletion.setCursorVisible(true);
                    Toast.makeText(SymptomCheckingActivity.this, "Symptoms Loaded successfully. You can start now...", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SymptomCheckingActivity.this, response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                    loadsymp();
                    progressLayout.setVisibility(View.GONE);
                }
            }
            @Override
            public void onFailure(Call<List<HelthItem>> call, Throwable t) {
                loadsymp();
                progressLayout.setVisibility(View.GONE);
            }
        });
    }

    public void propossedSymptoms(String gender,int yob){
        progressLayout.setVisibility(View.VISIBLE);
        progressText.setText("Loading Proposed symptoms....");
        if(adapter1!= null){
            adapter1.clear();
            proposeSYm.clear();
            adapter1.notifyDataSetChanged();
            proposedHm.clear();
        }
        retro userClient = retrofit.create(retro.class);
        String serialisedIds = null;
        try {
            serialisedIds = new ObjectMapper().writeValueAsString(sympIds);
            Log.i("serializd",serialisedIds);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        Call<List<HelthItem>> call = userClient.proposedSymptoms(serialisedIds,gender,yob,token,"en-gb","json");
        call.enqueue(new Callback<List<HelthItem>>() {
            @Override
            public void onResponse(Call<List<HelthItem>> call, Response<List<HelthItem>> response) {
                if (response.isSuccessful() && !response.body().isEmpty()) {
                    Log.i("Frsfd", "Entere here");
                    for (HelthItem a : response.body()) {
                        proposedHm.put(a.getName(), a.getID());
                    }
                    proposeSYm = new ArrayList<String>(proposedHm.keySet());
                    adapter1 = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, proposeSYm);
                    displayProposedSymptoms.setAdapter(adapter1);
                    displayProposedSymptoms.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            String key = adapterView.getItemAtPosition(i).toString();
                            display.add(key);
                            adapter.notifyDataSetChanged();
                            sympIds.add(proposedHm.get(key));
                            propossedSymptoms(gen, year_of_birth);
                        }
                    });
                    progressLayout.setVisibility(View.GONE);
                } else if (response.body()!=null&&response.body().isEmpty()) {
                    Log.i("fERd", "nullas");
                    Toast.makeText(SymptomCheckingActivity.this, "symptoms you have selected are not related!", Toast.LENGTH_SHORT).show();
                    adapter1.clear();
                    adapter1.notifyDataSetChanged();
                    progressLayout.setVisibility(View.GONE);
                } else {
                    Log.i("Infere", response.errorBody().toString());
                    progressLayout.setVisibility(View.GONE);
                    gettoken();
                    propossedSymptoms(gen,year_of_birth);
                }
            }
            @Override
            public void onFailure(Call<List<HelthItem>> call, Throwable t) {
                Log.i("failret",t.getMessage());
                Toast.makeText(SymptomCheckingActivity.this, "Please check Your Internet connection.", Toast.LENGTH_SHORT).show();
                progressLayout.setVisibility(View.GONE);
            }
        });
    }

    public void loadIssues(){

        retro userClient = retrofit.create(retro.class);
        Call<List<HelthItem>> call = userClient.issuesLoad(token,"en-gb","json");
        call.enqueue(new Callback<List<HelthItem>>() {
            @Override
            public void onResponse(Call<List<HelthItem>> call, Response<List<HelthItem>> response) {
                if(response.isSuccessful()){
                    for(HelthItem a:response.body()){
                        issues.put(a.getName(),a.getID());
                    }
                }else {
                    Toast.makeText(SymptomCheckingActivity.this, response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<HelthItem>> call, Throwable t) {

            }
        });
    }

    public void load_diagnosis(){
        progressLayout.setVisibility(View.VISIBLE);
        progressText.setText("Loading diagnosis...");
        retro userClient = retrofit.create(retro.class);
        String serialisedIds = null;
        try {
            serialisedIds = new ObjectMapper().writeValueAsString(sympIds);
            Log.i("serializd",serialisedIds);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        Call<List<HealthDiagnosis>> call = userClient.loadDiagnosi(serialisedIds,gen,year_of_birth,token,"en-gb","json");
        call.enqueue(new Callback<List<HealthDiagnosis>>() {
            @Override
            public void onResponse(Call<List<HealthDiagnosis>> call, Response<List<HealthDiagnosis>> response) {
                if(response.isSuccessful()&& !response.body().isEmpty()){
                    diagnosesResponse = response.body();
                    Log.i("infort",response.body().get(0).getIssue().getName());
                    Intent intent = new Intent(SymptomCheckingActivity.this,DisplayDiagnosis.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("List", (ArrayList<? extends Parcelable>) diagnosesResponse);
                    bundle.putString("token",token);
                    intent.putExtra("list_of_dignose",bundle);
                    startActivity(intent);
                    Log.i("Sussd","yes");
                    progressLayout.setVisibility(View.GONE);
                }else if(response.body().isEmpty()){
                    progressLayout.setVisibility(View.GONE);
                    Toast.makeText(SymptomCheckingActivity.this, "There is no analysis available for the selected conditions.", Toast.LENGTH_SHORT).show();
                }else {
                    progressLayout.setVisibility(View.GONE);
                    Toast.makeText(SymptomCheckingActivity.this, response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<HealthDiagnosis>> call, Throwable t) {
                progressLayout.setVisibility(View.GONE);
            }
        });
    }

    public void IsRedFlagSet(int ID){
        retro userClient = retrofit.create(retro.class);
        Call<String> call = userClient.isRed(ID,token,"en-gb","json");
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()&&!response.body().equals("")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(SymptomCheckingActivity.this);
                    builder.setTitle("Alert")
                            .setMessage("You have selected a symptom which requires a prompt check with a medical doctor.")
                            .setPositiveButton("Get Treatment", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    startActivity( new Intent( SymptomCheckingActivity.this,MapsActivity.class ) );

                                }
                            })
                            .setNegativeButton("Ignore", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                    Log.i("respof",response.body());
                } else if(response.body().equals("")){
                    Log.i("asdfas","nullasd");
                }else {
                    Toast.makeText(SymptomCheckingActivity.this, response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    class Adapter_display extends ArrayAdapter{
        Context context;
        List<String> dis_string;

        public Adapter_display(@NonNull Context context, int resource, @NonNull List objects) {
            super(context, resource, objects);
            this.context = context;
            dis_string = objects;
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View view = convertView;
            if (view == null)
                view = LayoutInflater.from(context).inflate(R.layout.display_item, parent, false);

            final String str = dis_string.get(position);

            TextView dis = view.findViewById(R.id.displ);
            dis.setText(str);
            Log.i("trigger","yer");

            Button btn = view.findViewById(R.id.remove);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    display.remove(position);
                    sympIds.remove(sympsHm.get(str));
                    remove(display_selected_sympoms.indexOfChild(view));
                    if(!sympIds.isEmpty())
                        propossedSymptoms(gen,year_of_birth);
                    else {
                        adapter1.clear();
                        adapter1.notifyDataSetChanged();
                    }
                }
            });
            return view;
        }
    }


}
