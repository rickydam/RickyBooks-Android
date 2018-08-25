package com.rickybooks.rickybooks.Retrofit;

import android.util.Log;

import com.rickybooks.rickybooks.MainActivity;
import com.rickybooks.rickybooks.Other.Alert;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PostNotifyItemCall {
    private MainActivity activity;
    private boolean isSuccessful;

    public PostNotifyItemCall(MainActivity activity) {
        this.activity = activity;
        isSuccessful = false;
    }

    public boolean isSuccessful() {
        return isSuccessful;
    }

    public void req(String category, String input) {
        String token = activity.getToken();
        String tokenString = "Token token=" + token;
        String userId = activity.getUserId();

        try {
            Retrofit retrofit = new RetrofitClient().getClient();
            TextbookService textbookService = retrofit.create(TextbookService.class);

            Call<Void> call = textbookService.postNotifyItem(tokenString, userId, category, input);
            Response<Void> response = call.execute();

            isSuccessful = response.isSuccessful();
            if(!isSuccessful) {
                if(response.code() == 422) {
                    Alert alert = new Alert(activity);
                    alert.create("What are you searching for?", "Make sure to add a keyword!");
                }
                else {
                    Alert alert = new Alert(activity);
                    alert.create("Oh no! Server problem!", "Seems like we are unable to " +
                            "reach the server at the moment.\n\nPlease try again later.");
                }
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
