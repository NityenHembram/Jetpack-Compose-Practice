package com.ndroid.jetpackcomposepractice.downloadManager

import android.app.DownloadManager
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import okhttp3.OkHttp
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class DownloadManagerViewModel(private val context: Context) : ViewModel() {

    private val _progress = MutableStateFlow(0)
    val progress = _progress.asStateFlow()

    private val _status = MutableStateFlow(Status.IDEAL.status)
    val status = _status.asStateFlow()

    private var downloadId: Long = -1
    private var downloadManager: DownloadManager =
        context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager


    private val client = OkHttpClient()


    //    Function to Download File using Okhttp
    fun downloadFile(url: String, outputFile: File) {
        _status.value = Status.DOWNLOADING.status
        _progress.value = 0

        viewModelScope.launch(Dispatchers.IO) {

            try {

                val request = Request.Builder().url(url).build()
                val resposne = client.newCall(request).execute()


                if (!resposne.isSuccessful) {
                    return@launch
                }

                val body = resposne.body
                val contentLength = body?.contentLength() ?: -1
                val inputStream: InputStream = body!!.byteStream()
                val outputStream = FileOutputStream(outputFile)

                val buffer = ByteArray(8192)
                var byteRead: Int
                var totalBytesRead = 0L

                while (inputStream.read(buffer).also { byteRead = it } != -1) {
                    outputStream.write(buffer, 0, byteRead)
                    totalBytesRead += byteRead

                    if (contentLength > 0) {
                        _progress.value = ((totalBytesRead * 100) / contentLength).toInt()
                    }

                }

                outputStream.flush()
                outputStream.close()
                inputStream.close()

                _status.value = Status.DOWNLOADING_COMPLETE.status

            } catch (e: Exception) {
                _progress.value = 0
                _status.value = "Error: ${e.message}"
                Log.d("DownloadManagerViewmodel", "downloadFile: ${e.message}")
            }
        }
    }


    //    Function to Download using Download manager
    fun downloadSomething(url: String) {
        _status.value = Status.DOWNLOADING.status
        _progress.value = 0

        val request = DownloadManager.Request(Uri.parse(url))
            .setTitle("Downloading File")
            .setDescription("Please wait while the file is being downloaded.")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "sample.mp4")

        downloadId = downloadManager.enqueue(request)

        // Monitor Progress
// Monitor Progress
        viewModelScope.launch(Dispatchers.IO) {
            var downloading = true
            while (downloading) {
                val query = DownloadManager.Query().setFilterById(downloadId)
                downloadManager.query(query).use { cursor ->  // Auto-closes cursor
                    if (cursor.moveToFirst()) {
                        val bytesDownloaded =
                            cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR))
                        val totalBytes =
                            cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES))

                        if (totalBytes > 0) {
                            _progress.value = ((bytesDownloaded.toDouble() / totalBytes.toDouble()) * 100).toInt()
                            val kd = bytesDownloaded / totalBytes
                            Log.d("dmvm", "downloadSomething: ${progress.value}  $bytesDownloaded $totalBytes ${ kd }")
                        }

                        when (cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS))) {
                            DownloadManager.STATUS_SUCCESSFUL -> {
                                _progress.value = 100  // Ensure progress is set to 100%
                                _status.value = "Download Complete! File saved in Downloads"
                                downloading = false
                            }
                            DownloadManager.STATUS_FAILED -> {
                                _status.value = "Download Failed!"
                                downloading = false
                            }
                        }
                    }
                }
            }

        }
    }

}