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

package com.splendo.kaluga.permissions.bluetooth

import com.splendo.kaluga.permissions.base.BasePermissionManager
import com.splendo.kaluga.permissions.base.BasePermissionsBuilder
import com.splendo.kaluga.permissions.base.PermissionContext
import com.splendo.kaluga.permissions.base.PermissionManager
import com.splendo.kaluga.permissions.base.PermissionStateRepo
import com.splendo.kaluga.permissions.base.defaultPermissionContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext
import kotlin.time.Duration

/**
 * A [PermissionManager] for managing [BluetoothPermission]
 */
typealias BluetoothPermissionManager = PermissionManager<BluetoothPermission>
expect class DefaultBluetoothPermissionManager : BasePermissionManager<BluetoothPermission>

interface BaseBluetoothPermissionManagerBuilder : BasePermissionsBuilder<BluetoothPermission> {
    /**
     * Creates a [BluetoothPermissionManager]
     * @param settings [BasePermissionManager.Settings] to configure the manager
     * @param coroutineScope The [CoroutineScope] the manager runs on
     */
    fun create(settings: BasePermissionManager.Settings = BasePermissionManager.Settings(), coroutineScope: CoroutineScope): BluetoothPermissionManager
}

/**
 * A builder for creating a [BluetoothPermissionManager]
 */
expect class BluetoothPermissionManagerBuilder(context: PermissionContext = defaultPermissionContext) : BaseBluetoothPermissionManagerBuilder

/**
 * A [PermissionStateRepo] for [BluetoothPermission]
 * @param builder The [BluetoothPermissionManagerBuilder] for creating the [BluetoothPermissionManager] associated with the permission
 * @param coroutineContext The [CoroutineContext] to run the state machine on.
 */
class BluetoothPermissionStateRepo(
    builder: BaseBluetoothPermissionManagerBuilder,
    monitoringInterval: Duration = defaultMonitoringInterval,
    settings: BasePermissionManager.Settings = BasePermissionManager.Settings(),
    coroutineContext: CoroutineContext = Dispatchers.Main.immediate
) : PermissionStateRepo<BluetoothPermission>(monitoringInterval, { builder.create(settings, it) }, coroutineContext)
