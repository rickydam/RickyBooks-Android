package com.rickybooks.rickybooks.Adapters;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rickybooks.rickybooks.Fragments.NotifyFragment;
import com.rickybooks.rickybooks.MainActivity;
import com.rickybooks.rickybooks.Models.NotifyItem;
import com.rickybooks.rickybooks.R;

import java.util.List;

public class NotifyItemAdapter extends RecyclerView.Adapter<NotifyItemAdapter.NotifyItemViewHolder> {
    private MainActivity activity;
    private List<NotifyItem> notifyItems;

    class NotifyItemViewHolder extends RecyclerView.ViewHolder {
        private TextView notifyItemCategory;
        private TextView notifyItemInput;

        NotifyItemViewHolder(View view) {
            super(view);
            notifyItemCategory = view.findViewById(R.id.notify_item_category);
            notifyItemInput = view.findViewById(R.id.notify_item_input);
        }
    }

    public NotifyItemAdapter(MainActivity activity, List<NotifyItem> notifyItems) {
        this.activity = activity;
        this.notifyItems = notifyItems;
    }

    @NonNull
    @Override
    public NotifyItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notify_item, parent , false);
        return new NotifyItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final NotifyItemViewHolder holder, int position) {
        final NotifyItem notifyItem = notifyItems.get(holder.getAdapterPosition());
        holder.notifyItemCategory.setText(notifyItem.getCategory());
        holder.notifyItemInput.setText(notifyItem.getInput());

        holder.itemView.setBackgroundResource(R.color.white);

        final NotifyFragment notifyFragment = getNotifyFragment();
        if(notifyFragment != null) {
            boolean actionMode = activity.getActionMode();
            if(actionMode) {
                boolean notifyItemExists = notifyFragment.notifyItemExists(notifyItem);
                if(notifyItemExists) {
                    holder.itemView.setBackgroundResource(R.color.lightGray);
                }
            }
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(notifyFragment != null) {
                    boolean actionMode = activity.getActionMode();
                    if(actionMode) {
                        notifyFragment.selectNotifyItem(notifyItem);
                        notifyItemChanged(holder.getAdapterPosition());
                    }
                }
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(notifyFragment != null) {
                    boolean actionMode = activity.getActionMode();
                    if(!actionMode) {
                        notifyFragment.prepareSelection(notifyItem);
                        notifyItemChanged(holder.getAdapterPosition());
                    }
                    else {
                        notifyFragment.selectNotifyItem(notifyItem);
                        notifyItemChanged(holder.getAdapterPosition());
                    }
                }
                return true;
            }
        });
    }

    private NotifyFragment getNotifyFragment() {
        FragmentManager fm = activity.getSupportFragmentManager();
        if(fm.findFragmentByTag("NotifyFragment") != null) {
            return (NotifyFragment) fm.findFragmentByTag("NotifyFragment");
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return notifyItems.size();
    }
}
