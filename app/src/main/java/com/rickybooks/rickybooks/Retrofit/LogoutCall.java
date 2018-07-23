package com.rickybooks.rickybooks.Retrofit;

import com.rickybooks.rickybooks.MainActivity;
import com.rickybooks.rickybooks.Other.Alert;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LogoutCall {
    private MainActivity activity;
    private TextbookService textbookService;

    public LogoutCall(MainActivity activity) {
        this.activity = activity;
        Retrofit retrofit = new RetrofitClient().getClient();
        textbookService = retrofit.create(TextbookService.class);
    }

    public void req() {
        String token = activity.getToken();
        String tokenString = "Token token=" + token;

        try {
            Call<Void> call = textbookService.logout(tokenString);
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
