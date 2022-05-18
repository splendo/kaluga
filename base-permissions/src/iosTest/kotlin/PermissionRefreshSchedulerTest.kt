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

import co.touchlab.stately.concurrency.AtomicReference
import co.touchlab.stately.concurrency.value
import com.splendo.kaluga.base.runBlocking
import com.splendo.kaluga.test.BaseTest
import com.splendo.kaluga.test.mock.matcher.ParameterMatcher.Companion.eq
import com.splendo.kaluga.test.mock.verification.VerificationRule.Companion.never
import com.splendo.kaluga.test.mock.verify
import com.splendo.kaluga.test.permissions.DummyPermission
import com.splendo.kaluga.test.permissions.MockPermissionManager
import com.splendo.kaluga.test.permissions.MockPermissionStateRepo
import kotlinx.coroutines.delay
import kotlin.test.BeforeTest
import kotlin.test.Test

class PermissionRefreshSchedulerTest : BaseTest() {

    private lateinit var permissionsManager: MockPermissionManager<DummyPermission>

    @BeforeTest
    fun setUp() {
        super.beforeTest()

        val repo = MockPermissionStateRepo<DummyPermission>()
        permissionsManager = repo.permissionManager
    }

    @Test
    fun testStartMonitoring() = runBlocking {
        val authorization: AtomicReference<IOSPermissionsHelper.AuthorizationStatus> = AtomicReference(IOSPermissionsHelper.AuthorizationStatus.NotDetermined)
        val timerHelper = PermissionRefreshScheduler(permissionsManager, { authorization.value }, this)

        timerHelper.startMonitoring(50)
        delay(50)
        permissionsManager.grandPermissionMock.verify(rule = never())
        permissionsManager.revokePermissionMock.verify(rule = never())

        authorization.set(IOSPermissionsHelper.AuthorizationStatus.Authorized)
        delay(60)
        permissionsManager.grandPermissionMock.verify()

        timerHelper.isWaiting.value = true
        authorization.set(IOSPermissionsHelper.AuthorizationStatus.Denied)
        permissionsManager.grandPermissionMock.resetCalls()
        permissionsManager.revokePermissionMock.resetCalls()
        delay(60)
        permissionsManager.grandPermissionMock.verify(rule = never())
        permissionsManager.revokePermissionMock.verify(rule = never())

        timerHelper.isWaiting.value = false
        delay(50)
        permissionsManager.revokePermissionMock.verify(eq(true))

        authorization.set(IOSPermissionsHelper.AuthorizationStatus.Authorized)
        permissionsManager.grandPermissionMock.resetCalls()
        permissionsManager.revokePermissionMock.resetCalls()
        timerHelper.stopMonitoring()
        delay(50)
        permissionsManager.grandPermissionMock.verify(rule = never())
        permissionsManager.revokePermissionMock.verify(rule = never())
        Unit
    }
}
