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

@objc(KalugaBluetoothEnabledDelegate)
public protocol KalugaBluetoothEnabledDelegate {
    @objc func didUpdateState(_ centralManager: CBCentralManager)
}

@objc(KalugaBluetoothScanningDelegate)
public protocol KalugaBluetoothScanningDelegate : KalugaBluetoothEnabledDelegate {
    @objc func didDiscoverPeripheral(_ peripheral: CBPeripheral, forCentralManager centralManager: CBCentralManager, advertisementData: [String : Any], rssi RSSI: NSNumber)
    @objc func didConnectPeripheral(_ peripheral: CBPeripheral, forCentralManager centralManager: CBCentralManager)
    @objc func didDisconnectPeripheral(_ peripheral: CBPeripheral, withError error: (any Error)?, forCentralManager centralManager: CBCentralManager)
    @objc func didFailToConnectPeripheral(_ peripheral: CBPeripheral, withError error: (any Error)?, forCentralManager centralManager: CBCentralManager)
}

@objc(KalugaBluetoothWrapper)
public class KalugaBluetoothWrapper : NSObject, CBCentralManagerDelegate {
    
    @objc public static func createByLinking(centralManager: CBCentralManager, to delegate: KalugaBluetoothEnabledDelegate) -> KalugaBluetoothWrapper {
        let wrapper = KalugaBluetoothWrapper(delegate: delegate) {
            centralManager.delegate = nil
        }
        centralManager.delegate = wrapper
        return wrapper
    }
    
    @objc public init(delegate: KalugaBluetoothEnabledDelegate, unlinkAction: @escaping () -> Void) {
        self.enabledDelegate = delegate
        self.scanningDelegate = delegate as? KalugaBluetoothScanningDelegate
        self.unlinkAction = unlinkAction
    }
    
    let enabledDelegate: KalugaBluetoothEnabledDelegate
    let scanningDelegate: KalugaBluetoothScanningDelegate?
    var unlinkAction: (() -> Void)?
    
    public func centralManagerDidUpdateState(_ central: CBCentralManager) {
        enabledDelegate.didUpdateState(central)
    }

    public func centralManager(_ central: CBCentralManager, didDiscover peripheral: CBPeripheral, advertisementData: [String : Any], rssi RSSI: NSNumber) {
        scanningDelegate?.didDiscoverPeripheral(peripheral, forCentralManager: central, advertisementData: advertisementData, rssi: RSSI)
    }

    public func centralManager(_ central: CBCentralManager, didConnect peripheral: CBPeripheral) {
        scanningDelegate?.didConnectPeripheral(peripheral, forCentralManager: central)
    }

    public func centralManager(_ central: CBCentralManager, didFailToConnect peripheral: CBPeripheral, error: (any Error)?) {
        scanningDelegate?.didFailToConnectPeripheral(peripheral, withError: error, forCentralManager: central)
    }

    public func centralManager(_ central: CBCentralManager, didDisconnectPeripheral peripheral: CBPeripheral, error: (any Error)?) {
        scanningDelegate?.didDisconnectPeripheral(peripheral, withError: error, forCentralManager: central)
    }
    
    @objc public func unlink() {
        if let unlinkAction = unlinkAction {
            unlinkAction()
        }
        unlinkAction = nil
    }
}

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
