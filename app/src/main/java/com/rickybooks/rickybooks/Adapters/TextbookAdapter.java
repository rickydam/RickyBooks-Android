package com.rickybooks.rickybooks.Adapters;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.rickybooks.rickybooks.Fragments.ProfileFragment;
import com.rickybooks.rickybooks.MainActivity;
import com.rickybooks.rickybooks.Models.Textbook;
import com.rickybooks.rickybooks.R;

import java.util.List;

public class TextbookAdapter extends RecyclerView.Adapter<TextbookAdapter.TextbookViewHolder> {
    private List<Textbook> textbookList;
    private MainActivity activity;

    class TextbookViewHolder extends RecyclerView.ViewHolder {
        private TextView textbook_title;
        private TextView textbook_author;
        private TextView textbook_edition;
        private TextView textbook_condition;
        private TextView textbook_type;
        private TextView textbook_coursecode;
        private TextView textbook_price;
        private TextView textbook_seller;
        private TextView textbook_timestamp;
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
            textbook_timestamp = view.findViewById(R.id.textbook_timestamp);
            imageView = view.findViewById(R.id.textbook_image);
        }
    }

    public TextbookAdapter(MainActivity activity, List<Textbook> textbookList) {
        this.activity = activity;
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
        final Textbook textbook = textbookList.get(holder.getAdapterPosition());

        holder.itemView.setBackgroundResource(R.color.white);

        holder.textbook_title.setText(textbook.getTitle());
        holder.textbook_author.setText(textbook.getAuthor());
        holder.textbook_edition.setText(textbook.getEdition());
        holder.textbook_condition.setText(textbook.getCondition());
        holder.textbook_type.setText(textbook.getType());
        holder.textbook_coursecode.setText(textbook.getCoursecode());
        holder.textbook_price.setText(textbook.getPrice());
        holder.textbook_seller.setText(textbook.getSellerName());
        holder.textbook_timestamp.setText(textbook.getTimestamp());

        final ImageView imageView = holder.imageView;

        final List<String> imagesUrls = textbook.getImageUrls();
        final String imageUrl;
        if(imagesUrls.size() > 0) {
            imageUrl = imagesUrls.get(0);
            Glide.with(activity).load(imageUrl).into(imageView);
        }
        else {
            imageUrl = "";
            Glide.with(activity).load(R.drawable.textbook_placeholder).into(imageView);
        }

        final ProfileFragment profileFragment = getProfileFragment(activity);
        if(profileFragment != null) {
            boolean actionMode = activity.getActionMode();
            if(actionMode) {
                boolean textbookExists = profileFragment.textbookExists(textbook);
                if(textbookExists) {
                    holder.itemView.setBackgroundResource(R.color.lightGray);
                }
            }
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity activity = (MainActivity) v.getContext();
                String currentFragmentName = activity.getCurrentFragmentName();

                if(currentFragmentName.equals("ProfileFragment")) {
                    if(profileFragment != null) {
                        boolean actionMode = activity.getActionMode();
                        if(actionMode) {
                            profileFragment.selectTextbook(textbook);
                            notifyItemChanged(holder.getAdapterPosition());
                        }
                    }
                }
                else {
                    Bundle bundle = createBundle(textbook, imageUrl);
                    activity.setDetailsBundle(bundle);
                    activity.replaceFragment("DetailsFragment");
                }
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                String currentFragmentName = activity.getCurrentFragmentName();
                if(currentFragmentName.equals("ProfileFragment")) {
                    if(profileFragment != null) {
                        boolean actionMode = activity.getActionMode();
                        if(!actionMode) {
                            profileFragment.prepareSelection(textbook);
                            notifyItemChanged(holder.getAdapterPosition());
                        }
                        else {
                            profileFragment.selectTextbook(textbook);
                            notifyItemChanged(holder.getAdapterPosition());
                        }
                    }
                }
                return true;
            }
        });
    }

    private Bundle createBundle(Textbook textbook, String imageUrl) {
        Bundle bundle = new Bundle();

        bundle.putString("Id", textbook.getId());
        bundle.putString("Title", textbook.getTitle());
        bundle.putString("Author", textbook.getAuthor());
        bundle.putString("Edition", textbook.getEdition());
        bundle.putString("Condition", textbook.getCondition());
        bundle.putString("Type", textbook.getType());
        bundle.putString("Coursecode", textbook.getCoursecode());
        bundle.putString("Price", textbook.getPrice());
        bundle.putString("SellerName", textbook.getSellerName());
        bundle.putString("SellerId", textbook.getSellerId());
        bundle.putString("Timestamp", textbook.getTimestamp());
        bundle.putString("ImageUrl", imageUrl);

        return bundle;
    }

    private ProfileFragment getProfileFragment(MainActivity activity) {
        FragmentManager fm = activity.getSupportFragmentManager();
        if(fm.findFragmentByTag("ProfileFragment") != null) {
            return (ProfileFragment) fm.findFragmentByTag("ProfileFragment");
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return textbookList.size();
    }
}
