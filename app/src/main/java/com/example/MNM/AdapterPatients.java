package com.example.MNM;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterPatients extends RecyclerView.Adapter<AdapterPatients.MyHolder>{

    Context context;
    List<ModelPatient> modelPatientList;
    static String userID;

    public AdapterPatients(Context context, List<ModelPatient> modelPatientList) {
        this.context = context;
        this.modelPatientList = modelPatientList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
       View view = LayoutInflater.from(context).inflate(R.layout.row_patients,viewGroup,false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myholder, int position) {
        ModelPatient modelPatient = modelPatientList.get(position);
        final String hisUid = modelPatient.getPaientID();
        String patientImage = modelPatient.getImage();
        String patientName = modelPatient.getName();
        String patientEmail = modelPatient.getEmail();

        myholder.mNameTv.setText(patientName);
        myholder.mEmailTv.setText(patientEmail);
        Log.i("false",hisUid+"ss");

        try{
            Picasso.get().load(patientImage)
                    .placeholder(R.drawable.ic_person_blue_24dp).into(myholder.mAvatarIv);
        }catch (Exception e){

        }
        myholder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,ChatActivity.class);
              //  intent.putExtra("hisUid", hisUid);
                userID =hisUid;
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return modelPatientList.size();
    }

     class MyHolder extends RecyclerView.ViewHolder{
         TextView mNameTv;
         TextView mEmailTv;
         ImageView mAvatarIv;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            mAvatarIv = itemView.findViewById(R.id.avatarIv);
            mNameTv = itemView.findViewById(R.id.nameTv);
            mEmailTv = itemView.findViewById(R.id.emailTv);
        }
    }

}
