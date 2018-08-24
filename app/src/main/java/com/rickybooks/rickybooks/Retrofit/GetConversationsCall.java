package com.rickybooks.rickybooks.Retrofit;

import com.google.gson.JsonObject;

import com.rickybooks.rickybooks.MainActivity;
import com.rickybooks.rickybooks.Models.Conversation;
import com.rickybooks.rickybooks.Models.Message;
import com.rickybooks.rickybooks.Other.Alert;

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
import retrofit2.Response;
import retrofit2.Retrofit;

public class GetConversationsCall {
    private MainActivity activity;
    private List<Conversation> conversations;
    private List<Message> lastMessages;

    public GetConversationsCall(MainActivity activity) {
        this.activity = activity;
        conversations = new ArrayList<>();
        lastMessages = new ArrayList<>();
    }

    public List<Conversation> getConversationsData() {
        return conversations;
    }

    public List<Message> getLastMessagesData() {
        return lastMessages;
    }

    public void req() {
        String token = activity.getToken();
        String tokenString = "Token token=" + token;
        String userId = activity.getUserId();

        try {
            Retrofit retrofit = new RetrofitClient().getClient();
            TextbookService textbookService = retrofit.create(TextbookService.class);

            Call<JsonObject> call = textbookService.getConversations(tokenString, userId);
            Response<JsonObject> response = call.execute();

            if(response.isSuccessful()) {
                JsonObject responseObj = response.body();
                String conversationStr = String.valueOf(responseObj.get("conversations"));
                String lastMessagesStr = String.valueOf(responseObj.get("last_messages"));
                try {
                    JSONArray conversationData = new JSONArray(conversationStr);
                    parseConversationData(conversationData);
                    JSONArray lastMessagesData =  new JSONArray(lastMessagesStr);
                    parseMessageData(lastMessagesData);
                } catch(JSONException e) {
                    e.printStackTrace();
                }
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

    private void parseConversationData(JSONArray conversationsData) {
        try {
            for(int i=0; i<conversationsData.length(); i++) {
                JSONObject obj = conversationsData.getJSONObject(i);
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

    private void parseMessageData(JSONArray lastMessagesData) {
        try {
            for(int i=0; i<lastMessagesData.length(); i++) {
                JSONObject obj = lastMessagesData.getJSONObject(i);

                String body = obj.getString("body");
                JSONObject user = obj.getJSONObject("user");
                String sender = user.getString("name");

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
                        Locale.CANADA);
                sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                String created_at = obj.getString("created_at");
                Date date = sdf.parse(created_at);

                Message message = new Message(body, sender, date);
                lastMessages.add(message);
            }
        } catch(JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
