package com.alvarezaaronai.sed.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alvarezaaronai.sed.PatientAdherenceActivity;
import com.alvarezaaronai.sed.R;
import com.alvarezaaronai.sed.Models.Patient;
import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class PhysicianRecyclerViewAdapter extends RecyclerView.Adapter<PhysicianRecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "PhysicianRecyclerViewAd";

    private ArrayList<Patient> mPatients = new ArrayList<>();;
    private Context mContext;

    public PhysicianRecyclerViewAdapter(ArrayList<Patient> patients, Context context) {

        for(int i = 0; i < patients.size(); i++) {
            Log.d(TAG, "PhysicianRecyclerViewAdapter: PATIENT!" + patients.get(i));
        }
        if(mPatients == null) {
            Log.d(TAG, "PhysicianRecyclerViewAdapter: Empty patients arraylist");
            mPatients = new ArrayList<>();
        } else {
            mPatients = patients;
        }

        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_patient_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Log.d(TAG, "onBindViewHolder: Patient Id: " + mPatients.get(position).getPatient_id());

        // Use Picasso to load image into CircleImageView
        Picasso.get().load(mPatients.get((position)).getPatient_image()).into(holder.patientImage);

        String fullName = mPatients.get(position).getFirst_name() + " " + mPatients.get(position).getLast_name();
        holder.patientName.setText(fullName);

        holder.patientEmail.setText(mPatients.get(position).getEmail());
    }

    @Override
    public int getItemCount() {
        return mPatients.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        CircleImageView patientImage;
        TextView patientName;
        TextView patientEmail;
        MaterialButton patientViewButton;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            patientImage = itemView.findViewById(R.id.patient_image);
            patientName = itemView.findViewById(R.id.patient_name);
            patientEmail = itemView.findViewById(R.id.patient_email);

            patientViewButton = itemView.findViewById(R.id.patient_view_button);
            patientViewButton.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(mContext, PatientAdherenceActivity.class);
            intent.putExtra("patient_id", mPatients.get(getAdapterPosition()).getPatient_id());
            mContext.startActivity(intent);
        }
    }
}
