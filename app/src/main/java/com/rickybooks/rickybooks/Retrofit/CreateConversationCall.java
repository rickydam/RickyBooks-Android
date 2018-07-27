package com.rickybooks.rickybooks.Retrofit;

import com.google.gson.JsonObject;

import com.rickybooks.rickybooks.MainActivity;
import com.rickybooks.rickybooks.Other.Alert;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CreateConversationCall {
    private MainActivity activity;
    private boolean isSuccessful;
    private String conversationId;

    public CreateConversationCall(MainActivity activity) {
        this.activity = activity;
        isSuccessful = false;
        conversationId = null;
    }

    public boolean isSuccessful() {
        return isSuccessful;
    }

    public String getData() {
        return conversationId;
    }

    public void req(String sellerId, String textbookId) {
        String token = activity.getToken();
        String tokenString = "Token token=" + token;
        String userId = activity.getUserId();

        try {
            Retrofit retrofit = new RetrofitClient().getClient();
            TextbookService textbookService = retrofit.create(TextbookService.class);

            Call<JsonObject> call = textbookService.createConversation(tokenString, userId, sellerId,
                    textbookId);
            Response<JsonObject> response = call.execute();

            isSuccessful = response.isSuccessful();
            if(isSuccessful) {
                JsonObject obj = response.body();
                conversationId = obj.get("conversation_id").getAsString();
            }
            else {
                Alert alert = new Alert(activity);
                alert.create("Oh no! Server problem!", "Seems like we are unable to " +
                        "reach the server at the moment.\n\nPlease try again later.");
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
