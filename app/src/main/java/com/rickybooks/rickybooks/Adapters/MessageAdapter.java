package com.rickybooks.rickybooks.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rickybooks.rickybooks.Models.Message;
import com.rickybooks.rickybooks.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MessageAdapter extends RecyclerView.Adapter {
    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;
    private List<Message> messageList;
    private Context context;

    public MessageAdapter(Context context, List<Message> messageList) {
        this.context = context;
        this.messageList = messageList;
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    // Determines the appropriate ViewType according to the sender of the message
    @Override
    public int getItemViewType(int position) {
        Message message = messageList.get(position);
        SharedPreferences sharedPref = context.getSharedPreferences("com.rickybooks.rickybooks",
                Context.MODE_PRIVATE);
        if(message.getSender().equals(sharedPref.getString("name", null))) {
            return VIEW_TYPE_MESSAGE_SENT;
        }
        else {
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }

    // Inflates the appropriate layout according to the ViewType
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        if(viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sent_message,
                    parent, false);
            return new SentMessageHolder(view);
        }
        else if(viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.received_message,
                    parent, false);
            return new ReceivedMessageHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messageList.get(position);

        switch(holder.getItemViewType()) {
            case VIEW_TYPE_MESSAGE_SENT:
                ((SentMessageHolder) holder).bind(message);
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                ((ReceivedMessageHolder) holder).bind(message);
        }
    }

    private class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        private TextView messageText;
        private TextView timeText;
        private TextView nameText;
        // private ImageView personImage;

        ReceivedMessageHolder(View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.messages_person_body);
            timeText = itemView.findViewById(R.id.messages_person_time);
            nameText = itemView.findViewById(R.id.messages_person_name);
            // personImage = itemView.findViewById(R.id.messages_person_picture);
        }

        void bind(Message message) {
            messageText.setText(message.getMessage());

            DateFormat df = new SimpleDateFormat("HH:mm, MMMM dd, yyyy", Locale.CANADA);
            Date date = message.getCreatedAt();
            String dateString = df.format(date);

            timeText.setText(dateString);
            nameText.setText(message.getSender());
            // image
        }
    }

    private class SentMessageHolder extends RecyclerView.ViewHolder {
        private TextView messageText;
        private TextView timeText;

        SentMessageHolder(View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.messages_self_body);
            timeText = itemView.findViewById(R.id.messages_self_time);
        }

        void bind(Message message) {
            messageText.setText(message.getMessage());

            DateFormat df = new SimpleDateFormat("HH:mm, MMMM dd, yyyy", Locale.CANADA);
            Date date = message.getCreatedAt();
            String dateString = df.format(date);

            timeText.setText(dateString);
        }
    }
}
