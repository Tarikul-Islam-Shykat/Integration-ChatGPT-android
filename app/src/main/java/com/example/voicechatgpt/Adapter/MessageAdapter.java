package com.example.voicechatgpt.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.voicechatgpt.Modal.Message;
import com.example.voicechatgpt.R;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private List<Message> mmsg;
    private static final int userViewType=0, botViewType=1;

    public MessageAdapter(List<Message> mmsg) {
        this.mmsg = mmsg;
    }

    @NonNull
    @Override
    public MessageAdapter.MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) { // viewType calling the getItemViewType method
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        if (viewType == userViewType)
            view = inflater.inflate(R.layout.text_message_user,parent, false);
        else
            view = inflater.inflate(R.layout.text_message_bot, parent, false);

        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.MessageViewHolder holder, int position) {
        Message message = mmsg.get(position);
        holder.setMessageInTextView(message);
    }

    @Override
    public int getItemCount() {
        return mmsg.size();
    }

    @Override
    public int getItemViewType(int position) {
        Message msg = mmsg.get(position);
        if (msg.isSentByUser())
            return userViewType;
        else
            return botViewType;
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView mText;
        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            mText = itemView.findViewById(R.id.text_message_user_);  // Message ViewHolder will use the view, that is first sent by the on create recView, so see that if you find error.
        }

        public  void setMessageInTextView(Message message) {
            if (message.isSentByUser()) {
                mText = itemView.findViewById(R.id.text_message_user_);
                mText.setText(message.getmText().toString());
            } else{
                mText = itemView.findViewById(R.id.text_message_bot_);
                mText.setText(message.getmText().toString());
            }
        }
    }
}
