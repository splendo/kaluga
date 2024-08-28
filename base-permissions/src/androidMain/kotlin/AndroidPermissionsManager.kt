/*

Copyright 2022 Splendo Consulting B.V. The Netherlands

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
import android.content.pm.PackageManager.PackageInfoFlags
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.splendo.kaluga.base.ApplicationHolder
import com.splendo.kaluga.logging.Logger
import com.splendo.kaluga.logging.RestrictedLogLevel
import com.splendo.kaluga.logging.RestrictedLogger
import com.splendo.kaluga.logging.debug
import com.splendo.kaluga.logging.error
import com.splendo.kaluga.logging.info
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.launch
import java.util.Timer
import java.util.concurrent.ConcurrentHashMap
import kotlin.concurrent.fixedRateTimer
import kotlin.time.Duration

/**
 * The state of a permission according to the Android System
 */
enum class AndroidPermissionState {

    /**
     * Permission has been granted
     */
    GRANTED,

    /**
     * Permission is denied
     */
    DENIED,

    /**
     * Permission is denied and the user has selected `Do not ask again`
     */
    DENIED_DO_NOT_ASK, ;

    companion object {

        /**
         * Gets the [AndroidPermissionState] of a [permission] for a given [Activity].
         * @param activity The [Activity] on which to check for the permission state
         * @param permission The name of the permission being checked. Should correspond to [android.Manifest.permission].
         */
        fun get(activity: Activity, permission: String): AndroidPermissionState {
            val permissionState = ContextCompat.checkSelfPermission(activity, permission)
            if (permissionState == PackageManager.PERMISSION_GRANTED) {
                return GRANTED
            }

            // This returns false if the `do not ask again` checkbox has been checked before.
            return if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                DENIED
            } else {
                DENIED_DO_NOT_ASK
            }
        }

        /**
         * Gets the [AndroidPermissionState] of a [permission] for a given [Context].
         * Does not return [DENIED_DO_NOT_ASK].
         * @param context The [Context] on which to check for the permission state
         * @param permission The name of the permission being checked. Should correspond to [android.Manifest.permission].
         */
        fun get(context: Context, permission: String): AndroidPermissionState {
            val permissionState = ContextCompat.checkSelfPermission(context, permission)

            return if (permissionState == PackageManager.PERMISSION_GRANTED) GRANTED else DENIED
        }
    }
}

/**
 * Convenience class for requesting a [Permission] and monitoring [AndroidPermissionState]
 * @param context The context for which to request the [Permission]
 * @param permissions List of permissions to request. Should correspond to [android.Manifest.permission].
 * @param coroutineScope the [CoroutineScope] to launch permission requests in.
 * @param logTag The tag used for logging
 * @param logger The [Logger] used for logging
 * @param onPermissionChanged A [AndroidPermissionStateHandler] that will be notified of changes to [AndroidPermissionState]
 */
class AndroidPermissionsManager constructor(
    private val context: Context = ApplicationHolder.applicationContext,
    private val permissions: Array<String> = emptyArray(),
    coroutineScope: CoroutineScope,
    private val logTag: String = "AndroidPermissionManager",
    private val logger: Logger = RestrictedLogger(RestrictedLogLevel.None),
    private val onPermissionChanged: AndroidPermissionStateHandler,
) : CoroutineScope by coroutineScope {

    internal companion object {
        val permissionsStates: MutableMap<String, AndroidPermissionState> = ConcurrentHashMap()
    }

    private val filteredPermissionsStates: Map<String, AndroidPermissionState>
        get() = permissionsStates.filterKeys { permissions.contains(it) }
    private var timer: Timer? = null

    /**
     * Starts to request the permissions.
     * Ensure [startMonitoring] was called to get notified of the permission change.
     * Sets the state to [AndroidPermissionState.DENIED_DO_NOT_ASK] if the permission cannot be requested.
     */
    fun requestPermissions() {
        if (missingPermissionsInManifest().isEmpty()) {
            launch {
                val intent = PermissionsActivity.intent(context, *permissions)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            }
        } else {
            onPermissionChanged.status(AndroidPermissionState.DENIED_DO_NOT_ASK)
        }
    }

    private fun missingPermissionsInManifest(): List<String> {
        val pm = context.packageManager

        val missingPermissions = permissions.toMutableList()

        try {
            val packageInfo = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                pm.getPackageInfo(context.packageName, PackageInfoFlags.of(PackageManager.GET_PERMISSIONS.toLong()))
            } else {
                @Suppress("DEPRECATION")
                pm.getPackageInfo(context.packageName, PackageManager.GET_PERMISSIONS)
            }

            val declaredPermissions: List<String> = packageInfo?.requestedPermissions?.toList().orEmpty()

            if (declaredPermissions.isNotEmpty()) {
                missingPermissions.toList().forEach { requestedPermissionName ->
                    if (declaredPermissions.contains(requestedPermissionName)) {
                        logger.debug(logTag) { "Permission $requestedPermissionName was declared in manifest" }
                        missingPermissions.remove(requestedPermissionName)
                    } else {
                        logger.error(logTag) { "Permission $requestedPermissionName was not declared in manifest" }
                    }
                }
            }
        } catch (e: PackageManager.NameNotFoundException) {
            logger.error(logTag) { e.message.orEmpty() }
        }

        return missingPermissions
    }

    /**
     * Starts monitoring for changes to the [AndroidPermissionState].
     * @param interval The [Duration] between checks in changes to the permission state.
     */
    fun startMonitoring(interval: Duration) {
        updatePermissionsStates()
        if (timer != null) return
        // TODO use a coroutine bases timer as in iOS
        timer = fixedRateTimer(period = interval.inWholeMilliseconds) {
            monitor()
        }
    }

    internal fun monitor() {
        updatePermissionsStates()
        val state = if (hasPermissions) {
            AndroidPermissionState.GRANTED
        } else {
            val locked = filteredPermissionsStates.any {
                it.value == AndroidPermissionState.DENIED_DO_NOT_ASK
            }
            if (locked) AndroidPermissionState.DENIED_DO_NOT_ASK else AndroidPermissionState.DENIED
        }
        onPermissionChanged.status(state)
    }

    /**
     * Stops monitoring for changes to the [AndroidPermissionState].
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

            // Since [AndroidPermissionState.get] doesn't distinguish between
            // [AndroidPermissionState.DENIED] and [AndroidPermissionState.DENIED_DO_NOT_ASK] states,
            // we need to prevent [updatePermissionsStates] from over-writing
            // current [AndroidPermissionState.DENIED_DO_NOT_ASK] state.
            if (oldPermissionState == AndroidPermissionState.DENIED_DO_NOT_ASK &&
                newPermissionState == AndroidPermissionState.DENIED
            ) {
                return
            }

            permissionsStates[it] = newPermissionState
        }
    }
}

interface AndroidPermissionStateHandler {
    fun status(state: AndroidPermissionState)
}

class DefaultAndroidPermissionStateHandler(private val eventChannel: SendChannel<PermissionManager.Event>, private val logTag: String, private val logger: Logger) :
    AndroidPermissionStateHandler {

    override fun status(state: AndroidPermissionState) {
        when (state) {
            AndroidPermissionState.DENIED -> {
                logger.info(logTag) { "Permission Revoked" }
                tryAndEmitEvent(PermissionManager.Event.PermissionDenied(locked = false))
            }
            AndroidPermissionState.GRANTED -> {
                logger.info(logTag) { "Permission Granted" }
                tryAndEmitEvent(PermissionManager.Event.PermissionGranted)
            }
            AndroidPermissionState.DENIED_DO_NOT_ASK -> {
                logger.info(logTag) { "Permission Locked" }
                tryAndEmitEvent(PermissionManager.Event.PermissionDenied(locked = true))
            }
        }
    }

    private fun tryAndEmitEvent(event: PermissionManager.Event) {
        eventChannel.trySend(event)
    }
}
