package com.rickybooks.rickybooks.Retrofit;

import com.rickybooks.rickybooks.MainActivity;
import com.rickybooks.rickybooks.Other.Alert;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DeleteUserCall {
    private MainActivity activity;
    private TextbookService textbookService;

    public DeleteUserCall(MainActivity activity) {
        this.activity = activity;
        Retrofit retrofit = new RetrofitClient().getClient();
        textbookService = retrofit.create(TextbookService.class);
    }

    public void req() {
        String token = activity.getToken();
        String tokenString = "Token token=" + token;
        String userId = activity.getUserId();

        try {
            Call<Void> call = textbookService.deleteUser(tokenString, userId);
            Response<Void> response = call.execute();

            if(response.isSuccessful()) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        activity.setInitialState();
                    }
                });
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
}
