import com.splendo.mpp.permissions.BluetoothPermissionManager
import platform.CoreBluetooth.CBCentralManager
import platform.CoreBluetooth.CBPeripheralManager

class MockCBCentralManager() : CBCentralManager(null, null, null) {

    var mockState = BluetoothPermissionManager.STATE_UNKNOWN

    override fun state(): platform.CoreBluetooth.CBManagerState {
        return mockState
    }
}

class MockCBPeripheralManager : CBPeripheralManager(null, null, null) {
    var mockAuthorizationStatus = BluetoothPermissionManager.AUTHORIZATION_STATUS_NOT_DETERMINED

//    override fun authorizationStatus(): CBPeripheralManagerAuthorizationStatus {
//        return mockAuthorizationStatus
//    }

}