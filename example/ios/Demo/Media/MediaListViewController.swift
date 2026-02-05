//
//  Copyright 2025 Splendo Consulting B.V. The Netherlands
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

class MediaListViewController: UITableViewController {

    private lazy var navigator: ViewControllerNavigator<MediaListNavigationAction> = ViewControllerNavigator(parentVC: self) { action in
        NavigationSpec.Segue(identifier: action.segueKey)
    }
    
    private lazy var viewModel = MediaListViewModel(navigator: navigator)
    private var lifecycleManager: LifecycleManager!

    private var media = [String]()
    private var onSelected: ((Int) -> Void)?

    deinit {
        lifecycleManager.unbind()
    }

    override func viewDidLoad() {
        super.viewDidLoad()

        title = "feature_media".localized()

        lifecycleManager = viewModel.addLifecycleManager(parent: self) { [weak self] in
            guard let viewModel = self?.viewModel else { return [] }
            return [
                viewModel.media.observeInitialized { next in
                    let media = next ?? []
                    self?.media = media.map { ($0 as? Media)?.title ?? "" }
                    self?.onSelected = { (index: Int) in
                        if let media = media[index] as? Media {
                            viewModel.onMediaSelected(media: media)
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
        return media.count
    }

    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        return tableView.dequeueTypedReusableCell(withIdentifier: MediaListCell.Const.identifier, for: indexPath) { (cell: MediaListCell) in
            cell.label.text = media[indexPath.row]
        }
    }

    override func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        _ = onSelected?(indexPath.row)
        tableView.deselectRow(at: indexPath, animated: true)
    }
}

class MediaListCell: UITableViewCell {
    
    enum Const {
        static let identifier = "MediaListCell"
    }
    
    @IBOutlet weak var label: UILabel!
}

private extension MediaListNavigationAction {
    var segueKey: String {
        switch self {
        case is MediaListNavigationAction.Media: return "showMedia"
        case is MediaListNavigationAction.Sound: return "showSound"
        default: return ""
        }
    }
}
