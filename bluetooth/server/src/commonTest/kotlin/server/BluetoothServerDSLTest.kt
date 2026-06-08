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

package com.splendo.kaluga.bluetooth.server

import com.splendo.kaluga.permissions.base.Permissions
import com.splendo.kaluga.base.test.BaseTest
import com.splendo.kaluga.bluetooth.test.randomUUID
import com.splendo.kaluga.permissions.test.MockPermissionsBuilder
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.test.Test
import kotlin.test.assertFailsWith

class BluetoothServerDSLTest : BaseTest() {

    private fun createDSL(): DefaultBluetoothServer.DSL = DefaultBluetoothServer.DSL(
        ServerSettings(Permissions(MockPermissionsBuilder())),
        ServerState.NotSupported,
        EmptyCoroutineContext,
    )

    @Test
    fun testAdvertiseCanOnlyBeSetOnce() {
        val dsl = createDSL()
        dsl.advertise { }
        assertFailsWith<IllegalArgumentException> {
            dsl.advertise { }
        }
    }

    @Test
    fun testServiceWithDuplicateUUIDThrows() {
        val dsl = createDSL()
        val uuid = randomUUID()
        dsl.service(uuid) { }
        assertFailsWith<IllegalArgumentException> {
            dsl.service(uuid) { }
        }
    }

    @Test
    fun testDifferentServiceUUIDsAreAllowed() {
        val dsl = createDSL()
        dsl.service(randomUUID()) { }
        dsl.service(randomUUID()) { }
    }
}
