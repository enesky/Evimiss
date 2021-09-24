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

object PermissionsUtil {

    val readCalendar: String = android.Manifest.permission.READ_CALENDAR
    val writeCalendar: String = android.Manifest.permission.WRITE_CALENDAR
    val fineLocation: String = android.Manifest.permission.ACCESS_FINE_LOCATION
    val coarseLocation: String = android.Manifest.permission.ACCESS_COARSE_LOCATION

    @ExperimentalPermissionsApi
    @Composable
    fun requestPermissions(
        permissions: List<String> = listOf<String>(),
        onSuccess: @Composable (() -> Unit)
    ) {
        // Track if the user doesn't want to see the rationale any more.
        var doNotShowRationale by remember { mutableStateOf(false) }

        val calendarPermissionState = rememberMultiplePermissionsState(permissions)
        PermissionsRequired(
            multiplePermissionsState = calendarPermissionState,
            permissionsNotGrantedContent = {
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
            },
            permissionsNotAvailableContent = {
                Column {
                    Text(
                        "Calendar permission denied. See this FAQ with information about why we " +
                                "need this permission. Please, grant us access on the Settings screen."
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = { }) {
                        Text("Open Settings")
                    }
                }
            }
        ) {
            //Permission Granted
            onSuccess()
        }
    }

}