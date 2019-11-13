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

import UIKit
import CoreLocation
import KotlinNativeFramework

class BeaconsViewController: UIViewController {

    @IBOutlet weak var label: UILabel!
    
private let locationManager = CLLocationManager()
    private let beaconMonitor = KotlinNativeFramework().beaconMonitor(locationManager: CLLocationManager())
    
    override func viewWillAppear(_ animated: Bool) {
        locationManager.requestWhenInUseAuthorization()
        label.text = "Beacon status: unknown"
        beaconMonitor
            .subscribe(beaconId: "f7826da6-4fa2-4e98-8024-bc5b71e0893e", listener: { (isPresent: KotlinBoolean) -> Void in
                self.label.text = isPresent.boolValue ? "Beacon status: found" : "Beacon status: not found"
            })
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        beaconMonitor.unsubscribe(beaconId: "f7826da6-4fa2-4e98-8024-bc5b71e0893e")
    }
}
