package com.splendo.kaluga.permissions
/*

Copyright 2019 Splendo Consulting B.V. The Netherlands

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

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class BluetoothPermissionManagerTest {
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    private lateinit var bluetoothPermissionManager: BluetoothPermissionManager
    private lateinit var mockContext: Context
    private lateinit var mockBluetoothAdapterWrapper: BluetoothPermissionManager.BluetoothAdapterWrapper

    @Before
    fun before() {
        Dispatchers.setMain(mainThreadSurrogate)
        mockContext = mock(Context::class.java)
        mockBluetoothAdapterWrapper = mock(BluetoothPermissionManager.BluetoothAdapterWrapper::class.java)
        bluetoothPermissionManager = BluetoothPermissionManager(mockContext, mockBluetoothAdapterWrapper)
    }

    @After
    fun after() {
        Dispatchers.resetMain()
    }

    @Test
    fun testBluetoothSupportAndAdapterIsNullThenNotSupported() {
        `when`(mockBluetoothAdapterWrapper.isAvailable()).thenReturn(false)

        val support: Support = runBlocking {
            return@runBlocking bluetoothPermissionManager.checkSupport()
        }

        assertNotNull(support)
        assertEquals(Support.NOT_SUPPORTED, support)
    }

    @Test
    fun testBluetoothSupportAndAdapterIsEnabledThenSupportPowerOn() {
        `when`(mockBluetoothAdapterWrapper.isAvailable()).thenReturn(true)
        `when`(mockBluetoothAdapterWrapper.isEnabled()).thenReturn(true)

        val support: Support = runBlocking {
            return@runBlocking bluetoothPermissionManager.checkSupport()
        }

        assertNotNull(support)
        assertEquals(Support.POWER_ON, support)
    }

    @Test
    fun testBluetoothSupportAndAdapterIsDisabledThenSupportPowerOff() {
        `when`(mockBluetoothAdapterWrapper.isAvailable()).thenReturn(true)
        `when`(mockBluetoothAdapterWrapper.isEnabled()).thenReturn(false)

        val support: Support = runBlocking {
            return@runBlocking bluetoothPermissionManager.checkSupport()
        }

        assertNotNull(support)
        assertEquals(Support.POWER_OFF, support)
    }

    @Test
    fun testBluetoothPermitCheckAndPermissionGrantedThenPermitAllowed() {
        `when`(mockContext.checkSelfPermission(Manifest.permission.BLUETOOTH)).thenReturn(PackageManager.PERMISSION_GRANTED)

        val permit: Permit = runBlocking {
            return@runBlocking bluetoothPermissionManager.checkPermit()
        }

        assertNotNull(permit)
        assertEquals(Permit.ALLOWED, permit)
    }

    @Test
    fun testBluetoothPermitCheckAndPermissionDeniedThenPermitDenied() {
        `when`(mockContext.checkSelfPermission(Manifest.permission.BLUETOOTH)).thenReturn(PackageManager.PERMISSION_DENIED)

        val permit: Permit = runBlocking {
            return@runBlocking bluetoothPermissionManager.checkPermit()
        }

        assertNotNull(permit)
        assertEquals(Permit.DENIED, permit)
    }
}
