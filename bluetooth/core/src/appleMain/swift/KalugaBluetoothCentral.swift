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
        self.delegate = delegate
        self.unlinkAction = unlinkAction
    }

    let delegate: KalugaBluetoothEnabledDelegate
    var unlinkAction: (() -> Void)?

    public func centralManagerDidUpdateState(_ central: CBCentralManager) {
        delegate.didUpdateState(central)
    }

    @objc public func unlink() {
        if let unlinkAction = unlinkAction {
            unlinkAction()
        }
        unlinkAction = nil
    }
}
