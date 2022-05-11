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

package com.splendo.kaluga.permissions

import com.splendo.kaluga.base.flow.filterOnlyImportant
import com.splendo.kaluga.permissions.PermissionState.Allowed
import com.splendo.kaluga.permissions.PermissionState.Denied.Locked
import com.splendo.kaluga.permissions.PermissionState.Denied.Requestable
import com.splendo.kaluga.test.BaseFlowTest
import com.splendo.kaluga.test.mock.matcher.ParameterMatcher.Companion.eq
import com.splendo.kaluga.test.mock.on
import com.splendo.kaluga.test.mock.verification.VerificationRule.Companion.never
import com.splendo.kaluga.test.mock.verify
import com.splendo.kaluga.test.permissions.MockPermissionStateRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlin.test.Test
import kotlin.test.assertIs

class PermissionStateTest : BaseFlowTest<PermissionStateTest.DesiredState, PermissionStateTest.Context, PermissionState<DummyPermission>, MockPermissionStateRepo<DummyPermission>>() {

    enum class DesiredState {
        ALLOWED,
        DISABLED,
        LOCKED
    }

    class Context(initialState: DesiredState) : TestContext {
        val permissionStateRepo = MockPermissionStateRepo<DummyPermission>()
        val permissionManager = permissionStateRepo.permissionManager.apply {
            currentState.value = when (initialState) {
                DesiredState.ALLOWED -> Allowed(monitoringInterval, this)
                DesiredState.DISABLED -> Requestable(monitoringInterval, this)
                DesiredState.LOCKED -> Locked(monitoringInterval, this)
            }
        }
    }

    override val filter: (Flow<PermissionState<DummyPermission>>) -> (Flow<PermissionState<DummyPermission>>) = {
        it.filterOnlyImportant()
    }

    override val createTestContextWithConfiguration: suspend (configuration: DesiredState, scope: CoroutineScope) -> Context = { configuration, _ -> Context(configuration) }
    override val flowFromTestContext: suspend Context.() -> MockPermissionStateRepo<DummyPermission> = { permissionStateRepo }

    @Test
    fun testInitialState() = testWithFlowAndTestContext(DesiredState.DISABLED) {
        mainAction {
            permissionManager.startMonitoringMock.verify(rule = never())
            permissionManager.stopMonitoringMock.verify(rule = never())
        }
        test {
            permissionManager.startMonitoringMock.verify(eq(permissionManager.monitoringInterval))
            assertIs<Requestable<DummyPermission>>(it)
        }
        action {
            resetFlow()
        }
        mainAction {
            permissionManager.stopMonitoringMock.verify()
        }
    }

    @Test
    fun testRequestPermission() = testWithFlowAndTestContext(DesiredState.DISABLED) {
        mainAction {
            permissionManager.startMonitoringMock.verify(rule = never())
            permissionManager.stopMonitoringMock.verify(rule = never())
        }
        test {
            permissionManager.startMonitoringMock.verify(eq(permissionManager.monitoringInterval))
            assertIs<Requestable<DummyPermission>>(it)
        }
        mainAction {
            permissionManager.requestPermissionMock.on().doExecuteSuspended { permissionManager.grantPermission() }
            permissionStateRepo.request(permissionManager)
        }
        test {
            assertIs<Allowed<DummyPermission>>(it)
        }
        action {
            resetFlow()
        }
        mainAction {
            permissionManager.stopMonitoringMock.verify()
        }
    }

    @Test
    fun testRequestPermissionDenied() = testWithFlowAndTestContext(DesiredState.DISABLED) {
        mainAction {
            permissionManager.startMonitoringMock.verify(rule = never())
            permissionManager.stopMonitoringMock.verify(rule = never())
        }
        test {
            assertIs<Requestable<DummyPermission>>(it)
        }
        mainAction {
            permissionManager.requestPermissionMock.on().doExecuteSuspended { permissionManager.revokePermission(true) }
            permissionStateRepo.request(permissionManager)
        }
        test {
            assertIs<Locked<DummyPermission>>(it)
        }
        action {
            resetFlow()
        }
        mainAction {
            permissionManager.stopMonitoringMock.verify()
        }
    }

    @Test
    fun testRevokePermission() = testWithFlowAndTestContext(DesiredState.ALLOWED) {
        mainAction {
            permissionManager.startMonitoringMock.verify(rule = never())
            permissionManager.stopMonitoringMock.verify(rule = never())
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
            permissionManager.stopMonitoringMock.verify()
        }
    }
}
