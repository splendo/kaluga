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
import KotlinNativeFramework

class LabelViewController : UITableViewController {
    
    private lazy var viewModel: LabelViewModel = KNArchitectureFramework().createLabelViewModel()
    private var lifecycleManager: LifecycleManager!

    private var labels = [KalugaLabel]()
    
    deinit {
        lifecycleManager.unbind()
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        tableView.allowsSelection = false
        lifecycleManager = KNArchitectureFramework().bind(viewModel: viewModel, to: self) { [weak self] in
            guard let viewModel = self?.viewModel else { return [] }
            return [
                viewModel.labels.observe { labels in
                    self?.labels = labels?.compactMap { $0 as?  KalugaLabel } ?? []
                    self?.tableView.reloadData()
                }
            ]
        }
    }
    
    override func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    
    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return labels.count
    }
    
    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: LabelListCell.Const.identifier, for: indexPath) as! LabelListCell
        TextStyleKt.bindLabel(cell.label, label: labels[indexPath.row])
        return cell
    }
}

class LabelListCell : UITableViewCell {
    
    struct Const {
        static let identifier = "LabelListCell"
    }
    
    @IBOutlet weak var label: UILabel!
    
}
