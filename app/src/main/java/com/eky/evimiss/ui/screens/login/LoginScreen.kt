package com.eky.evimiss.ui.screens.login

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.eky.evimiss.R
import com.eky.evimiss.main.MAIN
import com.eky.evimiss.ui.custom.GoogleButton
import com.eky.evimiss.ui.theme.EvimissTheme
import com.eky.evimiss.utils.activity

@Composable
fun LoginScreen(navController: NavController? = null) {

    val activity = LocalContext.current.activity

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    val isFormValid by derivedStateOf { email.isNotBlank() && password.length >= 7 }
    var needEmail by remember { mutableStateOf(false) }

    EvimissTheme {
        Scaffold(backgroundColor = MaterialTheme.colors.primary) {
            Column(
                Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = "App Logo",
                    modifier = Modifier
                        .weight(1f)
                        .size(200.dp)
                )
                Card(
                    Modifier
                        .weight(2.4f)
                        .padding(8.dp),
                    shape = RoundedCornerShape(32.dp)
                ) {
                    Column(
                        Modifier
                            .fillMaxSize()
                            .padding(32.dp)
                    ) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = stringResource(R.string.label_welcome),
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold,
                            fontSize = 32.sp
                        )
                        Column(
                            Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Spacer(modifier = Modifier.weight(1f))
                            OutlinedTextField(
                                modifier = Modifier.fillMaxWidth(),
                                value = email,
                                onValueChange = { email = it },
                                label = {
                                    Text(
                                        text = if (needEmail) stringResource(R.string.label_no_email_error)
                                               else stringResource(R.string.label_email))
                                },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Email,
                                    imeAction = ImeAction.Next
                                ),
                                trailingIcon = {
                                    if (email.isNotBlank())
                                        IconButton(onClick = { email = "" }) {
                                            Icon(
                                                imageVector = Icons.Filled.Clear,
                                                contentDescription = ""
                                            )
                                        }
                                },
                                colors = TextFieldDefaults.outlinedTextFieldColors(
                                    textColor = Color.White,
                                    focusedLabelColor = Color.White,
                                    focusedBorderColor = MaterialTheme.colors.onSurface.copy(alpha = TextFieldDefaults.IconOpacity)
                                ),
                                isError = needEmail,

                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                modifier = Modifier.fillMaxWidth(),
                                value = password,
                                onValueChange = { password = it },
                                label = {
                                    Text( //Todo: add password errors
                                        text = stringResource(R.string.label_password)
                                    )
                                },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Password,
                                    imeAction = ImeAction.Done
                                ),
                                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                trailingIcon = {
                                    IconButton(onClick = {
                                        isPasswordVisible = !isPasswordVisible
                                    }) {
                                        Icon(
                                            imageVector = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                            contentDescription = "Password Toggle"
                                        )
                                    }
                                },
                                colors = TextFieldDefaults.outlinedTextFieldColors(
                                    textColor = Color.White,
                                    focusedLabelColor = Color.White,
                                    focusedBorderColor = MaterialTheme.colors.onSurface.copy(alpha = TextFieldDefaults.IconOpacity)
                                )
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = {
                                    activity?.authUtil?.createUserWithEmailAndPassword(
                                        email = email,
                                        password = password,
                                        onSuccess = {
                                            navController?.navigate(MAIN)
                                        },
                                        onFail = {
                                            Log.d("@@@createUser", "FAILED: $it")
                                        }
                                    )
                                },
                                enabled = isFormValid,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Text(text = stringResource(R.string.label_log_in_sign_up), color = Color.White)
                            }
                            Spacer(modifier = Modifier.weight(1f))
                            GoogleButton {
                                activity?.googleSignInUtil?.signInWithGoogle { credential ->
                                    activity.authUtil.signInWithCredential(
                                        credential = credential,
                                        onSuccess = {
                                            navController?.navigate(MAIN)
                                        },
                                        onFail = {
                                            Log.d("@@@signInWithCredential", "FAILED: $it")
                                        }
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.weight(1f))
                            TextButton(
                                modifier = Modifier.padding(4.dp),
                                onClick = {
                                    activity?.authUtil?.signInAnonymously(
                                        onSuccess = {
                                            navController?.navigate(MAIN)
                                        },
                                        onFail = {
                                            Log.d("@@@signInAnonymously", "FAILED: $it")
                                        }
                                    )
                                }
                            ) {
                                Text(text = stringResource(R.string.label_sign_in_anonymously), color = Color.White)
                            }
                            Spacer(modifier = Modifier.weight(1f))
                            TextButton(
                                modifier = Modifier.padding(4.dp),
                                onClick = {
                                    if (email.isEmpty()) {
                                        needEmail = true
                                        return@TextButton
                                    }
                                    needEmail = false
                                    activity?.authUtil?.forgotPassword(
                                        email = email,
                                        onSuccess = {
                                            navController?.navigate(MAIN)
                                        },
                                        onFail = {
                                            Log.d("@@@forgotPassword", "FAILED: $it")
                                        }
                                    )
                                }
                            ) {
                                Text(text = stringResource(R.string.label_forgot_password), color = Color.White)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLoginScreen() {
    LoginScreen()
}