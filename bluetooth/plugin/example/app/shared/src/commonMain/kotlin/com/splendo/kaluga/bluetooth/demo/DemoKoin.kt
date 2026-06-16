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

package com.splendo.kaluga.bluetooth.demo

import com.splendo.kaluga.bluetooth.BluetoothClient
import com.splendo.kaluga.bluetooth.BluetoothClientBuilder
import com.splendo.kaluga.bluetooth.demo.ui.ClientModeViewModel
import com.splendo.kaluga.bluetooth.server.BaseBluetoothServerBuilder
import com.splendo.kaluga.bluetooth.server.BluetoothServerBuilder
import com.splendo.kaluga.permissions.base.Permissions
import com.splendo.kaluga.permissions.base.PermissionsBuilder
import com.splendo.kaluga.permissions.bluetooth.registerBluetoothPermissionIfNotRegistered
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import org.koin.mp.KoinPlatformTools
import kotlin.coroutines.CoroutineContext

// `BluetoothClientBuilder`/`BluetoothServerBuilder` are `expect class`es, so they can only be
// constructed in platform source sets.
internal expect fun newBluetoothClientBuilder(permissionsBuilder: suspend (CoroutineContext) -> Permissions): BluetoothClientBuilder
internal expect fun newBluetoothServerBuilder(permissionsBuilder: suspend (CoroutineContext) -> Permissions): BluetoothServerBuilder

private val permissions: suspend (CoroutineContext) -> Permissions = { coroutineContext ->
    val builder = PermissionsBuilder()
    builder.registerBluetoothPermissionIfNotRegistered()
    Permissions(builder, coroutineContext)
}

val demoModule: Module = module {
    single { DemoServerState() }
    single<DemoDeviceServer.Delegate> { DemoServerDelegate(get()) }
    single<BluetoothClient> { newBluetoothClientBuilder(permissions).createClient() }
    single<BaseBluetoothServerBuilder> { newBluetoothServerBuilder(permissions) }
    viewModel { ClientModeViewModel(get()) }
}

/** Starts Koin for the demo if it isn't already running. */
fun initDemoKoin() {
    if (KoinPlatformTools.defaultContext().getOrNull() == null) {
        startKoin { modules(demoModule) }
    }
}
