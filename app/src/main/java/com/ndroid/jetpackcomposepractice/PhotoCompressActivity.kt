package com.ndroid.jetpackcomposepractice

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import coil3.compose.AsyncImage

class PhotoCompressActivity : ComponentActivity() {
    private lateinit var workManager: WorkManager
    private val viewModel by viewModels<PhotoViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        workManager = WorkManager.getInstance(applicationContext)



        setContent {
            var progress by remember { mutableIntStateOf(0) }
            val scrollState = rememberScrollState()


            val workResult = viewModel.workerId?.let { id ->
                workManager.getWorkInfoByIdLiveData(id).observeAsState().value
            }


            LaunchedEffect(key1 = workResult?.progress) {
                workResult?.progress?.let { data ->
                    progress = data.getInt("PROGRESS", 0)
                }

                if (workResult?.outputData != null) {
                    val filePath =
                        workResult.outputData.getString(PhotoCompressionWorker.KEY_RESULT_PATH)
                    filePath?.let {
                        val bitmap = BitmapFactory.decodeFile(it)
                        viewModel.updateCompressedBitmap(bitmap)
                    }
                }
            }


            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(state = scrollState),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                viewModel.uncompressUri?.let {
                    Text("Uncompress Image")
                    AsyncImage(
                        modifier = Modifier.fillMaxWidth(),
                        model = it,
                        contentDescription = null
                    )
                }

                if (progress in 1..99) {
                    Text("Compression in Progress... $progress%")
                    LinearProgressIndicator(
                        progress = { progress / 100f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                    )
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize()
                    )
                }

                viewModel.compressedBitmap?.let {
                    Text("Compressed Image")
                    Image(
                        modifier = Modifier.fillMaxWidth(),
                        bitmap = it.asImageBitmap(),
                        contentScale = ContentScale.Fit,
                        contentDescription = null
                    )
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()


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

        // Update the ViewModel with the extracted URI
        viewModel.updateUncompressedUri(uriArray)

        // Create a work request for the PhotoCompressionWorker
        val request = OneTimeWorkRequestBuilder<PhotoCompressionWorker>().setInputData(
            workDataOf(
                PhotoCompressionWorker.KEY_CONTENT_URI to uriArray.toString(),
                PhotoCompressionWorker.KEY_COMPRESSION_THRESHOLD to 1024 * 5L
            )
        ).build()

        viewModel.updateWorkId(request.id)
        // Enqueue the work request
        workManager.enqueue(request)
    }
}