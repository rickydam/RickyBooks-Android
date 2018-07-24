package com.rickybooks.rickybooks.Retrofit;

import com.rickybooks.rickybooks.MainActivity;
import com.rickybooks.rickybooks.Other.Alert;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class GetSignedPostUrlCall {
    private MainActivity activity;
    private String signedPostUrl;

    public GetSignedPostUrlCall(MainActivity activity) {
        this.activity = activity;
    }

    public String getData() {
        return signedPostUrl;
    }

    public void req(String textbookId, String chosenImageFileExtension) {
        Retrofit retrofit = new RetrofitClient().getClient();
        TextbookService textbookService = retrofit.create(TextbookService.class);

        String token = activity.getToken();
        String tokenString = "Token token=" + token;

        try {
            Call<String> call = textbookService.getSignedPostUrl(tokenString, textbookId,
                    chosenImageFileExtension);
            Response<String> response = call.execute();

            if(response.isSuccessful()) {
                signedPostUrl = response.body();
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
