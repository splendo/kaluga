/*
 Copyright (c) 2020. Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.bluetooth.scanner

import com.splendo.kaluga.permissions.BasePermissions
import com.splendo.kaluga.permissions.PermissionManager
import com.splendo.kaluga.permissions.Permit
import com.splendo.kaluga.permissions.Support
import com.splendo.kaluga.utils.EmptyCompletableDeferred
import com.splendo.kaluga.utils.complete

class MockBasePermissions(private val permissionsManager: MockPermissionManager) : BasePermissions() {

    override fun getBluetoothManager(): PermissionManager {
        return permissionsManager
    }
}

class MockPermissionManager : PermissionManager {

    val openSettingsCompleted = EmptyCompletableDeferred()
    val requestPermissionsCompleted = EmptyCompletableDeferred()

    var support = Support.POWER_ON
    var permit = Permit.ALLOWED

    override fun checkSupport(): Support {
        return support
    }

    override fun checkPermit(): Permit {
        return permit
    }

    override fun openSettings() {
        openSettingsCompleted.complete()
    }

    override fun requestPermissions() {
        requestPermissionsCompleted.complete()
    }
}

