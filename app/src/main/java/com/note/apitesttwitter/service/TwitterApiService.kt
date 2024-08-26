package com.note.apitesttwitter.service

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface TwitterApiService {
    @POST("2/tweets")
    @Headers("Content-Type: application/json")
    fun postTweet(
        @Header("Authorization") authorization: String,
        @Body tweet: Tweet
    ): Call<TwitterResponse>
}

data class Tweet(val text: String)

data class TwitterResponse(val data: TweetData)

data class TweetData(val text: String)