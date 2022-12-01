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
import KalugaExampleShared

class DateTimePickerViewController : UIViewController {

    struct Const {
        static let storyboard = UIStoryboard(name: "Main", bundle: nil)
        static let storyboardId = "DateTimePicker"
    }

    static func instantiate() -> DateTimePickerViewController {
        Const.storyboard.instantiateViewController(withIdentifier: Const.storyboardId) as! DateTimePickerViewController
    }


    @IBOutlet private var currentTimeLabel: UILabel!
    @IBOutlet private var timeLabel: UILabel!
    @IBOutlet private var dateButton: UIButton!
    @IBOutlet private var timeButton: UIButton!
    
    lazy var viewModel = DateTimePickerViewModel(dateTimePickerPresenterBuilder: DateTimePickerPresenter.Builder(viewController: self))
    private var lifecycleManager: LifecycleManager!

    deinit {
        lifecycleManager.unbind()
    }

    override func viewDidLoad() {
        super.viewDidLoad()

        title = "feature_date_time_picker".localized()

        lifecycleManager = viewModel.addLifecycleManager(parent: self) { [weak self] in
            guard let viewModel = self?.viewModel else {
                return []
            }
            return [
                viewModel.dateLabel.observe { (time) in
                    self?.timeLabel.text = time as? String
                }
            ]
        }

        currentTimeLabel.text = viewModel.currentTimeTitle
        ButtonStyleKt.bindButton(dateButton, button: viewModel.selectDateButton)
        ButtonStyleKt.bindButton(timeButton, button: viewModel.selectTimeButton)
    }
}
