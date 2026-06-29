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

@objc(KalugaBluetoothServerDelegate)
public protocol KalugaBluetoothServerDelegate {
    @objc func didUpdateState(_ forPeripheralManager: CBPeripheralManager)
    @objc func didAddService(_ service: CBService, peripheralManager: CBPeripheralManager, error: (any Error)?)
    @objc func didSubscribe(_ central: CBCentral, toCharacteristic: CBCharacteristic, peripheralManager: CBPeripheralManager)
    @objc func didUnsubscribe(_ central: CBCentral, fromCharacteristic: CBCharacteristic, peripheralManager: CBPeripheralManager)
    @objc func didReceiveRead(_ request: CBATTRequest, peripheralManager: CBPeripheralManager)
    @objc func didReceiveWrite(_ requests: [CBATTRequest], peripheralManager: CBPeripheralManager)
    @objc func didStartAdvertising(_ peripheralManager: CBPeripheralManager, error: (any Error)?)
    @objc func isReady(_ peripheralManager: CBPeripheralManager)
}

@objc(KalugaBluetoothServerWrapper)
public class KalugaBluetoothServerWrapper : NSObject, CBPeripheralManagerDelegate {
    @objc public static func createByLinking(to delegate: KalugaBluetoothServerDelegate, queue: dispatch_queue_t?) -> KalugaBluetoothServerWrapper {
        let wrapper = KalugaBluetoothServerWrapper(delegate: delegate)
        wrapper.peripheralManager = CBPeripheralManager(delegate: wrapper, queue: queue)
        return wrapper
    }

    var peripheralManager: CBPeripheralManager!
    let delegate: KalugaBluetoothServerDelegate

    init(delegate: KalugaBluetoothServerDelegate) {
        self.delegate = delegate
    }

    @objc public func add(_ service: CBMutableService) {
        peripheralManager.add(service)
    }

    @objc public func remove(_ service: CBMutableService) {
        peripheralManager.remove(service)
    }

    @objc public func removeAllServices() {
        peripheralManager.removeAllServices()
    }

    @objc public func startAdvertising(_ advertisementData: [String : Any]?) {
        peripheralManager.startAdvertising(advertisementData)
    }

    @objc public func stopAdvertising() {
        peripheralManager.stopAdvertising()
    }

    @objc public func updateValue(_ value: Data,
                                  for characteristic: CBMutableCharacteristic,
                                  onSubscribedCentrals centrals: [CBCentral]?
    ) -> Bool {
        return peripheralManager.updateValue(value, for: characteristic, onSubscribedCentrals: centrals)
    }

    public func peripheralManagerDidUpdateState(_ peripheral: CBPeripheralManager) {
        delegate.didUpdateState(peripheral)
    }

    public func peripheralManager(_ peripheral: CBPeripheralManager, didAdd: CBService, error: (any Error)?) {
        delegate.didAddService(didAdd, peripheralManager: peripheral, error: error)
    }

    public func peripheralManager(_ peripheral: CBPeripheralManager, central: CBCentral, didSubscribeTo: CBCharacteristic) {
        delegate.didSubscribe(central, toCharacteristic: didSubscribeTo, peripheralManager: peripheral)
    }

    public func peripheralManager(_ peripheral: CBPeripheralManager, central: CBCentral, didUnsubscribeFrom: CBCharacteristic) {
        delegate.didUnsubscribe(central, fromCharacteristic: didUnsubscribeFrom, peripheralManager: peripheral)
    }

    public func peripheralManager(_ peripheral: CBPeripheralManager, didReceiveRead: CBATTRequest) {
        delegate.didReceiveRead(didReceiveRead, peripheralManager: peripheral)
    }

    public func peripheralManager(_ peripheral: CBPeripheralManager, didReceiveWrite: [CBATTRequest]) {
        delegate.didReceiveWrite(didReceiveWrite, peripheralManager: peripheral)
    }

    public func peripheralManagerDidStartAdvertising(_ peripheral: CBPeripheralManager, error: (any Error)?) {
        delegate.didStartAdvertising(peripheral, error: error)
    }

    public func peripheralManagerIsReady(_ peripheral: CBPeripheralManager) {
        delegate.isReady(peripheral)
    }

    @objc public func unlink() {
        peripheralManager.delegate = nil
    }
}
