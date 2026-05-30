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
import KalugaExample

/// Hosts the Compose Multiplatform root from `:shared`. Mobile-only `FeatureContribution`s
/// (registered in `:mobileshared`'s `mobileSharedContributionsModule` with `isCompose = false`)
/// arrive here as their string id via `onNativeLaunch`; we route those ids to the corresponding
/// SwiftUI screen (wrapped in `UIHostingController`) or programmatic UIKit controller.
class ExampleViewController: UIViewController {

    override func viewDidLoad() {
        super.viewDidLoad()

        let host = MainViewControllerKt.MainViewController { [weak self] id in
            self?.launchMobileFeature(id: id)
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

    private func launchMobileFeature(id: String) {
        let target: UIViewController
        switch id {
        case MobileFeatureIds.shared.ALERTS: target = UIHostingController(rootView: AlertsView())
        case MobileFeatureIds.shared.ARCHITECTURE: target = UIHostingController(rootView: ArchitectureView())
        case MobileFeatureIds.shared.BEACONS: target = BeaconsViewController()
        case MobileFeatureIds.shared.DATE_TIME_PICKER: target = UIHostingController(rootView: DateTimePickerView())
        case MobileFeatureIds.shared.KEYBOARD: target = UIHostingController(rootView: KeyboardManagerView())
        case MobileFeatureIds.shared.HUD: target = UIHostingController(rootView: LoadingView())
        case MobileFeatureIds.shared.MEDIA: target = MediaListViewController()
        case MobileFeatureIds.shared.RESOURCES: target = UIHostingController(rootView: ResourcesListView())
        default:
            assertionFailure("Feature \(id) is supposed to be handled inside CMP")
            return
        }
        navigationController?.pushViewController(target, animated: true)
    }
}
