package com.ndroid.jetpackcomposepractice

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import coil3.compose.AsyncImage
import com.ndroid.jetpackcomposepractice.PhotoCompress.PhotoCompressActivity
import com.ndroid.jetpackcomposepractice.PhotoCompress.PhotoCompressionWorker
import com.ndroid.jetpackcomposepractice.PhotoCompress.PhotoViewModel
import com.ndroid.jetpackcomposepractice.googleMlScanner.GoogleMlScanner
import com.ndroid.jetpackcomposepractice.loginScreen.LoginScreen
import com.ndroid.jetpackcomposepractice.navigationSystem.Screens
import com.ndroid.jetpackcomposepractice.navigationSystem.SetupNavHost
import com.ndroid.jetpackcomposepractice.openScanner.OpenScanner
import com.ndroid.jetpackcomposepractice.ui.theme.JetpackComposePracticeTheme
import org.opencv.android.OpenCVLoader

class MainActivity : ComponentActivity() {

    private val itemList = listOf("Phone Compressor", "Download manger", "new")

    private  lateinit var navHostController:NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val TAG = "OPENSCANNER"
        if(OpenCVLoader.initLocal()){
            Log.d(TAG, "OpenScanner: OpenCv Loaded Successfully")
        }else{
            Toast.makeText(this, "OpenCv Initialazation failed", Toast.LENGTH_LONG).show()
            Log.d(TAG, "OpenScanner: OpenCv Loaded failed")
        }

        askCameraPermission()

        setContent {
            navHostController = rememberNavController()
            JetpackComposePracticeTheme {
//                Scaffold(modifier = Modifier.windowInsetsPadding(WindowInsets.safeDrawing)) { innerpadding ->
                    SetupNavHost(this,navHostController = navHostController)
//                }
//                LoginScreen()
//                OpenScanner()
//                GoogleMlScanner()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun askCameraPermission(){
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(this,Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED ){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA,Manifest.permission.READ_MEDIA_IMAGES),0)
        }

    }
}








@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    JetpackComposePracticeTheme {
        Greeting("Android")
    }
}
