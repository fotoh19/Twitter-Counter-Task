package com.note.apitesttwitter.service

import com.github.scribejava.core.builder.ServiceBuilder
import com.github.scribejava.core.model.OAuth1AccessToken
import com.github.scribejava.core.model.OAuth1RequestToken
import com.github.scribejava.core.model.OAuthRequest
import com.github.scribejava.core.model.Verb
import com.github.scribejava.core.oauth.OAuth10aService
import com.note.apitesttwitter.api.TwitterApi
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TwitterOAuth(consumerKey: String, consumerSecret: String) {

    private val service: OAuth10aService = ServiceBuilder(consumerKey)
        .apiSecret(consumerSecret)
        .build(TwitterApi.instance())

    fun getRequestToken(): OAuth1RequestToken {
        return service.requestToken
    }

    fun getAccessToken(requestToken: OAuth1RequestToken, verifier: String): OAuth1AccessToken {
        return service.getAccessToken(requestToken, verifier)
    }

    fun postTweet(accessToken: OAuth1AccessToken, tweet: String) {
        val request = OAuthRequest(Verb.POST, "https://api.twitter.com/2/tweets")
        request.addHeader("Content-Type", "application/json")
        request.addHeader("Authorization", "OAuth ${accessToken.token}")

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.twitter.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val twitterApiService = retrofit.create(TwitterApiService::class.java)
        val call = twitterApiService.postTweet("OAuth ${accessToken.token}", Tweet(tweet))

        call.enqueue(object : retrofit2.Callback<TwitterResponse> {
            override fun onResponse(call: Call<TwitterResponse>, response: retrofit2.Response<TwitterResponse>) {
                if (response.isSuccessful) {
                    println("Tweet posted successfully: ${response.body()?.data?.text}")
                } else {
                    println("Failed to post tweet: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<TwitterResponse>, t: Throwable) {
                println("Error: ${t.message}")
            }
        })
    }
}



