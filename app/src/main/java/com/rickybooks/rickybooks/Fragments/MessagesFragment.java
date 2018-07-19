package com.rickybooks.rickybooks.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import com.rickybooks.rickybooks.Adapters.MessageAdapter;
import com.rickybooks.rickybooks.MainActivity;
import com.rickybooks.rickybooks.Models.Message;
import com.rickybooks.rickybooks.R;
import com.rickybooks.rickybooks.Retrofit.RetrofitClient;
import com.rickybooks.rickybooks.Retrofit.TextbookService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MessagesFragment extends Fragment {
    private List<Message> messagesList = new ArrayList<>();
    private MessageAdapter messageAdapter;
    private TextbookService textbookService;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Retrofit retrofit = new RetrofitClient().getClient();
        textbookService = retrofit.create(TextbookService.class);
        getMessagesReq();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_messages, container, false);

        final SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.messages_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getMessagesReq();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        Button sendButton = view.findViewById(R.id.messages_send_button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                sendMessageReq(view);
            }
        });

        MainActivity activity = (MainActivity) getActivity();
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        layoutManager.setStackFromEnd(true);
        messageAdapter = new MessageAdapter(activity, messagesList);

        RecyclerView messageRecycler = view.findViewById(R.id.messages_recycler);
        messageRecycler.setHasFixedSize(true);
        messageRecycler.setLayoutManager(layoutManager);
        messageRecycler.setAdapter(messageAdapter);

        return view;
    }

    public void getMessagesReq() {
        clearMessages();
        MainActivity activity = (MainActivity) getActivity();
        String tokenString = getTokenString();
        String conversationId = activity.getConversationId();

        Call<JsonArray> call = textbookService.getMessages(tokenString, conversationId);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                getMessagesRes(response);
            }
            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.e("Ricky", "getMessagesReq failure: " + t.getMessage());
                createAlert("Oh no! Server problem!", "Seems like we are unable to " +
                        "reach the server at the moment.\n\nPlease try again later.");
            }
        });
    }

    public void getMessagesRes(Response<JsonArray> response) {
        if(response.isSuccessful()) {
            try {
                String res = String.valueOf(response.body());
                JSONArray resData = new JSONArray(res);
                for(int i=0; i<resData.length(); i++) {
                    JSONObject obj = resData.getJSONObject(i);

                    String body = obj.getString("body");
                    JSONObject user = obj.getJSONObject("user");
                    String sender = user.getString("name");

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
                            Locale.CANADA);
                    sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                    String created_at = obj.getString("created_at");
                    Date date = sdf.parse(created_at);

                    Message message = new Message(body, sender, date);
                    messagesList.add(message);
                    messageAdapter.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        else {
            try {
                String errorMessage = response.errorBody().string();
                Log.e("Ricky", "getMessagesReq unsuccessful: " + errorMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }
            createAlert("Oh no! Server problem!", "Seems like we are unable to " +
                    "reach the server at the moment.\n\nPlease try again later.");
        }
    }

    public void sendMessageReq(final View view) {
        String tokenString = getTokenString();
        MainActivity activity = (MainActivity) getActivity();
        String conversationId = activity.getConversationId();
        EditText messageInput = view.findViewById(R.id.messages_message_input);
        String message = String.valueOf(messageInput.getText());
        String userId = activity.getUserId();

        Call<JsonObject> call = textbookService.sendMessage(tokenString, conversationId, message,
                userId);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                sendMessageRes(view, response);
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("Ricky", "sendMessageReq failure: " + t.getMessage());
                createAlert("Oh no! Server problem!", "Seems like we are unable to " +
                        "reach the server at the moment.\n\nPlease try again later.");
            }
        });
    }

    public void sendMessageRes(View v, Response<JsonObject> response) {
        if(response.isSuccessful()) {
            EditText messageInput = v.findViewById(R.id.messages_message_input);
            messageInput.getText().clear();
            hideKeyboard(v);
            getMessagesReq();
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

    public void clearMessages() {
        messagesList.clear();
    }

    public String getTokenString() {
        MainActivity activity = (MainActivity) getActivity();
        String token = activity.getToken();
        return "Token token=" + token;
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

    public void hideKeyboard(View view) {
        Context context = getActivity().getApplicationContext();
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        if(inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
