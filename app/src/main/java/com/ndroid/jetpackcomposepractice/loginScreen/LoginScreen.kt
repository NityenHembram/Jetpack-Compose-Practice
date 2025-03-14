package com.ndroid.jetpackcomposepractice.loginScreen

import android.graphics.BitmapFactory
import android.graphics.drawable.Icon
import android.graphics.drawable.shapes.RoundRectShape
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ndroid.jetpackcomposepractice.R


/**
 * Created by Nityen on 12-03-2025.
 */

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showSystemUi = true)
@Composable
fun LoginScreen() {
    var emailText by remember { mutableStateOf("") }
    var passwordText by remember { mutableStateOf("") }
    var isChecked by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }
    val context = LocalContext.current
    var showError by remember { mutableStateOf(false) }
    var showEmailError by remember { mutableStateOf(false) }
    var showPasswordError by remember { mutableStateOf(false) }
    var emailFocusRequester = remember { FocusRequester() }
    var passwordFocusRequester = remember { FocusRequester() }


    Box(contentAlignment = Alignment.Center) {
        Image(
            painter = painterResource(R.drawable.newloginbg),
            contentDescription = "Large Image",
            modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            elevation = CardDefaults.cardElevation(0.dp), // No shadow
            colors = CardDefaults.cardColors(
                containerColor = Color.Gray.copy(alpha = 0.3f)
            )
        ) {

            Column() {
                Text(
                    text = "Login",
                    modifier = Modifier
                        .padding(top = 39.dp)
                        .fillMaxWidth(),
                    style = TextStyle(
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    ),
                    textAlign = TextAlign.Center,
                )


//                Text( text = "Email", modifier = Modifier.padding(start = 15.dp), style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight(40)),)

                OutlinedTextField(
                    value = emailText,
                    onValueChange = { emailText = it
                                    showEmailError = false},
                    label = { Text("Enter Email", color = Color.White) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp, top = 20.dp, end = 10.dp, bottom = 0.dp)
                        .focusRequester(emailFocusRequester),
                    shape = RoundedCornerShape(20),
                    colors = OutlinedTextFieldDefaults.colors(
                        cursorColor = Color.White,
                        unfocusedContainerColor = Color.Black.copy(0.5f),
                        unfocusedBorderColor = Color.Black,
                        focusedTextColor = if(showEmailError) Color.Red  else Color.White,
                        focusedContainerColor =  Color.Black.copy(alpha = 0.5f),
                        focusedBorderColor = if(showEmailError) Color.Red  else Color.Black,

                    ),

                )



                OutlinedTextField(
                    value = passwordText,
                    onValueChange = { passwordText = it
                                    showPasswordError = false},
                    label = { Text("Enter Password", color = Color.White) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp).focusRequester(passwordFocusRequester),
                    shape = RoundedCornerShape(20),
                    colors = OutlinedTextFieldDefaults.colors(
                        cursorColor = Color.White,
                        unfocusedContainerColor = Color.Black.copy(0.5f),
                        unfocusedBorderColor = Color.Black,
                        focusedTextColor = if(showPasswordError) Color.Red else Color.White,
                        focusedContainerColor =  Color.Black.copy(alpha = 0.5f),
                        focusedBorderColor = if(showPasswordError) Color.Red else Color.Black,
                    ),
                    visualTransformation = PasswordVisualTransformation()

                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = isChecked,
                        onCheckedChange = {
                            isChecked = it
                            showError = false
                        },
                        colors = CheckboxDefaults.colors(
                            checkedColor = Color.White,
                            uncheckedColor = Color.Red.takeIf { showError } ?: Color.White,
                        ),
                    )
                    Text("Agree to the Term & Conditions", color = Color.White)
                }

                if (showError) {
                    Text("You must agree to continue", color = Color.Red, modifier = Modifier.padding(start = 14.dp, bottom = 30.dp))
                }

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp, end = 10.dp, bottom = 50.dp),
                    shape = RoundedCornerShape(20),
                    colors = ButtonColors(
                        containerColor = Color.Black.copy(0.5f),
                        contentColor = Color.White,
                        disabledContainerColor = Color.Black,
                        disabledContentColor = Color.Gray,
                    ),
                    onClick = {

                        if(emailText.isEmpty() || emailText.isBlank()){
                            showEmailError = true
                            emailFocusRequester.requestFocus()
                            return@Button
                        }else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(emailText).matches()){
                            showEmailError = true
                            emailFocusRequester.requestFocus()
                            return@Button
                        }
                        if(passwordText.isBlank() || passwordText.isEmpty()){
                            showPasswordError = true
                            passwordFocusRequester.requestFocus()
                            return@Button
                        }else if(passwordText.length < 6){
                            showPasswordError = true
                            passwordFocusRequester.requestFocus()
                            return@Button
                        }
                        if (!isChecked) {
                            showError = true
                            return@Button
                        }

                        Toast.makeText(
                            context,
                            "email:$emailText \n password:$passwordText ",
                            Toast.LENGTH_LONG
                        ).show()
                    },
                ) {
                    Text("Log in", modifier = Modifier.padding(7.dp))
                }


            }
        }
    }

}