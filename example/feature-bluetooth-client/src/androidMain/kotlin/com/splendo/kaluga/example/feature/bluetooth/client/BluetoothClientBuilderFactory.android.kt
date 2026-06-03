/*
 Copyright 2026 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.example.feature.bluetooth.client

import com.splendo.kaluga.bluetooth.BluetoothClientBuilder
import com.splendo.kaluga.permissions.base.BasePermissionManager
import com.splendo.kaluga.permissions.base.Permissions
import com.splendo.kaluga.permissions.base.PermissionsBuilder
import com.splendo.kaluga.permissions.location.registerLocationPermissionIfNotRegistered
import kotlin.coroutines.CoroutineContext

internal actual fun newBluetoothClientBuilder(permissionsBuilder: suspend (CoroutineContext) -> Permissions): BluetoothClientBuilder =
    BluetoothClientBuilder(permissionsBuilder = permissionsBuilder)

internal actual fun PermissionsBuilder.registerAdditionalPermissionIfNotRegistered(settings: BasePermissionManager.Settings) {
    registerLocationPermissionIfNotRegistered(settings = settings)
}
