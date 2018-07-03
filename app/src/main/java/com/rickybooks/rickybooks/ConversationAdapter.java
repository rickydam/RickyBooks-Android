package com.rickybooks.rickybooks;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter
        .ConversationViewHolder> {
    private List<Conversation> conversationList;

    class ConversationViewHolder extends RecyclerView.ViewHolder {
        private TextView recipientName;

        ConversationViewHolder(View view) {
            super(view);
            recipientName = view.findViewById(R.id.conversation_recipient_name);
        }
    }

    ConversationAdapter(List<Conversation> conversationList) {
        this.conversationList = conversationList;
    }

    @NonNull
    @Override
    public ConversationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.conversation, parent,
                false);
        return new ConversationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ConversationViewHolder holder, int position) {
        final Conversation conversation = conversationList.get(position);
        holder.recipientName.setText(conversation.getRecipientName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity activity = (MainActivity) v.getContext();
                SharedPreferences sharedPref = activity.getSharedPreferences("com.rickybooks.rickybooks",
                        Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("conversation_id", conversation.getConversationId());
                editor.apply();
                activity.replaceFragment("MessagesFragment");
            }
        });
    }

    @Override
    public int getItemCount() {
        return conversationList.size();
    }
}
