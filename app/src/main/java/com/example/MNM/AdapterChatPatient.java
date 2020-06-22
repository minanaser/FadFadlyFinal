package com.example.MNM;

import android.content.Context;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AdapterChatPatient extends RecyclerView.Adapter<AdapterChatPatient.MyHolder> {
    private static final int MSG_TYPE_LEfT = 0;
    private static final int MSG_TYPE_RIGHT = 1;
    Context context;
    List<ModelChat> chatList;
    String imageUr1;
    FirebaseUser fUser;







    public AdapterChatPatient(Context context, List<ModelChat> chatList, String imageUr1) {
        this.context = context;
        this.chatList = chatList;
        this.imageUr1 = imageUr1;
    }

    @NonNull
    @Override
    public AdapterChatPatient.MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int postion) {
        if(postion==MSG_TYPE_RIGHT){
            View view = LayoutInflater.from(context).inflate(R.layout.row_chat_right,viewGroup,false);
            return new AdapterChatPatient.MyHolder(view);
        }
        else{
            View view = LayoutInflater.from(context).inflate(R.layout.row_chat_left,viewGroup,false);
            return new AdapterChatPatient.MyHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        //get data
        String message = chatList.get(position).getMessage();
        String timeStamp = chatList.get(position).getTimestamp();
        Log.i("Value of Adapter ", String.valueOf(chatList));
        //convert time stamp to dd/mm/yyyy hh:mm am/pm
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(Long.parseLong(timeStamp));
        String dateTime = DateFormat.format("dd/MM/yyyy hh:mm aa",cal).toString();

        //set data
        holder.messageTv.setText(message);
        holder.timeTv.setText(dateTime);
        try {
            Picasso.get().load(imageUr1).into(holder.profileIv);
        }catch (Exception e){

        }
        //set seen/deliverd status of message
       /* if(position==chatList.size()-1){
            if(chatList.get(position).isSeen()){
                holder.isSeenTv.setText("Seen");
            }
            else{
                holder.isSeenTv.setText("Delivered");
            }
        }
        else {
            holder.isSeenTv.setVisibility(View.GONE);
        }*/
    }


    @Override
    public int getItemCount() {
        if(chatList==null)
            return 0;
        return chatList.size();
    }

    @Override
    public int getItemViewType(int position) {
        //get currently signed in user
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        Log.i("chatlist",chatList.get(position).getSender()+"starthelp");
        Log.i("chatlis2t",fUser.getUid()+"secondhelp");

        if( chatList.get(position).getSender().equals(fUser.getUid()) ){
            return MSG_TYPE_RIGHT;
        }
        else{
            return MSG_TYPE_LEfT;
        }
    }


    class MyHolder extends RecyclerView.ViewHolder{

        ImageView profileIv;
        TextView messageTv , timeTv ,isSeenTv;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            profileIv = itemView.findViewById(R.id.profileIv);
            messageTv = itemView.findViewById(R.id.messageIv);
            timeTv = itemView.findViewById(R.id.timeTv);
            isSeenTv = itemView.findViewById(R.id.idSeenTv);
        }
    }
}

