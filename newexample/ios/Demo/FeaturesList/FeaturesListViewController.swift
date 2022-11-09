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
import KalugaExampleShared

class FeaturesListViewController : UITableViewController {

    private lazy var navigator: ViewControllerNavigator<FeatureListNavigationAction> = ViewControllerNavigator(parentVC: self) { action in
        NavigationSpec.Segue(identifier: action.segueKey)
    }
    private lazy var viewModel: FeatureListViewModel = FeatureListViewModel(navigator: navigator)
    private var lifecycleManager: LifecycleManager!

    private var features = [String]()
    private var onSelected: ((Int) -> Void)? = nil

    deinit {
        lifecycleManager.unbind()
    }

    override func viewDidLoad() {
        super.viewDidLoad()

        lifecycleManager = viewModel.addLifecycleManager(parent: self) { [weak self] in
            guard let viewModel = self?.viewModel else { return [] }
            return [viewModel.feature.observeInitialized { next in
                let features = next ?? []
                self?.features = features.map { ($0 as! Feature).title }
                self?.onSelected = { (index: Int) in
                    viewModel.onFeaturePressed(feature: features[index] as! Feature)
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
        let cell = tableView.dequeueReusableCell(withIdentifier: FeaturesListCell.Const.identifier, for: indexPath) as! FeaturesListCell
        cell.label.text = features[indexPath.row]
        return cell
    }

    override func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let _ = onSelected?(indexPath.row)
        tableView.deselectRow(at: indexPath, animated: true)
    }
    
}

class FeaturesListCell : UITableViewCell {
    
    struct Const {
        static let identifier = "FeaturesListCell"
    }
    
    @IBOutlet weak var label: UILabel!
    
}

private extension FeatureListNavigationAction {
    var segueKey: String {
        get {
            switch self {
            case is FeatureListNavigationAction.Alerts: return "showAlerts"
            case is FeatureListNavigationAction.Architecture: return "showArchitecture"
            case is FeatureListNavigationAction.Beacons: return "showBeacons"
            case is FeatureListNavigationAction.Bluetooth: return "showBluetooth"
            case is FeatureListNavigationAction.DateTimePicker: return "showDateTimePicker"
            case is FeatureListNavigationAction.Keyboard: return "showKeyboard"
            case is FeatureListNavigationAction.Links: return "showLinks"
            case is FeatureListNavigationAction.LoadingIndicator: return "showHUD"
            case is FeatureListNavigationAction.Location: return "showLocation"
            case is FeatureListNavigationAction.Permissions: return "showPermissions"
            case is FeatureListNavigationAction.PlatformSpecific: return "showPlatformSpecific"
            case is FeatureListNavigationAction.Resources: return "showResources"
            case is FeatureListNavigationAction.System: return "showSystem"
            default: return ""
            }
        }
    }
}
