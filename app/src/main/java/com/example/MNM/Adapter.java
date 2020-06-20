package com.example.MNM;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.HolderData> {
    private Context context;
    public ArrayList<Data> data;
    static String doctorID;

    public Adapter(Context context, ArrayList<Data> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public HolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.row,parent,false);
        return new HolderData(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderData holder, int position) {
        Data modelData = data.get(position);
        final String hisUID = modelData.getUid();
        String name2 = modelData.getName();
        String speciality2 = modelData.getSpeciality();
        String profileImage = modelData.getProfileImage();
        holder.name.setText(name2);
        holder.speciality.setText(speciality2);

        try {
            Picasso.get().load(profileImage).placeholder(R.drawable.ic_person_blue_24dp).into(holder.profile);
        }
        catch (Exception e){
            holder.profile.setImageResource(R.drawable.ic_person_blue_24dp);
             }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,ChatPatient.class);
                //  intent.putExtra("hisUid", hisUid);
                doctorID=hisUID;
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        if (data==null)
            return 0;
        return data.size();
    }


    class HolderData extends RecyclerView.ViewHolder{
        private TextView name,speciality;
        private ImageView profile;

        public HolderData(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            speciality = itemView.findViewById(R.id.speciality);
            profile = itemView.findViewById(R.id.profile);

        }
    }
}


