//
//  Copyright 2026 Splendo Consulting B.V. The Netherlands
//
//    Licensed under the Apache License, Version 2.0 (the "License");
//    you may not use this file except in compliance with the License.
//    You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
//    Unless required by applicable law or agreed to in writing, software
//    distributed under the License is distributed on an "AS IS" BASIS,
//    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//    See the License for the specific language governing permissions and
//    limitations under the License.
//

import UIKit
import SwiftUI
import KalugaExampleShared
import KalugaMobileShared

/// Hosts the Compose Multiplatform root from `:shared`. Features that are not yet migrated to CMP
/// (Alerts, Architecture, Beacons, DateTimePicker, Keyboard, LoadingIndicator, Media, Resources)
/// arrive via `onUnmigratedFeatureSelected` and are launched as SwiftUI screens (wrapped in
/// `UIHostingController`) or — for Beacons and Media — programmatic UIKit screens from
/// `:mobileshared`.
class ExampleViewController: UIViewController {

    override func viewDidLoad() {
        super.viewDidLoad()

        let host = MainViewControllerKt.MainViewController { [weak self] feature in
            self?.launchMobileFeature(feature)
        }
        addChild(host)
        host.view.translatesAutoresizingMaskIntoConstraints = false
        view.addSubview(host.view)
        NSLayoutConstraint.activate([
            host.view.leadingAnchor.constraint(equalTo: view.leadingAnchor),
            host.view.trailingAnchor.constraint(equalTo: view.trailingAnchor),
            host.view.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor),
            host.view.bottomAnchor.constraint(equalTo: view.bottomAnchor),
        ])
        host.didMove(toParent: self)
    }

    private func launchMobileFeature(_ feature: Feature) {
        let target: UIViewController
        switch feature {
        case .alerts: target = UIHostingController(rootView: AlertsView())
        case .architecture: target = UIHostingController(rootView: ArchitectureView())
        case .beacons: target = BeaconsViewController()
        case .datetimepicker: target = UIHostingController(rootView: DateTimePickerView())
        case .keyboard: target = UIHostingController(rootView: KeyboardManagerView())
        case .loadingindicator: target = UIHostingController(rootView: LoadingView())
        case .media: target = MediaListViewController()
        case .resources: target = UIHostingController(rootView: ResourcesListView())
        default:
            assertionFailure("Feature \(feature.name) is supposed to be handled inside CMP")
            return
        }
        navigationController?.pushViewController(target, animated: true)
    }
}
