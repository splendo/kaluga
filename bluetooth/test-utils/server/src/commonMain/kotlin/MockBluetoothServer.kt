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

package com.splendo.kaluga.bluetooth.test.server

import com.splendo.kaluga.base.test.mock.call
import com.splendo.kaluga.base.test.mock.on
import com.splendo.kaluga.base.test.mock.parameters.mock
import com.splendo.kaluga.bluetooth.UUID
import com.splendo.kaluga.bluetooth.server.AdvertiseData
import com.splendo.kaluga.bluetooth.server.BluetoothServer
import com.splendo.kaluga.bluetooth.server.LocalService
import com.splendo.kaluga.bluetooth.server.ServerStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * A mock implementation of [BluetoothServer].
 * The [status], [isAdvertising] and [services] flows are backed by [MutableStateFlow]s the test can update directly,
 * while the methods are mocked using the Kaluga mocking library so calls can be stubbed and verified.
 * @param initialStatus the initial [ServerStatus]
 * @param setupMocks if `true` the mocks are configured with sensible default behaviour
 */
class MockBluetoothServer(initialStatus: ServerStatus = ServerStatus.AVAILABLE, setupMocks: Boolean = true) : BluetoothServer {

    /**
     * The [MutableStateFlow] backing [status]
     */
    val statusState = MutableStateFlow(initialStatus)
    override val status: StateFlow<ServerStatus> = statusState

    /**
     * The [MutableStateFlow] backing [isAdvertising]
     */
    val isAdvertisingState = MutableStateFlow(false)
    override val isAdvertising: StateFlow<Boolean> = isAdvertisingState

    /**
     * The [MutableStateFlow] backing [services]
     */
    val servicesState = MutableStateFlow<List<LocalService>>(emptyList())
    override val services: StateFlow<List<LocalService>> = servicesState

    /**
     * Mock for [advertise]
     */
    val advertiseMock = ::advertise.mock()

    /**
     * Mock for [stopAdvertising]
     */
    val stopAdvertisingMock = ::stopAdvertising.mock()

    /**
     * Mock for [add]
     */
    val addMock = ::add.mock()

    /**
     * Mock for [remove]
     */
    val removeMock = ::remove.mock()

    /**
     * Mock for [removeAllServices]
     */
    val removeAllServicesMock = ::removeAllServices.mock()

    /**
     * Mock for [close]
     */
    val closeMock = ::close.mock()

    init {
        if (setupMocks) {
            advertiseMock.on().doExecuteSuspended {
                isAdvertisingState.value = true
                true
            }
            stopAdvertisingMock.on().doExecute {
                isAdvertisingState.value = false
            }
            addMock.on().doExecuteSuspended { null }
            removeMock.on().doExecuteSuspended { true }
            removeAllServicesMock.on().doExecuteSuspended {
                servicesState.value = emptyList()
                true
            }
        }
    }

    override suspend fun advertise(data: AdvertiseData.Builder.() -> Unit): Boolean = advertiseMock.call(data)
    override fun stopAdvertising() {
        stopAdvertisingMock.call()
    }
    override suspend fun add(uuid: UUID, service: LocalService.DSL.Primary.() -> Unit): LocalService? = addMock.call(uuid, service)
    override suspend fun remove(service: LocalService): Boolean = removeMock.call(service)
    override suspend fun removeAllServices(): Boolean = removeAllServicesMock.call()
    override fun close() {
        closeMock.call()
    }
}
