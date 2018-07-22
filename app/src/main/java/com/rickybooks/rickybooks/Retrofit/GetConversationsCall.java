package com.rickybooks.rickybooks.Retrofit;

import com.google.gson.JsonArray;

import com.rickybooks.rickybooks.MainActivity;
import com.rickybooks.rickybooks.Models.Conversation;
import com.rickybooks.rickybooks.Other.Alert;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class GetConversationsCall {
    private MainActivity activity;
    private TextbookService textbookService;
    private List<Conversation> conversations;

    public GetConversationsCall(MainActivity activity) {
        Retrofit retrofit = new RetrofitClient().getClient();
        textbookService = retrofit.create(TextbookService.class);
        this.activity = activity;
        conversations = new ArrayList<>();
    }

    public List<Conversation> getData() {
        return conversations;
    }

    public void req() {
        String token = activity.getToken();
        String tokenString = "Token token=" + token;
        String userId = activity.getUserId();

        try {
            Call<JsonArray> call = textbookService.getConversations(tokenString, userId);
            Response<JsonArray> response = call.execute();

            if(response.isSuccessful()) {
                parseConversationData(response.body());
            }
            else {
                Alert alert = new Alert(activity);
                alert.create("Oh no! Server problem!", "Seems like we are unable to " +
                        "reach the server at the moment.\n\nPlease try again later.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseConversationData(JsonArray responseArr) {
        try {
            String res = String.valueOf(responseArr);
            JSONArray resData = new JSONArray(res);

            for(int i=0; i<resData.length(); i++) {
                JSONObject obj = resData.getJSONObject(i);
                String conversationId = obj.getString("id");

                JSONObject recipientObj = obj.getJSONObject("recipient");
                String recipientName = recipientObj.getString("name");

                JSONObject senderObj = obj.getJSONObject("sender");
                String senderName = senderObj.getString("name");
                String otherName;
                String userName = activity.getUserName();

                if(userName.equals(recipientName)) {
                    otherName = senderName;
                } else {
                    otherName = recipientName;
                }

                Conversation conversation = new Conversation(conversationId, otherName);
                conversations.add(conversation);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
