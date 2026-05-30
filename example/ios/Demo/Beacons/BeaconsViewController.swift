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

class BeaconsViewController: UICollectionViewController {

    private var beacons = [BeaconsListBeaconViewModel]() {
        didSet { collectionView.reloadData() }
    }

    private var lifecycleManager: LifecycleManager!

    private lazy var viewModel = BeaconsListViewModel()

    init() {
        let flowLayout = FittingWidthAutomaticHeightCollectionViewFlowLayout()
        flowLayout.estimatedItemSize = UICollectionViewFlowLayout.automaticSize
        flowLayout.sectionInset = UIEdgeInsets(top: 10, left: 0, bottom: 10, right: 0)
        flowLayout.minimumLineSpacing = 4
        super.init(collectionViewLayout: flowLayout)
    }

    required init?(coder: NSCoder) {
        fatalError("init(coder:) is unavailable")
    }

    deinit {
        lifecycleManager.unbind()
    }

    override func viewDidLoad() {
        super.viewDidLoad()

        title = "feature_beacons".localized()
        collectionView.backgroundColor = .systemBackground
        collectionView.register(BeaconsViewCell.self, forCellWithReuseIdentifier: BeaconsViewCell.identifier)

        lifecycleManager = viewModel.addLifecycleManager(parent: self) { [weak self] in
            guard let viewModel = self?.viewModel else { return [] }
            return [
                viewModel.isScanning.observe { isScanning in
                    self?.updateNavigationItem(isScanning: isScanning as? Bool ?? false)
                },
                viewModel.beacons.observe { devices in
                    self?.beacons = devices as? [BeaconsListBeaconViewModel] ?? []
                },
            ]
        }
    }

    override func numberOfSections(in collectionView: UICollectionView) -> Int { 1 }

    override func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        beacons.count
    }

    override func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: BeaconsViewCell.identifier, for: indexPath) as! BeaconsViewCell
        cell.configure(with: beacons[indexPath.row])
        return cell
    }

    override func collectionView(_ collectionView: UICollectionView, willDisplay cell: UICollectionViewCell, forItemAt indexPath: IndexPath) {
        (cell as? BeaconsViewCell)?.startMonitoring()
    }

    override func collectionView(_ collectionView: UICollectionView, didEndDisplaying cell: UICollectionViewCell, forItemAt indexPath: IndexPath) {
        (cell as? BeaconsViewCell)?.stopMonitoring()
    }

    override func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        collectionView.deselectItem(at: indexPath, animated: true)
    }

    private func updateNavigationItem(isScanning: Bool) {
        let title = isScanning ? "beacons_stop_monitoring" : "beacons_start_monitoring"
        let item = UIBarButtonItem(
            title: NSLocalizedString(title, comment: ""),
            style: .plain,
            target: viewModel,
            action: #selector(viewModel.onScanPressed)
        )
        navigationItem.setRightBarButton(item, animated: true)
    }
}
