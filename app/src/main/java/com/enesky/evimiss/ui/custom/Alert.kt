package com.enesky.evimiss.ui.custom

import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.enesky.evimiss.R
import com.enesky.evimiss.ui.theme.EvimissTheme
import com.enesky.evimiss.ui.theme.Shapes

@Composable
fun Alert(
    title: String? = "Sample",
    description: String? = "Lorem ipsum",
    confirmButtonText: String? = stringResource(R.string.label_confirm),
    onConfirm: (() -> Unit)? = null,
    dismissButtonText: String? = stringResource(R.string.label_dismiss),
    onDismiss: (() -> Unit)? = null,
) {
    var dialogState by remember { mutableStateOf(true) }

    EvimissTheme {
        if (dialogState)
            AlertDialog(
                onDismissRequest = { },
                title = {
                    Text(text = title.toString())
                },
                text = {
                    Text(text = description.toString())
                },
                confirmButton = {
                    Button(
                        modifier = Modifier.padding(all = 8.dp),
                        shape = Shapes.medium,
                        onClick = {
                            onConfirm?.invoke()
                            dialogState = !dialogState
                        }
                    ) {
                        Text(text = confirmButtonText.toString(), color = Color.White)
                    }
                },
                dismissButton = {
                    TextButton(
                        modifier = Modifier.padding(all = 8.dp),
                        shape = Shapes.medium,
                        onClick = {
                            onDismiss?.invoke()
                            dialogState = !dialogState
                        }
                    ) {
                        Text(text = dismissButtonText.toString(), color = Color.White)
                    }
                },
                properties = DialogProperties(
                    dismissOnBackPress = true,
                    dismissOnClickOutside = false
                ),
                shape = Shapes.large
            )
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewAlert() {
    Alert()
}