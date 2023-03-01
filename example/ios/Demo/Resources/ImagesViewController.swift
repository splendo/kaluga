//
//  Copyright 2023 Splendo Consulting B.V. The Netherlands
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

class ImagesViewController: UITableViewController {

    private var viewModel = ImagesViewModel()
    private var lifecycleManager: LifecycleManager!
    
    deinit {
        lifecycleManager.unbind()
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()

        title = "feature_resources_image".localized()
        
        tableView.allowsSelection = false
        lifecycleManager = viewModel.addLifecycleManager(parent: self) { [] }
    }
    
    override func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    
    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return max(viewModel.images.count, viewModel.tintedImages.count)
    }
    
    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        return tableView.dequeueTypedReusableCell(withIdentifier: ImageListCell.Const.identifier, for: indexPath) { (cell: ImageListCell) in
            let index = indexPath.row
            if index < viewModel.images.count {
                cell.plainImage.image = viewModel.images[index]
            } else {
                cell.plainImage.image = nil
            }
            if index < viewModel.tintedImages.count {
                cell.tintedImage.image = viewModel.tintedImages[index].uiImage
            } else {
                cell.tintedImage.image = nil
            }
        }
    }
}

class ImageListCell: UITableViewCell {
    
    enum Const {
        static let identifier = "ImageListCell"
    }
    
    @IBOutlet weak var plainImage: UIImageView!
    @IBOutlet weak var tintedImage: UIImageView!
}
