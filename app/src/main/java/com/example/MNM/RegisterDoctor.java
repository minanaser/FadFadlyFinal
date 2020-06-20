package com.example.MNM;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
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
import java.util.List;

public class RegisterDoctor extends AppCompatActivity {
    protected ImageView icon;

    protected EditText usernameText;

    protected EditText ageText;

    protected EditText emailText;

    protected EditText passwordText;

   // protected EditText nationalIdRegisterEdittext;

    protected EditText addressRegisterEdittext;

   // protected EditText bachelorEdit;

    protected Button buttonRegister;

    protected CardView card;

    protected RadioGroup group;
    ProgressDialog progressDialog;
    protected RadioButton genderButton;
    protected FirebaseAuth auth;
    protected String gender ;

    protected static final int Storage_Request_Code=300;
    protected static final int ImagePickGalleryCode=400;


    protected String[] StoragePermission;
    protected Uri ImgUri;
    protected    Uri downloadImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_doctor);
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

        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //picImg
                ShowImagePickDialog();
            }
        });
        buttonRegister.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                inputData();

            }





        });

    }

    String UsernameText,AgeText,EmailText,PasswordText, NationalID,Bachelor,Address;

    private void inputData() {

        UsernameText = usernameText.getText().toString();



        AgeText = ageText.getText().toString();



        EmailText = emailText.getText().toString();



        PasswordText = passwordText.getText().toString();



        // NationalID = nationalIdRegisterEdittext.getText().toString();



        //   Bachelor = bachelorEdit.getText().toString();



        Address = addressRegisterEdittext.getText().toString();





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







     /*   if (NationalID.trim().isEmpty()) {



            nationalIdRegisterEdittext.setError("required");



            return;



        }







        if (Bachelor.trim().isEmpty()) {



            bachelorEdit.setError("required");



            return;



        }*/



        if (Address.trim().isEmpty()) {



            addressRegisterEdittext.setError("required");



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

                        Toast.makeText(RegisterDoctor.this, e.getMessage(), Toast.LENGTH_SHORT).show();



                    }

                });



    }



    private void storeFirebaseData() {

        progressDialog.setMessage("Saving Account Info ...");

        if(ImgUri==null){



            HashMap<String, Object> hashMap = new HashMap<>();

            hashMap.put("uid",""+auth.getUid());

            hashMap.put("name",""+UsernameText);

            hashMap.put("age", ""+AgeText);



            hashMap.put("email",""+EmailText);

            hashMap.put("password",""+PasswordText);

            // hashMap.put("NationalID",""+NationalID);

            // hashMap.put("Bachelor",""+Bachelor);

            hashMap.put("address",""+ Address);

            hashMap.put("gender",""+gender);

            hashMap.put("account type",""+"doctor");

            hashMap.put("profileImage","");



            DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Doctors");

            databaseReference.child(auth.getUid()).setValue(hashMap)

                    .addOnSuccessListener(new OnSuccessListener<Void>() {

                        @Override

                        public void onSuccess(Void aVoid) {

                            //db updated

                            progressDialog.dismiss();

                            startActivity(new Intent(RegisterDoctor.this,Register2Doctor.class));

                            finish();

                        }

                    })

                    .addOnFailureListener(new OnFailureListener() {

                        @Override

                        public void onFailure(@NonNull Exception e) {

                            progressDialog.dismiss();

                            startActivity(new Intent(RegisterDoctor.this,Register2Doctor.class));

                            finish();

                        }

                    });





        }

        else{

            String filePathAndName = "Profile_Images/"+"" +auth.getUid();

            StorageReference storageReference = FirebaseStorage.getInstance().getReference(filePathAndName);

            storageReference.putFile(ImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                @Override

                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    //getUrl of uploaded Img

                    Task<Uri> uriTask=taskSnapshot.getStorage().getDownloadUrl();

                    while (!uriTask.isSuccessful());

                    downloadImageUri=uriTask.getResult();





                    if(uriTask.isSuccessful()){

                        HashMap<String, Object> hashMap = new HashMap<>();

                        hashMap.put("uid",""+auth.getUid());

                        hashMap.put("name",""+UsernameText);

                        hashMap.put("age", ""+AgeText);



                        hashMap.put("email",""+EmailText);

                        hashMap.put("password",""+PasswordText);

                        //  hashMap.put("NationalID",""+NationalID);

                        // hashMap.put("Bachelor",""+Bachelor);

                        hashMap.put("address",""+ Address);

                        hashMap.put("gender",""+gender);

                        hashMap.put("account type",""+"doctor");

                        hashMap.put("profileImage",""+downloadImageUri);



                        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Doctors");

                        databaseReference.child(auth.getUid()).setValue(hashMap)

                                .addOnSuccessListener(new OnSuccessListener<Void>() {

                                    @Override

                                    public void onSuccess(Void aVoid) {

                                        //db updated

                                        progressDialog.dismiss();

                                        startActivity(new Intent(RegisterDoctor.this,Register2Doctor.class));

                                        finish();

                                    }

                                })

                                .addOnFailureListener(new OnFailureListener() {

                                    @Override

                                    public void onFailure(@NonNull Exception e) {

                                        progressDialog.dismiss();

                                        startActivity(new Intent(RegisterDoctor.this,Register2Doctor.class));

                                        finish();

                                    }

                                });









                    }//uri succ

                }

            }).addOnFailureListener(new OnFailureListener() {

                @Override

                public void onFailure(@NonNull Exception e) {

                    progressDialog.dismiss();

                    Toast.makeText(RegisterDoctor.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();

                }

            });





        }//else









    }








    private void ShowImagePickDialog() {
        //opptions Display In Dialog
        String[]options={"Gallery"};
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Pick Image")
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {



                            if(CheckStoragePermission()){
                                PickFromGalery();
                            }
                            else {
                                ReqquestStoragePermisssion();
                            }


                    }
                }).show();
    }
    private  void PickFromGalery(){
        Intent intent=new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,ImagePickGalleryCode);
    }

    private  boolean CheckStoragePermission(){
        boolean result= ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)==
                (PackageManager.PERMISSION_GRANTED);
        return result;
    }
    private  void  ReqquestStoragePermisssion(){
        ActivityCompat.requestPermissions(this,StoragePermission,Storage_Request_Code);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode==RESULT_OK){

            if(requestCode==ImagePickGalleryCode){

                //get Picked Img
                ImgUri=data.getData();
                //set to Img View
                icon.setImageURI(ImgUri);
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
      {


            if(requestCode==Storage_Request_Code) {
                if( grantResults.length>0){

                    boolean StorageAcccepted= grantResults[0]==PackageManager.PERMISSION_GRANTED;
                    if( StorageAcccepted){
                        //permissionAllowed
                        PickFromGalery();
                    }
                    else {//permissionDenied
                        Toast.makeText(this,"Storage Permission is Neccessary",Toast.LENGTH_SHORT).show();

                    }
                }

            }





        }






        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }















    private void initView() {


        icon = (ImageView) findViewById(R.id.icon);

        usernameText = (EditText) findViewById(R.id.username_text);


        ageText = (EditText) findViewById(R.id.age_text);



        emailText = (EditText) findViewById(R.id.email_text);



        passwordText = (EditText) findViewById(R.id.password_text);

      //  nationalIdRegisterEdittext = (EditText) findViewById(R.id.nationalId_register_edittext);



        addressRegisterEdittext = (EditText) findViewById(R.id.address_register_edittext);



      //  bachelorEdit = (EditText) findViewById(R.id.bachelor_edit);



        group = (RadioGroup) findViewById(R.id.radio);

        buttonRegister = (Button) findViewById(R.id.button_Register);



        card = (CardView) findViewById(R.id.card);

        StoragePermission=new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

    }

}
