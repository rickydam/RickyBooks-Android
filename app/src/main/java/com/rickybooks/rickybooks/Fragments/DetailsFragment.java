package com.rickybooks.rickybooks.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.rickybooks.rickybooks.MainActivity;
import com.rickybooks.rickybooks.Other.Alert;
import com.rickybooks.rickybooks.R;
import com.rickybooks.rickybooks.Retrofit.CreateConversationCall;
import com.rickybooks.rickybooks.Retrofit.SendMessageCall;

public class DetailsFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);

        Button button = view.findViewById(R.id.details_message_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messageButtonPressed();
            }
        });

        loadDetails(view);

        return view;
    }

    public void loadDetails(View view) {
        TextView detailsTitle = view.findViewById(R.id.details_title);
        TextView detailsAuthor = view.findViewById(R.id.details_author);
        TextView detailsEdition = view.findViewById(R.id.details_edition);
        TextView detailsCondition = view.findViewById(R.id.details_condition);
        TextView detailsType = view.findViewById(R.id.details_type);
        TextView detailsCoursecode = view.findViewById(R.id.details_coursecode);
        TextView detailsPrice = view.findViewById(R.id.details_price);
        TextView detailsTimestamp = view.findViewById(R.id.details_timestamp);
        TextView detailsSellerName = view.findViewById(R.id.details_seller);

        Bundle bundle = getArguments();

        detailsTitle.setText(bundle.getString("Title"));
        detailsAuthor.setText(bundle.getString("Author"));
        detailsEdition.setText(bundle.getString("Edition"));
        detailsCondition.setText(bundle.getString("Condition"));
        detailsType.setText(bundle.getString("Type"));
        detailsCoursecode.setText(bundle.getString("Coursecode"));
        detailsPrice.setText(bundle.getString("Price"));
        detailsSellerName.setText(bundle.getString("SellerName"));
        detailsTimestamp.setText(bundle.getString("Timestamp"));

        final String imageUrl = bundle.getString("ImageUrl");
        final ImageView detailsImage = view.findViewById(R.id.details_image);
        if(!imageUrl.equals("")) {
            view.findViewById(R.id.details_image_instruction).setVisibility(View.VISIBLE);
            Glide.with(this).load(imageUrl).into(detailsImage);
            detailsImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity activity = (MainActivity) getActivity();
                    Bundle bundle = new Bundle();
                    bundle.putString("ImageUrl", imageUrl);
                    activity.setImageBundle(bundle);
                    activity.replaceFragment("ImageFragment");
                }
            });
        }
        else {
            view.findViewById(R.id.details_image_instruction).setVisibility(View.GONE);
            Glide.with(this).load(R.drawable.textbook_placeholder).into(detailsImage);
        }

        MainActivity activity = (MainActivity) getActivity();
        String textbookSellerId = bundle.getString("SellerId");
        if(activity.getUserId() != null) {
            // User is logged in
            String appUserId = activity.getUserId();
            if(appUserId.equals(textbookSellerId)) {
                // Textbook belongs to this user, hide the MESSAGE button
                Button messageButton = view.findViewById(R.id.details_message_button);
                messageButton.setVisibility(View.GONE);
            }
        }
    }

    public void messageButtonPressed() {
        MainActivity activity = (MainActivity) getActivity();
        String token = activity.getToken();
        if(token != null) {
            createSendDialog();
        }
        else {
            activity.replaceFragment("AccountFragment");
        }
    }

    public void createSendDialog() {
        MainActivity activity = (MainActivity) getActivity();
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity,
                R.style.AppTheme_AlertDialogTheme);

        final EditText messageInput = new EditText(activity);
        messageInput.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        messageInput.setSingleLine(false);
        messageInput.setHint("Type a message...");
        messageInput.setBackgroundResource(R.drawable.border);
        messageInput.setPadding(20, 15, 20, 15);
        messageInput.setHintTextColor(getResources().getColor(R.color.lightGray));
        messageInput.setMinWidth(1000);
        messageInput.setMaxHeight(500);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(60, 10, 60, 0);
        messageInput.setLayoutParams(layoutParams);

        LinearLayout parentLayout = new LinearLayout(activity);
        parentLayout.addView(messageInput);

        Bundle bundle = getArguments();
        String sellerName = bundle.getString("SellerName");
        alertDialog.setTitle("Message " + sellerName);
        alertDialog.setView(parentLayout);
        alertDialog.setCancelable(false);

        alertDialog.setPositiveButton("SEND", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String body = String.valueOf(messageInput.getText());
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        createConversation(body);
                    }
                }).start();
                dialog.dismiss();
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }

    public void createConversation(String body) {
        MainActivity activity = (MainActivity) getActivity();
        Bundle bundle = getArguments();
        String sellerId = bundle.getString("SellerId");
        String textbookId = bundle.getString("Id");

        CreateConversationCall createConversationCall = new CreateConversationCall(activity);
        createConversationCall.req(sellerId, textbookId);
        boolean isSuccessful = createConversationCall.isSuccessful();
        if(isSuccessful) {
            String conversationId = createConversationCall.getData();
            sendMessage(conversationId, body);
        }
    }

    public void sendMessage(final String conversationId, String body) {
        final MainActivity activity = (MainActivity) getActivity();

        SendMessageCall sendMessageCall = new SendMessageCall(activity);
        sendMessageCall.req(conversationId, body);

        boolean isSuccessful = sendMessageCall.isSuccessful();
        if(isSuccessful) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Bundle bundle = new Bundle();
                    bundle.putString("conversation_id", conversationId);
                    activity.setMessagesBundle(bundle);
                    activity.replaceFragment("MessagesFragment");
                }
            });
        }
        else {
            Alert alert = new Alert(activity);
            alert.create("Oh no! Server problem!", "Seems like we are unable to " +
                    "reach the server at the moment.\n\nPlease try again later.");
        }
    }
}
