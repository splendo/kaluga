//
//  BeaconsViewController.swift
//  Demo
//
//  Created by Grigory Avdyushin on 05/08/2020.
//  Copyright © 2020 Splendo. All rights reserved.
//

import UIKit
import KotlinNativeFramework

class BeaconsViewController: UICollectionViewController {

    lazy var viewModel = KNArchitectureFramework().createBeaconsListViewModel(parent: self, service: KNBeaconsFramework().service)

    private var beacons: [BeaconsListBeaconViewModel] = []
    private var lifecycleManager: LifecycleManager!

    override func awakeFromNib() {
        super.awakeFromNib()

        let flowLayout = FittingWidthAutomaticHeightCollectionViewFlowLayout()
        flowLayout.estimatedItemSize = UICollectionViewFlowLayout.automaticSize
        flowLayout.sectionInset = UIEdgeInsets(top: 10, left: 0, bottom: 10, right: 0)
        flowLayout.minimumLineSpacing = 4
        collectionView.collectionViewLayout = flowLayout
    }

    override func viewDidLoad() {
        super.viewDidLoad()

        lifecycleManager = KNArchitectureFramework().bind(viewModel: viewModel, to: self) { [weak self] (disposeBag) in
            guard let viewModel = self?.viewModel else { return }

            viewModel.isScanning.observe { isScanning in
                self?.updateNavigationItem(isScanning: isScanning as? Bool ?? false)
            }.addTo(disposeBag: disposeBag)

            viewModel.beacons.observe { devices in
                self?.beacons = devices as? [BeaconsListBeaconViewModel] ?? []
                self?.collectionView?.reloadData()
                self?.collectionView.layoutIfNeeded()
            }.addTo(disposeBag: disposeBag)
        }
    }

    private func updateNavigationItem(isScanning: Bool) {
        if (isScanning) {
            self.navigationItem.setRightBarButton(UIBarButtonItem(title: NSLocalizedString("bluetooth_stop_scanning", comment: ""), style: .plain, target: self, action: #selector(self.toggleScanning)), animated: true)
        } else {
            self.navigationItem.setRightBarButton(UIBarButtonItem(title: NSLocalizedString("bluetooth_start_scanning", comment: ""), style: .plain, target: self, action: #selector(self.toggleScanning)), animated: true)
        }
    }

    @objc private func toggleScanning() {
        viewModel.onScanPressed()
    }

    override func numberOfSections(in collectionView: UICollectionView) -> Int {
        return 1
    }

    override func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        beacons.count
    }

    override func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let bluetoothCell = collectionView.dequeueReusableCell(withReuseIdentifier: BluetoothCell.Companion.identifier, for: indexPath) as! BluetoothCell
        // bluetoothCell.device = devices[indexPath.row]
        return bluetoothCell
    }

    override func collectionView(_ collectionView: UICollectionView, willDisplay cell: UICollectionViewCell, forItemAt indexPath: IndexPath) {
        guard let btCell = cell as? BluetoothCell else {
            return
        }

        btCell.startMonitoring()
    }

    override func collectionView(_ collectionView: UICollectionView, didEndDisplaying cell: UICollectionViewCell, forItemAt indexPath: IndexPath) {
        guard let btCell = cell as? BluetoothCell else {
            return
        }

        btCell.stopMonitoring()
    }

    override func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        collectionView.deselectItem(at: indexPath, animated: true)
    }
}
