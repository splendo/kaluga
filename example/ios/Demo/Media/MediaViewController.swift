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
import KalugaMobileShared
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

    private let selectMediaButton = UIButton(type: .system)
    private let volumeButton = UIButton(type: .system)
    private let loadingIndicator = UIActivityIndicatorView(style: .medium)
    private let playerView = PlayerView()
    private var playerAspectRatio: NSLayoutConstraint!
    private let containerView = UIView()
    private let currentPlayTimeLabel = UILabel()
    private let playtimeProgress = UISlider()
    private let totalDurationLabel = UILabel()
    private let playButton = UIButton(type: .system)
    private let pauseButton = UIButton(type: .system)
    private let stopButton = UIButton(type: .system)
    private let loopButton = UIButton(type: .system)
    private let rateButton = UIButton(type: .system)

    deinit { lifecycleManager.unbind() }

    override func loadView() {
        let root = UIView()
        root.backgroundColor = .systemBackground

        currentPlayTimeLabel.font = .preferredFont(forTextStyle: .caption1)
        totalDurationLabel.font = .preferredFont(forTextStyle: .caption1)
        totalDurationLabel.textAlignment = .right

        playerView.translatesAutoresizingMaskIntoConstraints = false
        playerAspectRatio = playerView.widthAnchor.constraint(equalTo: playerView.heightAnchor, multiplier: 1.0)
        playerAspectRatio.isActive = true

        let timeRow = UIStackView(arrangedSubviews: [currentPlayTimeLabel, totalDurationLabel])
        timeRow.axis = .horizontal
        timeRow.distribution = .fillEqually

        let transportRow = UIStackView(arrangedSubviews: [playButton, pauseButton, stopButton])
        transportRow.axis = .horizontal
        transportRow.distribution = .fillEqually
        transportRow.spacing = 8

        let modifierRow = UIStackView(arrangedSubviews: [loopButton, rateButton, volumeButton])
        modifierRow.axis = .horizontal
        modifierRow.distribution = .fillEqually
        modifierRow.spacing = 8

        let containerStack = UIStackView(arrangedSubviews: [timeRow, playtimeProgress, transportRow, modifierRow])
        containerStack.axis = .vertical
        containerStack.spacing = 12
        containerStack.translatesAutoresizingMaskIntoConstraints = false
        containerView.addSubview(containerStack)
        NSLayoutConstraint.activate([
            containerStack.leadingAnchor.constraint(equalTo: containerView.leadingAnchor),
            containerStack.trailingAnchor.constraint(equalTo: containerView.trailingAnchor),
            containerStack.topAnchor.constraint(equalTo: containerView.topAnchor),
            containerStack.bottomAnchor.constraint(equalTo: containerView.bottomAnchor),
        ])

        let rootStack = UIStackView(arrangedSubviews: [selectMediaButton, playerView, loadingIndicator, containerView])
        rootStack.axis = .vertical
        rootStack.spacing = 16
        rootStack.translatesAutoresizingMaskIntoConstraints = false
        root.addSubview(rootStack)
        NSLayoutConstraint.activate([
            rootStack.leadingAnchor.constraint(equalTo: root.layoutMarginsGuide.leadingAnchor),
            rootStack.trailingAnchor.constraint(equalTo: root.layoutMarginsGuide.trailingAnchor),
            rootStack.topAnchor.constraint(equalTo: root.layoutMarginsGuide.topAnchor),
            rootStack.bottomAnchor.constraint(lessThanOrEqualTo: root.layoutMarginsGuide.bottomAnchor),
        ])
        view = root
    }

    override func viewDidLoad() {
        super.viewDidLoad()
        title = "feature_media".localized()

        playtimeProgress.minimumValue = 0.0
        playtimeProgress.maximumValue = 1.0
        playtimeProgress.addTarget(self, action: #selector(sliderValueChanged), for: .valueChanged)

        ButtonStyleKt.bindButton(selectMediaButton, button: viewModel.selectMediaButton)

        lifecycleManager = viewModel.addLifecycleManager(parent: self) { [unowned self] in
            [
                self.viewModel.hasControls.observe { hasControls in
                    self.containerView.isHidden = !(hasControls?.boolValue ?? false)
                },
                self.viewModel.isPreparing.observe { isPreparing in
                    let isPreparing = isPreparing?.boolValue ?? false
                    self.loadingIndicator.isHidden = !isPreparing
                    if isPreparing { self.loadingIndicator.startAnimating() } else { self.loadingIndicator.stopAnimating() }
                },
                self.viewModel.isShowingVideo.observe { isShowingVideo in
                    let isShowingVideo = isShowingVideo?.boolValue ?? false
                    self.playerView.isHidden = !isShowingVideo
                    self.mediaSurfaceProvider.update(value: isShowingVideo ? self.playerView : nil)
                },
                self.viewModel.resolution.observe { resolution in
                    let resolution = resolution ?? Resolution.companion.ZERO
                    self.playerAspectRatio.isActive = false
                    var ratio = CGFloat(1.0)
                    if resolution.height != 0 {
                        ratio = CGFloat(Float(resolution.width) / Float(resolution.height))
                    }
                    self.playerAspectRatio = self.playerView.widthAnchor.constraint(equalTo: self.playerView.heightAnchor, multiplier: ratio)
                    self.playerAspectRatio.isActive = true
                },
                self.viewModel.currentPlaytime.observe { currentPlayTime in
                    self.currentPlayTimeLabel.text = currentPlayTime as? String
                },
                self.viewModel.totalDuration.observe { totalDuration in
                    self.totalDurationLabel.text = totalDuration as? String
                },
                self.viewModel.progress.observe { progress in
                    self.playtimeProgress.value = progress?.floatValue ?? 0.0
                },
                self.viewModel.playButton.observe { button in
                    if let button { ButtonStyleKt.bindButton(self.playButton, button: button) }
                },
                self.viewModel.pauseButton.observe { button in
                    if let button { ButtonStyleKt.bindButton(self.pauseButton, button: button) }
                },
                self.viewModel.stopButton.observe { button in
                    if let button { ButtonStyleKt.bindButton(self.stopButton, button: button) }
                },
                self.viewModel.loopButton.observe { button in
                    if let button { ButtonStyleKt.bindButton(self.loopButton, button: button) }
                },
                self.viewModel.rateButton.observe { button in
                    if let button { ButtonStyleKt.bindButton(self.rateButton, button: button) }
                },
                self.viewModel.volumeButton.observe { button in
                    if let button { ButtonStyleKt.bindButton(self.volumeButton, button: button) }
                },
            ]
        }
    }

    @objc private func sliderValueChanged() {
        viewModel.seekTo(progress: Double(playtimeProgress.value))
    }
}

extension MediaViewController: MPMediaPickerControllerDelegate {

    func mediaPicker(_ mediaPicker: MPMediaPickerController, didPickMediaItems mediaItemCollection: MPMediaItemCollection) {
        if !mediaItemCollection.items.isEmpty {
            let item = mediaItemCollection.items[0]
            if let url = item.value(forProperty: MPMediaItemPropertyAssetURL) as? NSURL {
                viewModel.didSelectFileAt(source: MediaSource.URL(url: url as URL, options: [MediaSource.URLOptionPreferPreciseDurationAndTiming(isPreferred: true)]))
            } else {
                viewModel.didSelectFileAt(source: nil)
            }
        } else {
            viewModel.didSelectFileAt(source: nil)
        }
    }
}
