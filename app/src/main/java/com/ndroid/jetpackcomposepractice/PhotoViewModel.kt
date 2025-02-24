package com.ndroid.jetpackcomposepractice

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import java.util.UUID


/**
 * Created by Nityen on 18-02-2025.
 */


class PhotoViewModel : ViewModel() {

    var uncompressUri: Uri? by mutableStateOf(null)
        private set

    var compressedBitmap: Bitmap? by mutableStateOf(null)
        private set

    var workerId: UUID? by mutableStateOf(null)
        private set



    fun updateUncompressedUri(uri: Uri?) {
        uncompressUri = uri
    }

    fun updateCompressedBitmap(bitmap: Bitmap?) {
        compressedBitmap = bitmap
    }

    fun updateWorkId(uuid: UUID?) {
        workerId = uuid
    }

}