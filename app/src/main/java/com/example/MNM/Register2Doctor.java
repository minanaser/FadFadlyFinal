package com.example.MNM;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;

public class Register2Doctor extends AppCompatActivity {
    protected EditText nationalIdRegisterEdittext;
    protected EditText bachelorEdit;

    protected Button submit;

    ArrayList<String> selection = new ArrayList<String>();

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2_doctor);
        nationalIdRegisterEdittext = (EditText) findViewById(R.id.nationalId_register_edittext);
        bachelorEdit = (EditText) findViewById(R.id.bachelor_edit);
        submit = findViewById(R.id.submit);
        firebaseAuth = FirebaseAuth.getInstance();
        selection.add(0,"false");
        selection.add(1,"false");
        selection.add(2,"false");
        selection.add(3,"false");
        selection.add(4,"false");
        selection.add(5,"false");
        selection.add(6,"false");

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData();
            }
        });

    }

    String value;
    public void onCheckBoxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();

        switch (view.getId()){
            case R.id.sself:
                if (checked){
                    value = "true";
                    Log.i("select", "checked " + value);
                }
                selection.set(0,"true");
                break;

            case R.id.ffamily:
                if (checked){
                    value = "true";
                    Log.i("select", "checked " + value);
                }
                selection.set(1,"true");
                break;

            case R.id.ffriends:
                if (checked){
                    value = "true";
                    Log.i("select", "checked " + value);
                }
                selection.set(2,"true");
                break;

            case R.id.eemo:
                if (checked){
                    value = "true";
                    Log.i("select", "checked " + value);
                }
                selection.set(3,"true");
                break;

            case R.id.wwork:
                if (checked){
                    value = "true";
                    Log.i("select", "checked " + value);
                }
                selection.set(4,"true");
                break;

            case R.id.lloss:
                if (checked){
                    value = "true";
                    Log.i("select", "checked " + value);
                }
                selection.set(5,"true");
                break;

            case R.id.ggeneral:
                if (checked){
                    value = "true";
                    Log.i("select", "checked " + value);
                }
                selection.set(6,"true");
                break;
        }
    }

    String concatenation = "";
    String self,family,friends,emotion, work, loss,general;

    private void getData() {
        for (int i=0;i < selection.size();i++)
        {
            Log.i("doctor selection "+i, String.valueOf(selection.get(i)));

        }

        self=selection.get(0);
        family=selection.get(1);
        friends=selection.get(2);
        emotion=selection.get(3);
        work=selection.get(4);
        loss=selection.get(5);
        general=selection.get(6);

        addToDb();
    }


    String NationalId, Bachelor;
    private void addToDb() {
        NationalId = nationalIdRegisterEdittext.getText().toString();
        Bachelor = bachelorEdit.getText().toString();

        if (NationalId.trim().isEmpty()){
            nationalIdRegisterEdittext.setError("required");
            return;
        }

        if(Bachelor.trim().isEmpty()){
            bachelorEdit.setError("required");
            return;
        }

        HashMap<String,Object> hashMap = new HashMap<>();

        if(selection.get(0).equals("true"))
            concatenation= concatenation+" self ,";

        if(selection.get(1).equals("true"))
            concatenation= concatenation+" family ,";

        if(selection.get(2).equals("true"))
            concatenation= concatenation+" friends ,";

        if(selection.get(3).equals("true"))
            concatenation= concatenation+" emotion ,";

        if(selection.get(4).equals("true"))
            concatenation= concatenation+" work ,";

        if(selection.get(5).equals("true"))
            concatenation= concatenation+" loss ,";

        if(selection.get(6).equals("true"))
            concatenation= concatenation+" general ";


        hashMap.put("speciality", concatenation);
        hashMap.put("Bachelor", Bachelor);
        hashMap.put("NationalID", NationalId);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Doctors");
        reference.child(firebaseAuth.getUid()).updateChildren(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        startActivity(new Intent(Register2Doctor.this,TransactionActivity.class));
                        Log.i("Register2","speciality added successfully");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Log.i("Register2","speciality not added "+ e.getLocalizedMessage());
                    }
                });

    }
}



