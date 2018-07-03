package com.example.rickydam.rickybooks;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface TextbookFormService {
    @FormUrlEncoded
    @POST("textbooks")
    Call<String> POST(@Header("Authorization") String tokenString,
                      @Field("user_id") String userId,
                      @Field("textbook_title") String textbookTitle,
                      @Field("textbook_author") String textbookAuthor,
                      @Field("textbook_edition") String textbookEdition,
                      @Field("textbook_condition") String textbookCondition,
                      @Field("textbook_type") String textbookType,
                      @Field("textbook_coursecode") String textbookCoursecode,
                      @Field("textbook_price") String textbookPrice
    );
}
