package com.enesky.evimiss.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.enesky.evimiss.R
import com.enesky.evimiss.main.MainActivity
import com.enesky.evimiss.ui.custom.Alert
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionsRequired
import com.google.accompanist.permissions.rememberMultiplePermissionsState

/**
 * Created by Enes Kamil YILMAZ on 19/09/2021
 */

object PermissionsUtil {

    private const val readCalendar: String = android.Manifest.permission.READ_CALENDAR
    private const val writeCalendar: String = android.Manifest.permission.WRITE_CALENDAR
    private const val fineLocation: String = android.Manifest.permission.ACCESS_FINE_LOCATION
    private const val coarseLocation: String = android.Manifest.permission.ACCESS_COARSE_LOCATION

    @Composable
    fun RequestCalendarPermissions(onPermissionsGranted: @Composable () -> Unit) {
        val activity = LocalContext.current.activity
        RequestPermissions(
            permissions = listOf(readCalendar, writeCalendar),
            onPermissionsGranted = onPermissionsGranted,
            onPermissionsNotAvailable = { activity?.openSettings() }
        )
    }

    @Composable
    fun RequestLocationsPermissions(onPermissionsGranted: @Composable () -> Unit) {
        val activity = LocalContext.current.activity
        RequestPermissions(
            permissions = listOf(fineLocation, coarseLocation),
            onPermissionsGranted = onPermissionsGranted,
            onPermissionsNotAvailable = { activity?.openSettings() }
        )
    }

    @OptIn(ExperimentalPermissionsApi::class)
    @Composable
    private fun RequestPermissions(
        permissions: List<String> = listOf(),
        onPermissionsGranted: @Composable () -> Unit,
        onPermissionsNotAvailable: () -> Unit
    ) {
        val permissionsState = rememberMultiplePermissionsState(permissions)
        val permissionName = if (permissions.contains(readCalendar)) "Calendar" else "Location"

        PermissionsRequired(
            multiplePermissionsState = permissionsState,
            permissionsNotGrantedContent = {
                Alert(
                    title = stringResource(R.string.label_permission_needed, permissionName),
                    description = stringResource(R.string.label_permission_needed_desc, permissionName),
                    onConfirm = { permissionsState.launchMultiplePermissionRequest() },
                )
            },
            permissionsNotAvailableContent = {
                Alert(
                    title = stringResource(R.string.label_permission_denied, permissionName),
                    description = stringResource(R.string.label_permission_denied_desc, permissionName),
                    confirmButtonText = stringResource(R.string.label_open_settings),
                    onConfirm = { onPermissionsNotAvailable() },
                )
            }
        ) {
            onPermissionsGranted()
        }
    }

}