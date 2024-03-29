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
import SwiftUI
import KalugaExampleShared

class SwiftUIOrUIKitSelectionViewController: UITableViewController {

    static func create(uiKitViewController: @escaping () -> UIViewController, swiftUIView: @escaping () -> some View) -> SwiftUIOrUIKitSelectionViewController {
        let viewController = MainStoryboard.instantiateSwiftUIOrUIKitSelectionViewController()

        let navigator = ViewControllerNavigator<SwiftUIOrUIKitNavigationAction>(parentVC: viewController) { action in
            switch action {
            case is SwiftUIOrUIKitNavigationAction.SwiftUI: return NavigationSpec.Push(animated: true) {
                UIHostingController(rootView: swiftUIView())
            }
            case is SwiftUIOrUIKitNavigationAction.UIKit: return NavigationSpec.Push(animated: true, push: uiKitViewController)
            default: fatalError("Unknown navigation action \(action)")
            }
        }
        viewController.viewModel = SwiftUIOrUIKitSelectionViewModel(navigator: navigator)
        return viewController
    }

    var viewModel: SwiftUIOrUIKitSelectionViewModel!
    private var lifecycleManager: LifecycleManager!

    private var uiTypes = [String]()
    private var onSelected: ((Int) -> Void)?

    deinit {
        lifecycleManager.unbind()
    }

    override func viewDidLoad() {
        super.viewDidLoad()

        title = "ios_ui_type_selector".localized()

        lifecycleManager = viewModel.addLifecycleManager(parent: self) { [weak self] in
            guard let viewModel = self?.viewModel else { return [] }
            return [
                viewModel.uiTypes.observeInitialized { next in
                    let uiTypes = next ?? []
                    self?.uiTypes = uiTypes.map { ($0 as? UIType)?.title ?? "" }
                    self?.onSelected = { (index: Int) in
                        if let uiType = uiTypes[index] as? UIType {
                            viewModel.onUITypePressed(uiType: uiType)
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
        return uiTypes.count
    }

    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        return tableView.dequeueTypedReusableCell(withIdentifier: UITypesListCell.Const.identifier, for: indexPath) { (cell: UITypesListCell) in
            cell.label.text = uiTypes[indexPath.row]
        }
    }

    override func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        _ = onSelected?(indexPath.row)
        tableView.deselectRow(at: indexPath, animated: true)
    }
}

class UITypesListCell: UITableViewCell {

    enum Const {
        static let identifier = "UITypesListCell"
    }

    @IBOutlet weak var label: UILabel!
}
