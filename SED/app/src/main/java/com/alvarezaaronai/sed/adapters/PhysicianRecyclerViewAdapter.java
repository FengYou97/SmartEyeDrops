package com.alvarezaaronai.sed.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alvarezaaronai.sed.R;
import com.alvarezaaronai.sed.Models.Patient;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class PhysicianRecyclerViewAdapter extends RecyclerView.Adapter<PhysicianRecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "PhysicianRecyclerViewAd";

    private ArrayList<Patient> mPatients = new ArrayList<>();

    public PhysicianRecyclerViewAdapter(ArrayList<Patient> patients) {
        mPatients = patients;
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

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView patientImage;
        TextView patientName;
        TextView patientEmail;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            patientImage = itemView.findViewById(R.id.patient_image);
            patientName = itemView.findViewById(R.id.patient_name);
            patientEmail = itemView.findViewById(R.id.patient_email);
        }
    }
}
