package com.example.hotspotapp
import android.Manifest
import android.content.Context
import android.location.Location
import android.location.LocationManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.firestore.PropertyName

class User {
    @PropertyName("uid")
    var uid = ""

//    @PropertyName("age")
//    var age = 0
//
//    @PropertyName("followers")
//    var followers = 0

    @PropertyName("position")
    lateinit var position : LatLng

    constructor(name : String, l : LatLng) {
        this.uid = name
//        this.age = 0
//        this.followers = 0

        var location = Location(LocationManager.GPS_PROVIDER)

        this.position = l
    }

    constructor() : this("", LatLng(0.0, 0.0))

    fun update_location(loc : Location) {
        this.position = LatLng(loc.latitude, loc.longitude)
    }



}