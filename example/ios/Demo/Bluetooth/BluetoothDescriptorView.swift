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

class BluetoothDescriptorView: UICollectionViewCell {

    enum Companion {
        static let identifier = "BluetoothDescriptorView"
    }

    var descriptor: BluetoothDescriptorViewModel?
    private let disposeBag = DisposeBag()

    @IBOutlet var descriptorIdentifier: UILabel!
    @IBOutlet var descriptorValue: UILabel!

    func startMonitoring() {
        disposeBag.dispose()
        guard let descriptor = self.descriptor else {
            return
        }
        descriptor.didResume()

        descriptorIdentifier.text = descriptor.uuid

        descriptor.value.observe { [weak self] value in
            self?.descriptorValue.text = value as String?
        }
        .addTo(disposeBag: disposeBag)
    }

    func stopMonitoring() {
        disposeBag.dispose()
        descriptor?.didResume()
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
