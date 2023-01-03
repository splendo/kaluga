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

class BluetoothServiceView: UICollectionViewCell, UICollectionViewDelegate, UICollectionViewDataSource {

    enum Companion {
        static let identifier = "BluetoothServiceView"
    }

    weak var parent: BluetoothDeviceDetailsViewController?
    var service: BluetoothServiceViewModel?
    private let disposeBag = DisposeBag()

    private var isInvalidating = false

    @IBOutlet var serviceHeader: UILabel!
    @IBOutlet var serviceIdentifier: UILabel!
    @IBOutlet var characteristicsHeader: UILabel!
    @IBOutlet var characteristicsList: UICollectionView!
    @IBOutlet var characteristicsListHeight: NSLayoutConstraint!

    private var characteristics: [BluetoothCharacteristicViewModel] = []

    override func awakeFromNib() {
        super.awakeFromNib()

        serviceHeader.text = "bluetooth_service".localized()
        characteristicsHeader.text = "bluetooth_characteristics".localized()

        let flowLayout = FittingWidthAutomaticHeightCollectionViewFlowLayout()
        flowLayout.estimatedItemSize = UICollectionViewFlowLayout.automaticSize
        flowLayout.sectionInset = UIEdgeInsets(top: 10, left: 8, bottom: 10, right: 8)
        flowLayout.minimumLineSpacing = 4
        characteristicsList.collectionViewLayout = flowLayout
        characteristicsList.dataSource = self
        characteristicsList.delegate = self
        characteristicsList.register(
            UINib(nibName: "BluetoothCharacteristicCell", bundle: nil),
            forCellWithReuseIdentifier: BluetoothCharacteristicView.Companion.identifier
        )
    }

    func startMonitoring() {
        disposeBag.dispose()
        guard let service = self.service else {
            return
        }
        service.didResume()

        serviceIdentifier.text = service.uuid
        service.characteristics.observe { [weak self] characteristics in
            self?.characteristics = characteristics as? [BluetoothCharacteristicViewModel] ?? []
            self?.characteristicsList.reloadData()
            self?.updateListSize(isInvalidating: false)
        }
        .addTo(disposeBag: disposeBag)
    }

    func stopMonitoring() {
        service?.didPause()
        disposeBag.dispose()
    }

    func updateListSize(isInvalidating: Bool) {
        self.isInvalidating = isInvalidating
        characteristicsList.collectionViewLayout.invalidateLayout()
        characteristicsList.layoutIfNeeded()
        let height = characteristicsList.contentSize.height
        characteristicsListHeight.constant = height
        parent?.updateListSize()
        self.isInvalidating = false
    }

    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return characteristics.count
    }

    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        return collectionView.dequeueTypedReusableCell(
            withReuseIdentifier: BluetoothCharacteristicView.Companion.identifier,
            for: indexPath
        ) { (characteristicCell: BluetoothCharacteristicView) in
            characteristicCell.parent = self
            characteristicCell.characteristic = characteristics[indexPath.row]
        }
    }

    func collectionView(_ collectionView: UICollectionView, willDisplay cell: UICollectionViewCell, forItemAt indexPath: IndexPath) {
        if !isInvalidating {
            (cell as? BluetoothCharacteristicView)?.startMonitoring()
        }
    }

    func collectionView(_ collectionView: UICollectionView, didEndDisplaying cell: UICollectionViewCell, forItemAt indexPath: IndexPath) {
        if !isInvalidating {
            (cell as? BluetoothCharacteristicView)?.stopMonitoring()
        }
    }

    override func preferredLayoutAttributesFitting(_ layoutAttributes: UICollectionViewLayoutAttributes) -> UICollectionViewLayoutAttributes {
        let layoutAttributes = super.preferredLayoutAttributesFitting(layoutAttributes)
        layoutIfNeeded()
        layoutAttributes.frame.size = systemLayoutSizeFitting(
            UIView.layoutFittingCompressedSize,
            withHorizontalFittingPriority: .required,
            verticalFittingPriority: .fittingSizeLevel
        )
        return layoutAttributes
    }
}
