package com.example.rickydam.rickybooks;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

    class TextbookViewHolder extends RecyclerView.ViewHolder {
        private TextView textbook_title;
        private TextView textbook_author;
        private TextView textbook_edition;
        private TextView textbook_condition;
        private TextView textbook_type;
        private TextView textbook_coursecode;
        private TextView textbook_price;
        private TextView textbook_seller;
        private ImageView imageView;

        TextbookViewHolder(View view) {
            super(view);
            textbook_title = view.findViewById(R.id.textbook_title);
            textbook_author = view.findViewById(R.id.textbook_author);
            textbook_edition = view.findViewById(R.id.textbook_edition);
            textbook_condition = view.findViewById(R.id.textbook_condition);
            textbook_type = view.findViewById(R.id.textbook_type);
            textbook_coursecode = view.findViewById(R.id.textbook_coursecode);
            textbook_price = view.findViewById(R.id.textbook_price);
            textbook_seller = view.findViewById(R.id.textbook_seller);
            imageView = view.findViewById(R.id.textbook_image);
        }
    }

    TextbookAdapter(Context context, List<Textbook> textbookList) {
        this.context = context;
        this.textbookList = textbookList;
    }

    @NonNull
    @Override
    public TextbookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.textbook, parent,
                false);
        return new TextbookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final TextbookAdapter.TextbookViewHolder holder,
                                 final int position) {
        final Textbook textbook = textbookList.get(position);

        holder.textbook_title.setText(textbook.getTitle());
        holder.textbook_author.setText(textbook.getAuthor());
        holder.textbook_edition.setText(textbook.getEdition());
        holder.textbook_condition.setText(textbook.getCondition());
        holder.textbook_type.setText(textbook.getType());
        holder.textbook_coursecode.setText(textbook.getCoursecode());
        holder.textbook_price.setText(textbook.getPrice());
        holder.textbook_seller.setText(textbook.getSellerName());

        final ImageView imageView = holder.imageView;
        Glide.with(context).load(R.drawable.placeholder_img).into(imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("Title", textbook.getTitle());
                bundle.putString("Author", textbook.getAuthor());
                bundle.putString("Edition", textbook.getEdition());
                bundle.putString("Condition", textbook.getCondition());
                bundle.putString("Type", textbook.getType());
                bundle.putString("Coursecode", textbook.getCoursecode());
                bundle.putString("Price", textbook.getPrice());
                bundle.putString("SellerName", textbook.getSellerName());

                MainActivity activity = (MainActivity) v.getContext();
                activity.setDetailsBundle(bundle);
                activity.replaceFragment("DetailsFragment");
            }
        });
    }

    @Override
    public int getItemCount() {
        return textbookList.size();
    }
}
