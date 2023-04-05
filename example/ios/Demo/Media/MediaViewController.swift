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
import MediaPlayer

class MediaViewController: UIViewController {
    
    private lazy var navigator: ViewControllerNavigator<MediaNavigationAction> = ViewControllerNavigator(parentVC: self) { action in
        switch action {
        case is MediaNavigationAction.SelectLocal: return NavigationSpec.MediaPicker(
            types: [NavigationSpec.MediaPickerType.anyAudio],
            delegate: self,
            settings: NavigationSpec.MediaPickerSettings(
                allowsPickingMultipleItems: false,
                showsCloudItems: true,
                prompt: nil,
                showsItemsWithProtectedAssets: false
            ),
            animated: true,
            completion: nil
        )
        default: fatalError("Unknown action")
        }
    }
    
    private lazy var viewModel = MediaViewModel(
        builder: DefaultMediaManager.Builder(),
        alertPresenterBuilder: AlertPresenter.Builder(viewController: self),
        navigator: navigator
    )
    private var lifecycleManager: LifecycleManager!
    
    @IBOutlet var selectMediaButton: UIButton!
    @IBOutlet var loadingIndicator: UIActivityIndicatorView!
    @IBOutlet var containerView: UIView!
    @IBOutlet var currentPlayTimeLabel: UILabel!
    @IBOutlet var playtimeProgress: UISlider!
    @IBOutlet var totalDurationLabel: UILabel!
    @IBOutlet var playButton: UIButton!
    @IBOutlet var pauseButton: UIButton!
    @IBOutlet var stopButton: UIButton!
    @IBOutlet var loopButton: UIButton!
    @IBOutlet var rateButton: UIButton!

    deinit {
        lifecycleManager.unbind()
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()

        title = "feature_media".localized()
        
        playtimeProgress.minimumValue = 0.0
        playtimeProgress.maximumValue = 1.0
        
        ButtonStyleKt.bindButton(selectMediaButton, button: viewModel.selectMediaButton)
        
        lifecycleManager = viewModel.addLifecycleManager(parent: self) { [unowned self] in
            [
                self.viewModel.isLoaded.observe { isLoaded in
                    let isLoaded = isLoaded?.boolValue ?? false
                    self.loadingIndicator.isHidden = isLoaded
                    self.containerView.isHidden = !isLoaded
                    if isLoaded {
                        self.loadingIndicator.stopAnimating()
                    } else {
                        self.loadingIndicator.startAnimating()
                    }
                },
                self.viewModel.currentPlaytime.observe { currentPlayTime in
                    self.currentPlayTimeLabel.text = currentPlayTime as? String
                },
                self.viewModel.totalDuration.observe { totalDuration in
                    self.totalDurationLabel.text = totalDuration as? String
                },
                self.viewModel.progress.observe { progress in
                    let progress = progress?.floatValue ?? 0.0
                    self.playtimeProgress.value = progress
                },
                self.viewModel.playButton.observe { playButton in
                    if let playButton = playButton {
                        ButtonStyleKt.bindButton(self.playButton, button: playButton)
                    }
                },
                self.viewModel.pauseButton.observe { pauseButton in
                    if let pauseButton = pauseButton {
                        ButtonStyleKt.bindButton(self.pauseButton, button: pauseButton)
                    }
                },
                self.viewModel.stopButton.observe { stopButton in
                    if let stopButton = stopButton {
                        ButtonStyleKt.bindButton(self.stopButton, button: stopButton)
                    }
                },
                self.viewModel.loopButton.observe { loopButton in
                    if let loopButton = loopButton {
                        ButtonStyleKt.bindButton(self.loopButton, button: loopButton)
                    }
                },
                self.viewModel.rateButton.observe { rateButton in
                    if let rateButton = rateButton {
                        ButtonStyleKt.bindButton(self.rateButton, button: rateButton)
                    }
                }
            ]
        }
    }
    
    @IBAction func sliderValueChanged(_ sender: Any) {
        viewModel.seekTo(progress: Double(playtimeProgress.value))
    }
}

extension MediaViewController: MPMediaPickerControllerDelegate {
 
    func mediaPicker(_ mediaPicker: MPMediaPickerController, didPickMediaItems mediaItemCollection: MPMediaItemCollection) {
        if !mediaItemCollection.items.isEmpty {
            let item = mediaItemCollection.items[0]
            if let url = item.value(forProperty: MPMediaItemPropertyAssetURL) as? NSURL {
                viewModel.didSelectFileAt(source: MediaSource.URL(url: url as URL))
            } else {
                viewModel.didSelectFileAt(source: nil)
            }
        } else {
            viewModel.didSelectFileAt(source: nil)
        }
    }
}
