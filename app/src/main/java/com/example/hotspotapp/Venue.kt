package com.example.hotspotapp

import android.location.Location
import com.google.firebase.firestore.PropertyName

class Venue { //focus on just rating for the venue
    @PropertyName("location")
    lateinit var location : LatLng

    @PropertyName("name")
    var name = ""

    @PropertyName("max_rating")
    var max_rating = 0 // not actually a max rating, actually latest rating

    constructor(name : String, location : LatLng) {
        this.location = location
        this.name = name
    }

    constructor () {
        this.name = ""
        this.max_rating = 0
    }

    fun rate(input : Int) {
        this.max_rating = input
    }
}