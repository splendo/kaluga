//
//  Copyright 2021 Splendo Consulting B.V. The Netherlands
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
import KotlinNativeFramework

class ResourcesListViewController : UITableViewController {
    
    private lazy var viewModel: ResourcesListViewModel = KNArchitectureFramework().createResourcesViewModel(parent: self)
    private var lifecycleManager: LifecycleManager!

    private var resources = [String]()
    private var onSelected: ((KotlinInt) -> KotlinUnit)? = nil
    
    deinit {
        lifecycleManager.unbind()
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        lifecycleManager = KNArchitectureFramework().bind(viewModel: viewModel, to: self) { [weak self] in
            guard let viewModel = self?.viewModel else { return [] }
            return [viewModel.observeResources() { (resources: [String], onSelected: @escaping (KotlinInt) -> KotlinUnit) in
                self?.resources = resources
                self?.onSelected = onSelected
                self?.tableView.reloadData()
            }]
        }
    }
    
    override func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    
    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return resources.count
    }
    
    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: ResourcesListCell.Const.identifier, for: indexPath) as! ResourcesListCell
        cell.label.text = resources[indexPath.row]
        return cell
    }
    
    override func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let _ = onSelected?(KotlinInt.init(int: Int32(indexPath.row)))
        tableView.deselectRow(at: indexPath, animated: true)
    }
    
}

class ResourcesListCell : UITableViewCell {
    
    struct Const {
        static let identifier = "ResourcesListCell"
    }
    
    @IBOutlet weak var label: UILabel!
    
}
