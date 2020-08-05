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

    static let identifier = String(describing: BeaconsViewCell.self)

    @IBOutlet private var namespaceLabel: UILabel!
    @IBOutlet private var instanceLabel: UILabel!
    @IBOutlet private var txPowerLabel: UILabel!

    private let disposeBag = DisposeBag()
    var viewModel: BeaconsListBeaconViewModel?

    func startMonitoring() {

        viewModel?.namespace_.observe { [weak self] namespace in
            self?.namespaceLabel.text = namespace as? String
        }.addTo(disposeBag: disposeBag)

        viewModel?.instance.observe { [weak self] instance in
            self?.instanceLabel.text = instance as? String
        }.addTo(disposeBag: disposeBag)

        viewModel?.txPower.observe { [weak self] txPower in
            self?.txPowerLabel.text = txPower as? String
        }
    }

    func stopMonitoring() {
        disposeBag.dispose()
    }
}
