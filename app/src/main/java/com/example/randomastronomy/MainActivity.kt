package com.example.randomastronomy

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.RequestParams
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers
import java.util.*

class MainActivity : AppCompatActivity() {
    private var astronomyImageURL = ""
    private var astronomyDate = ""
    private var astronomyTitle = ""
    private var chosenDate = ""
    private var API_KEY = "fVv4g2w2qdrudjNjJbnbwgXsSrbaTjAxocWMhLbc"

    lateinit var pickDateBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.astronomyButton)
        val imageView = findViewById<ImageView>(R.id.astronomyImage)
        val date = findViewById<TextView>(R.id.date)
        val title = findViewById<TextView>(R.id.title)
        val pickDateBtn = findViewById<Button>(R.id.datePickerButton)


        getAstronomyData()

        button.setOnClickListener {
            getAstronomyData()

            Glide.with(this)
                . load(astronomyImageURL)
                .fitCenter()
                .into(imageView)

            date.setText(astronomyDate)
            title.setText(astronomyTitle)

        }

        pickDateBtn.setOnClickListener {
            val c = Calendar.getInstance()

            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                { view, year, monthOfYear, dayOfMonth ->

                        chosenDate = ("$year-"+(monthOfYear+1)+"-$dayOfMonth")

                    Log.d("Astronomy", "$chosenDate dateChosen")
                },
                year,
                month,
                day
            )
            datePickerDialog.getDatePicker().setMaxDate(c.timeInMillis);
            val minDay = 16
            val minMonth = 6
            val minYear = 1995
            c.set(minYear, minMonth-1, minDay)
            datePickerDialog.datePicker.minDate = c.timeInMillis
            datePickerDialog.show()
            getAstronomyDataChosen(chosenDate)
        }

    }

    private fun getAstronomyDataChosen(chosenDate:String) {
        val client = AsyncHttpClient()

        val params = RequestParams()
        params["api_key"] = API_KEY
        params["date"] = chosenDate
        params["hd"] = "False"
        params["count"] = "1"



        client["https://api.nasa.gov/planetary/apod?", params, object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers, json: JsonHttpResponseHandler.JSON) {
                Log.d("Astronomy", "response successful $json")
                var jsonObject = json.jsonArray.getJSONObject(0)
                astronomyImageURL = jsonObject.getString("url")
                Log.d("Astronomy", "$astronomyImageURL image URL set")
                astronomyDate = jsonObject.getString("date")
                Log.d("Astronomy", "$astronomyDate date set")
                astronomyTitle = jsonObject.getString("title")
                Log.d("Astronomy", "$astronomyTitle title set")

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

    private fun getAstronomyData() {
        val client = AsyncHttpClient()

        val params = RequestParams()
        params["api_key"] = API_KEY
        params["hd"] = "False"
        params["count"] = "1"

        client["https://api.nasa.gov/planetary/apod?", params, object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers, json: JsonHttpResponseHandler.JSON) {
                Log.d("Astronomy", "response successful $json")
                var jsonObject = json.jsonArray.getJSONObject(0)
                astronomyImageURL = jsonObject.getString("url")
                Log.d("Astronomy", "$astronomyImageURL image URL set")
                astronomyDate = jsonObject.getString("date")
                Log.d("Astronomy", "$astronomyDate date set")
                astronomyTitle = jsonObject.getString("title")
                Log.d("Astronomy", "$astronomyTitle title set")

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


}