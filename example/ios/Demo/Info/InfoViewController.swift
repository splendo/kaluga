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
import MessageUI
import KalugaExampleShared

class InfoViewController: UITableViewController {

    private lazy var navigator = InfoNavigatorKt.InfoNavigator(
        parent: self,
        onDialogSpec: { dialogSpec in
            NavigationSpec.Present(
                animated: true,
                presentationStyle: Int64(UIModalPresentationStyle.automatic.rawValue),
                transitionStyle: Int64(UIModalTransitionStyle.coverVertical.rawValue)
            ) {
                let alert = UIAlertController.init(title: dialogSpec.title, message: dialogSpec.message, preferredStyle: .alert)
                alert.addAction(UIAlertAction(title: "OK", style: .default))
                return alert
            }
        },
        onLink: { link in NavigationSpec.Browser(url: URL(string: link)!, viewType: NavigationSpec.BrowserTypeNormal()) },
        onMailSpec: { mailSpec in
            let settings = NavigationSpec.Email.EmailEmailSettings(
                type: NavigationSpec.EmailTypePlain(),
                to: mailSpec.to,
                cc: [],
                bcc: [],
                subject: mailSpec.subject,
                body: nil,
                attachments: []
            )
            return NavigationSpec.Email(emailSettings: settings, delegate: nil, animated: true)
        }
    )
    private lazy var viewModel = InfoViewModel(
        reviewManagerBuilder: ReviewManager.Builder(),
        navigator: navigator)

    private var buttons = [String]()
    private var onSelected: ((Int) -> Void)?
    private var lifecycleManager: LifecycleManager!

    deinit {
        lifecycleManager.unbind()
    }

    override func viewDidLoad() {
        super.viewDidLoad()

        lifecycleManager = viewModel.addLifecycleManager(parent: self) { [weak self] in

            guard let viewModel = self?.viewModel else {
                return []
            }

            return [
                viewModel.buttons.observeInitialized { next in
                    let buttons = next?.compactMap { $0 as? InfoViewModel.Button } ?? []
                    self?.buttons = buttons.map { $0.title }
                    self?.onSelected = { (index: Int) in viewModel.onButtonPressed(button: buttons[index]) }
                    self?.tableView.reloadData()
                }
            ]
        }
    }

    override func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }

    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return buttons.count
    }

    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        return tableView.dequeueTypedReusableCell(withIdentifier: InfoButtonCell.Const.identifier, for: indexPath) { (cell: InfoButtonCell) in
            cell.label.text = buttons[indexPath.row]
        }
    }

    override func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        _ = onSelected?(indexPath.row)
        tableView.deselectRow(at: indexPath, animated: true)
    }
}

class InfoButtonCell: UITableViewCell {
    
    enum Const {
        static let identifier = "InfoButtonCell"
    }
    
    @IBOutlet weak var label: UILabel!
}
