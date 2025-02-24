package com.ndroid.jetpackcomposepractice

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import kotlin.math.log
import kotlin.math.roundToInt


/**
 * Created by Nityen on 13-02-2025.
 */


class PhotoCompressionWorker(private val appContext: Context, private val params: WorkerParameters) : CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            val stringUri = params.inputData.getString(KEY_CONTENT_URI)
            val compressionThresholdsInBytes = params.inputData.getLong(KEY_COMPRESSION_THRESHOLD, 0)
            val uri = Uri.parse(stringUri)
            val bytes = appContext.contentResolver.openInputStream(uri)?.use { it -> it.readBytes() } ?: return@withContext Result.failure()
            val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)

            var outputByte: ByteArray
            var quality = 100
            var progress = 0

            do {
                val outputStream = ByteArrayOutputStream()
                outputStream.use { outputStream ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
                    outputByte = outputStream.toByteArray()
                    quality -= 1
                }

                // Calculate progress percentage (e.g., reducing quality)
                progress = ((100 - quality) * 100) / 95  // Quality reduces from 100 to 5
                setProgress(workDataOf("PROGRESS" to progress))
                Log.d("logd", "doWork: ${outputByte.size} $compressionThresholdsInBytes  $quality")

            } while (outputByte.size > compressionThresholdsInBytes && quality > 1)

            val file = File(appContext.cacheDir, "${params.id}.img")
            file.writeBytes(outputByte)

            Result.success(
                workDataOf(
                    KEY_RESULT_PATH to file.absolutePath
                )
            )
        }
    }

    companion object {
        const val KEY_CONTENT_URI = "KEY_CONTENT_URI"
        const val KEY_COMPRESSION_THRESHOLD = "KEY_COMPRESSION_THRESHOLD"
        const val KEY_RESULT_PATH = "KEY_RESULT_PATH"
    }
}
