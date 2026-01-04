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
import com.splendo.kaluga.test.base.BaseTest
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.yield
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.assertEquals

class AndroidPermissionsManagerTest : BaseTest() {

    companion object {
        private const val PACKAGE_NAME = "package"
        private val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    private class MockPermissionStateHandler : AndroidPermissionStateHandler {
        var value: AndroidPermissionState? = null

        override fun status(state: AndroidPermissionState) {
            value = state
        }
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
        MockitoAnnotations.openMocks(this)

        `when`(context.packageManager).thenReturn(packageManager)
        `when`(context.packageName).thenReturn(PACKAGE_NAME)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            `when`(
                packageManager.getPackageInfo(
                    Mockito.eq(PACKAGE_NAME),
                    Mockito.eq(PackageManager.PackageInfoFlags.of(PackageManager.GET_PERMISSIONS.toLong())),
                ),
            )
        } else {
            @Suppress("DEPRECATION")
            `when`(
                packageManager.getPackageInfo(
                    Mockito.eq(PACKAGE_NAME),
                    Mockito.eq(PackageManager.GET_PERMISSIONS),
                ),
            )
        }.thenReturn(packageInfo)
        AndroidPermissionsManager.permissionsStates.clear()
    }

    @AfterTest
    override fun afterTest() {
        super.afterTest()
    }

    @Test
    fun testMissingDeclaration() = runTest(UnconfinedTestDispatcher()) {
        val onPermissionChanged = MockPermissionStateHandler()
        androidPermissionsManager = AndroidPermissionsManager(context, permissions, this, onPermissionChanged = onPermissionChanged)
        packageInfo.requestedPermissions = emptyArray()
        androidPermissionsManager.requestPermissions()

        assertEquals(AndroidPermissionState.DENIED_DO_NOT_ASK, onPermissionChanged.value)
    }

    @Test
    fun testRequestPermissions() = runTest(UnconfinedTestDispatcher()) {
        val onPermissionChangedFlow = MockPermissionStateHandler()
        androidPermissionsManager = AndroidPermissionsManager(context, permissions, this, onPermissionChanged = onPermissionChangedFlow)
        packageInfo.requestedPermissions = permissions
        androidPermissionsManager.requestPermissions()
        verify(context).startActivity(ArgumentMatchers.any(Intent::class.java))
    }

    @Test
    fun test_monitor_permissionsGranted() = runBlocking {
        val onPermissionChangedFlow = MockPermissionStateHandler()
        androidPermissionsManager = AndroidPermissionsManager(context, permissions, this, onPermissionChanged = onPermissionChangedFlow)
        permissions.forEach {
            `when`(context.checkPermission(Mockito.eq(it), ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt())).thenReturn(PackageManager.PERMISSION_GRANTED)
        }

        androidPermissionsManager.monitor()
        yield()

        assertEquals(AndroidPermissionState.GRANTED, onPermissionChangedFlow.value)
    }

    @Test
    fun test_monitor_permissionsDenied() = runBlocking {
        val onPermissionChangedFlow = MockPermissionStateHandler()
        androidPermissionsManager = AndroidPermissionsManager(context, permissions, this, onPermissionChanged = onPermissionChangedFlow)
        permissions.forEach {
            `when`(context.checkPermission(Mockito.eq(it), ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt())).thenReturn(PackageManager.PERMISSION_DENIED)
        }

        androidPermissionsManager.monitor()
        yield()

        assertEquals(AndroidPermissionState.DENIED, onPermissionChangedFlow.value)
    }

    @Test
    fun test_monitor_permissionsDeniedDoNotAsk() = runBlocking {
        val onPermissionChangedFlow = MockPermissionStateHandler()
        androidPermissionsManager = AndroidPermissionsManager(context, permissions, this, onPermissionChanged = onPermissionChangedFlow)
        permissions.forEach {
            AndroidPermissionsManager.permissionsStates[it] = AndroidPermissionState.DENIED_DO_NOT_ASK
            `when`(context.checkPermission(Mockito.eq(it), ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt())).thenReturn(PackageManager.PERMISSION_DENIED)
        }

        androidPermissionsManager.monitor()
        yield()

        assertEquals(AndroidPermissionState.DENIED_DO_NOT_ASK, onPermissionChangedFlow.value)
    }
}
