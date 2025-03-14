package com.ndroid.jetpackcomposepractice

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.ndroid.jetpackcomposepractice.PhotoCompress.PhotoCompressActivity
import com.ndroid.jetpackcomposepractice.navigationSystem.Screens


private val itemList = listOf("Phone Compressor", Screens.DownloadManager.route, Screens.LoginScreen.route)


@Composable
fun HomeScreen(context: Context, navHostController: NavHostController) {
    val scrollState = rememberScrollState()
    Column(modifier = Modifier.verticalScroll(state = scrollState)) {
        itemList.forEach { it ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)
                    .clickable {
                        if (it == "Phone Compressor") {
                            context.startActivity(
                                Intent(
                                    context,
                                    PhotoCompressActivity::class.java
                                )
                            )
                        }
                        when (it) {
                            Screens.DownloadManager.route -> navHostController.navigate(route = Screens.DownloadManager.route)
                        }
                    },
                colors = CardColors(
                    containerColor = Color(android.graphics.Color.parseColor("#3c4b37")),
                    contentColor =  Color(android.graphics.Color.parseColor("#e5deff")),
                    disabledContainerColor = Color(android.graphics.Color.parseColor("#2e322b")),
                    disabledContentColor =  Color(android.graphics.Color.parseColor("#2e322b"))
                ),
            ) {
                Text(
                    modifier = Modifier.padding(20.dp),
                    text = it,

                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}