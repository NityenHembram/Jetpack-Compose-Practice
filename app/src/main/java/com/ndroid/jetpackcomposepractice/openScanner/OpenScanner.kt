package com.ndroid.jetpackcomposepractice.openScanner

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions.RESULT_FORMAT_JPEG
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions.RESULT_FORMAT_PDF
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions.SCANNER_MODE_FULL
import com.google.mlkit.vision.documentscanner.GmsDocumentScanning
import com.google.mlkit.vision.documentscanner.GmsDocumentScanningResult
import com.ndroid.jetpackcomposepractice.googleMlScanner.ImageDetails
import org.opencv.android.Utils
import org.opencv.core.Mat
import org.opencv.core.MatOfPoint
import org.opencv.core.MatOfPoint2f
import org.opencv.core.Point
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin


/**
 * Created by Nityen on 17-03-2025.
 */


@Composable
fun  OpenScanner (imageDetails: ImageDetails?){
    Log.d("clicked", "OpenScanner: $imageDetails")

    val context = LocalContext.current
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var processedBitmap by remember { mutableStateOf<Bitmap?>(null) }

    var capturedBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var croppedBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var cornerPoints by remember { mutableStateOf(listOf<Point>()) }


    var scale by remember { mutableFloatStateOf(1f) }
    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }

    val imagePicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
        uri ->
        selectedImageUri = uri
        uri?.let {

            val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
            capturedBitmap = bitmap
            cornerPoints = detectDocumentEdges(bitmap) // Auto-detect document edges
//            val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
//            processedBitmap = detectAndCropDocument(bitmap)
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) {
        bitmap ->



    }

//    Surface() {
//       Column(modifier = Modifier.fillMaxSize().padding(18.dp).verticalScroll(rememberScrollState())) {
//           Button(onClick = {
////               imagePicker.launch("image/*")
//
//               cameraLauncher.launch()
//           }) {
//               Text("Pick an image")
//           }
//
//           Spacer(modifier = Modifier.height(16.dp))
//
//           selectedImageUri?.let {
//               Text("Original Image:")
//               Image(bitmap = BitmapFactory.decodeStream(context.contentResolver.openInputStream(it)).asImageBitmap(),
//                   contentDescription = "Original Image",
//                   modifier = Modifier.fillMaxSize())
//           }
//
//           processedBitmap?.let {
//               Spacer(modifier = Modifier.height(16.dp))
//               Text("Processed Image:")
//               Image(
//                   bitmap = it.asImageBitmap(),
//                   contentDescription = "Processed Image",
//                   modifier = Modifier.fillMaxWidth())
//           }
//       }
//    }



//    var imageDetails by remember { mutableStateOf<ImageDetails?>(imageDetails) }
//
//    val options = GmsDocumentScannerOptions.Builder()
//        .setGalleryImportAllowed(false)
//        .setPageLimit(2)
//        .setResultFormats(RESULT_FORMAT_JPEG, RESULT_FORMAT_PDF)
//        .setScannerMode(SCANNER_MODE_FULL)
//        .build()
//
//
//    val scanner = GmsDocumentScanning.getClient(options)
//    val scannerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) {
//        result ->
//        Log.d("resultScanner", "OpenScanner: ${result.data?.extras}")
//
//
//        if (result.resultCode == RESULT_OK) {
//            val result =
//                GmsDocumentScanningResult.fromActivityResultIntent(result.data)
//            if (result != null) {
//                result.pages?.let { pages ->
//                    for (page in pages) {
//                         imageUri = pages[0].imageUri
//                    }
//                }
//
//                result.pdf?.let { pdf ->
//                    val pdfUri = pdf.uri
//                    val pageCount = pdf.pageCount
//                }
//            }
//
//        }
//    }

    fun Offset.rotateBy(angle: Float): Offset {
        val angleInRadians = angle * (PI / 180)
        val cos = cos(angleInRadians)
        val sin = sin(angleInRadians)
        return Offset((x * cos - y * sin).toFloat(), (x * sin + y * cos).toFloat())
    }

    var offset by remember { mutableStateOf(Offset.Zero) }
    var zoom by remember { mutableFloatStateOf(1f) }
    var angle by remember { mutableFloatStateOf(0f) }


    Column(modifier = Modifier
        .fillMaxSize()
        .padding(top = 39.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {



        imageDetails?.let {
            data ->
            AsyncImage(
                model = data.uri,
                contentDescription = "cropped image",
                modifier = Modifier.fillMaxSize().pointerInput(Unit) {
                    detectTransformGestures(
                        onGesture = { centroid, pan, gestureZoom, gestureRotate ->
                            val oldScale = zoom
                            val newScale = zoom * gestureZoom

                            // For natural zooming and rotating, the centroid of the gesture should
                            // be the fixed point where zooming and rotating occurs.
                            // We compute where the centroid was (in the pre-transformed coordinate
                            // space), and then compute where it will be after this delta.
                            // We then compute what the new offset should be to keep the centroid
                            // visually stationary for rotating and zooming, and also apply the pan.
                            offset =
                                (offset + centroid / oldScale).rotateBy(gestureRotate) -
                                        (centroid / newScale + pan / oldScale)
                            zoom = newScale
                            angle += gestureRotate
                        }
                    )
                }
                    .graphicsLayer {
                        translationX = -offset.x * zoom
                        translationY = -offset.y * zoom
                        scaleX = zoom
                        scaleY = zoom
                        rotationZ = angle
                        transformOrigin = TransformOrigin(0f, 0f)
                    }
            )
        }



        croppedBitmap?.let {
            Spacer(modifier = Modifier.height(16.dp))
            Text("Cropped Document:")
            Image(bitmap = it.asImageBitmap(), contentDescription = "Cropped Image", modifier = Modifier
                .fillMaxWidth()
                .height(300.dp))
        }
    }
}

// Draggable point UI component
@Composable
fun DraggablePoint(initialOffset: Offset, onPositionChange: (Offset) -> Unit) {
    var offset by remember { mutableStateOf(initialOffset) }

    Canvas(modifier = Modifier
        .offset(offset.x.dp, offset.y.dp)

        .pointerInput(Unit) {
            detectDragGestures { change, dragAmount ->
                change.consume()
                offset = Offset(offset.x + dragAmount.x, offset.y + dragAmount.y)
                onPositionChange(offset)
            }
        }) {
        drawCircle(color = androidx.compose.ui.graphics.Color.Red, radius = 15f)
    }
}


// Function to detect edges and return four corner points
fun detectDocumentEdges(inputBitmap: Bitmap): List<Point> {
    val srcMat = Mat()
    Utils.bitmapToMat(inputBitmap, srcMat)

    val grayMat = Mat()
    Imgproc.cvtColor(srcMat, grayMat, Imgproc.COLOR_BGR2GRAY)
    Imgproc.GaussianBlur(grayMat, grayMat, Size(5.0, 5.0), 0.0)
    Imgproc.Canny(grayMat, grayMat, 75.0, 200.0)

    val contours = ArrayList<MatOfPoint>()
    val hierarchy = Mat()
    Imgproc.findContours(grayMat, contours, hierarchy, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE)

    var largestContour: MatOfPoint? = null
    var largestArea = 0.0

    for (contour in contours) {
        val approx = MatOfPoint2f()
        val contour2f = MatOfPoint2f(*contour.toArray())
        val perimeter = Imgproc.arcLength(contour2f, true)
        Imgproc.approxPolyDP(contour2f, approx, 0.02 * perimeter, true)

        if (approx.total() == 4L) {
            val area = Imgproc.contourArea(approx)
            if (area > largestArea) {
                largestArea = area
                largestContour = MatOfPoint(*approx.toArray())
            }
        }
    }

    return largestContour?.toList() ?: listOf(
        Point(100.0, 100.0),
        Point(700.0, 100.0),
        Point(700.0, 900.0),
        Point(100.0, 900.0)
    )
}

// Function to apply perspective transform
fun cropDocument(inputBitmap: Bitmap, points: List<Point>): Bitmap {
    val width = 800
    val height = 1000
    val srcMat = Mat()
    Utils.bitmapToMat(inputBitmap, srcMat)

    val destPoints = listOf(
        Point(0.0, 0.0),
        Point(width.toDouble(), 0.0),
        Point(width.toDouble(), height.toDouble()),
        Point(0.0, height.toDouble())
    )

    val perspectiveTransform = Imgproc.getPerspectiveTransform(
        MatOfPoint2f(*points.toTypedArray()),
        MatOfPoint2f(*destPoints.toTypedArray())
    )

    val outputMat = Mat()
    Imgproc.warpPerspective(srcMat, outputMat, perspectiveTransform, Size(width.toDouble(), height.toDouble()))

    val outputBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    Utils.matToBitmap(outputMat, outputBitmap)

    return outputBitmap
}




fun detectEdges(inputBitmap:Bitmap) : Bitmap{
//    Convert Bitmap to OpenCv mat
    val srcMat = Mat()
    Utils.bitmapToMat(inputBitmap, srcMat)

    val grayMat = Mat()
    Imgproc.cvtColor(srcMat, grayMat, Imgproc.COLOR_BGRA2GRAY)

//    apply Gaussian Blur (reduce noise)
    Imgproc.GaussianBlur(grayMat, grayMat, Size(5.0,5.0),0.0)

    val edgesMat = Mat()
    Imgproc.Canny(grayMat, edgesMat, 50.0, 50.0)

    val outputBitmap = Bitmap.createBitmap(edgesMat.cols(),edgesMat.rows(),Bitmap.Config.ARGB_8888)

    Utils.matToBitmap(edgesMat, outputBitmap)

    return outputBitmap
}

fun detectAndCropDocument(inputBitmap: Bitmap): Bitmap {
    // Convert Bitmap to OpenCV Mat
    val srcMat = Mat()
    Utils.bitmapToMat(inputBitmap, srcMat)

    // Convert to Grayscale
    val grayMat = Mat()
    Imgproc.cvtColor(srcMat, grayMat, Imgproc.COLOR_BGR2GRAY)

    // Apply Gaussian Blur to smooth the image
    Imgproc.GaussianBlur(grayMat, grayMat, Size(5.0, 5.0), 0.0)

    // Apply Canny Edge Detection
    val edgesMat = Mat()
    Imgproc.Canny(grayMat, edgesMat, 75.0, 200.0)

    // Find contours
    val contours = ArrayList<MatOfPoint>()
    val hierarchy = Mat()
    Imgproc.findContours(edgesMat, contours, hierarchy, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE)

    // Find the biggest quadrilateral
    var largestContour: MatOfPoint? = null
    var largestArea = 0.0
    for (contour in contours) {
        val approx = MatOfPoint2f()
        val contour2f = MatOfPoint2f(*contour.toArray())
        val perimeter = Imgproc.arcLength(contour2f, true)
        Imgproc.approxPolyDP(contour2f, approx, 0.02 * perimeter, true)

        // Look for a quadrilateral (4 points)
        if (approx.total() == 4L) {
            val area = Imgproc.contourArea(approx)
            if (area > largestArea) {
                largestArea = area
                largestContour = MatOfPoint(*approx.toArray())
            }
        }
    }

    // If no document is detected, return the original image
    if (largestContour == null) {
        return inputBitmap
    }

    // Get the 4 corner points of the document
    val points = largestContour.toList()
    val sortedPoints = sortCorners(points)

    // Define the destination points for perspective transform
    val width = 800
    val height = 1000
    val destPoints = listOf(
        Point(0.0, 0.0),
        Point(width.toDouble(), 0.0),
        Point(width.toDouble(), height.toDouble()),
        Point(0.0, height.toDouble())
    )

    // Apply Perspective Transform
    val perspectiveTransform = Imgproc.getPerspectiveTransform(MatOfPoint2f(*sortedPoints), MatOfPoint2f(*destPoints.toTypedArray()))
    val outputMat = Mat()
    Imgproc.warpPerspective(srcMat, outputMat, perspectiveTransform, Size(width.toDouble(), height.toDouble()))

    // Convert back to Bitmap
    val outputBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    Utils.matToBitmap(outputMat, outputBitmap)

    return outputBitmap
}

// Function to sort document corners in a proper order (Top-left, Top-right, Bottom-right, Bottom-left)
private fun sortCorners(points: List<Point>): Array<Point> {
    val sortedPoints = points.sortedBy { it.x + it.y }
    val topLeft = sortedPoints[0]
    val bottomRight = sortedPoints[3]

    val sortedByX = points.sortedBy { it.x }
    val topRight = sortedByX[1]
    val bottomLeft = sortedByX[2]

    return arrayOf(topLeft, topRight, bottomRight, bottomLeft)
}

