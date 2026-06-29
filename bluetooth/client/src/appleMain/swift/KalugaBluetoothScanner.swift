/*

Copyright 2025 Splendo Consulting B.V. The Netherlands

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

import Foundation
import CoreBluetooth

@objc(KalugaBluetoothScanningDelegate)
public protocol KalugaBluetoothScanningDelegate {
    @objc func didUpdateState(_ centralManager: CBCentralManager)
    @objc func didDiscoverPeripheral(_ peripheral: CBPeripheral, forCentralManager centralManager: CBCentralManager, advertisementData: [String : Any], rssi RSSI: NSNumber)
    @objc func didConnectPeripheral(_ peripheral: CBPeripheral, forCentralManager centralManager: CBCentralManager)
    @objc func didDisconnectPeripheral(_ peripheral: CBPeripheral, withError error: (any Error)?, forCentralManager centralManager: CBCentralManager)
    @objc func didFailToConnectPeripheral(_ peripheral: CBPeripheral, withError error: (any Error)?, forCentralManager centralManager: CBCentralManager)
}

@objc(KalugaBluetoothScanningWrapper)
public class KalugaBluetoothScanningWrapper : NSObject, CBCentralManagerDelegate {

    @objc public static func createByLinking(centralManager: CBCentralManager, to delegate: KalugaBluetoothScanningDelegate) -> KalugaBluetoothScanningWrapper {
        let wrapper = KalugaBluetoothScanningWrapper(delegate: delegate) {
            centralManager.delegate = nil
        }
        centralManager.delegate = wrapper
        return wrapper
    }

    @objc public init(delegate: KalugaBluetoothScanningDelegate, unlinkAction: @escaping () -> Void) {
        self.delegate = delegate
        self.unlinkAction = unlinkAction
    }

    let delegate: KalugaBluetoothScanningDelegate
    var unlinkAction: (() -> Void)?

    public func centralManagerDidUpdateState(_ central: CBCentralManager) {
        delegate.didUpdateState(central)
    }

    public func centralManager(_ central: CBCentralManager, didDiscover peripheral: CBPeripheral, advertisementData: [String : Any], rssi RSSI: NSNumber) {
        delegate.didDiscoverPeripheral(peripheral, forCentralManager: central, advertisementData: advertisementData, rssi: RSSI)
    }

    public func centralManager(_ central: CBCentralManager, didConnect peripheral: CBPeripheral) {
        delegate.didConnectPeripheral(peripheral, forCentralManager: central)
    }

    public func centralManager(_ central: CBCentralManager, didFailToConnect peripheral: CBPeripheral, error: (any Error)?) {
        delegate.didFailToConnectPeripheral(peripheral, withError: error, forCentralManager: central)
    }

    public func centralManager(_ central: CBCentralManager, didDisconnectPeripheral peripheral: CBPeripheral, error: (any Error)?) {
        delegate.didDisconnectPeripheral(peripheral, withError: error, forCentralManager: central)
    }

    @objc public func unlink() {
        if let unlinkAction = unlinkAction {
            unlinkAction()
        }
        unlinkAction = nil
    }
}
