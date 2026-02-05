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
import AVKit
import KalugaExampleShared
import MediaPlayer

class MediaSoundViewController: UIViewController {
    
    private lazy var viewModel = MediaSoundViewModel()
    private var lifecycleManager: LifecycleManager!
    
    @IBOutlet var containerView: UIView!
    @IBOutlet weak var playStopSoundButton: UIButton!
    @IBOutlet weak var plusBPMButton: UIButton!
    @IBOutlet weak var minusBPMButton: UIButton!
    @IBOutlet weak var soundBPMLabel: UILabel!
    
    deinit {
        lifecycleManager.unbind()
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()

        title = "feature_media_sound".localized()
        
        lifecycleManager = viewModel.addLifecycleManager(parent: self) { [unowned self] in
            [
                self.viewModel.playStopSoundButton.observe { playStopSoundButton in
                    if let playStopSoundButton = playStopSoundButton {
                        ButtonStyleKt.bindButton(self.playStopSoundButton, button: playStopSoundButton)
                    }
                },
                self.viewModel.soundBPMLabel.observe { soundBPM in
                    self.soundBPMLabel.text = soundBPM as? String
                },
                self.viewModel.minusBPMButton.observe { minusBPMButton in
                    if let minusBPMButton = minusBPMButton {
                        ButtonStyleKt.bindButton(self.minusBPMButton, button: minusBPMButton)
                    }
                },
                self.viewModel.plusBPMButton.observe { plusBPMButton in
                    if let plusBPMButton = plusBPMButton {
                        ButtonStyleKt.bindButton(self.plusBPMButton, button: plusBPMButton)
                    }
                }
            ]
        }
    }
}
