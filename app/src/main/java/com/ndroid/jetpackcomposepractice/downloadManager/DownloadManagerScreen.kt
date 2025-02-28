package com.ndroid.jetpackcomposepractice.downloadManager

import android.os.Environment
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ndroid.jetpackcomposepractice.viewModelUtils.getViewModel
import java.io.File
import kotlin.contracts.contract


@Composable
fun DownloadManagerScreen() {
    val context = LocalContext.current
    val viewModel = getViewModel{ DownloadManagerViewModel(context)}


    val progress by viewModel.progress.collectAsState()
    val status by viewModel.status.collectAsState()

    val permissionManager =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission(),
            onResult = { isGranted ->

            })



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

        val url = "https://testfile.org/1.3GBiconpng"
        Button(modifier = Modifier.fillMaxWidth(), onClick = {
            val file =
                File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "sample.mp4")
            viewModel.downloadFile(
                url,
                file
            )
//            viewModel.downloadSomething(url)
        }, enabled = progress == 0 || progress == 100) {
            Text("Normal DownloadFile")
        }

        Button(modifier = Modifier.fillMaxWidth(), onClick = {
//            val file =
//                File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "sample.mp4")
//            viewModel.downloadFile(
//                url,
//                file
//            )
            viewModel.downloadSomething(url)
        }, enabled = progress == 0 || progress == 100) {
            Text("Download Manager DownloadFile")
        }

    }


}