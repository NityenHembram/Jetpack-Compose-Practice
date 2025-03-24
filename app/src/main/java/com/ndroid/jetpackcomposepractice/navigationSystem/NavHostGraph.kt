package com.ndroid.jetpackcomposepractice.navigationSystem

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ndroid.jetpackcomposepractice.HomeScreen
import com.ndroid.jetpackcomposepractice.downloadManager.DownloadManagerScreen
import com.ndroid.jetpackcomposepractice.googleMlScanner.GoogleMlScanner
import com.ndroid.jetpackcomposepractice.googleMlScanner.ImageDetails
import com.ndroid.jetpackcomposepractice.openScanner.OpenScanner


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun SetupNavHost(context: Context, navHostController: NavHostController) {
    NavHost(navController = navHostController, startDestination = Screens.GoogleMlScanner.route) {
        composable(Screens.HomeScreen.route){ HomeScreen(context, navHostController) }
        composable(Screens.DownloadManager.route) { DownloadManagerScreen() }
        composable(Screens.GoogleMlScanner.route){ GoogleMlScanner(context,navHostController) }
        composable(Screens.OpenImageScreen.route,
            enterTransition = { slideInHorizontally(initialOffsetX = { 1000 }) },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -1000 }) }
            ){ navBackStackEntry ->  OpenScanner(navHostController.previousBackStackEntry?.savedStateHandle?.get("imageDetails")) }
    }
}