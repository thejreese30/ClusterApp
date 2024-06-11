package com.example.hotspotapp

import android.os.Bundle
import android.widget.Button
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class VenueDisplay : AppCompatActivity() {

    private lateinit var venueNameTextView: TextView
    private lateinit var venueRatingBar: RatingBar
    private lateinit var rateButton: Button
    private lateinit var backButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.venue_display)

        venueNameTextView = findViewById(R.id.venueNameTextView)
        venueRatingBar = findViewById(R.id.venueRatingBar)
        rateButton = findViewById(R.id.rateButton)
        backButton = findViewById(R.id.backButton)

        // Retrieve data from the Intent
        val venueName = intent.getStringExtra("VENUE_NAME")


        // Set the venue name and current rating
        venueNameTextView.text = venueName
//        venueRatingBar.rating = currentRating

        // Handle rate button click
        rateButton.setOnClickListener {
            val newRating = venueRatingBar.rating
            DatabaseHolder.db.rate_venue(venueName!!, newRating.toInt())
            Toast.makeText(this, "Rated $venueName: $newRating stars", Toast.LENGTH_SHORT).show()
        }


        backButton.setOnClickListener {
            finish()
        }
    }
}
