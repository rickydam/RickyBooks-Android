package com.example.rickydam.rickybooks;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface TextbookUrlService {
    @GET("aws/{textbook_id}/{extension}")
    Call<String> GET(@Header("Authorization") String tokenString,
                     @Path("textbook_id") String textbookId,
                     @Path("extension") String extension
    );
}
