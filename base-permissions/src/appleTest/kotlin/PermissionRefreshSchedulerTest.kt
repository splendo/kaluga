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

import com.splendo.kaluga.base.runBlocking
import com.splendo.kaluga.test.base.BaseTest
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.time.Duration.Companion.milliseconds

class PermissionRefreshSchedulerTest : BaseTest() {

    @Test
    fun testStartMonitoring() = runBlocking {
        var authorization = IOSPermissionsHelper.AuthorizationStatus.NotDetermined
        val authorizationProvider = object : CurrentAuthorizationStatusProvider {
            override suspend fun provide(): IOSPermissionsHelper.AuthorizationStatus = authorization
        }

        val onPermissionChangedFlow = MutableStateFlow<IOSPermissionsHelper.AuthorizationStatus?>(null)
        val timerHelper = PermissionRefreshScheduler(
            currentAuthorizationStatusProvider = authorizationProvider,
            authorizationStatusHandler = object : AuthorizationStatusHandler {
                override fun status(status: IOSPermissionsHelper.AuthorizationStatus) {
                    onPermissionChangedFlow.value = status
                }
            },
            coroutineScope = this,
        )

        timerHelper.startMonitoring(50.milliseconds)
        delay(50)
        assertNull(onPermissionChangedFlow.value)

        authorization = IOSPermissionsHelper.AuthorizationStatus.Authorized
        delay(60)
        assertEquals(
            IOSPermissionsHelper.AuthorizationStatus.Authorized,
            onPermissionChangedFlow.value,
        )

        timerHelper.waitingLock.lock()
        authorization = IOSPermissionsHelper.AuthorizationStatus.Denied
        onPermissionChangedFlow.value = null
        delay(60)
        assertNull(onPermissionChangedFlow.value)

        timerHelper.waitingLock.unlock()
        delay(50)
        assertEquals(IOSPermissionsHelper.AuthorizationStatus.Denied, onPermissionChangedFlow.value)

        authorization = IOSPermissionsHelper.AuthorizationStatus.Authorized
        onPermissionChangedFlow.value = null
        timerHelper.stopMonitoring()
        delay(50)
        assertNull(onPermissionChangedFlow.value)
    }
}
