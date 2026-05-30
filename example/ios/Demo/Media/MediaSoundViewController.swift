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
import KalugaExample

class MediaSoundViewController: UIViewController {

    private lazy var viewModel = MediaSoundViewModel()
    private var lifecycleManager: LifecycleManager!

    private let playStopSoundButton = UIButton(type: .system)
    private let plusBPMButton = UIButton(type: .system)
    private let minusBPMButton = UIButton(type: .system)
    private let soundBPMLabel: UILabel = {
        let label = UILabel()
        label.textAlignment = .center
        label.font = .preferredFont(forTextStyle: .title2)
        return label
    }()

    deinit { lifecycleManager.unbind() }

    override func loadView() {
        let root = UIView()
        root.backgroundColor = .systemBackground

        let bpmRow = UIStackView(arrangedSubviews: [minusBPMButton, soundBPMLabel, plusBPMButton])
        bpmRow.axis = .horizontal
        bpmRow.alignment = .center
        bpmRow.distribution = .equalCentering
        bpmRow.spacing = 16

        let stack = UIStackView(arrangedSubviews: [playStopSoundButton, bpmRow])
        stack.axis = .vertical
        stack.alignment = .fill
        stack.spacing = 24
        stack.translatesAutoresizingMaskIntoConstraints = false
        root.addSubview(stack)

        NSLayoutConstraint.activate([
            stack.leadingAnchor.constraint(equalTo: root.layoutMarginsGuide.leadingAnchor),
            stack.trailingAnchor.constraint(equalTo: root.layoutMarginsGuide.trailingAnchor),
            stack.centerYAnchor.constraint(equalTo: root.centerYAnchor),
            soundBPMLabel.widthAnchor.constraint(greaterThanOrEqualToConstant: 120),
        ])
        view = root
    }

    override func viewDidLoad() {
        super.viewDidLoad()
        title = "feature_media_sound".localized()

        lifecycleManager = viewModel.addLifecycleManager(parent: self) { [unowned self] in
            [
                self.viewModel.playStopSoundButton.observe { button in
                    if let button { ButtonStyleKt.bindButton(self.playStopSoundButton, button: button) }
                },
                self.viewModel.soundBPMLabel.observe { bpm in
                    self.soundBPMLabel.text = bpm as? String
                },
                self.viewModel.minusBPMButton.observe { button in
                    if let button { ButtonStyleKt.bindButton(self.minusBPMButton, button: button) }
                },
                self.viewModel.plusBPMButton.observe { button in
                    if let button { ButtonStyleKt.bindButton(self.plusBPMButton, button: button) }
                },
            ]
        }
    }
}
