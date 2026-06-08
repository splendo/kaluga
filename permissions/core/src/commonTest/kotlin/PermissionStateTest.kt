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

package com.splendo.kaluga.permissions.base

import com.splendo.kaluga.base.flow.filterOnlyImportant
import com.splendo.kaluga.permissions.base.PermissionState.Allowed
import com.splendo.kaluga.permissions.base.PermissionState.Denied.Locked
import com.splendo.kaluga.permissions.base.PermissionState.Denied.Requestable
import com.splendo.kaluga.test.base.BaseFlowTest
import com.splendo.kaluga.test.base.mock.matcher.ParameterMatcher.Companion.eq
import com.splendo.kaluga.test.base.mock.on
import com.splendo.kaluga.test.base.mock.verify
import com.splendo.kaluga.test.base.yieldMultiple
import com.splendo.kaluga.test.permissions.DummyPermission
import com.splendo.kaluga.test.permissions.MockPermissionManager
import com.splendo.kaluga.test.permissions.MockPermissionStateRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlin.test.Test
import kotlin.test.assertIs

class PermissionStateTest :
    BaseFlowTest<
        MockPermissionManager.Builder<DummyPermission>,
        PermissionStateTest.Context,
        PermissionState<DummyPermission>,
        PermissionStateRepo<DummyPermission>,
        >() {

    class Context(val builder: MockPermissionManager.Builder<DummyPermission>, coroutineScope: CoroutineScope) : TestContext {
        val permissionStateRepo = MockPermissionStateRepo(builder = builder, coroutineContext = coroutineScope.coroutineContext)
        val permissionManager get() = builder.createdManagers.first()
    }

    override val filter: (Flow<PermissionState<DummyPermission>>) -> (Flow<PermissionState<DummyPermission>>) = {
        it.filterOnlyImportant()
    }

    override val createTestContextWithConfiguration: suspend (
        configuration: MockPermissionManager.Builder<DummyPermission>,
        scope: CoroutineScope,
    ) -> Context = { configuration, scope -> Context(configuration, scope) }
    override val flowFromTestContext: suspend Context.() -> MockPermissionStateRepo<DummyPermission> = { permissionStateRepo }

    @Test
    fun testInitialState() = testWithFlowAndTestContext(
        MockPermissionManager.Builder(DummyPermission),
    ) {
        test {
            permissionManager.monitoringDidStartMock.verify(eq(permissionManager.monitoringInterval))
            assertIs<Requestable<DummyPermission>>(it)
        }
        action {
            resetFlow()
        }
        mainAction {
            yieldMultiple(10)
            permissionManager.monitoringDidStopMock.verify()
        }
    }

    @Test
    fun testRequestPermission() = testWithFlowAndTestContext(
        MockPermissionManager.Builder(DummyPermission),
    ) {
        test {
            permissionManager.monitoringDidStartMock.verify(eq(permissionManager.monitoringInterval))
            assertIs<Requestable<DummyPermission>>(it)
        }
        mainAction {
            permissionManager.requestPermissionDidStartMock.on().doExecute { permissionManager.grantPermission() }
            permissionStateRepo.request()
        }
        test {
            assertIs<Allowed<DummyPermission>>(it)
        }
        action {
            resetFlow()
        }
        mainAction {
            yieldMultiple(10)
            permissionManager.monitoringDidStopMock.verify()
        }
    }

    @Test
    fun testRequestPermissionDenied() = testWithFlowAndTestContext(
        MockPermissionManager.Builder(DummyPermission),
    ) {
        test {
            assertIs<Requestable<DummyPermission>>(it)
        }
        mainAction {
            permissionManager.requestPermissionDidStartMock.on().doExecute { permissionManager.revokePermission(true) }
            permissionStateRepo.request()
        }
        test {
            assertIs<Locked<DummyPermission>>(it)
        }
        action {
            resetFlow()
        }
        mainAction {
            yieldMultiple(10)
            permissionManager.monitoringDidStopMock.verify()
        }
    }

    @Test
    fun testRevokePermission() = testWithFlowAndTestContext(
        MockPermissionManager.Builder(DummyPermission),
    ) {
        test {
            permissionManager.monitoringDidStartMock.verify(eq(permissionManager.monitoringInterval))
            assertIs<Requestable<DummyPermission>>(it)
        }
        mainAction {
            permissionManager.grantPermission()
        }
        test {
            assertIs<Allowed<DummyPermission>>(it)
        }
        mainAction {
            permissionManager.revokePermission(true)
        }
        test {
            assertIs<Locked<DummyPermission>>(it)
        }
        action {
            resetFlow()
        }
        mainAction {
            yieldMultiple(10)
            permissionManager.monitoringDidStopMock.verify()
        }
    }
}
