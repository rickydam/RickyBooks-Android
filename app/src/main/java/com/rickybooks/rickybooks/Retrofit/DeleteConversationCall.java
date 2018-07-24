package com.rickybooks.rickybooks.Retrofit;

import com.rickybooks.rickybooks.MainActivity;
import com.rickybooks.rickybooks.Models.Conversation;
import com.rickybooks.rickybooks.Other.Alert;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DeleteConversationCall {
    private MainActivity activity;

    public DeleteConversationCall(MainActivity activity) {
        this.activity = activity;
    }

    public void req(Conversation conversation) {
        String token = activity.getToken();
        String tokenString = "Token token=" + token;
        String conversationId = conversation.getId();

        try {
            Retrofit retrofit = new RetrofitClient().getClient();
            TextbookService textbookService = retrofit.create(TextbookService.class);

            Call<Void> call = textbookService.deleteConversation(tokenString, conversationId);
            Response<Void> response = call.execute();

            if(!response.isSuccessful()) {
                Alert alert = new Alert(activity);
                alert.create("Oh no! Server problem!", "Seems like we are unable to " +
                        "reach the server at the moment.\n\nPlease try again later.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
