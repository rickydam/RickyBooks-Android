package com.rickybooks.rickybooks.Adapters;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rickybooks.rickybooks.Fragments.ConversationsFragment;
import com.rickybooks.rickybooks.MainActivity;
import com.rickybooks.rickybooks.Models.Conversation;
import com.rickybooks.rickybooks.Models.Message;
import com.rickybooks.rickybooks.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter
        .ConversationViewHolder> {
    private List<Conversation> conversationList;
    private List<Message> lastMessagesList;
    private MainActivity activity;

    class ConversationViewHolder extends RecyclerView.ViewHolder {
        private TextView otherName;
        private TextView timestamp;
        private TextView preview;

        ConversationViewHolder(View view) {
            super(view);
            otherName = view.findViewById(R.id.conversation_other_name);
            timestamp = view.findViewById(R.id.conversation_timestamp);
            preview = view.findViewById(R.id.conversation_preview);
        }
    }

    public ConversationAdapter(MainActivity activity, List<Conversation> conversationList,
                               List<Message> lastMessages) {
        this.activity = activity;
        this.conversationList = conversationList;
        this.lastMessagesList = lastMessages;
    }

    @NonNull
    @Override
    public ConversationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.conversation, parent,
                false);
        return new ConversationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ConversationViewHolder holder, int position) {
        final Conversation conversation = conversationList.get(position);

        holder.otherName.setText(conversation.getOtherName());

        Message message = lastMessagesList.get(position);

        String timestampStr;

        long currentTime = new Date().getTime();
        Date createdAtDate = message.getCreatedAt();
        long createdAtTime = createdAtDate.getTime();

        long differenceMillis = currentTime - createdAtTime;
        int differenceSeconds = (int) (differenceMillis/1000);
        int differenceMinutes = differenceSeconds/60;
        int differenceHours = differenceMinutes/60;

        if(differenceHours >= 24) {
            DateFormat df = new SimpleDateFormat("h:mm a, MMM dd", Locale.CANADA);
            timestampStr = df.format(createdAtDate);
        }
        else if(differenceHours < 24 && differenceHours > 1) {
            timestampStr = differenceHours + " hours ago";
        }
        else if(differenceHours == 1) {
            timestampStr = differenceHours + " hour ago";
        }
        else if(differenceMinutes < 60 && differenceMinutes > 1) {
            timestampStr = differenceMinutes + " minutes ago";
        }
        else if(differenceMinutes == 1) {
            timestampStr = differenceMinutes + " minute ago";
        }
        else if(differenceSeconds < 60 && differenceSeconds > 30) {
            timestampStr = differenceSeconds + " seconds ago";
        }
        else {
            timestampStr = "Just now";
        }

        holder.timestamp.setText(timestampStr);

        String messageStr = message.getMessage();
        String nameStr = message.getSender();
        String previewStr = nameStr + ": " + messageStr;
        holder.preview.setText(previewStr);

        holder.itemView.setBackgroundResource(R.color.white);

        final ConversationsFragment conversationsFragment = getConversationsFragment(activity);
        if(conversationsFragment != null) {
            boolean actionMode = activity.getActionMode();
            if(actionMode) {
                boolean conversationExists = conversationsFragment.conversationExists(conversation);
                if(conversationExists) {
                    holder.itemView.setBackgroundResource(R.color.lightGray);
                }
            }
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean actionMode = activity.getActionMode();
                if(actionMode) {
                    conversationsFragment.selectConversation(conversation);
                    notifyItemChanged(holder.getAdapterPosition());
                }
                else {
                    Bundle bundle = new Bundle();
                    bundle.putString("conversation_id", conversation.getId());
                    activity.setMessagesBundle(bundle);
                    activity.replaceFragment("MessagesFragment");
                }
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                boolean actionMode = activity.getActionMode();
                if(!actionMode) {
                    conversationsFragment.prepareSelection(conversation);
                    notifyItemChanged(holder.getAdapterPosition());
                }
                else {
                    conversationsFragment.selectConversation(conversation);
                    notifyItemChanged(holder.getAdapterPosition());
                }
                return true;
            }
        });
    }

    private ConversationsFragment getConversationsFragment(MainActivity activity) {
        FragmentManager fm = activity.getSupportFragmentManager();
        ConversationsFragment conversationsFragment = (ConversationsFragment) fm.findFragmentByTag(
                "ConversationsFragment");
        if(conversationsFragment != null) {
            return conversationsFragment;
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return conversationList.size();
    }
}
