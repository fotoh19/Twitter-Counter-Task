package com.note.apitesttwitter.activity

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import com.note.apitesttwitter.R
import com.note.apitesttwitter.service.TwitterOAuth

class MainActivity : AppCompatActivity() {
    private val maxChar = 280
    private val consumerKey = "OWHFUbSTgIu2DYIdHzb4u7uPf"
    private val consumerSecret = "OvpZPwFKMaYGMv5BxWp1SXCEoWub5H0Mvta1hBsVQmBEJX7wDH"
    private lateinit var twitterOAuth: TwitterOAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val inputText: AppCompatEditText = findViewById(R.id.inputText)
        val charCount: TextView = findViewById(R.id.charCount)
        val charRemain: TextView = findViewById(R.id.charRemain)
        val copyButton: AppCompatButton = findViewById(R.id.copybtn)
        val clearButton: AppCompatButton = findViewById(R.id.clearbtn)
        val postButton: AppCompatButton = findViewById(R.id.postbtn)


        postButton.setOnClickListener {
            val tweetText = inputText.text.toString()
            authorizeAndPostTweet(tweetText)
        }
        twitterOAuth = TwitterOAuth(consumerKey, consumerSecret)




        inputText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }


            @SuppressLint("SetTextI18n")
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val charCounter = s?.length ?: 0
                val charRemaining = maxChar - charCounter
                charCount.text = "$charCounter /280"
                charRemain.text = "$charRemaining"

                if (charCounter >= maxChar) {
                    inputText.filters = arrayOf(android.text.InputFilter.LengthFilter(maxChar))
                }

            }

            override fun afterTextChanged(s: Editable?) {
            }


        })
        copyButton.setOnClickListener {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Copied Text", inputText.text.toString())
            clipboard.setPrimaryClip(clip)
        }
        clearButton.setOnClickListener {
            inputText.text?.clear()
        }


    }

    private fun authorizeAndPostTweet(tweetText: String) {
        val requestToken = twitterOAuth.getRequestToken()
        val authUrl = "https://api.twitter.com/oauth/authorize?oauth_token=${requestToken.token}"

        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(authUrl))
        startActivity(intent)

        val verifierPin = ""

        val accessToken = twitterOAuth.getAccessToken(requestToken, verifierPin)
        twitterOAuth.postTweet(accessToken, tweetText)
    }
    }