package com.enesky.evimiss.utils

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.*

/**
 * Created by Enes Kamil YILMAZ on 19/09/2021
 */

class PermissionsUtil {

    @ExperimentalPermissionsApi
    @Composable
    fun requestPermissions(onSuccess: @Composable (() -> Unit)) {
        // Track if the user doesn't want to see the rationale any more.
        var doNotShowRationale by remember { mutableStateOf(false) }

        val calendarPermissionState = rememberMultiplePermissionsState(listOf(
            android.Manifest.permission.READ_CALENDAR,
            android.Manifest.permission.WRITE_CALENDAR
        ))
        PermissionsRequired(
            multiplePermissionsState = calendarPermissionState,
            permissionsNotGrantedContent = {
                if (doNotShowRationale) {
                    Text("Feature not available")
                } else {
                    Column {
                        Text("The calendar is important for this app. Please grant the permission.")
                        Spacer(modifier = Modifier.height(8.dp))
                        Row {
                            Button(onClick = { calendarPermissionState.launchMultiplePermissionRequest() }) {
                                Text("Ok!")
                            }
                            Spacer(Modifier.width(8.dp))
                            Button(onClick = { doNotShowRationale = true }) {
                                Text("Nope")
                            }
                        }
                    }
                }
            },
            permissionsNotAvailableContent = {
                Column {
                    Text(
                        "Calendar permission denied. See this FAQ with information about why we " +
                                "need this permission. Please, grant us access on the Settings screen."
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = { //todo
                         }) {
                        Text("Open Settings")
                    }
                }
            }
        ) {
            Text("Calendar permission Granted")
            onSuccess()
        }
    }

}