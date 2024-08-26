package com.note.apitesttwitter.api

import com.github.scribejava.core.builder.api.DefaultApi10a

class TwitterApi private constructor() : DefaultApi10a() {
    override fun getRequestTokenEndpoint(): String {
        return "https://api.twitter.com/oauth/request_token"
    }

    override fun getAccessTokenEndpoint(): String {
        return "https://api.twitter.com/oauth/access_token"
    }

    override fun getAuthorizationBaseUrl(): String {
        return "https://api.twitter.com/oauth/authorize"
    }

    companion object {
        private val instance = TwitterApi()
        fun instance(): TwitterApi = instance
    }
}