
package com.example.hotspotapp

import android.location.Location
import android.location.LocationListener
import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.ktx.toObject

class DatabaseManager : LocationListener {

    val db = Firebase.firestore
    var users: List<User> = listOf()
    var venues: List<Venue> = listOf()
    lateinit var user: User

    constructor(name: String) {
        user = User(name, LatLng(0.0, 0.0))
        load_user(name)
    }

    override fun onLocationChanged(location: Location) {
        val loc = LatLng(location.latitude, location.longitude)
        user.update_location(location)
        update_user_location(user.uid, loc)
        Log.d(MainActivity.MA, "New Location: ${location.latitude}, ${location.longitude}")
    }

    fun load_user(uid: String) {
        val docRef = db.collection("users").document(uid)
        docRef.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                val u = documentSnapshot.toObject<User>()
                user = u!!
            } else {
                val u = User(uid, LatLng(0.0, 0.0))
                user = u
                Log.d(MainActivity.MA, "User successfully written")
                docRef.set(u)
            }
        }
    }

    fun load_venue(name: String) {
        val docRef = db.collection("venues").document(name)
        docRef.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                val v = documentSnapshot.toObject<Venue>()
                if (v != null) {
                    // Handle the loaded venue
                    Log.d(MainActivity.MA, "Venue loaded: ${v.name}")
                }
            } else {
                // Create a new venue
                val newVenue = Venue(name, user.position) // Adjust the LatLng to your requirements
                docRef.set(newVenue).addOnSuccessListener {
                    Log.d(MainActivity.MA, "Venue successfully written: $name")
                }.addOnFailureListener { e ->
                    Log.e(MainActivity.MA, "Error writing venue: ", e)
                }
            }
        }.addOnFailureListener { e ->
            Log.e(MainActivity.MA, "Error loading venue: ", e)
        }
    }

    fun load_all_venues() {
        db.collection("venues")
            .get()
            .addOnSuccessListener { result ->
                venues = result.documents.mapNotNull { it.toObject<Venue>() }
                Log.d(MainActivity.MA, "All venues loaded successfully. ${venues.size}")
            }
            .addOnFailureListener { exception ->
                Log.e(MainActivity.MA, "Error getting venues: ", exception)
            }
    }

    fun rate_venue(venueName: String, rating: Int) {
        val venueRef = db.collection("venues").document(venueName)
        venueRef.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                val venue = documentSnapshot.toObject<Venue>()
                venue?.let {
                    it.rate(rating) // Call rate method on the venue
                    venueRef.set(it).addOnSuccessListener {
                        Log.d(MainActivity.MA, "Venue rating updated successfully for $venueName")
                    }.addOnFailureListener { e ->
                        Log.e(MainActivity.MA, "Error updating venue rating: ", e)
                    }
                }
            } else {
                val newVenue = Venue(venueName, LatLng(0.0, 0.0))
                newVenue.rate(rating) // Call rate method on the new venue
                venueRef.set(newVenue).addOnSuccessListener {
                    Log.d(MainActivity.MA, "Venue created and rated successfully for $venueName")
                }.addOnFailureListener { e ->
                    Log.e(MainActivity.MA, "Error creating and rating venue: ", e)
                }
            }
        }.addOnFailureListener { e ->
            Log.e(MainActivity.MA, "Error loading venue: ", e)
        }
    }

    fun get_users() {
        db.collection("users")
            .get()
            .addOnSuccessListener { result ->
//                users = result.documents.mapNotNull { it.toObject<User>() }
                users = result.documents.mapNotNull { it.toObject<User>() }

                Log.d(MainActivity.MA, "All users loaded successfully. ${users.size}")
            }
            .addOnFailureListener { exception ->
                Log.e(MainActivity.MA, "Error getting documents: ", exception)
            }
    }

    fun update_user_location(uid: String, loc: LatLng) {
        val docRef = db.collection("users").document(uid)
        docRef.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                docRef.update("location", loc).addOnSuccessListener {
                    Log.d(MainActivity.MA, "User location updated successfully for $uid")
                }.addOnFailureListener { e ->
                    Log.e(MainActivity.MA, "Error updating user location: ", e)
                }
            } else {
                val newUser = User(uid, loc)
                docRef.set(newUser).addOnSuccessListener {
                    Log.d(MainActivity.MA, "User document created successfully for $uid")
                }.addOnFailureListener { e ->
                    Log.e(MainActivity.MA, "Error creating user document: ", e)
                }
            }
        }.addOnFailureListener { e ->
            Log.e(MainActivity.MA, "Error checking user existence: ", e)
        }
    }
}
