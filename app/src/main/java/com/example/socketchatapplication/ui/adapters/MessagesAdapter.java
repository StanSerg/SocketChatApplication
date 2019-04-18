package com.example.socketchatapplication.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.socketchatapplication.R;
import com.example.socketchatapplication.models.MessageModel;

import java.util.ArrayList;
import java.util.List;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MessageViewHolder> {
    private List<MessageModel> messageList;

    public class MessageViewHolder extends RecyclerView.ViewHolder {
        public TextView tvMessage;

        public MessageViewHolder(View view) {
            super(view);
            tvMessage = view.findViewById(R.id.tv_chat_item_message);
        }
    }

    public MessagesAdapter(List<MessageModel> messagesList) {
        this.messageList = messagesList;
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    @Override
    public MessagesAdapter.MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat_item, parent, false);
        return new MessagesAdapter.MessageViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MessagesAdapter.MessageViewHolder holder, final int position) {
        MessageModel messageModel = messageList.get(position);
        String tmp = messageModel.getUserName();
        String nickname = (tmp.equals("Guest") || tmp.equals("")) ? "" : tmp;
        if (nickname.isEmpty()) {
            holder.tvMessage.setGravity(Gravity.END);
            holder.tvMessage.setText(messageModel.getMessage());
        }
        else {
            holder.tvMessage.setGravity(Gravity.START);
            holder.tvMessage.setText((nickname+ ": " +messageModel.getMessage()));
        }

    }

    public void setMessageList(List<MessageModel> messageList) {
        if (this.messageList == null) {
            this.messageList = new ArrayList<>();
        }
        this.messageList.clear();
        this.messageList.addAll(messageList);
        notifyDataSetChanged();
    }
}