package com.example.MNM;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterUser extends AppCompatActivity {
     protected TextView RegisterAsDoc;
     protected Button submit;

    protected TextView title;
    protected TextView username;
    protected EditText usernameText;
    protected TextView age;
    protected EditText ageText;
    protected TextView email;
    protected EditText emailText;
    protected TextView password;
    protected EditText passwordText;

    protected RadioGroup group;
    protected RadioButton genderButton;
    private String gender ;

    ProgressDialog progressDialog;

    protected FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        auth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        initView();
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                genderButton = group.findViewById(i);
                switch (i){
                    case R.id.male :
                        gender = genderButton.getText().toString();
                        break;
                    case R.id.female :
                        gender = genderButton.getText().toString();
                        break;
                }
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {
                                          inputData();

                                      }
                                  });
        RegisterAsDoc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(RegisterUser.this, RegisterDoctor.class));
                    }
                });
    }

    String UsernameText,AgeText,EmailText,PasswordText;
    private void inputData() {
        UsernameText = usernameText.getText().toString();

        AgeText = ageText.getText().toString();

        EmailText = emailText.getText().toString();

        PasswordText = passwordText.getText().toString();



        if (UsernameText.trim().isEmpty()) {

            usernameText.setError("required");

            return;

        }

        if (AgeText.trim().isEmpty()) {

            ageText.setError("required");

            return;

        }

        if (EmailText.trim().isEmpty()) {

            emailText.setError("required");

            return;

        }
        if(!Patterns.EMAIL_ADDRESS.matcher(EmailText).matches()){
            Toast.makeText(this,"Invalid email pattern ..",Toast.LENGTH_SHORT).show();
            return;
        }

        if (PasswordText.trim().isEmpty()) {

            passwordText.setError("required");

            return;

        }






        if (gender.trim().isEmpty()) {

            Toast.makeText(this, "please enter gender", Toast.LENGTH_SHORT).show();

            return;

        }


        CreateAccount();


    }

    private void CreateAccount() {
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();


        //create account
        auth.createUserWithEmailAndPassword(EmailText,PasswordText)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {



                    @Override
                    public void onSuccess(AuthResult authResult) {
                        //accountCreated
                        storeFirebaseData();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(RegisterUser.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

    }

    private void storeFirebaseData() {
        progressDialog.setMessage("Saving Account Info ...");


            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("uid",""+auth.getUid());
            hashMap.put("name",""+UsernameText);
            hashMap.put("age", ""+AgeText);

            hashMap.put("email",""+EmailText);
            hashMap.put("password",""+PasswordText);

            hashMap.put("gender",""+gender);
            hashMap.put("account type",""+"user");


            DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("user");
            databaseReference.child(auth.getUid()).setValue(hashMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            //db updated
                            progressDialog.dismiss();
                            startActivity(new Intent(RegisterUser.this,Assessment.class));
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            startActivity(new Intent(RegisterUser.this,Assessment.class));
                            finish();
                        }
                    });


        }


















    private void initView() {
        title = (TextView) findViewById(R.id.title);
        username = (TextView) findViewById(R.id.username);
        usernameText = (EditText) findViewById(R.id.username_text);
        age = (TextView) findViewById(R.id.age);
        ageText = (EditText) findViewById(R.id.age_text);
        email = (TextView) findViewById(R.id.email);
        emailText = (EditText) findViewById(R.id.email_text);
        password = (TextView) findViewById(R.id.password);
        passwordText = (EditText) findViewById(R.id.password_text);
        group = (RadioGroup) findViewById(R.id.radio_group);
        submit = (Button) findViewById(R.id.submit);
        RegisterAsDoc = findViewById(R.id.registerAsDoc);


    }
}
