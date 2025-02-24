package com.ndroid.jetpackcomposepractice

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.AlignmentLine
import androidx.compose.ui.tooling.preview.Preview
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import coil3.compose.AsyncImage
import com.ndroid.jetpackcomposepractice.ui.theme.JetpackComposePracticeTheme

class MainActivity : ComponentActivity() {

    private lateinit var workManager: WorkManager
    private val viewModel by viewModels <PhotoViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        workManager = WorkManager.getInstance(applicationContext)
        setContent {
            JetpackComposePracticeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                   val workResult = viewModel.workerId?.let { id ->
                       workManager.getWorkInfoByIdLiveData(id).observeAsState().value
                   }
                    LaunchedEffect(key1 = workResult?.outputData) {
                        if(workResult?.outputData != null){
                            val filePath = workResult.outputData.getString(PhotoCompressionWorker.KEY_RESULT_PATH)
                            filePath?.let {
                                val bitmap = BitmapFactory.decodeFile(it)
                                viewModel.updateCompressedBitmap(bitmap)
                            }
                        }
                    }

                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        viewModel.uncompressUri?.let {
                            Text("Uncompress Image")
                            AsyncImage(model = it, contentDescription =  null)
                        }

                        viewModel.compressedBitmap?.let {
                            Text("Compressed Image")
                            Image(bitmap = it.asImageBitmap(), contentDescription =  null)
                        }
                    }
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        Log.d("photocompress", "uriArray: hey")
        super.onNewIntent(intent)

        // Extract the URI array from the intent
        Log.d("photocompress", "uriArray: hey")
        val uriArray = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(Intent.EXTRA_STREAM, Uri::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(Intent.EXTRA_STREAM)
        }

        // Check if the array is not empty and get the first URI
//        val uri = uriArray?.firstOrNull() as? Uri ?: return
        Log.d("photocompress", "uriArray: $uriArray")
        Log.d("photocompress", "onNewIntent: $uriArray")
//
        // Update the ViewModel with the extracted URI
        viewModel.updateUncompressedUri(uriArray)

        // Create a work request for the PhotoCompressionWorker
        val request = OneTimeWorkRequestBuilder<PhotoCompressionWorker>().setInputData(
            workDataOf(
                PhotoCompressionWorker.KEY_CONTENT_URI to uriArray.toString(),
                PhotoCompressionWorker.KEY_COMPRESSION_THRESHOLD to 1024 * 20L
            )
        ).build()

        viewModel.updateWorkId(request.id)
        // Enqueue the work request
        workManager.enqueue(request)
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