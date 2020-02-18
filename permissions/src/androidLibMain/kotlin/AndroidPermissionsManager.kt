package com.splendo.kaluga.permissions

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

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.splendo.kaluga.base.ApplicationHolder
import com.splendo.kaluga.log.debug
import com.splendo.kaluga.log.error
import com.splendo.kaluga.log.warn
import java.util.*
import kotlin.concurrent.fixedRateTimer

class AndroidPermissionsManager<P : Permission> constructor(private val context: Context = ApplicationHolder.applicationContext, private val permissionManager: PermissionManager<P>, private val permissions: Array<String> = emptyArray()) {

    companion object {
        const val TAG = "Permissions"
    }

    private var timer: Timer? = null

    fun requestPermissions() {
        if (checkPermissionsDeclaration().isEmpty()) {
            val intent = PermissionsActivity.intent(context, *permissions)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        } else {
            permissionManager.revokePermission(true)
        }
    }

    private fun checkPermissionsDeclaration(): List<String> {
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

    fun startMonitoring(interval: Long) {
        if (timer == null) return
        timer = fixedRateTimer(period = interval) {
            if (hasPermissions) {
                permissionManager.grantPermission()
            } else {
                permissionManager.revokePermission(false)
            }
        }
    }

    fun stopMonitoring() {
        timer?.cancel()
        timer = null
    }

    internal val hasPermissions: Boolean get() {
        return permissions.fold(true) { previous, permission ->
            when (ContextCompat.checkSelfPermission(context, permission)) {
                PackageManager.PERMISSION_DENIED -> false
                PackageManager.PERMISSION_GRANTED -> true
                else -> false
            } && previous
        }
    }

}