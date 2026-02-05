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
import KalugaExampleShared

class ColorViewController: UIViewController {
    
    @IBOutlet var backdropColorBackground: UIView!
    @IBOutlet var sourceColorBackground: UIView!
    @IBOutlet var blendedColorBackground: UIView!
    
    @IBOutlet var backdropInputField: UITextField!
    @IBOutlet var sourceInputField: UITextField!
    
    @IBOutlet var blendModeButton: UIButton!
    @IBOutlet var flipButton: UIButton!
    
    @IBOutlet var backdropLightenedColorsCollectionView: UICollectionView!
    @IBOutlet var backdropDarkenedColorsCollectionView: UICollectionView!
    @IBOutlet var sourceLightenedColorsCollectionView: UICollectionView!
    @IBOutlet var sourceDarkenedColorsCollectionView: UICollectionView!
    @IBOutlet var blendedLightenedColorsCollectionView: UICollectionView!
    @IBOutlet var blendedDarkenedColorsCollectionView: UICollectionView!
    
    @IBOutlet var currentModeLabel: UILabel!
    @IBOutlet var currentModeColorsCollectionView: UICollectionView!
    @IBOutlet var lightModeLabel: UILabel!
    @IBOutlet var lightModeColorsCollectionView: UICollectionView!
    @IBOutlet var darkModeLabel: UILabel!
    @IBOutlet var darkModeColorsCollectionView: UICollectionView!
    
    private var backdropLightenedColors: [KalugaBackgroundStyle] = []
    private var backdropDarkenedColors: [KalugaBackgroundStyle] = []
    private var sourceLightenedColors: [KalugaBackgroundStyle] = []
    private var sourceDarkenedColors: [KalugaBackgroundStyle] = []
    private var blendedLightenedColors: [KalugaBackgroundStyle] = []
    private var blendedDarkenedColors: [KalugaBackgroundStyle] = []
    
    private lazy var viewModel = ColorViewModel(alertPresenterBuilder: AlertPresenter.Builder(viewController: self))
    private var lifecycleManager: LifecycleManager!

    deinit {
        lifecycleManager.unbind()
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()

        title = "feature_resources_color".localized()
        currentModeLabel.text = "resources_color_current_mode".localized()
        lightModeLabel.text = "resources_color_light_mode".localized()
        darkModeLabel.text = "resources_color_dark_mode".localized()

        backdropLightenedColorsCollectionView.register(ColorCollectionCellView.self, forCellWithReuseIdentifier: ColorCollectionCellView.Const.identifier)
        backdropDarkenedColorsCollectionView.register(ColorCollectionCellView.self, forCellWithReuseIdentifier: ColorCollectionCellView.Const.identifier)
        sourceLightenedColorsCollectionView.register(ColorCollectionCellView.self, forCellWithReuseIdentifier: ColorCollectionCellView.Const.identifier)
        sourceDarkenedColorsCollectionView.register(ColorCollectionCellView.self, forCellWithReuseIdentifier: ColorCollectionCellView.Const.identifier)
        blendedLightenedColorsCollectionView.register(ColorCollectionCellView.self, forCellWithReuseIdentifier: ColorCollectionCellView.Const.identifier)
        blendedDarkenedColorsCollectionView.register(ColorCollectionCellView.self, forCellWithReuseIdentifier: ColorCollectionCellView.Const.identifier)
        
        currentModeColorsCollectionView.register(ColorCollectionCellView.self, forCellWithReuseIdentifier: ColorCollectionCellView.Const.identifier)
        lightModeColorsCollectionView.register(ColorCollectionCellView.self, forCellWithReuseIdentifier: ColorCollectionCellView.Const.identifier)
        darkModeColorsCollectionView.register(ColorCollectionCellView.self, forCellWithReuseIdentifier: ColorCollectionCellView.Const.identifier)
        
        lifecycleManager = viewModel.addLifecycleManager(parent: self) { [weak self] in
            guard let viewModel = self?.viewModel else { return [] }
            
            return [
                viewModel.backdropColorBackground.observe { next in
                    guard let backdropColorBackground = self?.backdropColorBackground, let backdropBackgroundStyle = next else {
                        return
                    }
                    BackgroundStyleKt.applyBackgroundStyle(backdropColorBackground, style: backdropBackgroundStyle)
                },
                viewModel.sourceColorBackground.observe { next in
                    guard let sourceColorBackground = self?.sourceColorBackground, let sourceBackgroundStyle = next else {
                        return
                    }
                    BackgroundStyleKt.applyBackgroundStyle(sourceColorBackground, style: sourceBackgroundStyle)
                },
                viewModel.blendedColorBackground.observe { next in
                    guard let blendedColorBackground = self?.blendedColorBackground, let blendedBackgroundStyle = next else {
                        return
                    }
                    BackgroundStyleKt.applyBackgroundStyle(blendedColorBackground, style: blendedBackgroundStyle)
                },
                viewModel.backdropText.observe { text in
                    self?.backdropInputField.text = text as String?
                },
                viewModel.sourceText.observe { text in
                    self?.sourceInputField.text = text as String?
                },
                viewModel.blendModeButton.observe { next in
                    guard let blendModeButton = self?.blendModeButton, let buttonStyle = next else {
                        return
                    }
                    ButtonStyleKt.bindButton(blendModeButton, button: buttonStyle)
                },
                viewModel.lightenBackdrops.observe { next in
                    self?.backdropLightenedColors = next?.compactMap { $0 as? KalugaBackgroundStyle } ?? []
                    self?.backdropLightenedColorsCollectionView?.reloadData()
                },
                viewModel.darkenBackdrops.observe { next in
                    self?.backdropDarkenedColors = next?.compactMap { $0 as? KalugaBackgroundStyle } ?? []
                    self?.backdropDarkenedColorsCollectionView?.reloadData()
                },
                viewModel.lightenSource.observe { next in
                    self?.sourceLightenedColors = next?.compactMap { $0 as? KalugaBackgroundStyle } ?? []
                    self?.sourceLightenedColorsCollectionView?.reloadData()
                },
                viewModel.darkenSource.observe { next in
                    self?.sourceDarkenedColors = next?.compactMap { $0 as? KalugaBackgroundStyle } ?? []
                    self?.sourceDarkenedColorsCollectionView?.reloadData()
                },
                viewModel.lightenBlended.observe { next in
                    self?.blendedLightenedColors = next?.compactMap { $0 as? KalugaBackgroundStyle } ?? []
                    self?.blendedLightenedColorsCollectionView?.reloadData()
                },
                viewModel.darkenBlended.observe { next in
                    self?.blendedDarkenedColors = next?.compactMap { $0 as? KalugaBackgroundStyle } ?? []
                    self?.blendedDarkenedColorsCollectionView?.reloadData()
                }
            ]
        }
        
        ButtonStyleKt.bindButton(flipButton, button: viewModel.flipButton)
    }
}

extension ColorViewController: UITextFieldDelegate {
    func textFieldDidEndEditing(_ textField: UITextField) {
        if textField == backdropInputField {
            viewModel.submitBackdropText(backdropText: textField.text ?? "")
        }
        if textField == sourceInputField {
            viewModel.submitSourceText(sourceText: textField.text ?? "")
        }
    }
}

extension ColorViewController: UICollectionViewDataSource, UICollectionViewDelegateFlowLayout {
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        if collectionView == backdropLightenedColorsCollectionView {
            return backdropLightenedColors.count
        }
        if collectionView == backdropDarkenedColorsCollectionView {
            return backdropDarkenedColors.count
        }
        if collectionView == sourceLightenedColorsCollectionView {
            return sourceLightenedColors.count
        }
        if collectionView == sourceDarkenedColorsCollectionView {
            return sourceDarkenedColors.count
        }
        if collectionView == blendedLightenedColorsCollectionView {
            return blendedLightenedColors.count
        }
        if collectionView == blendedDarkenedColorsCollectionView {
            return blendedDarkenedColors.count
        }
        if collectionView == currentModeColorsCollectionView {
            return viewModel.currentModeColors.count
        }
        if collectionView == lightModeColorsCollectionView {
            return viewModel.lightModeColors.count
        }
        if collectionView == darkModeColorsCollectionView {
            return viewModel.darkModeColors.count
        }
        return 0
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        return CGSize.init(width: collectionView.frame.height, height: collectionView.frame.height)
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        return collectionView.dequeueTypedReusableCell(
            withReuseIdentifier: ColorCollectionCellView.Const.identifier,
            for: indexPath
        ) { (cell: ColorCollectionCellView) in
            if collectionView == backdropLightenedColorsCollectionView {
                BackgroundStyleKt.applyBackgroundStyle(cell, style: backdropLightenedColors[indexPath.row])
            }
            if collectionView == backdropDarkenedColorsCollectionView {
                BackgroundStyleKt.applyBackgroundStyle(cell, style: backdropDarkenedColors[indexPath.row])
            }
            if collectionView == sourceLightenedColorsCollectionView {
                BackgroundStyleKt.applyBackgroundStyle(cell, style: sourceLightenedColors[indexPath.row])
            }
            if collectionView == sourceDarkenedColorsCollectionView {
                BackgroundStyleKt.applyBackgroundStyle(cell, style: sourceDarkenedColors[indexPath.row])
            }
            if collectionView == blendedLightenedColorsCollectionView {
                BackgroundStyleKt.applyBackgroundStyle(cell, style: blendedLightenedColors[indexPath.row])
            }
            if collectionView == blendedDarkenedColorsCollectionView {
                BackgroundStyleKt.applyBackgroundStyle(cell, style: blendedDarkenedColors[indexPath.row])
            }
            if collectionView == currentModeColorsCollectionView {
                BackgroundStyleKt.applyBackgroundStyle(cell, style: viewModel.currentModeColors[indexPath.row])
            }
            if collectionView == lightModeColorsCollectionView {
                BackgroundStyleKt.applyBackgroundStyle(cell, style: viewModel.lightModeColors[indexPath.row])
            }
            if collectionView == darkModeColorsCollectionView {
                BackgroundStyleKt.applyBackgroundStyle(cell, style: viewModel.darkModeColors[indexPath.row])
            }
        }
    }
    
    override func traitCollectionDidChange(_ previousTraitCollection: UITraitCollection?) {
        super.traitCollectionDidChange(previousTraitCollection)

        currentModeColorsCollectionView.reloadData()
        lightModeColorsCollectionView.reloadData()
        darkModeColorsCollectionView.reloadData()
    }
}

class ColorCollectionCellView: UICollectionViewCell {
    
    enum Const {
        static let identifier = "ColorCollectionCellView"
    }
    
    override init(frame: CGRect) {
        super.init(frame: frame)
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
}
