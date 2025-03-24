package com.ndroid.jetpackcomposepractice.navigationSystem

import kotlinx.serialization.Serializable


@Serializable
sealed  class Screens(val route:String) {
     @Serializable
     data object HomeScreen:Screens(route = "Home_Screen")

     @Serializable
     data object DownloadManager:Screens(route = "Download Manager")

     @Serializable
     data object LoginScreen:Screens(route = "Login Screen")

     @Serializable
     data object OpenImageScreen:Screens(route = "OpenImage Screen")

     @Serializable
     data object GoogleMlScanner:Screens(route = "googleMlScanner Screen")
}