package com.example.rickydam.rickybooks;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.PUT;
import retrofit2.http.Url;

public interface TextbookImageService {
    @PUT
    Call<Void> PUT(@Url String url,
                   @Body RequestBody imageFile
    );
}
