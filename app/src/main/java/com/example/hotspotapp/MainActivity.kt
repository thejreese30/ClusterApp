package com.example.hotspotapp

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private lateinit var datab: DatabaseManager
    private lateinit var editText: EditText

    companion object {
        lateinit var db: DatabaseManager
        const val MA: String = "MainActivity"
        const val PREFS_NAME = "HotspotAppPrefs"
        const val PREF_USER_ID = "UserId"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        FirebaseApp.initializeApp(this)

        editText = findViewById(R.id.uid_box)
        val button = findViewById<Button>(R.id.submit)

        // Load saved user ID from preferences
        loadPreferences()

        button.setOnClickListener {
            val inputText = editText.text.toString()
            DatabaseHolder.db = DatabaseManager(inputText)

            // Save the user ID to preferences
            savePreferences(inputText)

            val intent = Intent(this, UserPage::class.java)
            startActivity(intent)

            Toast.makeText(this, "User ID: ${inputText}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun savePreferences(userId: String) {
        val sharedPref = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString(PREF_USER_ID, userId)
            apply()
        }
    }

    private fun loadPreferences() {
        val sharedPref = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val savedUserId = sharedPref.getString(PREF_USER_ID, "")
        editText.setText(savedUserId)
    }
}
