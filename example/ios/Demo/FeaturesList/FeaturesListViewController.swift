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
import KotlinNativeFramework

class FeaturesListViewController : UITableViewController {
    
    private lazy var viewModel: FeatureListViewModel = KNArchitectureFramework().createFeatureListViewModel(parent: self)
    private var lifecycleManager: LifecycleManager!

    private var features = [String]()
    private var onSelected: ((KotlinInt) -> KotlinUnit)? = nil
    
    deinit {
        lifecycleManager.unbind()
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        lifecycleManager = KNArchitectureFramework().bind(viewModel: viewModel, to: self) { [weak self] in
            guard let viewModel = self?.viewModel else { return [] }
            return [viewModel.observeFeatures() { (features: [String], onSelected: @escaping (KotlinInt) -> KotlinUnit) in
                self?.features = features
                self?.onSelected = onSelected
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
        let _ = onSelected?(KotlinInt.init(int: Int32(indexPath.row)))
        tableView.deselectRow(at: indexPath, animated: true)
    }
    
}

class FeaturesListCell : UITableViewCell {
    
    struct Const {
        static let identifier = "FeaturesListCell"
    }
    
    @IBOutlet weak var label: UILabel!
    
}
