package com.example.codex_pc.mob_dco;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DisplayDiagnosis extends AppCompatActivity {
    ListView display_dia;
    String token = null;
    private Retrofit.Builder builder;
    private Retrofit retrofit;
    private ProgressBar progressBar;
    private List<HealthDiagnosis> diagnoses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_diagnosis);
        Bundle bundle = getIntent().getBundleExtra("list_of_dignose");
        diagnoses = bundle.getParcelableArrayList("List");
        token = getIntent().getBundleExtra("list_of_dignose").getString("token");
        progressBar = findViewById(R.id.progress_detail);
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);
        builder = new Retrofit.Builder()
                .baseUrl("https://healthservice.priaid.ch/")
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create());
        retrofit = builder.build();

        display_dia = findViewById(R.id.list_display_diagnpse);
        display_dia.setAdapter(new MainAdapter(this, R.id.list_display_diagnpse, diagnoses));

    }

    public IssueInfo loadIssueInfo(int ID) {
        progressBar.setVisibility(View.VISIBLE);
        final IssueInfo[] issueInfo = new IssueInfo[1];
        retro userClient = retrofit.create(retro.class);
        Call<IssueInfo> call = userClient.loadIssueINfor(ID, token, "en-gb", "json");
        call.enqueue(new Callback<IssueInfo>() {
            @Override
            public void onResponse(Call<IssueInfo> call, Response<IssueInfo> response) {
                if (response.isSuccessful()) {
                    issueInfo[0] = response.body();
                    Intent intent = new Intent(DisplayDiagnosis.this,DetailedDiagnosis.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("Info",issueInfo[0]);
                    intent.putExtra("des",bundle);
                    startActivity(intent);
                    progressBar.setVisibility(View.INVISIBLE);
                } else {
                    Toast.makeText(DisplayDiagnosis.this, response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<IssueInfo> call, Throwable t) {
                Log.i("failureinissue", "yes");
            }
        });
        return issueInfo[0];
    }

    class MainAdapter extends ArrayAdapter<HealthDiagnosis> {

        private Context context;
        private List<HealthDiagnosis> diagnoses = new ArrayList<>();


        public MainAdapter(@NonNull Context context, int resource, @NonNull List<HealthDiagnosis> objects) {
            super(context, resource, objects);
            this.context = context;
            diagnoses = objects;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View view = convertView;
            if (view == null)
                view = LayoutInflater.from(context).inflate(R.layout.diagnose_display_list_item, parent, false);


            final HealthDiagnosis healthDiagnosis = diagnoses.get(position);

            TextView name = view.findViewById(R.id.Name);
            name.setText(healthDiagnosis.getIssue().getName());

            ProgressBar accuraccy = view.findViewById(R.id.accuracy);
            accuraccy.setProgress((int) healthDiagnosis.getIssue().getAccuracy());

            List<String> specialistName = new ArrayList<>();
            for (DiagnosedSpecialisation a : healthDiagnosis.getSpecialisation()) {
                specialistName.add(a.getName());
            }
            ListView specialist = view.findViewById(R.id.specialist);
            ArrayAdapter adapter = new ArrayAdapter(context, R.layout.specialist_item, specialistName);
            specialist.setAdapter(adapter);

            Button moreInfo = view.findViewById(R.id.more_info);
            moreInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    loadIssueInfo(healthDiagnosis.getIssue().getID());
                }
            });
            return view;
        }
    }}
