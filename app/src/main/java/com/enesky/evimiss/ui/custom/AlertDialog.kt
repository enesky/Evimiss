package com.enesky.evimiss.ui.custom

import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.enesky.evimiss.ui.theme.EvimissTheme
import com.enesky.evimiss.ui.theme.Shapes

@Composable
fun Alert(
    dialogState: Boolean? = true,
    onDialogStateChange: ((Boolean) -> Unit)? = null,
    title: String? = "Sample",
    description: String? = "Lorem ipsum",
    confirmButtonText: String? = "Confirm",
    onConfirm: (() -> Unit)? = null,
    dismissButtonText: String? = "Dismiss",
    onDismiss: (() -> Unit)? = null,
) {
    if (dialogState == true)
        AlertDialog(
            onDismissRequest = {
                onDialogStateChange?.invoke(false)
                onDismiss?.invoke()
            },
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
                    onClick = { onConfirm?.invoke() }
                ) {
                    Text(text = confirmButtonText.toString(), color = Color.White)
                }
            },
            dismissButton = {
                TextButton(
                    modifier = Modifier.padding(all = 8.dp),
                    shape = Shapes.medium,
                    onClick = { onDismiss?.invoke() }
                ) {
                    Text(text = dismissButtonText.toString(), color = Color.White)
                }
            },
            properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = false),
            shape = Shapes.large
        )
}

@Composable
@Preview(showBackground = true)
fun PreviewAlert() {
    EvimissTheme {
        Alert()
    }
}