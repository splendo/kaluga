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

@objc(KalugaBluetoothPeripheralDelegate)
public protocol KalugaBluetoothPeripheralDelegate {
    @objc func didRead(rssi RSSI: NSNumber, forPeripheral peripheral: CBPeripheral, error: (any Error)?)
    @objc func didDiscoverServices(for peripheral: CBPeripheral, error: (any Error)?)
    @objc func didDiscoverCharacteristics(for service: CBService, peripheral: CBPeripheral, error: (any Error)?)
    @objc func didUpdateValue(forCharacteristic characteristic: CBCharacteristic, peripheral: CBPeripheral, error: (any Error)?)
    @objc func didWriteValue(forCharacteristic characteristic: CBCharacteristic, peripheral: CBPeripheral, error: (any Error)?)
    @objc func didUpdateNotificationState(for characteristic: CBCharacteristic, peripheral: CBPeripheral, error: (any Error)?)
    @objc func didDiscoverDescriptors(for characteristic: CBCharacteristic, peripheral: CBPeripheral, error: (any Error)?)
    @objc func didUpdateValue(forDescriptor descriptor: CBDescriptor, peripheral: CBPeripheral, error: (any Error)?)
    @objc func didWriteValue(forDescriptor descriptor: CBDescriptor, peripheral: CBPeripheral, error: (any Error)?)
}

@objc(KalugaBluetoothPeripheralWrapper)
public class KalugaBluetoothPeripheralWrapper : NSObject, CBPeripheralDelegate {

    @objc public static func createByLinking(peripheral: CBPeripheral, to delegate: KalugaBluetoothPeripheralDelegate) -> KalugaBluetoothPeripheralWrapper {
        let wrapper = KalugaBluetoothPeripheralWrapper(delegate: delegate) {
            peripheral.delegate = nil
        }
        peripheral.delegate = wrapper
        return wrapper
    }

    let delegate: KalugaBluetoothPeripheralDelegate
    var unlinkAction: (() -> Void)?

    init(delegate: KalugaBluetoothPeripheralDelegate, unlinkAction: @escaping () -> Void) {
        self.delegate = delegate
        self.unlinkAction = unlinkAction
    }

    public func peripheral(_ peripheral: CBPeripheral, didReadRSSI RSSI: NSNumber, error: (any Error)?) {
        delegate.didRead(rssi: RSSI, forPeripheral: peripheral, error: error)
    }

    public func peripheral(_ peripheral: CBPeripheral, didDiscoverServices error: (any Error)?) {
        delegate.didDiscoverServices(for: peripheral, error: error)
    }

    public func peripheral(_ peripheral: CBPeripheral, didDiscoverCharacteristicsFor service: CBService, error: (any Error)?) {
        delegate.didDiscoverCharacteristics(for: service, peripheral: peripheral, error: error)
    }

    public func peripheral(_ peripheral: CBPeripheral, didUpdateValueFor characteristic: CBCharacteristic, error: (any Error)?) {
        delegate.didUpdateValue(forCharacteristic: characteristic, peripheral: peripheral, error: error)
    }

    public func peripheral(_ peripheral: CBPeripheral, didWriteValueFor characteristic: CBCharacteristic, error: (any Error)?) {
        delegate.didWriteValue(forCharacteristic: characteristic, peripheral: peripheral, error: error)
    }

    public func peripheral(_ peripheral: CBPeripheral, didUpdateNotificationStateFor characteristic: CBCharacteristic, error: (any Error)?) {
        delegate.didUpdateNotificationState(for: characteristic, peripheral: peripheral, error: error)
    }

    public func peripheral(_ peripheral: CBPeripheral, didDiscoverDescriptorsFor characteristic: CBCharacteristic, error: (any Error)?) {
        delegate.didDiscoverDescriptors(for: characteristic, peripheral: peripheral, error: error)
    }

    public func peripheral(_ peripheral: CBPeripheral, didUpdateValueFor descriptor: CBDescriptor, error: (any Error)?) {
        delegate.didUpdateValue(forDescriptor: descriptor, peripheral: peripheral, error: error)
    }

    public func peripheral(_ peripheral: CBPeripheral, didWriteValueFor descriptor: CBDescriptor, error: (any Error)?) {
        delegate.didWriteValue(forDescriptor: descriptor, peripheral: peripheral, error: error)
    }

    @objc public func unlink() {
        if let unlinkAction = unlinkAction {
            unlinkAction()
        }
        unlinkAction = nil
    }
}
