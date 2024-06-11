package com.example.hotspotapp

object DatabaseHolder {
    lateinit var db: DatabaseManager
    lateinit var cm: ClusterMap

    val us = listOf(
        User("User1", LatLng(37.134, -122.074)), // Center
        User("User2", LatLng(37.144, -122.084)), // North
        User("User3", LatLng(37.124, -122.064)), // South
        User("User4", LatLng(37.134, -122.064)), // East
        User("User5", LatLng(37.134, -122.084)), // West
        User("User6", LatLng(37.144, -122.064)), // North-East
        User("User7", LatLng(37.124, -122.084)), // South-West
        User("User8", LatLng(37.144, -122.094)), // North-West
        User("User9", LatLng(37.124, -122.054)), // South-East
        User("User10", LatLng(37.154, -122.084)), // Far North
        User("User11", LatLng(37.114, -122.074)), // Far South
        User("User12", LatLng(37.134, -122.044)), // Far East
        User("User13", LatLng(37.134, -122.104)), // Far West
        User("User14", LatLng(37.154, -122.054)), // North-East
        User("User15", LatLng(37.114, -122.094)), // South-West
        User("User16", LatLng(37.154, -122.094)), // North-West
        User("User17", LatLng(37.114, -122.054)), // South-East
        User("User18", LatLng(37.164, -122.084)), // Far North
        User("User19", LatLng(37.104, -122.074)), // Far South
        User("User20", LatLng(37.134, -122.034)), // Far East
        User("User21", LatLng(37.134, -122.114)), // Far West
        User("User22", LatLng(37.144, -122.064)), // North-East
        User("User23", LatLng(37.124, -122.084)), // South-West
        User("User24", LatLng(37.144, -122.094)), // North-West
        User("User25", LatLng(37.124, -122.054)), // South-East
        User("User26", LatLng(37.154, -122.084)), // Far North
        User("User27", LatLng(37.114, -122.074)), // Far South
        User("User28", LatLng(37.134, -122.044)), // Far East
        User("User29", LatLng(37.134, -122.104)), // Far West
        User("User30", LatLng(37.154, -122.054)), // North-East
        User("User31", LatLng(37.114, -122.094)), // South-West
        User("User32", LatLng(37.154, -122.094)), // North-West
        User("User33", LatLng(37.114, -122.054)), // South-East
        User("User34", LatLng(37.164, -122.084)), // Far North
        User("User35", LatLng(37.104, -122.074)), // Far South
        User("User36", LatLng(37.134, -122.034)), // Far East
        User("User37", LatLng(37.134, -122.114)), // Far West
        User("User38", LatLng(37.144, -122.064)), // North-East
        User("User39", LatLng(37.124, -122.084)), // South-West
        User("User40", LatLng(37.144, -122.094))  // North-West
    )

    val venues = listOf(
        Venue("Venue1", LatLng(37.134, -122.074)), // Center
        Venue("Venue2", LatLng(37.234, -122.074)), // North
        Venue("Venue3", LatLng(37.034, -122.074)), // South
        Venue("Venue4", LatLng(37.134, -122.174)), // East
        Venue("Venue5", LatLng(37.134, -121.974)), // West
        Venue("Venue6", LatLng(37.234, -121.974)), // North-West
        Venue("Venue7", LatLng(37.034, -122.174))  // South-East
    )
}
