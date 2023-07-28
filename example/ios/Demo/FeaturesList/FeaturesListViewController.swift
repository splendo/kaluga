/*
 Copyright 2022 Splendo Consulting B.V. The Netherlands

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
import SwiftUI
import KalugaExampleShared
import PartialSheet

class FeaturesListViewController: UITableViewController {

    private lazy var navigator: ViewControllerNavigator<FeatureListNavigationAction> = ViewControllerNavigator(parentVC: self) { action in
        action.spec
    }
    private lazy var viewModel = FeatureListViewModel(navigator: navigator)
    private var lifecycleManager: LifecycleManager!

    private var features = [String]()
    private var onSelected: ((Int) -> Void)?

    deinit {
        lifecycleManager.unbind()
    }

    override func viewDidLoad() {
        super.viewDidLoad()

        lifecycleManager = viewModel.addLifecycleManager(parent: self) { [weak self] in
            guard let viewModel = self?.viewModel else { return [] }
            return [viewModel.feature.observeInitialized { next in
                let features = next ?? []
                self?.features = features.map { ($0 as? Feature)?.title ?? "" }
                self?.onSelected = { (index: Int) in
                    if let feature = features[index] as? Feature {
                        viewModel.onFeaturePressed(feature: feature)
                    }
                }
                self?.tableView.reloadData()
            }]
        }
    }

    override func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }

    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return features.count
    }

    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        return tableView.dequeueTypedReusableCell(withIdentifier: FeaturesListCell.Const.identifier, for: indexPath) { (cell: FeaturesListCell) in
            cell.label.text = features[indexPath.row]
        }
    }

    override func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        _ = onSelected?(indexPath.row)
        tableView.deselectRow(at: indexPath, animated: true)
    }
}

class FeaturesListCell: UITableViewCell {
    
    enum Const {
        static let identifier = "FeaturesListCell"
    }
    
    @IBOutlet weak var label: UILabel!
}

private extension FeatureListNavigationAction {
    var spec: NavigationSpec {
        switch self {
        case is FeatureListNavigationAction.Alerts: return NavigationSpec.Push(animated: true) {
            SwiftUIOrUIKitSelectionViewController.create(
                uiKitViewController: { MainStoryboard.instantiateAlertsViewController() },
                swiftUIView: { AlertsView() }
            )
        }
        case is FeatureListNavigationAction.Architecture: return NavigationSpec.Push(animated: true) {
            SwiftUIOrUIKitSelectionViewController.create(
                uiKitViewController: { MainStoryboard.instantiateArchitectureViewController() },
                swiftUIView: { ArchitectureView().attachPartialSheetToRoot() }
            )
        }
        case is FeatureListNavigationAction.Beacons: return NavigationSpec.Segue(identifier: "showBeacons")
        case is FeatureListNavigationAction.Bluetooth: return NavigationSpec.Push(animated: true) {
            UIHostingController(rootView: BluetoothListView())
        }
        case is FeatureListNavigationAction.DateTime: return NavigationSpec.Push(animated: true) {
            UIHostingController(rootView: TimerView())
        }
        case is FeatureListNavigationAction.DateTimePicker: return NavigationSpec.Push(animated: true) {
            SwiftUIOrUIKitSelectionViewController.create(
                uiKitViewController: { MainStoryboard.instantiateDateTimePickerViewController() },
                swiftUIView: { DateTimePickerView() }
            )
        }
        case is FeatureListNavigationAction.Keyboard: return NavigationSpec.Push(animated: true) {
            SwiftUIOrUIKitSelectionViewController.create(
                uiKitViewController: { MainStoryboard.instantiateKeyboardManagerViewController() },
                swiftUIView: { KeyboardManagerView() }
            )
        }
        case is FeatureListNavigationAction.Links: return NavigationSpec.Segue(identifier: "showLinks")
        case is FeatureListNavigationAction.LoadingIndicator: return NavigationSpec.Push(animated: true) {
            SwiftUIOrUIKitSelectionViewController.create(
                uiKitViewController: { MainStoryboard.instantiateLoadingViewController() },
                swiftUIView: { LoadingView() }
            )
        }
        case is FeatureListNavigationAction.Location: return NavigationSpec.Segue(identifier: "showLocation")
        case is FeatureListNavigationAction.Media: return NavigationSpec.Segue(identifier: "showMedia")
        case is FeatureListNavigationAction.Permissions: return NavigationSpec.Segue(identifier: "showPermissions")
        case is FeatureListNavigationAction.PlatformSpecific: return NavigationSpec.Segue(identifier: "showPlatformSpecific")
        case is FeatureListNavigationAction.Resources: return NavigationSpec.Push(animated: true) {
            SwiftUIOrUIKitSelectionViewController.create(
                uiKitViewController: { MainStoryboard.instantiateResourcesListViewController() },
                swiftUIView: { ResourcesListView() }
            )
        }
        case is FeatureListNavigationAction.Scientific: return NavigationSpec.Push(animated: true) {
            UIHostingController(rootView: ScientificView())
        }
        case is FeatureListNavigationAction.System: return NavigationSpec.Segue(identifier: "showSystem")
        default: fatalError("Unknown action")
        }
    }
}
