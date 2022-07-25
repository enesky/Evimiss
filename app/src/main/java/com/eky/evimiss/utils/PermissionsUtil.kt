package com.eky.evimiss.utils

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.eky.evimiss.R
import com.eky.evimiss.ui.custom.Alert
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionsRequired
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

/**
 * Created by Enes Kamil YILMAZ on 19/09/2021
 */

@ActivityScoped
class PermissionsUtil @Inject constructor(
    @ActivityContext private val context: Context
){

    private val readCalendar: String = android.Manifest.permission.READ_CALENDAR
    private val writeCalendar: String = android.Manifest.permission.WRITE_CALENDAR
    private val fineLocation: String = android.Manifest.permission.ACCESS_FINE_LOCATION
    private val coarseLocation: String = android.Manifest.permission.ACCESS_COARSE_LOCATION

    @Composable
    fun RequestCalendarPermissions(onPermissionsGranted: @Composable () -> Unit) {
        RequestPermissions(
            permissions = listOf(readCalendar, writeCalendar),
            onPermissionsGranted = onPermissionsGranted,
            onPermissionsNotAvailable = { context.openSettings() }
        )
    }

    @Composable
    fun RequestLocationsPermissions(onPermissionsGranted: @Composable () -> Unit) {
        RequestPermissions(
            permissions = listOf(fineLocation, coarseLocation),
            onPermissionsGranted = onPermissionsGranted,
            onPermissionsNotAvailable = { context.openSettings() }
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