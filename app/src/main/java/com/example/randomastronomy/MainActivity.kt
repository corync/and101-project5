package com.example.randomastronomy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers

class MainActivity : AppCompatActivity() {
    private var astronomyImageURL = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.astronomyButton)
        val imageView = findViewById<ImageView>(R.id.astronomyImage)

        getAstronomyImageURL()
        getNextImage(button, imageView)
    }

    private fun getAstronomyImageURL() {
        val client = AsyncHttpClient()

        client["https://api.nasa.gov/planetary/apod?api_key=fVv4g2w2qdrudjNjJbnbwgXsSrbaTjAxocWMhLbc&hd=False&count=1", object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers, json: JsonHttpResponseHandler.JSON) {
                Log.d("Astronomy", "response successful $json")
                var jsonObject = json.jsonArray.getJSONObject(0)
                astronomyImageURL = jsonObject.getString("url")
                Log.d("Astronomy", "$astronomyImageURL image URL set")

            }

            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                errorResponse: String,
                throwable: Throwable?
            ) {
                Log.d("Astronomy Error", errorResponse)
            }
        }]
    }

    private fun getNextImage(button: Button, imageView: ImageView) {
        button.setOnClickListener {
            getAstronomyImageURL()

            Glide.with(this)
                . load(astronomyImageURL)
                .fitCenter()
                .into(imageView)
        }
    }

}