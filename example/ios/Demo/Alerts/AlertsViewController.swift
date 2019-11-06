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
import KotlinNativeFramework

class AlertsViewController: UITableViewController {

    lazy var alertsBuilder = AlertsAlertBuilder(viewController: self)

    override func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        tableView.deselectRow(at: indexPath, animated: true)

        switch indexPath.row {
        case 0: showAlert()
        case 1: showWithDismiss()
        default: ()
        }
    }

    fileprivate func showAlert() {
        alertsBuilder.alert { builder in
            builder.setTitle(title: "Hello")
            builder.setMessage(message: "From Kaluga")
            builder.addActions(actions: [
                AlertsAlert.Action(title: "Default", style: .default_) { debugPrint("OK") },
                AlertsAlert.Action(title: "Destructive", style: .destructive) { debugPrint("Not OK") },
                AlertsAlert.Action(title: "Cancel", style: .cancel) { debugPrint("Cancel") },
            ])
        }.show(animated: true) { }
    }

    fileprivate func showWithDismiss() {
        let alert = alertsBuilder.alert { builder in
            builder.setTitle(title: "Wait for 3 sec...")
            builder.setPositiveButton(title: "OK") { }
        }
        alert.show(animated: true) {
            DispatchQueue.main.asyncAfter(deadline: .now() + 3) {
                alert.dismiss(animated: true)
            }
        }
    }
}
