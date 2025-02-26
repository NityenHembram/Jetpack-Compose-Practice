package com.ndroid.jetpackcomposepractice.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.ndroid.jetpackcomposepractice.PhotoCompressActivity
import com.ndroid.jetpackcomposepractice.PhotoCompressionWorker
import com.ndroid.jetpackcomposepractice.PhotoViewModel
import com.ndroid.jetpackcomposepractice.ui.theme.JetpackComposePracticeTheme

class MainActivity : ComponentActivity() {


    private val itemList = listOf("Phone Compressor", "Download manger", "new")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val scrollState = rememberScrollState()
            JetpackComposePracticeTheme {
                Scaffold(modifier = Modifier.windowInsetsPadding(WindowInsets.safeDrawing)) { innerpadding ->
                    Column(modifier = Modifier.verticalScroll(state = scrollState)) {
                        itemList.forEach { it ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 5.dp)
                                    .clickable {
                                        if(it == "Phone Compressor"){
                                            startActivity(Intent(this@MainActivity,PhotoCompressActivity::class.java))
                                        }
                                    }) {
                                Text(modifier = Modifier.padding(20.dp), text = it)
                            }
                        }
                    }
                    var progress by remember { mutableIntStateOf(0) }
                }
            }
        }
    }
}
