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

class BluetoothCharacteristicView: UICollectionViewCell {

    enum Companion {
        static let identifier = "BluetoothCharacteristicView"
    }

    weak var parent: BluetoothServiceView?
    var characteristic: BluetoothCharacteristicViewModel?
    private let disposeBag = DisposeBag()

    private var isInvalidating = false

    @IBOutlet var characteristicIdentifier: UILabel!
    @IBOutlet var characteristicValue: UILabel!
    @IBOutlet var descriptorsHeader: UILabel!
    @IBOutlet var descriptorsList: UICollectionView!

    @IBOutlet var descriptorsListHeight: NSLayoutConstraint!

    private var descriptors: [BluetoothDescriptorViewModel] = []

    override func awakeFromNib() {
        super.awakeFromNib()

        descriptorsHeader.text = "bluetooth_descriptors".localized()

        let flowLayout = FittingWidthAutomaticHeightCollectionViewFlowLayout()
        flowLayout.estimatedItemSize = UICollectionViewFlowLayout.automaticSize
        flowLayout.sectionInset = UIEdgeInsets(top: 10, left: 8, bottom: 10, right: 8)
        flowLayout.minimumLineSpacing = 4
        descriptorsList.collectionViewLayout = flowLayout
        descriptorsList.register(
            UINib(nibName: "BluetoothDescriptorCell", bundle: nil),
            forCellWithReuseIdentifier: BluetoothDescriptorView.Companion.identifier
        )
    }

    func startMonitoring() {
        disposeBag.dispose()
        guard let characteristic = self.characteristic else {
            return
        }
        characteristic.didResume()

        characteristicIdentifier.text = characteristic.uuid
        characteristic.descriptors.observe { [weak self] descriptors in
            self?.descriptors = descriptors as? [BluetoothDescriptorViewModel] ?? []
            self?.descriptorsList.reloadData()
            self?.updateListSize(isInvalidating: false)
        }
        .addTo(disposeBag: disposeBag)

        characteristic.value.observe { [weak self] value in
            self?.characteristicValue.text = value as String?
        }
        .addTo(disposeBag: disposeBag)
    }

    func stopMonitoring() {
        disposeBag.dispose()
        characteristic?.didPause()
    }

    private func updateListSize(isInvalidating: Bool) {
        self.isInvalidating = isInvalidating
        descriptorsList.collectionViewLayout.invalidateLayout()
        descriptorsList.layoutIfNeeded()
        let height = descriptorsList.contentSize.height
        descriptorsListHeight.constant = height
        parent?.updateListSize(isInvalidating: true)
        self.isInvalidating = false
    }

    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return descriptors.count
    }

    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        return collectionView.dequeueTypedReusableCell(
            withReuseIdentifier: BluetoothDescriptorView.Companion.identifier,
            for: indexPath
        ) { (descriptorCell: BluetoothDescriptorView) in
            descriptorCell.descriptor = descriptors[indexPath.row]
        }
    }

    func collectionView(_ collectionView: UICollectionView, willDisplay cell: UICollectionViewCell, forItemAt indexPath: IndexPath) {
        if !isInvalidating {
            (cell as? BluetoothDescriptorView)?.startMonitoring()
        }
    }

    func collectionView(_ collectionView: UICollectionView, didEndDisplaying cell: UICollectionViewCell, forItemAt indexPath: IndexPath) {
        if !isInvalidating {
            (cell as? BluetoothDescriptorView)?.stopMonitoring()
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
