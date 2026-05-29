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
import KalugaMobileShared

class MediaListViewController: UITableViewController {

    private static let cellIdentifier = "MediaListCell"

    private lazy var navigator: ViewControllerNavigator<MediaListNavigationAction> = ViewControllerNavigator(parentVC: self) { action in
        switch action {
        case is MediaListNavigationAction.Media: return NavigationSpec.Push(animated: true) { MediaViewController() }
        case is MediaListNavigationAction.Sound: return NavigationSpec.Push(animated: true) { MediaSoundViewController() }
        default: fatalError("Unknown navigation action \(action)")
        }
    }

    private lazy var viewModel = MediaListViewModel(navigator: navigator)
    private var lifecycleManager: LifecycleManager!

    private var media = [String]()
    private var onSelected: ((Int) -> Void)?

    init() { super.init(style: .plain) }

    required init?(coder: NSCoder) {
        fatalError("init(coder:) is unavailable")
    }

    deinit { lifecycleManager.unbind() }

    override func viewDidLoad() {
        super.viewDidLoad()
        title = "feature_media".localized()
        tableView.register(UITableViewCell.self, forCellReuseIdentifier: Self.cellIdentifier)

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
                },
            ]
        }
    }

    override func numberOfSections(in tableView: UITableView) -> Int { 1 }

    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int { media.count }

    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: Self.cellIdentifier, for: indexPath)
        cell.textLabel?.text = media[indexPath.row]
        return cell
    }

    override func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        onSelected?(indexPath.row)
        tableView.deselectRow(at: indexPath, animated: true)
    }
}
