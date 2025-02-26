package com.ndroid.jetpackcomposepractice.navigationSystem

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ndroid.jetpackcomposepractice.HomeScreen
import com.ndroid.jetpackcomposepractice.downloadManager.DownloadManagerScreen


@Composable
fun SetupNavHost(context: Context, navHostController: NavHostController) {
    NavHost(navController = navHostController, startDestination = Screens.HomeScreen.route) {
        composable(Screens.HomeScreen.route){ HomeScreen(context, navHostController) }
        composable(Screens.DownloadManager.route) { DownloadManagerScreen() }
    }
}