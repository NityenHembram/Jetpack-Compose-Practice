package com.ndroid.jetpackcomposepractice.utility

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import com.ndroid.jetpackcomposepractice.googleMlScanner.ImageDetails
import java.text.CharacterIterator
import java.text.DecimalFormat
import java.text.MessageFormat
import java.text.SimpleDateFormat
import java.text.StringCharacterIterator
import java.util.Date
import java.util.Locale


object Utilites {
    fun uriToBitmap(context: Context, uri: Uri): Bitmap? {
        return try {
            context.contentResolver.openInputStream(uri).use { inputStream ->
                BitmapFactory.decodeStream(inputStream)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun uriToBitmapModern(context: Context, uri: Uri): Bitmap? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            try {
                val source = ImageDecoder.createSource(context.contentResolver, uri)
                ImageDecoder.decodeBitmap(source)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        } else {
            uriToBitmap(context, uri)
        }
    }

    fun saveImageToPicture(
        context: Context, bitmap: Bitmap, fileName: String = "${System.currentTimeMillis()}.jpg"
    ): Boolean {
        val contentValue = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            put(
                MediaStore.MediaColumns.RELATIVE_PATH,
                Environment.DIRECTORY_PICTURES + "/ScannerApp"
            )
        }

        val uri = context.contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValue
        )

        return uri?.let {
            context.contentResolver.openOutputStream(it)?.use { outputStream ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            }
            true
        } ?: false
    }

    fun gerCurrentDate(): String {
        val dateFormat = SimpleDateFormat("ddMMyy", Locale.getDefault())
        return dateFormat.format(Date())
    }


    fun readImages(context: Context): List<ImageDetails> {

        val imageList = mutableListOf<ImageDetails>()

        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.RELATIVE_PATH,
            MediaStore.Images.Media.SIZE,
            MediaStore.Images.Media.RESOLUTION
        )

        val selection = "${MediaStore.Images.Media.RELATIVE_PATH} = ?"
        val selectionArg = arrayOf("${Environment.DIRECTORY_PICTURES}/Screenshots/")

        Log.d("ImageLoader", "Loaded $selection  $selectionArg")

        val cursor: Cursor? = context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            selectionArg,
            "${MediaStore.Images.Media.DATE_ADDED} DESC"
        )

        cursor?.use {
            val idColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val nameColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
            val sizeColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)
            val resolutionColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.RESOLUTION)

            while (it.moveToNext()){
                val id = it.getLong(idColumn)
                val name = it.getString(nameColumn)
                val size = it.getString(sizeColumn)
                val resolution = it.getString(resolutionColumn)
                val uri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id.toString())
                imageList.add(ImageDetails(uri,name, size, resolution))
            }
        }

        Log.d("ImageLoader", "Loaded ${imageList.size} images from /Pictures/")
        return imageList
    }

    fun byteToKb(bytes: Long): String {
        var bytes = bytes
        if (-1000 < bytes && bytes < 1000) {
            return "$bytes B"
        }
        val ci: CharacterIterator = StringCharacterIterator("kMGTPE")
        while (bytes <= -999950 || bytes >= 999950) {
            bytes /= 1000
            ci.next()
        }
        return String.format("%.1f %cB", bytes / 1000.0, ci.current(),)
    }


    private val K = 1024L
    private val M = K * K
    private val G = M * K
    private val T = G * K

    fun convertToStringRepresentation(value: Long): String? {
        val dividers = longArrayOf(T, G, M, K, 1)
        val units = arrayOf("TB", "GB", "MB", "KB", "B")
        if (value < 1) throw IllegalArgumentException("Invalid file size: $value")
        var result: String? = null
        for (i in dividers.indices) {
            val divider = dividers[i]
            if (value >= divider) {
                result = format(value, divider, units[i])
                break
            }
        }
        return result;
    }

    fun format(value: Long, divider: Long, unit: String): String {
        val result = if (divider > 1) value.toDouble() / divider.toDouble() else value.toDouble()
        return DecimalFormat("#,##0.#").format(result) + " " + unit
    }
}