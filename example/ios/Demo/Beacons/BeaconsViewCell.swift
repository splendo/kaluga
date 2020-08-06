//
//  BeaconsViewCell.swift
//  Demo
//
//  Created by Grigory Avdyushin on 05/08/2020.
//  Copyright © 2020 Splendo. All rights reserved.
//

import UIKit
import KotlinNativeFramework

class BeaconsViewCell: UICollectionViewCell {

    static var identifier: String { String(describing: Self.self) }

    @IBOutlet private var namespaceLabel: UILabel!
    @IBOutlet private var instanceLabel: UILabel!
    @IBOutlet private var txPowerLabel: UILabel!

    private let disposeBag = DisposeBag()
    private var viewModel: BeaconsListBeaconViewModel?

    func configure(with viewModel: BeaconsListBeaconViewModel) {
        self.viewModel = viewModel
    }

    func startMonitoring() {

        viewModel?.namespace_.observe { [weak self] namespace in
            self?.namespaceLabel.text = namespace as? String
        }.addTo(disposeBag: disposeBag)

        viewModel?.instance.observe { [weak self] instance in
            self?.instanceLabel.text = instance as? String
        }.addTo(disposeBag: disposeBag)

        viewModel?.txPower.observe { [weak self] txPower in
            self?.txPowerLabel.text = txPower as? String
        }.addTo(disposeBag: disposeBag)
    }

    func stopMonitoring() {
        disposeBag.dispose()
    }
}
