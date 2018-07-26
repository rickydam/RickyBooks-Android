package com.rickybooks.rickybooks.Retrofit;

import com.google.firebase.iid.FirebaseInstanceId;

import com.rickybooks.rickybooks.MainActivity;
import com.rickybooks.rickybooks.Other.Alert;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class StoreFirebaseTokenCall {
    private MainActivity activity;

    public StoreFirebaseTokenCall(MainActivity activity) {
        this.activity = activity;
    }

    public void req() {
        Retrofit retrofit = new RetrofitClient().getClient();
        TextbookService textbookService = retrofit.create(TextbookService.class);

        String token = activity.getToken();
        String tokenString = "Token token=" + token;
        String userId = activity.getUserId();
        String firebaseToken = FirebaseInstanceId.getInstance().getToken();

        try {
            Call<Void> call = textbookService.storeFirebaseToken(tokenString, userId, firebaseToken);
            Response<Void> response = call.execute();

            if(response.isSuccessful()) {
                // LoginFragment -> AccountFragment -> The fragment the user was at
                activity.getSupportFragmentManager().popBackStack();

                // AccountFragment -> The fragment the user was at
                activity.getSupportFragmentManager().popBackStack();

                // Get the fragment the user wanted
                final String wantedFragmentName = activity.getWantedFragmentName();

                // Redirect to the fragment the user wants
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        activity.replaceFragment(wantedFragmentName);
                    }
                });

                // Special case, no need to add this to the stacks, so pop it
                activity.loginPop();
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
