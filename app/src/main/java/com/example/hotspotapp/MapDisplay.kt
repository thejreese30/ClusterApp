

package com.example.hotspotapp

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.LatLng as GmsLatLng

class MapDisplay : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap
    lateinit var cluster_map: ClusterMap
    var proximity = 100.0
    var gs = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.map_display)

        proximity = intent.getDoubleExtra("proximity", 100.0)
        gs = intent.getIntExtra("group_size", 1)


//        DatabaseHolder.db.get_users()
//        DatabaseHolder.db.load_all_venues()
        cluster_map = ClusterMap()
        cluster_map.proximity_load(proximity)
        if(gs <= 1 || gs > cluster_map.near_users.size) {
            cluster_map.kMeansClustering(gs)
        }
        else {
            cluster_map.kMeansClustering(cluster_map.near_users.size/gs)
        }




        mapView = findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

//        cluster_map.kMeansClustering((cluster_map.near_users.size / gs))

        val backButton: Button = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            finish()
        }
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

//        cluster_map = ClusterMap()
//        cluster_map.proximity_load(proximity)
//        cluster_map.kMeansClustering(1)
//        Toast.makeText(this, "OG list: ${DatabaseHolder.db.users.size}", Toast.LENGTH_LONG).show()
////        Toast.makeText(this, "Cluster list: ${cm.near_users.size}", Toast.LENGTH_LONG).show()
//        Toast.makeText(this, "Venue list: ${DatabaseHolder.db.venues.size}", Toast.LENGTH_LONG).show()
//        val centers = DatabaseHolder.us
        val centers = cluster_map.clusterCenters

//        val location = GmsLatLng(37.164, -122.084) // Example coordinates for Sydney
//        map.addMarker(MarkerOptions().position(location).title("Marker in Sydney"))
//        map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 10f)) // Adjust the zoom level as needed

//        for (u in DatabaseHolder.us) {
//            map.addMarker(MarkerOptions().position(GmsLatLng(u.position.lat,u.position.long)).title("${u.uid} was here!"))
//           googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(GmsLatLng(u.position.lat, u.position.long), 10f))
//        }
        for (center in centers) {
//            Log.d(MainActivity.MA, "Position: ${center.position.lat}, ${center.position.long}")
            val position = center.key
            val size = center.value
            googleMap.addCircle(
                CircleOptions()
                    .center(GmsLatLng(position.lat, position.long))
                    .radius(size*1000.0) // Adjust the multiplier as needed to get appropriate circle sizes
                    .strokeColor(Color.RED)
                    .fillColor(Color.argb(50, 0, 0, 255)) // Semi-transparent fill color
                    .strokeWidth(2f)
            )
        }

        for (venue in DatabaseHolder.venues) {
            map.addMarker(MarkerOptions().position(GmsLatLng(venue.location.lat,venue.location.long)).title("${venue.name} rated recently ${venue.max_rating}"))
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(GmsLatLng(venue.location.lat, venue.location.long), 10f))
        }


        // Optionally, move the camera to the first center
//        if (centers.isNotEmpty()) {
//            val p = centers.keys.first()
//            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(GmsLatLng(p.lat, p.long), 14f))
//        }
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }
}
