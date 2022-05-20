/*

Copyright 2019 Splendo Consulting B.V. The Netherlands

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

*/

package com.splendo.kaluga.permissions.base

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.splendo.kaluga.base.ApplicationHolder
import com.splendo.kaluga.logging.debug
import com.splendo.kaluga.logging.error
import com.splendo.kaluga.logging.warn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.Timer
import java.util.concurrent.ConcurrentHashMap
import kotlin.concurrent.fixedRateTimer

internal enum class AndroidPermissionState {
    GRANTED,
    DENIED,
    DENIED_DO_NOT_ASK;

    companion object {

        /**
         * According to my understanding shouldShowRationalePermissionRationale() method
         * returns false in three cases:
         * 1. If we call this method very first time before asking permission.
         * 2. If user selects "Don't ask again" and deny permission.
         * 3. If the device policy prohibits the app from having that permission.
         *
         * That's the reason we utilize shouldShowRationalePermissionRationale()
         * only from [Activity.onRequestPermissionsResult].
         */
        fun get(activity: Activity, permission: String): AndroidPermissionState {
            val permissionState = ContextCompat.checkSelfPermission(activity, permission)
            if (permissionState == PackageManager.PERMISSION_GRANTED) {
                return GRANTED
            }

            return if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                DENIED
            } else {
                DENIED_DO_NOT_ASK
            }
        }

        fun get(context: Context, permission: String): AndroidPermissionState {
            val permissionState = ContextCompat.checkSelfPermission(context, permission)

            return if (permissionState == PackageManager.PERMISSION_GRANTED) GRANTED else DENIED
        }
    }
}

/**
 * Convenience class for requesting a [Permission]
 * @param context The context for which to request the [Permission]
 * @param permissionManager The [PermissionManager] managing the requested permission
 * @param permissions List of permissions to request. Should correspond to [Manifest.permission].
 * @param coroutineScope The coroutineScope on which to handle permission requests.
 */
class AndroidPermissionsManager<P : Permission> constructor(
    private val context: Context = ApplicationHolder.applicationContext,
    private val permissionManager: PermissionManager<P>,
    private val permissions: Array<String> = emptyArray(),
    coroutineScope: CoroutineScope = permissionManager
) : CoroutineScope by coroutineScope {

    internal companion object {
        const val TAG = "Permissions"

        val permissionsStates: MutableMap<String, AndroidPermissionState> = ConcurrentHashMap()
    }

    private val filteredPermissionsStates: Map<String, AndroidPermissionState>
        get() = permissionsStates.filterKeys { permissions.contains(it) }
    private var timer: Timer? = null

    /**
     * Starts to request the permissions.
     * Use [startMonitoring] to get notified of the permission change.
     * Calls [PermissionManager.requestPermission] if the permission cannot be granted.
     */
    fun requestPermissions() {
        if (missingPermissionsInManifest().isEmpty()) {
            launch {
                val intent = PermissionsActivity.intent(context, *permissions)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            }
        } else {
            permissionManager.revokePermission(true)
        }
    }

    private fun missingPermissionsInManifest(): List<String> {
        val pm = context.packageManager

        val missingPermissions = permissions.toMutableList()

        try {
            val packageInfo = pm.getPackageInfo(context.packageName, PackageManager.GET_PERMISSIONS)

            var declaredPermissions: List<String> = mutableListOf()

            if (packageInfo != null && declaredPermissions.isEmpty()) {
                declaredPermissions = packageInfo.requestedPermissions.toList()
            }

            if (declaredPermissions.isNotEmpty()) {
                missingPermissions.toList().forEach { requestedPermissionName ->
                    if (declaredPermissions.contains(requestedPermissionName)) {
                        debug(TAG, "Permission $requestedPermissionName was declared in manifest")
                        missingPermissions.remove(requestedPermissionName)
                    } else {
                        warn(TAG, "Permission $requestedPermissionName was not declared in manifest")
                    }
                }
            }
        } catch (e: PackageManager.NameNotFoundException) {
            error(TAG, e)
        }

        return missingPermissions
    }

    /**
     * Starts monitoring for changes to the permission.
     * Calls [PermissionManager.grantPermission] if the permission became granted
     * and [PermissionManager.revokePermission] if it became denied.
     * @param interval The interval in milliseconds between checks in changes to the permission state.
     */
    fun startMonitoring(interval: Long) {
        updatePermissionsStates()
        if (timer != null) return
        // TODO use a coroutine bases timer as in iOS
        timer = fixedRateTimer(period = interval) {
            monitor()
        }
    }

    internal fun monitor() {
        updatePermissionsStates()
        if (hasPermissions) {
            permissionManager.grantPermission()
        } else {
            val locked = filteredPermissionsStates.any {
                it.value == AndroidPermissionState.DENIED_DO_NOT_ASK
            }
            permissionManager.revokePermission(locked)
        }
    }

    /**
     * Stops monitoring for changes to the permission.
     */
    fun stopMonitoring() {
        timer?.cancel()
        timer = null
    }

    /**
     * `true` if the permission has been granted.
     */
    val hasPermissions: Boolean
        get() = filteredPermissionsStates.values.all { it == AndroidPermissionState.GRANTED }

    private fun updatePermissionsStates() {
        permissions.forEach {
            val oldPermissionState = permissionsStates[it]
            val newPermissionState = AndroidPermissionState.get(context, it)
            /**
             * Since [AndroidPermissionState.get] doesn't distinguish between
             * [AndroidPermissionState.DENIED] and [AndroidPermissionState.DENIED_DO_NOT_ASK] states,
             * we need to prevent [updatePermissionsStates] from over-writing
             * current [AndroidPermissionState.DENIED_DO_NOT_ASK] state.
             */
            if (oldPermissionState == AndroidPermissionState.DENIED_DO_NOT_ASK &&
                newPermissionState == AndroidPermissionState.DENIED
            ) {
                return
            }

            permissionsStates[it] = newPermissionState
        }
    }
}
