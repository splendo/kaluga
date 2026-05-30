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
import KalugaExample

class BeaconsViewCell: UICollectionViewCell {

    static var identifier: String { String(describing: Self.self) }

    private let namespaceLabel = UILabel()
    private let instanceLabel = UILabel()
    private let txPowerLabel = UILabel()

    private let disposeBag = DisposeBag()
    private var viewModel: BeaconsListBeaconViewModel?

    override init(frame: CGRect) {
        super.init(frame: frame)
        let stack = UIStackView(arrangedSubviews: [namespaceLabel, instanceLabel, txPowerLabel])
        stack.axis = .vertical
        stack.spacing = 4
        stack.translatesAutoresizingMaskIntoConstraints = false
        contentView.addSubview(stack)
        NSLayoutConstraint.activate([
            stack.leadingAnchor.constraint(equalTo: contentView.layoutMarginsGuide.leadingAnchor),
            stack.trailingAnchor.constraint(equalTo: contentView.layoutMarginsGuide.trailingAnchor),
            stack.topAnchor.constraint(equalTo: contentView.layoutMarginsGuide.topAnchor),
            stack.bottomAnchor.constraint(equalTo: contentView.layoutMarginsGuide.bottomAnchor),
        ])
        namespaceLabel.font = .preferredFont(forTextStyle: .headline)
        instanceLabel.font = .preferredFont(forTextStyle: .body)
        txPowerLabel.font = .preferredFont(forTextStyle: .caption1)
        txPowerLabel.textColor = .secondaryLabel
    }

    required init?(coder: NSCoder) {
        fatalError("init(coder:) is unavailable")
    }

    func configure(with viewModel: BeaconsListBeaconViewModel) {
        self.viewModel = viewModel
    }

    func startMonitoring() {
        viewModel?.namespace_.observe { [weak self] namespace in
            self?.namespaceLabel.text = namespace as String?
        }
        .addTo(disposeBag: disposeBag)

        viewModel?.instance.observe { [weak self] instance in
            self?.instanceLabel.text = instance as String?
        }
        .addTo(disposeBag: disposeBag)

        viewModel?.txPower.observe { [weak self] txPower in
            self?.txPowerLabel.text = txPower as String?
        }
        .addTo(disposeBag: disposeBag)
    }

    func stopMonitoring() {
        disposeBag.dispose()
    }
}
