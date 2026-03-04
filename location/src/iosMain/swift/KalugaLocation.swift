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

@objc(KalugaLocationDelegate)
public protocol KalugaLocationDelegate {
    @objc func didUpdateLocations(_ locations: [CLLocation], manager: CLLocationManager)
    @objc func didFinishDeferredUpdates(error: (any Error)?, manager: CLLocationManager)
}

@objc(KalugaLocationWrapper)
public class KalugaLocationWrapper : NSObject, CLLocationManagerDelegate {
    
    @objc public static func createByLinking(locationManager: CLLocationManager, to delegate: KalugaLocationDelegate) -> KalugaLocationWrapper {
        let wrapper = KalugaLocationWrapper(delegate: delegate) {
            locationManager.delegate = nil
        }
        locationManager.delegate = wrapper
        return wrapper
    }
    
    @objc public init(delegate: KalugaLocationDelegate, unlinkAction: @escaping () -> Void) {
        self.delegate = delegate
        self.unlinkAction = unlinkAction
    }
    
    let delegate: KalugaLocationDelegate
    var unlinkAction: (() -> Void)?
    
    public func locationManager(_ manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
        delegate.didUpdateLocations(locations, manager: manager)
    }

    public func locationManager(_ manager: CLLocationManager, didFinishDeferredUpdatesWithError error: (any Error)?) {
        delegate.didFinishDeferredUpdates(error: error, manager: manager)
    }
    
    @objc public func unlink() {
        if let unlinkAction = unlinkAction {
            unlinkAction()
        }
        unlinkAction = nil
    }
}
