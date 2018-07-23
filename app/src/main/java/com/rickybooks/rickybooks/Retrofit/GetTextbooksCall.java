package com.rickybooks.rickybooks.Retrofit;

import com.google.gson.JsonArray;

import com.rickybooks.rickybooks.MainActivity;
import com.rickybooks.rickybooks.Models.Textbook;
import com.rickybooks.rickybooks.Other.Alert;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class GetTextbooksCall {
    private MainActivity activity;
    private TextbookService textbookService;
    private List<Textbook> textbooks;

    public GetTextbooksCall(MainActivity activity) {
        this.activity = activity;
        Retrofit retrofit = new RetrofitClient().getClient();
        textbookService = retrofit.create(TextbookService.class);
        textbooks = new ArrayList<>();
    }

    public List<Textbook> getData() {
        return textbooks;
    }

    public void req() {
        try {
            Call<JsonArray> call = textbookService.getTextbooks();
            Response<JsonArray> response = call.execute();

            if(response.isSuccessful()) {
                parseTextbookData(response.body());
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

    private void parseTextbookData(JsonArray responseArr) {
        String res = String.valueOf(responseArr);
        try {
            JSONArray resData = new JSONArray(res);

            for(int i=0; i<resData.length(); i++) {
                JSONObject textbookObj = resData.getJSONObject(i);
                String id         = textbookObj.getString("id");
                String title      = textbookObj.getString("textbook_title");
                String author     = textbookObj.getString("textbook_author");
                String edition    = textbookObj.getString("textbook_edition");
                String condition  = textbookObj.getString("textbook_condition");
                String type       = textbookObj.getString("textbook_type");
                String coursecode = textbookObj.getString("textbook_coursecode");
                String price      = "$ " + textbookObj.getString("textbook_price");

                JSONObject user = textbookObj.getJSONObject("user");
                String sellerId = textbookObj.getString("user_id");
                String sellerName = user.getString("name");

                List<String> imageUrls = new ArrayList<>();
                JSONArray imagesJSONarr = textbookObj.getJSONArray("images");

                for(int j=0; j<imagesJSONarr.length(); j++) {
                    JSONObject imageObj = imagesJSONarr.getJSONObject(j);
                    String url = imageObj.getString("url");
                    imageUrls.add(url);
                }

                String createdAt = textbookObj.getString("created_at");
                String timestamp = parseTime(createdAt);

                Textbook textbook = new Textbook(id, title, author, edition, condition, type,
                        coursecode, price, sellerId, sellerName, timestamp, imageUrls);

                textbooks.add(textbook);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String parseTime(String createdAt) {
        SimpleDateFormat rubyDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
                Locale.CANADA);
        rubyDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        Date createdAtDate = null;
        try {
            createdAtDate = rubyDateFormat.parse(createdAt);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String timestamp = "";

        Long currentTime = new Date().getTime();
        Long createdAtTime = createdAtDate.getTime();
        Long differenceMillis = currentTime - createdAtTime;
        int differenceSeconds = (int) (differenceMillis/1000);
        int differenceMinutes = differenceSeconds/60;
        int differenceHours = differenceMinutes/60;

        if(differenceHours >= 24) {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm, MMMM dd, yyyy", Locale.CANADA);
            timestamp = sdf.format(createdAtDate);
        }
        if(differenceHours < 24 && differenceHours > 1) {
            timestamp = differenceHours + " hours ago";
        }
        if(differenceHours == 1) {
            timestamp = differenceHours + " hour ago";
        }
        if(differenceMinutes < 60 && differenceMinutes > 1) {
            timestamp = differenceMinutes + " minutes ago";
        }
        if(differenceMinutes == 1) {
            timestamp = differenceMinutes + " minute ago";
        }
        if(differenceSeconds < 60 && differenceSeconds > 30) {
            timestamp = differenceSeconds + " seconds ago";
        }
        if(differenceSeconds <= 30) {
            timestamp = "Just now";
        }

        return timestamp;
    }
}
