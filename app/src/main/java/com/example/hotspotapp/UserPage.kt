
package com.example.hotspotapp

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.location.LocationManager
import android.widget.Button
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.hotspotapp.MapDisplay
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds

class UserPage : AppCompatActivity() {

    lateinit var locationManager: LocationManager
    private lateinit var proximitySeekBar: SeekBar
    private lateinit var groupSizeEditText: EditText
    private lateinit var proximityTextView: TextView
    private lateinit var venueNameEditText: EditText
    lateinit var adView: AdView

    companion object {
        const val PREFS_NAME = "HotspotAppPrefs"
        const val PREF_PROXIMITY = "Proximity"
        const val PREF_GROUP_SIZE = "GroupSize"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.user_page)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.user_page)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager

        // Check for location permissions
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        } else {
            getLastKnownLocation()
            startLocationUpdates()
        }

        // Set up buttons and handlers
        val showLocationButton = findViewById<Button>(R.id.showLocationButton)
        val navigateButton = findViewById<Button>(R.id.navigateButton)
        proximitySeekBar = findViewById(R.id.proximity)
        groupSizeEditText = findViewById(R.id.groupSize)
        proximityTextView = findViewById(R.id.proximityTextView)
        venueNameEditText = findViewById(R.id.venueName)

        // Load saved preferences
        loadPreferences()

        // Initialize the Mobile Ads SDK
        MobileAds.initialize(this) {}

        // Find the AdView and load the ad
        adView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)

        // Update TextView text when SeekBar progress changes
        proximitySeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                proximityTextView.text = "Proximity: $progress"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                // call last known location lol
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                savePreferences()
            }
        })

        showLocationButton.setOnClickListener {
            getLastKnownLocation()

            // Capture the values of the SeekBar and EditText
            val proximityValue = proximitySeekBar.progress
            val groupSizeValue = groupSizeEditText.text.toString().toIntOrNull() ?: 0

            // Save the values
            savePreferences()

            // Display the values in a Toast message
            Toast.makeText(this, "Proximity: $proximityValue, Group Size: $groupSizeValue", Toast.LENGTH_LONG).show()

            //send proximity, group size in intent extra
            val intent = Intent(this, MapDisplay::class.java)
            intent.putExtra("proximity", proximityValue)
            intent.putExtra("group_size", groupSizeValue)
            startActivity(intent)
        }

        navigateButton.setOnClickListener {
            val venueName = venueNameEditText.text.toString()
            if (venueName.isNotEmpty()) {
                DatabaseHolder.db.load_venue(venueName)

                val intent = Intent(this, VenueDisplay::class.java)

                intent.putExtra("VENUE_NAME", venueName)

                startActivity(intent)
                Toast.makeText(this, "Loading venue: $venueName", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Please enter a venue name", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun getLastKnownLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            location?.let {
                Toast.makeText(this, "Location: ${it.latitude}, ${it.longitude}", Toast.LENGTH_LONG).show()
                DatabaseHolder.db.onLocationChanged(it)
            }
        }
    }

    private fun startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                1000L, // Minimum time interval between updates (in milliseconds)
                1f,   // Minimum distance between updates (in meters)
                DatabaseHolder.db
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        locationManager.removeUpdates(DatabaseHolder.db)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getLastKnownLocation()
            startLocationUpdates()
        }
    }

    private fun savePreferences() {
        val sharedPref = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putInt(PREF_PROXIMITY, proximitySeekBar.progress)
            putInt(PREF_GROUP_SIZE, groupSizeEditText.text.toString().toIntOrNull() ?: 0)
            apply()
        }
    }

    private fun loadPreferences() {
        val sharedPref = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val savedProximity = sharedPref.getInt(PREF_PROXIMITY, 0)
        val savedGroupSize = sharedPref.getInt(PREF_GROUP_SIZE, 0)

        proximitySeekBar.progress = savedProximity
        proximityTextView.text = "Proximity: $savedProximity"
        groupSizeEditText.setText(savedGroupSize.toString())
    }
}
