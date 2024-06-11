package com.example.hotspotapp

import com.google.firebase.firestore.PropertyName

class LatLng {
    @PropertyName("lat")
    var lat : Double = 0.0

    @PropertyName("long")
    var long : Double  = 0.0

    constructor(la : Double, lo : Double) {
        this.lat = la
        this.long = lo
    }

    constructor() : this(0.0, 0.0)
}