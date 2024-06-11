package com.example.hotspotapp

import android.util.Log
import kotlin.math.*

class ClusterMap { // Correctly gather list of users in proximity, run k-means, track centers, size of centers?, rating of centers, google maps functionality here too

    var near_users: ArrayList<User> = arrayListOf()
    var clusterCenters: HashMap<LatLng, Int> = hashMapOf()
    lateinit var venues: ArrayList<Venue>

    fun haversine(c1: LatLng, c2: LatLng): Double {
        val radius = 3959.0 // Radius of the Earth in miles
        val phi1 = c1.lat * (Math.PI / 180)
        val phi2 = c2.lat * (Math.PI / 180)
        val deltaPhi = (c2.lat - c1.lat) * (Math.PI / 180)
        val deltaLambda = (c2.long - c1.long) * (Math.PI / 180)
        val a = sin(deltaPhi / 2) * sin(deltaPhi / 2) + cos(phi1) * cos(phi2) * sin(deltaLambda / 2) * sin(deltaLambda / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return radius * c
    }

    fun proximity_load(dist: Double) {
        for (u in DatabaseHolder.us) {
            if (haversine(DatabaseHolder.db.user.position, u.position) <= dist) {
                near_users.add(u)
            }
        }
        Log.d(MainActivity.MA, "SIZE OF NEAR USERS: ${near_users.size}")
    }

    fun kMeansClustering(n: Int) {

        var k = n
        if (n >= near_users.size || n <= 0) {
            k = near_users.size
        }

        // Initialize centroids randomly
        val centroids = near_users.shuffled().take(k).map { it.position }.toMutableList()

        var clusters: MutableList<MutableList<LatLng>> = MutableList(k) { mutableListOf() }
        var newCentroids = centroids.toList()
        var iteration = 0
        val maxIterations = 20

//        if (k == near_users.size) {
//            clusterCenters.clear()
//            newCentroids.forEachIndexed { index, centroid ->
//                clusterCenters[centroid] = clusters[index].size
//                return
//            }
        while (iteration < maxIterations) {
            clusters = MutableList(k) { mutableListOf() }

            // Assign users to the nearest centroid
            for (user in near_users) {
                val closestCentroidIndex = newCentroids.indices.minByOrNull { i ->
                    haversine(
                        user.position,
                        newCentroids[i]
                    )
                } ?: 0
                clusters[closestCentroidIndex].add(user.position)
            }

            // Update centroids
            val updatedCentroids = newCentroids.mapIndexed { index, _ ->
                val cluster = clusters[index]
                if (cluster.isNotEmpty()) {
                    val avgLat = cluster.map { it.lat }.average()
                    val avgLon = cluster.map { it.long }.average()
                    LatLng(avgLat, avgLon)
                } else {
                    newCentroids[index]
                }
            }

            // Check for convergence
            if (updatedCentroids == newCentroids) break

            newCentroids = updatedCentroids
            iteration++
        }

        // Update the clusterCenters map
        clusterCenters.clear()
        newCentroids.forEachIndexed { index, centroid ->
            clusterCenters[centroid] = clusters[index].size
        }
    }
}

