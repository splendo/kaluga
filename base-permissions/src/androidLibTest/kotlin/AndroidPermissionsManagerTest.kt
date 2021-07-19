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

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import com.splendo.kaluga.base.runBlocking
import com.splendo.kaluga.test.BaseTest
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.eq
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import kotlin.test.AfterTest
import kotlin.test.BeforeTest

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

    @Mock
    lateinit var permissionsManager: PermissionManager<DummyPermission>
    private lateinit var androidPermissionsManager: AndroidPermissionsManager<DummyPermission>

    @BeforeTest
    override fun beforeTest() {
        super.beforeTest()
        MockitoAnnotations.initMocks(this)

        `when`(context.packageManager).thenReturn(packageManager)
        `when`(context.packageName).thenReturn(packageName)
        `when`(packageManager.getPackageInfo(eq(packageName), eq(PackageManager.GET_PERMISSIONS))).thenReturn(packageInfo)
        AndroidPermissionsManager.permissionsStates.clear()
    }

    @AfterTest
    override fun afterTest() {
        super.afterTest()
    }

    @Test
    fun testMissingDeclaration() = runBlockingTest {
        androidPermissionsManager = AndroidPermissionsManager(context, permissionsManager, permissions, this)
        packageInfo.requestedPermissions = emptyArray()
        androidPermissionsManager.requestPermissions()

        verify(permissionsManager).revokePermission(eq(true))
    }

    @Test
    fun testRequestPermissions() = runBlockingTest {
        androidPermissionsManager = AndroidPermissionsManager(context, permissionsManager, permissions, this)
        packageInfo.requestedPermissions = permissions
        androidPermissionsManager.requestPermissions()
        verify(context).startActivity(ArgumentMatchers.any(Intent::class.java))
    }

    @Test
    fun test_monitor_permissionsGranted() = runBlocking {
        androidPermissionsManager = AndroidPermissionsManager(context, permissionsManager, permissions, this)
        permissions.forEach {
            `when`(context.checkPermission(eq(it), ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt())).thenReturn(PackageManager.PERMISSION_GRANTED)
        }

        androidPermissionsManager.monitor()

        verify(permissionsManager).grantPermission()
    }

    @Test
    fun test_monitor_permissionsDenied() = runBlocking {
        androidPermissionsManager = AndroidPermissionsManager(context, permissionsManager, permissions, this)
        permissions.forEach {
            `when`(context.checkPermission(eq(it), ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt())).thenReturn(PackageManager.PERMISSION_DENIED)
        }

        androidPermissionsManager.monitor()

        verify(permissionsManager).revokePermission(false)
    }

    @Test
    fun test_monitor_permissionsDeniedDoNotAsk() = runBlocking {
        androidPermissionsManager = AndroidPermissionsManager(context, permissionsManager, permissions, this)
        permissions.forEach {
            AndroidPermissionsManager.permissionsStates[it] = AndroidPermissionState.DENIED_DO_NOT_ASK
            `when`(context.checkPermission(eq(it), ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt())).thenReturn(PackageManager.PERMISSION_DENIED)
        }

        androidPermissionsManager.monitor()

        verify(permissionsManager).revokePermission(true)
    }
}
