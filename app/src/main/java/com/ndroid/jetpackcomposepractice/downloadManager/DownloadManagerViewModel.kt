package com.ndroid.jetpackcomposepractice.downloadManager

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
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

class DownloadManagerViewModel : ViewModel() {

    private val _progress = MutableStateFlow(0)
    val progress = _progress.asStateFlow()

    private val _status = MutableStateFlow(Status.IDEAL.status)
    val status = _status.asStateFlow()


    private val client = OkHttpClient()


    //    Function to Download File
    fun downloadFile(url: String, outputFile: File) {
        _status.value = Status.DOWNLOADING.status
        _progress.value = 0

        viewModelScope.launch(Dispatchers.IO) {

            try {

                val request = Request.Builder().url(url).build()
                val resposne = client.newCall(request).execute()


                if(!resposne.isSuccessful){
                    return@launch
                }

                val body = resposne.body
                val contentLength = body?.contentLength() ?: -1
                val inputStream:InputStream = body!!.byteStream()
                val outputStream =  FileOutputStream(outputFile)

                val buffer = ByteArray(8192)
                var byteRead:Int
                var totalBytesRead = 0L

                while (inputStream.read(buffer).also { byteRead = it } != -1) {
                    outputStream.write(buffer, 0 , byteRead)
                    totalBytesRead += byteRead

                    if(contentLength > 0){
                        _progress.value = ((totalBytesRead * 100 ) / contentLength).toInt()
                    }

                }

                outputStream.flush()
                outputStream.close()
                inputStream.close()

                _status.value = Status.DOWNLOADING_COMPLETE.status

            }catch (e:Exception){
                _progress.value = 0
                _status.value = "Error: ${e.message}"
            }
        }
    }

}