package com.alvarezaaronai.sed;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.alvarezaaronai.sed.adapters.PhysicianRecyclerViewAdapter;
import com.alvarezaaronai.sed.models.Patient;
import com.alvarezaaronai.sed.models.Physician;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PhysicianProfileActivity extends AppCompatActivity {

    // TODO: Add patient_id to list of patients under Physician
    // TODO: Add patient_id to POJO "Patient"
    /*
        TODO: Use retrieved patient_id and pass it as an Extra
        when switching over to graph Activity. This will let us
        make a call to the Patient Table to retrieve specific
        Patient information needed to create their graph.
     */

    private RecyclerView mRecyclerView;
    private ArrayList<Patient> mPatients = new ArrayList<>();

    private TextView mPhysicianName;

    private ImageView mPhysicianImage;

    private static final String TAG = "PhysicianProfile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_physician_profile);

        mPhysicianName = findViewById(R.id.physician_name);
        mPhysicianImage = findViewById(R.id.physician_image);

        initPhysicianProfile();
    }

    public void initPhysicianProfile() {
        // For Testing purposes only!!!
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://9l27z1ir6b.execute-api.us-west-2.amazonaws.com/prod/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AWSPhysicianInfoAPI awsPhysicianInfoAPI = retrofit.create(AWSPhysicianInfoAPI.class);
        Call<Physician> call = awsPhysicianInfoAPI.getPhysician();
        call.enqueue(new Callback<Physician>() {
            @Override
            public void onResponse(Call<Physician> call, Response<Physician> response) {
                if(!response.isSuccessful()) {
                    System.out.println("Status Code:" + response.code());
                    return;
                }
                System.out.println("Success");
                // System.out.println(response);
                Physician physician = response.body();
                Log.d(TAG, "onResponse: response " + response);
                Log.d(TAG, "onResponse: Physician " + physician.toString());

                // Converting array to ArrayList which is of type List
                mPatients = physician.getPatientModel();

                String physicianFullName = physician.getFirst_name() + " " + physician.getLast_name();
                mPhysicianName.setText(physicianFullName);

                String physicianImgURL = physician.getPhysician_image();
                Picasso.get().load(physicianImgURL).into(mPhysicianImage);

                // Initialize RecyclerView after retrieving Physician info
                initRecyclerView();
            }

            @Override
            public void onFailure(Call<Physician> call, Throwable t) {
                System.out.println("API call to AWS Failed");
                System.out.println(t);
            }
        });
    }

    public void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: " + mPatients);
        mRecyclerView = findViewById(R.id.recycler_view_patients);

        PhysicianRecyclerViewAdapter adapter = new PhysicianRecyclerViewAdapter(mPatients);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
