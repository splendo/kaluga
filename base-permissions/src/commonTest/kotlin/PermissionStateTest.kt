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

import co.touchlab.stately.collections.sharedMutableListOf
import com.splendo.kaluga.base.flow.filterOnlyImportant
import com.splendo.kaluga.permissions.PermissionState.Allowed
import com.splendo.kaluga.permissions.PermissionState.Denied.Locked
import com.splendo.kaluga.permissions.PermissionState.Denied.Requestable
import com.splendo.kaluga.test.BaseFlowTest
import com.splendo.kaluga.test.mock.call
import com.splendo.kaluga.test.mock.matcher.ParameterMatcher.Companion.eq
import com.splendo.kaluga.test.mock.on
import com.splendo.kaluga.test.mock.parameters.mock
import com.splendo.kaluga.test.mock.verify
import com.splendo.kaluga.test.permissions.DummyPermission
import com.splendo.kaluga.test.permissions.MockPermissionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlin.test.Test
import kotlin.test.assertIs

class PermissionStateTest : BaseFlowTest<PermissionStateTest.MockPermissionManagerBuilder, PermissionStateTest.Context, PermissionState<DummyPermission>, PermissionStateRepo<DummyPermission>>() {

    class MockPermissionStateRepo(builder: MockPermissionManagerBuilder, coroutineScope: CoroutineScope) : PermissionStateRepo<DummyPermission>(
        builder.monitoringInterval,
        coroutineScope.coroutineContext,
        {
            builder.create(it)
        }
    )

    class MockPermissionManagerBuilder(
        val initialState: MockPermissionManager.MockPermissionState = MockPermissionManager.MockPermissionState.DENIED,
        val monitoringInterval: Long = PermissionStateRepo.defaultMonitoringInterval,
    ) {
        val createdManagers = sharedMutableListOf<MockPermissionManager<DummyPermission>>()
        val createMock = ::create.mock()

        init {
            createMock.on().doExecute { (repo) ->
                MockPermissionManager(repo, initialState, monitoringInterval).also { createdManagers.add(it) }
            }
        }

        fun create(repo: PermissionStateRepo<DummyPermission>): MockPermissionManager<DummyPermission> = createMock.call(repo)
    }

    class Context(
        val builder: MockPermissionManagerBuilder,
        coroutineScope: CoroutineScope
    ) : TestContext {
        val permissionStateRepo = MockPermissionStateRepo(builder, coroutineScope)
        val permissionManager get() = builder.createdManagers.first()
    }

    override val filter: (Flow<PermissionState<DummyPermission>>) -> (Flow<PermissionState<DummyPermission>>) = {
        it.filterOnlyImportant()
    }

    override val createTestContextWithConfiguration: suspend (configuration: MockPermissionManagerBuilder, scope: CoroutineScope) -> Context = { configuration, scope -> Context(configuration, scope) }
    override val flowFromTestContext: suspend Context.() -> MockPermissionStateRepo = { permissionStateRepo }

    @Test
    fun testInitialState() = testWithFlowAndTestContext(MockPermissionManagerBuilder()) {
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
    fun testRequestPermission() = testWithFlowAndTestContext(MockPermissionManagerBuilder()) {
        test {
            permissionManager.startMonitoringMock.verify(eq(permissionManager.monitoringInterval))
            assertIs<Requestable<DummyPermission>>(it)
        }
        mainAction {
            permissionManager.requestPermissionMock.on().doExecuteSuspended { permissionManager.grantPermission() }
            permissionStateRepo.request()
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
    fun testRequestPermissionDenied() = testWithFlowAndTestContext(MockPermissionManagerBuilder()) {
        test {
            assertIs<Requestable<DummyPermission>>(it)
        }
        mainAction {
            permissionManager.requestPermissionMock.on().doExecuteSuspended { permissionManager.revokePermission(true) }
            permissionStateRepo.request()
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
    fun testRevokePermission() = testWithFlowAndTestContext(MockPermissionManagerBuilder(initialState = MockPermissionManager.MockPermissionState.ALLOWED)) {
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
