package com.example.MNM;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    protected EditText email;
    protected EditText password;
    protected TextView sign_up;
    protected TextView forget;
    protected Button login;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initiateView();

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait..");
        progressDialog.setCanceledOnTouchOutside(false);


        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegisterUser.class));
            }
        });
        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,ForgetPasswordActivity.class));

            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });
    }

    private String Email, Password;
    private void loginUser() {
        Email = email.getText().toString().trim();
        Password = password.getText().toString().trim();

        if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches()){
            Toast.makeText(this,"Invalid email pattern", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(Password)){
            Toast.makeText(this,"Enter password",Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Logging in");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(Email,Password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        checkUserType();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void checkUserType(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("user");
        ref.orderByChild("uid").equalTo(firebaseAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Log.i("check user", "patient ");
                            progressDialog.dismiss();
                            startActivity(new Intent(LoginActivity.this, ShowDoctorsActivity.class));
                            finish();
                        }
                        else{
                            checkUserDctor();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void checkUserDctor() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Doctors");
        ref.orderByChild("uid").equalTo(firebaseAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Log.i("check user", "doctor ");
                            progressDialog.dismiss();
                            startActivity(new Intent(LoginActivity.this, TransactionActivity.class));
                            finish();
                        }
                        else{
                            checkUserDctor();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

/*    private void checkUserType() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("user");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds :dataSnapshot.getChildren()){
                    if(ds.child("uid").equals(firebaseAuth.getUid())){
                        Log.i("check user", "patient "+ds.child("uid").getValue());
                        progressDialog.dismiss();
                        startActivity(new Intent(LoginActivity.this,ShowDoctorsActivity.class));
                        finish();
                    }
                    else
                        checkUserTypeDoctor();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
    private void checkUserTypeDoctor() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Doctors");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (ds.child("uid").equals(firebaseAuth.getUid())) {
                        Log.i("check user", "doctor "+ds.child("uid").getValue());
                        progressDialog.dismiss();
                        startActivity(new Intent(LoginActivity.this, TransactionActivity.class));
                        finish();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }*/

    private void initiateView() {
        email = (EditText) findViewById(R.id.email);
        sign_up=(TextView) findViewById(R.id.sign_up);
        password = (EditText) findViewById(R.id.password);
        forget=(TextView) findViewById(R.id.forget);
        login = (Button) findViewById(R.id.login);
    }


}