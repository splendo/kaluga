import com.splendo.mpp.permissions.BluetoothPermissionManager
import com.splendo.mpp.permissions.Permit
import com.splendo.mpp.permissions.Support
import kotlinx.coroutines.runBlocking
import kotlin.test.*

class BluetoothPermissionMangerTest {

    private lateinit var bluetoothPermissionManager: BluetoothPermissionManager
    private lateinit var mockCBCentralManager: MockCBCentralManager
    private lateinit var mockCBPeripheralManager: MockCBPeripheralManager

    @BeforeTest
    fun before() {
        mockCBCentralManager = MockCBCentralManager()
        mockCBPeripheralManager = MockCBPeripheralManager()

        bluetoothPermissionManager = BluetoothPermissionManager(mockCBCentralManager, mockCBPeripheralManager)
    }

    @AfterTest
    fun after() {
    }

    @Test
    fun testBluetoothStateUnknownThenNotSupported() {
        mockCBCentralManager.mockState = BluetoothPermissionManager.STATE_UNKNOWN

        val support: Support = runBlocking {
            return@runBlocking bluetoothPermissionManager.checkSupport()
        }

        assertEquals(Support.NOT_SUPPORTED, support)
    }

    @Test
    fun testBluetoothStateResettingThenResetting() {
        mockCBCentralManager.mockState = BluetoothPermissionManager.STATE_RESETTING

        val support: Support = runBlocking {
            return@runBlocking bluetoothPermissionManager.checkSupport()
        }

        assertEquals(Support.RESETTING, support)
    }

    @Test
    fun testBluetoothStateNotSupportedThenNotSupported() {
        mockCBCentralManager.mockState = BluetoothPermissionManager.STATE_UNSUPPORTED

        val support: Support = runBlocking {
            return@runBlocking bluetoothPermissionManager.checkSupport()
        }

        assertEquals(Support.NOT_SUPPORTED, support)
    }

    @Test
    fun testBluetoothStateUnauthorizedThenUnauthorized() {
        mockCBCentralManager.mockState = BluetoothPermissionManager.STATE_UNAUTHORIZED

        val support: Support = runBlocking {
            return@runBlocking bluetoothPermissionManager.checkSupport()
        }

        assertEquals(Support.UNAUTHORIZED, support)
    }

    @Test
    fun testBluetoothStatePowerOffThenPowerOff() {
        mockCBCentralManager.mockState = BluetoothPermissionManager.STATE_POWERED_OFF

        val support: Support = runBlocking {
            return@runBlocking bluetoothPermissionManager.checkSupport()
        }

        assertEquals(Support.POWER_OFF, support)
    }

    @Test
    fun testBluetoothStatePowerOnThenPowerOn() {
        mockCBCentralManager.mockState = BluetoothPermissionManager.STATE_POWERED_ON

        val support: Support = runBlocking {
            return@runBlocking bluetoothPermissionManager.checkSupport()
        }

        assertEquals(Support.POWER_ON, support)
    }

    @Test
    fun testBluetoothStateOutOfRangeThenNotSupported() {
        mockCBCentralManager.mockState = Long.MAX_VALUE

        val support: Support = runBlocking {
            return@runBlocking bluetoothPermissionManager.checkSupport()
        }

        assertEquals(Support.NOT_SUPPORTED, support)
    }

    @Ignore
    @Test
    fun testBluetoothAuthorizationIsNotDeterminedThenPermitUndefined() {
        mockCBPeripheralManager.mockAuthorizationStatus = BluetoothPermissionManager.AUTHORIZATION_STATUS_NOT_DETERMINED

        val permit: Permit = runBlocking {
            return@runBlocking bluetoothPermissionManager.checkPermit()
        }

        assertEquals(Permit.UNDEFINED, permit)
    }

    @Ignore
    @Test
    fun testBluetoothAuthorizationIsRestrictedThenPermitRestricted() {
        mockCBPeripheralManager.mockAuthorizationStatus = BluetoothPermissionManager.AUTHORIZATION_STATUS_RESTRICTED

        val permit: Permit = runBlocking {
            return@runBlocking bluetoothPermissionManager.checkPermit()
        }

        assertEquals(Permit.RESTRICTED, permit)
    }

    @Ignore
    @Test
    fun testBluetoothAuthorizationIsDeniedThenPermitDenied() {
        mockCBPeripheralManager.mockAuthorizationStatus = BluetoothPermissionManager.AUTHORIZATION_STATUS_DENIED

        val permit: Permit = runBlocking {
            return@runBlocking bluetoothPermissionManager.checkPermit()
        }

        assertEquals(Permit.DENIED, permit)
    }

    @Ignore
    @Test
    fun testBluetoothAuthorizationIsAuthorizedThenPermitAllowed() {
        mockCBPeripheralManager.mockAuthorizationStatus = BluetoothPermissionManager.AUTHORIZATION_STATUS_AUTHORIZED

        val permit: Permit = runBlocking {
            return@runBlocking bluetoothPermissionManager.checkPermit()
        }

        assertEquals(Permit.ALLOWED, permit)
    }

    @Ignore
    @Test
    fun testBluetoothAuthorizationIsLongMaxThenPermitUndefined() {
        mockCBPeripheralManager.mockAuthorizationStatus = Long.MAX_VALUE

        val permit: Permit = runBlocking {
            return@runBlocking bluetoothPermissionManager.checkPermit()
        }

        assertEquals(Permit.UNDEFINED, permit)
    }

}
