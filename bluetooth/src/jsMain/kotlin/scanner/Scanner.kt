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

import com.splendo.kaluga.bluetooth.UUID
import com.splendo.kaluga.permissions.Permissions
import com.splendo.kaluga.state.StateRepoAccesor
import kotlinx.coroutines.CoroutineScope

actual class Scanner(permissions: Permissions, stateRepoAccessor: StateRepoAccesor<ScanningState>, coroutineScope: CoroutineScope) : BaseScanner(permissions, stateRepoAccessor, coroutineScope) {

    class Builder(override val autoEnableBluetooth: Boolean) : BaseScanner.Builder {
        override fun create(stateRepoAccessor: StateRepoAccesor<ScanningState>, coroutineScope: CoroutineScope): Scanner {
            return Scanner(Permissions(), stateRepoAccessor, coroutineScope)
        }
    }

    override fun scanForDevices(filter: Set<UUID>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun stopScanning() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun startMonitoringBluetooth() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun stopMonitoringBluetooth() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}