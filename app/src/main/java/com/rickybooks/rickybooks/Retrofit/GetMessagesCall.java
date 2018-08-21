package com.rickybooks.rickybooks.Retrofit;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import com.rickybooks.rickybooks.MainActivity;
import com.rickybooks.rickybooks.Models.Message;

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

public class GetMessagesCall {
    private MainActivity activity;
    private List<Message> messages;

    public GetMessagesCall(MainActivity activity) {
        this.activity = activity;
        messages = new ArrayList<>();
    }

    public List<Message> getData() {
        return messages;
    }

    public void req(String conversationId) {
        try {
            Retrofit retrofit = new RetrofitClient().getClient();
            TextbookService textbookService = retrofit.create(TextbookService.class);

            String token = activity.getToken();
            String tokenString = "Token token=" + token;

            Call<JsonArray> call = textbookService.getMessages(tokenString, conversationId);
            Response<JsonArray> response = call.execute();

            if(response.isSuccessful()) {
                parseConversationData(response.body());
            }
        } catch(IOException e) {
            e.printStackTrace();
        } catch(NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void parseConversationData(JsonArray responseArr) {
        try {
            for(int i=0; i<responseArr.size(); i++) {
                JsonObject obj = responseArr.get(i).getAsJsonObject();

                String body = obj.get("body").getAsString();
                JsonObject user = obj.get("user").getAsJsonObject();
                String sender = user.get("name").getAsString();

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
                        Locale.CANADA);
                sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                String created_at = obj.get("created_at").getAsString();
                Date date = sdf.parse(created_at);

                Message message = new Message(body, sender, date);
                messages.add(message);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
