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

class PermissionListViewController : UITableViewController {

    private lazy var navigator: ViewControllerNavigator<PermissionsListNavigationAction> = ViewControllerNavigator(parentVC: self) { action in
        NavigationSpec.Push(animated: true) {
            guard let permission = action.value?.permission else {
                return UIViewController()
            }

            return PermissionViewController.create(permission: permission)
        }
    }
    private lazy var viewModel: PermissionsListViewModel = PermissionsListViewModel(navigator: navigator)
    private var lifecycleManager: LifecycleManager!

    private var permissions = [PermissionView]()
    private var onSelected: ((Int) -> Void)? = nil

    deinit {
        lifecycleManager.unbind()
    }

    override func viewDidLoad() {
        super.viewDidLoad()

        lifecycleManager = viewModel.addLifecycleManager(parent: self) { [weak self] in
            guard let viewModel = self?.viewModel else { return [] }
            return [
                viewModel.permissions.observeInitialized { next in
                    let permissions = next?.compactMap { $0 as? PermissionView } ?? []
                    self?.permissions = permissions
                    self?.onSelected = { (index: Int) in viewModel.onPermissionPressed(permissionView: permissions[index]) }
                    self?.tableView.reloadData()
                }
            ]
        }
    }

    override func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }

    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return permissions.count
    }

    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: PermissionsListCell.Const.identifier, for: indexPath) as! PermissionsListCell
        cell.label.text = permissions[indexPath.row].title
        return cell
    }

    override func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let _ = onSelected?(indexPath.row)
        tableView.deselectRow(at: indexPath, animated: true)
    }
    
}

class PermissionsListCell : UITableViewCell {
    
    struct Const {
        static let identifier = "PermissionsListCell"
    }
    
    @IBOutlet weak var label: UILabel!
    
}
