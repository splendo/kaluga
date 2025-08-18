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
import CoreLocation

@objc(KalugaLocationPermissionDelegate)
public protocol KalugaLocationPermissionDelegate {
    @objc func didChangeAuthorization(forLocationManager manager: CLLocationManager)
}

@objc(KalugaLocationPermissionWrapper)
public class KalugaLocationPermissionWrapper : NSObject, CLLocationManagerDelegate {
    
    @objc public static func createByLinking(locationManager: CLLocationManager, to delegate: KalugaLocationPermissionDelegate) -> KalugaLocationPermissionWrapper {
        let wrapper = KalugaLocationPermissionWrapper(delegate: delegate) {
            locationManager.delegate = nil
        }
        locationManager.delegate = wrapper
        return wrapper
    }
    
    @objc public init(delegate: KalugaLocationPermissionDelegate, unlinkAction: @escaping () -> Void) {
        self.delegate = delegate
        self.unlinkAction = unlinkAction
    }
    
    let delegate: KalugaLocationPermissionDelegate
    var unlinkAction: (() -> Void)?
    
    public func locationManagerDidChangeAuthorization(_ manager: CLLocationManager) {
        delegate.didChangeAuthorization(forLocationManager: manager)
    }
    
    @objc public func unlink() {
        if let unlinkAction = unlinkAction {
            unlinkAction()
        }
        unlinkAction = nil
    }
}
