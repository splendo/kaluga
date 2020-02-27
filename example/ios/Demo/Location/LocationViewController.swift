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

class LocationViewController: UIViewController {

    //MARK: Properties

    @IBOutlet weak var label: UILabel!

    override func viewDidLoad() {
        super.viewDidLoad()

        label.text = KotlinNativeFramework().hello()

        // show how dependencies are also exposed if we declare them in a method
        KotlinNativeFramework().logger().log(level: Hydra_logLogLevel.debug, tag: "hidra", message: "hi")
        KotlinNativeFramework().location(label: label, locationManager: CLLocationManager())
    }
}
