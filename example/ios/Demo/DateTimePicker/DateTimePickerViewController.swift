/*
 Copyright 2021 Splendo Consulting B.V. The Netherlands

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

class DateTimePickerViewController : UIViewController {
    
    @IBOutlet private var timeLabel: UILabel!
    
    lazy var viewModel = DateTimePickerViewModel(dateTimePickerPresenterBuilder: DateTimePickerPresenter.Builder(viewController: self))
    private var lifecycleManager: LifecycleManager!
    
    deinit {
        lifecycleManager.unbind()
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        lifecycleManager = KNArchitectureFramework().bind(viewModel: viewModel, to: self, onLifecycleChanges: { [weak self] in
            guard let viewModel = self?.viewModel else {
                return []
            }
            return [
                viewModel.dateLabel.observe(onNext: { (time) in
                    if let timeString = (time as? String) {
                        self?.timeLabel.text = String(timeString)
                    }
            })
            ]
        })
    }
    
    @IBAction
    func selectDatePressed() {
        viewModel.onSelectDatePressed()
    }
    
    @IBAction
    func selectTimePressed() {
        viewModel.onSelectTimePressed()
    }
}
