//
//  Copyright 2022 Splendo Consulting B.V. The Netherlands
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
import KalugaExampleShared

class ResourcesListViewController: UITableViewController {

    private lazy var navigator: ViewControllerNavigator<ResourcesListNavigationAction> = ViewControllerNavigator(parentVC: self) { action in
        NavigationSpec.Segue(identifier: action.segueKey)
    }
    
    private lazy var viewModel = ResourcesListViewModel(navigator: navigator)
    private var lifecycleManager: LifecycleManager!

    private var resources = [String]()
    private var onSelected: ((Int) -> Void)?

    deinit {
        lifecycleManager.unbind()
    }

    override func viewDidLoad() {
        super.viewDidLoad()

        title = "feature_resources".localized()

        lifecycleManager = viewModel.addLifecycleManager(parent: self) { [weak self] in
            guard let viewModel = self?.viewModel else { return [] }
            return [
                viewModel.resources.observeInitialized { next in
                    let resources = next ?? []
                    self?.resources = resources.map { ($0 as? Resource)?.title ?? "" }
                    self?.onSelected = { (index: Int) in
                        if let resource = resources[index] as? Resource {
                            viewModel.onResourceSelected(resource: resource)
                        }
                    }
                    self?.tableView.reloadData()
                }
            ]
        }
    }

    override func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }

    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return resources.count
    }

    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        return tableView.dequeueTypedReusableCell(withIdentifier: ResourcesListCell.Const.identifier, for: indexPath) { (cell: ResourcesListCell) in
            cell.label.text = resources[indexPath.row]
        }
    }

    override func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        _ = onSelected?(indexPath.row)
        tableView.deselectRow(at: indexPath, animated: true)
    }
}

class ResourcesListCell: UITableViewCell {
    
    enum Const {
        static let identifier = "ResourcesListCell"
    }
    
    @IBOutlet weak var label: UILabel!
}

private extension ResourcesListNavigationAction {
    var segueKey: String {
        switch self {
        case is ResourcesListNavigationAction.Button: return "showButton"
        case is ResourcesListNavigationAction.Color: return "showColor"
        case is ResourcesListNavigationAction.Label: return "showLabel"
        default: return ""
        }
    }
}
