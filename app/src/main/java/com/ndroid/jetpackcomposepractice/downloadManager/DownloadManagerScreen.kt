package com.ndroid.jetpackcomposepractice.downloadManager

import android.os.Environment
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import java.io.File


@Composable
fun DownloadManagerScreen() {
    val viewModel: DownloadManagerViewModel = viewModel()
    val context = LocalContext.current

    val progress by viewModel.progress.collectAsState()
    val status by viewModel.status.collectAsState()



    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Download Manager")
        Spacer(modifier = Modifier.height(10.dp))

        Text("Status: $status")

//        ProgressBar
        LinearProgressIndicator(
            progress = { progress / 100f },
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text("Progress: $progress%")
        Spacer(modifier = Modifier.height(10.dp))


        Button(modifier = Modifier.fillMaxWidth(), onClick = {
            val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS),"sample.mp4")
            viewModel.downloadFile("https://www.sample-videos.com/video321/mp4/720/big_buck_bunny_720p_30mb.mp4",file)
        }, enabled = progress == 0 || progress == 100) {
            Text("DownloadFile")
        }

    }


}