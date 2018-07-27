package com.rickybooks.rickybooks.Retrofit;

import com.rickybooks.rickybooks.MainActivity;
import com.rickybooks.rickybooks.Other.Alert;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SendMessageCall {
    private MainActivity activity;
    private boolean isSuccessful;

    public SendMessageCall(MainActivity activity) {
        this.activity = activity;
        isSuccessful = false;
    }

    public boolean isSuccessful() {
        return isSuccessful;
    }

    public void req(String conversationId, String body) {
        String token = activity.getToken();
        String tokenString = "Token token=" + token;
        String userId = activity.getUserId();

        try {
            Retrofit retrofit = new RetrofitClient().getClient();
            TextbookService textbookService = retrofit.create(TextbookService.class);

            Call<Void> call = textbookService.sendMessage(tokenString, conversationId, body, userId);
            Response<Void> response = call.execute();

            isSuccessful = response.isSuccessful();
            if(!isSuccessful) {
                Alert alert = new Alert(activity);
                alert.create("Oh no! Server problem!", "Seems like we are unable to " +
                        "reach the server at the moment.\n\nPlease try again later.");
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
