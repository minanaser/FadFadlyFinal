package com.example.MNM;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;


public class Filtering2 extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    Button next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtering2);

        next= (Button) findViewById(R.id.next);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);


        getDataFromWeka();

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(Filtering2.this,ShowDoctorsActivity.class));
                finish();
            }
        });

    }

    String self, family,friends, emotion, work, loss, general, decision;
    private void getDataFromWeka() {
        for (int i=0;i < Self5.list.size();i++)
        {
            Log.i("weka of filtr element "+i, String.valueOf(Self5.list.get(i)));

        }

        self=Self5.list.get(0);
        family=Self5.list.get(1);
        friends=Self5.list.get(2);
        emotion=Self5.list.get(3);
        work=Self5.list.get(4);
        loss=Self5.list.get(5);
        general=Self5.list.get(6);
        decision=Self5.list.get(7);
        addToDb();
    }

    private void addToDb() {
        progressDialog.setMessage("please wait..");

        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("self",self);
        hashMap.put("family",family);
        hashMap.put("friends",friends);
        hashMap.put("emotion",emotion);
        hashMap.put("work",work);
        hashMap.put("loss",loss);
        hashMap.put("general",general);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("user");
        reference.child(firebaseAuth.getUid()).child("WekaResults").updateChildren(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressDialog.dismiss();
                        Log.i("Filtering2","weka results added successfully");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Log.i("Filtering2","weka results not added "+ e.getLocalizedMessage());
                    }
                });

    }
}
