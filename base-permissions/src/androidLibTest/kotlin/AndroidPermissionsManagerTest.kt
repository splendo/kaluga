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

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import com.splendo.kaluga.base.runBlocking
import com.splendo.kaluga.logging.RestrictedLogLevel
import com.splendo.kaluga.logging.RestrictedLogger
import com.splendo.kaluga.test.base.BaseTest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.yield
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.assertEquals

class AndroidPermissionsManagerTest : BaseTest() {

    companion object {
        private const val packageName = "package"
        private val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    @Mock
    lateinit var context: Context
    @Mock
    lateinit var packageManager: PackageManager
    @Mock
    lateinit var packageInfo: PackageInfo

    private lateinit var androidPermissionsManager: AndroidPermissionsManager

    @BeforeTest
    override fun beforeTest() {
        super.beforeTest()
        MockitoAnnotations.initMocks(this)

        `when`(context.packageManager).thenReturn(packageManager)
        `when`(context.packageName).thenReturn(packageName)
        `when`(packageManager.getPackageInfo(Mockito.eq(packageName), Mockito.eq(PackageManager.GET_PERMISSIONS))).thenReturn(packageInfo)
        AndroidPermissionsManager.permissionsStates.clear()
    }

    @AfterTest
    override fun afterTest() {
        super.afterTest()
    }

    @Test
    fun testMissingDeclaration() = runBlockingTest {
        val onPermissionChangedFlow = MutableStateFlow<AndroidPermissionState?>(null)
        androidPermissionsManager = AndroidPermissionsManager(context, permissions, this, "", RestrictedLogger(RestrictedLogLevel.Verbose), onPermissionChangedFlow = onPermissionChangedFlow)
        packageInfo.requestedPermissions = emptyArray()
        androidPermissionsManager.requestPermissions()

        assertEquals(AndroidPermissionState.DENIED_DO_NOT_ASK, onPermissionChangedFlow.value)
    }

    @Test
    fun testRequestPermissions() = runBlockingTest {
        val onPermissionChangedFlow = MutableStateFlow<AndroidPermissionState?>(null)
        androidPermissionsManager = AndroidPermissionsManager(context, permissions, this, "", RestrictedLogger(RestrictedLogLevel.Verbose), onPermissionChangedFlow = onPermissionChangedFlow)
        packageInfo.requestedPermissions = permissions
        androidPermissionsManager.requestPermissions()
        verify(context).startActivity(ArgumentMatchers.any(Intent::class.java))
    }

    @Test
    fun test_monitor_permissionsGranted() = runBlocking {
        val onPermissionChangedFlow = MutableStateFlow<AndroidPermissionState?>(null)
        androidPermissionsManager = AndroidPermissionsManager(context, permissions, this, "", RestrictedLogger(RestrictedLogLevel.Verbose), onPermissionChangedFlow = onPermissionChangedFlow)
        permissions.forEach {
            `when`(context.checkPermission(Mockito.eq(it), ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt())).thenReturn(PackageManager.PERMISSION_GRANTED)
        }

        androidPermissionsManager.monitor()
        yield()

        assertEquals(AndroidPermissionState.GRANTED, onPermissionChangedFlow.value)
    }

    @Test
    fun test_monitor_permissionsDenied() = runBlocking {
        val onPermissionChangedFlow = MutableStateFlow<AndroidPermissionState?>(null)
        androidPermissionsManager = AndroidPermissionsManager(context, permissions, this, "", RestrictedLogger(RestrictedLogLevel.Verbose), onPermissionChangedFlow = onPermissionChangedFlow)
        permissions.forEach {
            `when`(context.checkPermission(Mockito.eq(it), ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt())).thenReturn(PackageManager.PERMISSION_DENIED)
        }

        androidPermissionsManager.monitor()
        yield()

        assertEquals(AndroidPermissionState.DENIED, onPermissionChangedFlow.value)
    }

    @Test
    fun test_monitor_permissionsDeniedDoNotAsk() = runBlocking {
        val onPermissionChangedFlow = MutableStateFlow<AndroidPermissionState?>(null)
        androidPermissionsManager = AndroidPermissionsManager(context, permissions, this, "", RestrictedLogger(RestrictedLogLevel.Verbose), onPermissionChangedFlow = onPermissionChangedFlow)
        permissions.forEach {
            AndroidPermissionsManager.permissionsStates[it] = AndroidPermissionState.DENIED_DO_NOT_ASK
            `when`(context.checkPermission(Mockito.eq(it), ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt())).thenReturn(PackageManager.PERMISSION_DENIED)
        }

        androidPermissionsManager.monitor()
        yield()

        assertEquals(AndroidPermissionState.DENIED_DO_NOT_ASK, onPermissionChangedFlow.value)
    }
}
