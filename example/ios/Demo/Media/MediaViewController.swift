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

/// A view that displays the visual contents of a player object.
class PlayerView: UIView {
    override static var layerClass: AnyClass { AVPlayerLayer.self }
}

class MediaViewController: UIViewController {
    
    private let mediaSurfaceProvider = UIViewMediaSurfaceProvider(initialView: nil)
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
            animated: true) {
        }
        default: fatalError("Unknown action")
        }
    }
    
    private lazy var viewModel = MediaViewModel(
        mediaSurfaceProvider: mediaSurfaceProvider,
        builder: DefaultMediaManager.Builder(settings: DefaultMediaManager.Settings(playInBackground: true, playAfterDeviceUnavailable: true)),
        alertPresenterBuilder: AlertPresenter.Builder(viewController: self),
        navigator: navigator
    )
    private var lifecycleManager: LifecycleManager!
    
    @IBOutlet var selectMediaButton: UIButton!
    @IBOutlet var volumeButton: UIButton!
    @IBOutlet var loadingIndicator: UIActivityIndicatorView!
    @IBOutlet var playerView: PlayerView!
    @IBOutlet var playerAspectRatio: NSLayoutConstraint!
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
                self.viewModel.hasControls.observe { hasControls in
                    let hasControls = hasControls?.boolValue ?? false
                    self.containerView.isHidden = !hasControls
                },
                self.viewModel.isPreparing.observe { isPreparing in
                    let isPreparing = isPreparing?.boolValue ?? false
                    self.loadingIndicator.isHidden = !isPreparing
                    if isPreparing {
                        self.loadingIndicator.startAnimating()
                    } else {
                        self.loadingIndicator.stopAnimating()
                    }
                },
                self.viewModel.isShowingVideo.observe { isShowingVideo in
                    let isShowingVideo = isShowingVideo?.boolValue ?? false
                    self.playerView.isHidden = !isShowingVideo
                    if isShowingVideo {
                        self.mediaSurfaceProvider.update(value: self.playerView)
                    } else {
                        self.mediaSurfaceProvider.update(value: nil)
                    }
                },
                self.viewModel.resolution.observe { resolution in
                    let resolution = resolution ?? Resolution.companion.ZERO
                    self.playerAspectRatio.isActive = false
                    var ratio = CGFloat(1.0)
                    if resolution.height != 0 {
                        ratio = CGFloat(Float(resolution.width) / Float(resolution.height))
                    }
                    self.playerAspectRatio = NSLayoutConstraint(
                        item: self.playerView as Any,
                        attribute: .width,
                        relatedBy: .equal,
                        toItem: self.playerView,
                        attribute: .height,
                        multiplier: ratio,
                        constant: 0
                    )
                    self.playerAspectRatio.isActive = true
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
                },
                self.viewModel.volumeButton.observe { volumeButton in
                    if let volumeButton = volumeButton {
                        ButtonStyleKt.bindButton(self.volumeButton, button: volumeButton)
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
                viewModel.didSelectFileAt(source: MediaSource.URL(url: url as URL, options: [MediaSource.URLOptionsPreferPreciseDurationAndTiming(isPreferred: true)]))
            } else {
                viewModel.didSelectFileAt(source: nil)
            }
        } else {
            viewModel.didSelectFileAt(source: nil)
        }
    }
}
