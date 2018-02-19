package com.example.rickydam.rickybooks;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class TextbookAdapter extends RecyclerView.Adapter<TextbookAdapter.TextbookViewHolder> {
    private List<Textbook> textbookList;
    private Context context;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    class TextbookViewHolder extends RecyclerView.ViewHolder {
        private TextView item_title;
        private TextView item_author;
        private TextView item_edition;
        private TextView item_condition;
        private TextView item_type;
        private TextView item_coursecode;
        private TextView item_price;
        private ImageView imageView;

        TextbookViewHolder(View view) {
            super(view);
            item_title = view.findViewById(R.id.item_title);
            item_author = view.findViewById(R.id.item_author);
            item_edition = view.findViewById(R.id.item_edition);
            item_condition = view.findViewById(R.id.item_condition);
            item_type = view.findViewById(R.id.item_type);
            item_coursecode = view.findViewById(R.id.item_coursecode);
            item_price = view.findViewById(R.id.item_price);
            imageView = view.findViewById(R.id.textbook_image);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    TextbookAdapter(Context context, List<Textbook> textbookList) {
        this.context = context;
        this.textbookList = textbookList;
    }

    // Create new views (invoke by the layout manager)
    @Override
    public TextbookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new TextbookViewHolder(view);
    }

    // Replace the contents of a view (invoked by layout manager)
    @Override
    public void onBindViewHolder(TextbookAdapter.TextbookViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Textbook textbook = textbookList.get(position);
        holder.item_title.setText(textbook.getTitle());
        holder.item_author.setText(textbook.getAuthor());
        holder.item_edition.setText(textbook.getEdition());
        holder.item_condition.setText(textbook.getCondition());
        holder.item_type.setText(textbook.getType());
        holder.item_coursecode.setText(textbook.getCoursecode());
        holder.item_price.setText(textbook.getPrice());

        ImageView imageView = holder.imageView;
        Glide.with(context).load(R.drawable.textbook).into(imageView);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return textbookList.size();
    }

}
