package com.ndroid.jetpackcomposepractice.googleMlScanner

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.icu.number.Scale
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions.RESULT_FORMAT_JPEG
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions.RESULT_FORMAT_PDF
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions.SCANNER_MODE_FULL
import com.google.mlkit.vision.documentscanner.GmsDocumentScanning
import com.google.mlkit.vision.documentscanner.GmsDocumentScanningResult
import com.ndroid.jetpackcomposepractice.utility.Utilites

@Preview(showSystemUi = true)
@Composable
fun GoogleMlScanner() {
    val context = LocalContext.current

    var imageUri by remember { mutableStateOf<Uri?>(null) }

    var scale by remember { mutableFloatStateOf(1f) }
    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }
    var imageUris by remember { mutableStateOf(Utilites.readImages(context)) }

    val options = GmsDocumentScannerOptions.Builder().setGalleryImportAllowed(false).setPageLimit(2)
        .setResultFormats(RESULT_FORMAT_JPEG, RESULT_FORMAT_PDF).setScannerMode(SCANNER_MODE_FULL)
        .build()

    val scanner = GmsDocumentScanning.getClient(options)
    val scannerLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            Log.d("resultScanner", "OpenScanner: ${result.data?.extras}")

            if (result.resultCode == RESULT_OK) {
                val result = GmsDocumentScanningResult.fromActivityResultIntent(result.data)
                if (result != null) {
                    result.pages?.let { pages ->
                        for (page in pages) {
                            imageUri = pages[0].imageUri
                        }
                        imageUri?.let {
                            Utilites.uriToBitmapModern(context,it)?.let {bitmap ->
                                Utilites.saveImageToPicture(context, bitmap)
                                imageUris = Utilites.readImages(context)
                                Log.d("ImageReload", "Image saved and list updated.")
                            }
                        }
                    }

                    result.pdf?.let { pdf ->
                        val pdfUri = pdf.uri
                        val pageCount = pdf.pageCount
                    }
                }
                Utilites.readImages(context)
            }
        }



    Scaffold(floatingActionButton = {
        Column {
            SmallFloatingActionButton(onClick = {
                scanner.getStartScanIntent(context as Activity)
                    .addOnSuccessListener { intentSender ->
                        scannerLauncher.launch(IntentSenderRequest.Builder(intentSender).build())
                    }.addOnFailureListener {

                    }
            }, modifier = Modifier.padding(4.dp)) {
                Icon(
                    Icons.Default.Add, contentDescription = "add"
                )
            }

        }
    }) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            if(imageUris.isNotEmpty()){
                LazyColumn(Modifier.fillMaxSize().padding(8.dp)) {
                    items(imageUris){ imageUri ->
                        imageItem(imageUri)
                    }
                }


            }


//            AsyncImage(model = imageUri,
//                contentDescription = "hello",
//                modifier = Modifier
//                    .pointerInput(Unit) {
//                        detectTransformGestures { _, pan, zoom, _ ->
//                            scale *= zoom
//                            offsetX += pan.x
//                            offsetY += pan.y
//                        }
//                    }
//                    .graphicsLayer(
//                        scaleX = scale.coerceIn(1f, 5f), // Prevent crazy zoom-out/in
//                        scaleY = scale.coerceIn(1f, 5f),
//                        translationX = offsetX,
//                        translationY = offsetY
//                    ))
        }
    }
}

@Composable
fun imageItem(uri: Uri){
    Row(horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(5.dp)) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(uri)
                .crossfade(true)
                .scale(coil3.size.Scale.FILL)
                .build(),
            contentDescription = "Image From Scanner",
            modifier = Modifier.padding(4.dp).height(50.dp)
        )
        Column (modifier = Modifier.padding(start = 10.dp)){
            Text("Name = Some Image", style = TextStyle(color = Color.White))
            Text("Size: 100k", style = TextStyle(color = Color.White))
            Text("resolution: 1080", style = TextStyle(color = Color.White))
        }

    }

}