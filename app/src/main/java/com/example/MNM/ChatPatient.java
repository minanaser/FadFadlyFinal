package com.example.MNM;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChatPatient extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;
    ImageView profileIv;
    TextView nameTv,userStatusTv;
    EditText massageEt;
    ImageButton sendBtn;

    //firebase auth
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference userDbRef;

    //for checking if user has seen message or not
    ValueEventListener seenListener;
    DatabaseReference userRefForSeen;

    List<ModelChat> chatList= new ArrayList<>();
    AdapterChatPatient adapterChatPatient;


    String hisUid = Adapter.doctorID;
    String hisImage;
    String uid ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_patient);
        Toolbar toolbar =findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("");
        recyclerView =findViewById(R.id.chat_recyclerVeiw);
        profileIv = findViewById(R.id.profileIcon);
        nameTv = findViewById(R.id.nameTV);
        userStatusTv = findViewById(R.id.userStatusTV);
        massageEt = findViewById(R.id.messageET);
        sendBtn = findViewById(R.id.sendBtn);

        LinearLayoutManager linearLayoutManager =new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);


        /* on clicking user from users list we have passed that user's UID using intent
         *so get that uid here to get the profile picture,name and start vhat with that user
         */
        //Intent intent = getIntent();
        //hisUid = intent.getStringExtra("hisUid");
        firebaseAuth = FirebaseAuth.getInstance();
        uid =firebaseAuth.getUid();
        Log.i("check",hisUid+"right");
        Log.i("checkw",uid+"left");
        firebaseDatabase = FirebaseDatabase.getInstance();
        userDbRef = firebaseDatabase.getReference("Doctors");
        //seach user to get that user's info
        Query userQuery = userDbRef.orderByChild("uid").equalTo(hisUid);
        // get user picture and name
        userQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //check untill required info is received
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    //get data
                    String name = ""+ds.child("name").getValue();
                    // hisImage =""+ds.child("image").getValue();

                    //set data
                    nameTv.setText(name);
                   /* try {
                        // image recived , set it to imageview in toolbar
                        Picasso.get().load(hisImage)
                                .placeholder(R.drawable.ic_profile2).into(profileIv);

                    }
                    catch (Exception e){
                        //there is exception getting picture ,set default picture
                        Picasso.get().load(R.drawable.ic_profile2).into(profileIv);
                    }*/

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //click button to send message
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get text from edit text
                String message = massageEt.getText().toString().trim();
                //check if text is empty or not
                if(TextUtils.isEmpty(message)){
                    //text empty
                    Toast.makeText(ChatPatient.this,"Cannot send the empty message...",Toast.LENGTH_SHORT).show();
                }
                else{
                    //text not empty
                    sendMessage(message);
                }
            }
        });
       readMessages();
        //     seenMessage();


    }


    private void seenMessage() {
        userRefForSeen = FirebaseDatabase.getInstance().getReference("Chats");
        seenListener = userRefForSeen.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    ModelChat chat = ds.getValue(ModelChat.class);
                    if(chat.getReceiver().equals(uid)&& chat.getSender().equals(hisUid)){
                        HashMap<String, Object> hasSeenHashMap = new HashMap<>();
                        hasSeenHashMap.put("isSeen" ,true);
                        ds.getRef().updateChildren(hasSeenHashMap);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void readMessages() {
        // chatList = new ArrayList<>();
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Chats");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatList.clear();
                for(DataSnapshot ds :dataSnapshot.getChildren()){
                    ModelChat chat = ds.getValue(ModelChat.class);
                    Log.i("error",uid+"ff");
                    Log.i("error2",hisUid);
                    Log.i("help",chat.getReceiver()+"help??");
                    Log.i("help2",chat+"help??");
                    if(uid.equals(chat.getReceiver()) && hisUid.equals(chat.getSender())||
                            uid.equals(chat.getSender()) &&hisUid.equals(chat.getReceiver())){
                        chatList.add(chat);

                    }
                    adapterChatPatient = new AdapterChatPatient(ChatPatient.this,chatList,hisImage);
                    adapterChatPatient.notifyDataSetChanged();


                    recyclerView.setAdapter(adapterChatPatient);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendMessage(String message) {
        //"chats" node will be created that will contain all chats

        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();

        String timestamp = String.valueOf(System.currentTimeMillis());

        HashMap<String,Object> hashMap =new HashMap<>();
        hashMap.put("sender", uid);
        hashMap.put("receiver", hisUid);
        hashMap.put("message", message);
        hashMap.put("timestamp", timestamp);
        hashMap.put("isSeen", false);
        databaseReference.child("Chats").push().setValue(hashMap);

        //reset edittext after sending message
        massageEt.setText("");
    }

    private  void checkUserStatus(){
        //get current user
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user!=null){
            //user is signed in stay here
            //set email of logged in user
            uid = user.getUid();
        }else {
            //user not signed in ,go to main activity
            // startActivity(new Intent(this,patientsFragment.class));
            finish();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //  userRefForSeen.removeEventListener(seenListener);
    }

    @Override
    protected void onStart() {
        //  checkUserStatus();
        super.onStart();
    }
}
