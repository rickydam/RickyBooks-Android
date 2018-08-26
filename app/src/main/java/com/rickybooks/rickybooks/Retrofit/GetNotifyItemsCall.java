package com.rickybooks.rickybooks.Retrofit;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import com.rickybooks.rickybooks.MainActivity;
import com.rickybooks.rickybooks.Models.NotifyItem;
import com.rickybooks.rickybooks.Other.Alert;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class GetNotifyItemsCall {
    private MainActivity activity;
    private List<NotifyItem> notifyItems;
    private boolean isSuccessful;

    public GetNotifyItemsCall(MainActivity activity) {
        this.activity = activity;
        notifyItems = new ArrayList<>();
        isSuccessful = false;
    }

    public List<NotifyItem> getData() {
        return notifyItems;
    }

    public boolean isSuccessful() {
        return isSuccessful;
    }

    public void req() {
        String token = activity.getToken();
        String tokenString = "Token token=" + token;

        try {
            Retrofit retrofit = new RetrofitClient().getClient();
            TextbookService textbookService = retrofit.create(TextbookService.class);

            Call<JsonArray> call = textbookService.getNotifyItems(tokenString);
            Response<JsonArray> response = call.execute();

            isSuccessful = response.isSuccessful();
            if(isSuccessful) {
                JsonArray resData = response.body();
                for(int i=0; i<resData.size(); i++) {
                    JsonObject obj = resData.get(i).getAsJsonObject();
                    String idValue = obj.get("id").getAsString();
                    String categoryValue = obj.get("category").getAsString();
                    String inputValue = obj.get("input").getAsString();
                    NotifyItem notifyItem = new NotifyItem(idValue, categoryValue, inputValue);
                    notifyItems.add(notifyItem);
                }
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
