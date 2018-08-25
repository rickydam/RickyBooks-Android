package com.rickybooks.rickybooks.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rickybooks.rickybooks.Models.NotifyItem;
import com.rickybooks.rickybooks.R;

import java.util.List;

public class NotifyItemAdapter extends RecyclerView.Adapter<NotifyItemAdapter.NotifyItemViewHolder> {
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

    public NotifyItemAdapter(List<NotifyItem> notifyItems) {
        this.notifyItems = notifyItems;
    }

    @NonNull
    @Override
    public NotifyItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notify_item, parent , false);
        return new NotifyItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotifyItemViewHolder holder, int position) {
        NotifyItem notifyItem = notifyItems.get(holder.getAdapterPosition());
        holder.notifyItemCategory.setText(notifyItem.getCategory());
        holder.notifyItemInput.setText(notifyItem.getInput());
    }

    @Override
    public int getItemCount() {
        return notifyItems.size();
    }
}
