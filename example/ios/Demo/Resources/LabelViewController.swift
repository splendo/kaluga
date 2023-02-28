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

class LabelViewController: UITableViewController {

    private var viewModel = LabelViewModel(styledStringBuilderProvider: StyledStringBuilder.Provider())
    private var lifecycleManager: LifecycleManager!

    private var labels = [KalugaLabel]()
    
    deinit {
        lifecycleManager.unbind()
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()

        title = "feature_resources_label".localized()
        
        tableView.allowsSelection = false
        lifecycleManager = viewModel.addLifecycleManager(parent: self) { [weak self] in
            guard let viewModel = self?.viewModel else { return [] }
            return [
                viewModel.labels.observe { labels in
                    self?.labels = labels?.compactMap { $0 as? KalugaLabel } ?? []
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
        return tableView.dequeueTypedReusableCell(withIdentifier: LabelListCell.Const.identifier, for: indexPath) { (cell: LabelListCell) in
            TextStyleKt.bindLabel(cell.label, label: labels[indexPath.row])
        }
    }
}

class LabelListCell: UITableViewCell {
    
    enum Const {
        static let identifier = "LabelListCell"
    }
    
    @IBOutlet weak var label: UILabel!
}
