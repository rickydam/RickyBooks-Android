package com.rickybooks.rickybooks.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.google.gson.JsonObject;

import com.rickybooks.rickybooks.MainActivity;
import com.rickybooks.rickybooks.R;
import com.rickybooks.rickybooks.Retrofit.RetrofitClient;
import com.rickybooks.rickybooks.Retrofit.TextbookService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DetailsFragment extends Fragment {
    private TextbookService textbookService;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Retrofit retrofit = new RetrofitClient().getClient();
        textbookService = retrofit.create(TextbookService.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);

        Button button = view.findViewById(R.id.details_send_button);
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
        TextView detailsSellerName = view.findViewById(R.id.details_seller);

        Bundle bundle = getArguments();

        detailsTitle.setText((String) bundle.get("Title"));
        detailsAuthor.setText((String) bundle.get("Author"));
        detailsEdition.setText((String) bundle.get("Edition"));
        detailsCondition.setText((String) bundle.get("Condition"));
        detailsType.setText((String) bundle.get("Type"));
        detailsCoursecode.setText((String) bundle.get("Coursecode"));
        detailsPrice.setText((String) bundle.get("Price"));
        detailsSellerName.setText((String) bundle.get("SellerName"));

        String imageUrl = bundle.getString("ImageUrl");
        ImageView detailsImage = view.findViewById(R.id.details_image);
        if(!imageUrl.equals("")) {
            Glide.with(this).load(imageUrl).into(detailsImage);
        }
        else {
            Glide.with(this).load(R.drawable.textbook_placeholder).into(detailsImage);
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
        Bundle bundle = getArguments();
        String sellerName = (String) bundle.get("SellerName");
        Context context = getActivity();
        SharedPreferences sharedPref = context.getSharedPreferences(
                "com.rickybooks.rickybooks", Context.MODE_PRIVATE);
        String currentPersonName = sharedPref.getString("name", null);

        if(!sellerName.equals(currentPersonName)) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context,
                    R.style.AppTheme_AlertDialogTheme);

            final EditText messageInput = new EditText(context);
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

            LinearLayout parentLayout = new LinearLayout(context);
            parentLayout.addView(messageInput);

            alertDialog.setTitle("Message " + sellerName);
            alertDialog.setView(parentLayout);
            alertDialog.setCancelable(false);

            alertDialog.setPositiveButton("SEND", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String message = String.valueOf(messageInput.getText());
                    createConversationReq(message);
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
        else {
            createAlert("This is your textbook!", "Don't send yourself messages...");
        }
    }

    public void createConversationReq(final String message) {
        String tokenString = getTokenString();
        String userId = getUserId();
        Bundle bundle = getArguments();
        String sellerId = bundle.getString("SellerId");
        String textbookId = bundle.getString("Id");

        Call<JsonObject> call = textbookService.createConversation(tokenString, userId, sellerId,
                textbookId);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                createConversationRes(message, response);
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("Ricky", "createConversationReq failure: " + t.getMessage());
                createAlert("Oh no! Server problem!", "Seems like we are unable to " +
                        "reach the server at the moment.\n\nPlease try again later.");
            }
        });
    }

    public void createConversationRes(String message, Response<JsonObject> response) {
        if(response.isSuccessful()) {
            String res = String.valueOf(response.body());
            String conversationId = "";
            try {
                JSONObject obj = new JSONObject(res);
                conversationId = obj.getString("conversation_id");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            sendMessageReq(message, conversationId);
        }
        else {
            try {
                String errorMessage = response.errorBody().string();
                Log.e("Ricky", "createConversationReq unsuccessful: " + errorMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }
            createAlert("Oh no! Server problem!", "Seems like we are unable to " +
                    "reach the server at the moment.\n\nPlease try again later.");
        }
    }

    public void sendMessageReq(String message, String conversationId) {
        String tokenString = getTokenString();
        String userId = getUserId();
        Call<JsonObject> call = textbookService.sendMessage(tokenString, conversationId, message,
            userId);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                sendMessageRes(response);
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("Ricky", "sendMessageReq failure: " + t.getMessage());
                createAlert("Oh no! Server problem!", "Seems like we are unable to " +
                        "reach the server at the moment.\n\nPlease try again later.");
            }
        });
    }

    public void sendMessageRes(Response<JsonObject> response) {
        if(response.isSuccessful()) {
            createAlert("Success!", "Message has been sent successfully.");
        }
        else {
            try {
                String errorMessage = response.errorBody().string();
                Log.e("Ricky", "sendMessageReq unsuccessful: " + errorMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }
            createAlert("Seems like you forgot something...", "That's right! " +
                    "The message!");
        }
    }

    public String getTokenString() {
        MainActivity activity = (MainActivity) getActivity();
        String token = activity.getToken();
        return "Token token=" + token;
    }

    public String getUserId() {
        MainActivity activity = (MainActivity) getActivity();
        return activity.getUserId();
    }

    public void createAlert(String title, String message) {
        Activity activity = getActivity();
        AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }
}
