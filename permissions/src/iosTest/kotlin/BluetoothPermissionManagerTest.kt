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
import com.splendo.kaluga.base.runBlocking
import com.splendo.kaluga.permissions.bluetooth.BluetoothPermission
import com.splendo.kaluga.permissions.Permit
import com.splendo.kaluga.permissions.Support
import platform.CoreBluetooth.*
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class BluetoothPermissionMangerTest {

    private lateinit var bluetoothPermissionManager: BluetoothPermission
    private lateinit var mockCBCentralManager: MockCBCentralManager
    private var cbPeripheralManagerAuthorizationStatus: CBPeripheralManagerAuthorizationStatus =
        CBPeripheralManagerAuthorizationStatusNotDetermined

    @BeforeTest
    fun before() {
        mockCBCentralManager = MockCBCentralManager()

        bluetoothPermissionManager =
            BluetoothPermission(mockCBCentralManager, { this@BluetoothPermissionMangerTest.cbPeripheralManagerAuthorizationStatus })
    }

    @AfterTest
    fun after() {
    }

    @Test
    fun testBluetoothStateUnknownThenNotSupported() {
        mockCBCentralManager.mockState = BluetoothPermission.CBCentralManagerState.UNKNOWN

        val support: Support = runBlocking {
            return@runBlocking bluetoothPermissionManager.checkSupport()
        }

        assertEquals(Support.NOT_SUPPORTED, support)
    }

    @Test
    fun testBluetoothStateResettingThenResetting() {
        mockCBCentralManager.mockState = BluetoothPermission.CBCentralManagerState.RESETTING

        val support: Support = runBlocking {
            return@runBlocking bluetoothPermissionManager.checkSupport()
        }

        assertEquals(Support.RESETTING, support)
    }

    @Test
    fun testBluetoothStateNotSupportedThenNotSupported() {
        mockCBCentralManager.mockState = BluetoothPermission.CBCentralManagerState.UNSUPPORTED

        val support: Support = runBlocking {
            return@runBlocking bluetoothPermissionManager.checkSupport()
        }

        assertEquals(Support.NOT_SUPPORTED, support)
    }

    @Test
    fun testBluetoothStateUnauthorizedThenUnauthorized() {
        mockCBCentralManager.mockState = BluetoothPermission.CBCentralManagerState.UNAUTHORIZED

        val support: Support = runBlocking {
            return@runBlocking bluetoothPermissionManager.checkSupport()
        }

        assertEquals(Support.UNAUTHORIZED, support)
    }

    @Test
    fun testBluetoothStatePowerOffThenPowerOff() {
        mockCBCentralManager.mockState = BluetoothPermission.CBCentralManagerState.POWER_OFF

        val support: Support = runBlocking {
            return@runBlocking bluetoothPermissionManager.checkSupport()
        }

        assertEquals(Support.POWER_OFF, support)
    }

    @Test
    fun testBluetoothStatePowerOnThenPowerOn() {
        mockCBCentralManager.mockState = BluetoothPermission.CBCentralManagerState.POWER_ON

        val support: Support = runBlocking {
            return@runBlocking bluetoothPermissionManager.checkSupport()
        }

        assertEquals(Support.POWER_ON, support)
    }

    @Test
    fun testBluetoothAuthorizationIsNotDeterminedThenPermitUndefined() {
        cbPeripheralManagerAuthorizationStatus = CBPeripheralManagerAuthorizationStatusNotDetermined

        val permit: Permit = runBlocking {
            return@runBlocking bluetoothPermissionManager.checkPermit()
        }

        assertEquals(Permit.UNDEFINED, permit)
    }

    @Test
    fun testBluetoothAuthorizationIsRestrictedThenPermitRestricted() {
        cbPeripheralManagerAuthorizationStatus = CBPeripheralManagerAuthorizationStatusRestricted

        val permit: Permit = runBlocking {
            return@runBlocking bluetoothPermissionManager.checkPermit()
        }

        assertEquals(Permit.RESTRICTED, permit)
    }

    @Test
    fun testBluetoothAuthorizationIsDeniedThenPermitDenied() {
        cbPeripheralManagerAuthorizationStatus = CBPeripheralManagerAuthorizationStatusDenied

        val permit: Permit = runBlocking {
            return@runBlocking bluetoothPermissionManager.checkPermit()
        }

        assertEquals(Permit.DENIED, permit)
    }

    @Test
    fun testBluetoothAuthorizationIsAuthorizedThenPermitAllowed() {
        cbPeripheralManagerAuthorizationStatus = CBPeripheralManagerAuthorizationStatusAuthorized

        val permit: Permit = runBlocking {
            return@runBlocking bluetoothPermissionManager.checkPermit()
        }

        assertEquals(Permit.ALLOWED, permit)
    }

}
