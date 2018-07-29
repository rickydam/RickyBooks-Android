package com.rickybooks.rickybooks.Retrofit;

import com.rickybooks.rickybooks.MainActivity;
import com.rickybooks.rickybooks.Other.Alert;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DeleteImageCall {
    private MainActivity activity;
    private boolean isSuccessful;

    public DeleteImageCall(MainActivity activity) {
        this.activity = activity;
        isSuccessful = false;
    }

    public boolean isSuccessful() {
        return isSuccessful;
    }

    public void req(String signedDeleteUrl) {
        try {
            Retrofit retrofit = new RetrofitClient().getClient();
            TextbookService textbookService = retrofit.create(TextbookService.class);

            Call<Void> call = textbookService.deleteImage(signedDeleteUrl);
            Response<Void> response = call.execute();

            isSuccessful = response.isSuccessful();
            if(!isSuccessful) {
                Alert alert = new Alert(activity);
                alert.create("Oh no! Server problem!", "Seems like we are unable to " +
                        "reach the server at the moment.\n\nPlease try again later.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
