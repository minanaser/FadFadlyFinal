package com.example.MNM;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ShowDoctorsActivity extends AppCompatActivity {
    TextView name,speciality;
    ImageView profileIv;

    String currentGender2;

    private RecyclerView recyclerView;
    private static ArrayList<Data> dataArrayList;
    private Adapter adapter;

    static ArrayList<String> userProblemsList;
    static ArrayList<String> userProblems ;
    private static ArrayList<Data> finalArray;


    static ArrayList<Data> trial ;
    LinearLayoutManager linearLayoutManager;

    FirebaseAuth firebaseAuth;
    //static String currenttID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_doctors);

        name = (TextView) findViewById(R.id.name);
        speciality =(TextView) findViewById(R.id.speciality);
        profileIv = (ImageView) findViewById(R.id.profile);
        recyclerView = findViewById(R.id.recycler_view);

        userProblemsList = new ArrayList<String>();
       // userProblems = new ArrayList<String>();
        finalArray = new ArrayList<Data>();
        dataArrayList = new ArrayList<Data>();

        //trial = new ArrayList<Data>();


        recyclerView.setAdapter(adapter);

        firebaseAuth = FirebaseAuth.getInstance();

        loadMyInfo();
    }

    //get user's gender and pass it to next function

    private void loadMyInfo() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("user");
        ref.orderByChild("uid").equalTo(firebaseAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds :dataSnapshot.getChildren()){
                            String currentName = ""+ds.child("name").getValue();
                            String currentGender = ""+ds.child("gender").getValue();

                            currentGender2 = currentGender;
                            loadDoctors(currentGender2);
                        }
                        Log.i("TAG" , ""+currentGender2);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    //search for doctors with same gender as current user

    private void loadDoctors(String currentGender2) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Doctors");
        ref.orderByChild("gender").equalTo(currentGender2)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        dataArrayList.clear();
                        for(DataSnapshot ds :dataSnapshot.getChildren()){
                            Data doctorsdata = ds.getValue(Data.class);
                            dataArrayList.add(doctorsdata);
                        }
                       loadInfo();
                       /* adapter = new Adapter(ShowDoctorsActivity.this,dataArrayList);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(ShowDoctorsActivity.this));*/
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    //get current user's id and pass it to next function

    private void loadInfo() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("user");
        ref.orderByChild("uid").equalTo(firebaseAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()){
                            String uid = ""+ds.child("uid").getValue();
                            Log.i("TAG",""+uid);
                            loadWekaResults(uid);
                            //currenttID = uid;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    //get current user's weka results and keys

    private void loadWekaResults(String uid) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("user");
        ref.child(uid).child("WekaResults")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<String> values = new ArrayList<>();
                        List<String> keys = new ArrayList<>();

                        String key = dataSnapshot.getKey();
                        String dataKeys;

                        for (DataSnapshot ds :dataSnapshot.getChildren()){
                            String value = ds.getValue(String.class);
                            values.add(value);
                            dataKeys = ""+ds.getKey();
                            keys.add(dataKeys);
                        }
                        Log.i("TAG1",values.toString());
                        Log.i("TAG2", keys.toString());

                        //get the bad fields from both arrays keys and values + user's decision

                        for (int i= 0; i< values.size();i++ ){
                            if (values.get(i).equals("bad")){
                                userProblemsList.add(keys.get(i));
                            }
                            if (keys.get(i).equals("decision")){
                                if (!userProblemsList.contains(values.get(i)))
                                    userProblemsList.add(values.get(i));
                            }
                        }
                        Log.i("TAG3",""+userProblemsList.toString());

                        loadSpecialDoctors(userProblemsList,dataArrayList);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    //search if doctor's speciality is contained in userProblems array list

    private void loadSpecialDoctors(ArrayList<String> userProblemsList, ArrayList<Data> dataArrayList) {
        ArrayList<Data> finalDoctorsList=new ArrayList<>();
        for (int i=0 ; i<dataArrayList.size();i++) {
            for (int j = 0; j < userProblemsList.size(); j++) {
                if ((dataArrayList.get(i).getSpeciality().toString()).contains(userProblemsList.get(j).toString())) {
                    finalDoctorsList.add(dataArrayList.get(i));
                    Log.i("TAG5", "" + dataArrayList.get(i).getName());
                }
            }
        }

        //to remove duplication of doctors

        for (Data doctor : finalDoctorsList) {
            if (!finalArray.contains(doctor)) {
                finalArray.add(doctor);
            }
        }
        for (int i = 0; i < finalArray.size(); i++) {
            Log.i("TAG6", "" + finalArray.get(i).getName());
        }

        adapter = new Adapter(ShowDoctorsActivity.this, finalArray );
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(ShowDoctorsActivity.this));
    }
}
