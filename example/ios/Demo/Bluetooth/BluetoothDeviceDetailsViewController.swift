/*
 Copyright 2022 Splendo Consulting B.V. The Netherlands

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

 */

import UIKit
import KalugaExampleShared

class BluetoothDeviceDetailsViewController: UIViewController {
    
    static func create(identifier: UUID) -> BluetoothDeviceDetailsViewController {
        let viewController = MainStoryboard.instantiateBluetoothDeviceDetailsViewController()
        if #available(iOS 13.0, *) {
            viewController.isModalInPresentation = true
        }
        viewController.viewModel = BluetoothDeviceDetailViewModel(identifier: identifier)
        return viewController
    }

    var viewModel: BluetoothDeviceDetailViewModel!
    private var lifecycleManager: LifecycleManager!
    
    @IBOutlet var deviceName: UILabel!
    @IBOutlet var deviceIdentifier: UILabel!
    @IBOutlet var rssi: UILabel!
    @IBOutlet var distance: UILabel!
    @IBOutlet var connectionStatus: UILabel!
    @IBOutlet var servicesHeader: UILabel!
    @IBOutlet var servicesList: UICollectionView!
    
    private var services: [BluetoothServiceViewModel] = []

    private var isInvalidating = false

    deinit {
        lifecycleManager.unbind()
    }

    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)

        title = "feature_bluetooth".localized()

        servicesHeader.text = "bluetooth_services_header".localized()
        deviceIdentifier.text = viewModel.identifierString

        let flowLayout = FittingWidthAutomaticHeightCollectionViewFlowLayout()
        flowLayout.estimatedItemSize = UICollectionViewFlowLayout.automaticSize
        flowLayout.sectionInset = UIEdgeInsets(top: 10, left: 8, bottom: 10, right: 8)
        flowLayout.minimumLineSpacing = 4
        servicesList.collectionViewLayout = flowLayout

        lifecycleManager = viewModel.addLifecycleManager(parent: self) { [weak self] in

            guard let viewModel = self?.viewModel else { return []}

            return [
            viewModel.name.observe { name in
                self?.deviceName.text = name as String?
            }
            ,
            viewModel.rssi.observe { rssiValue in
                self?.rssi.text = rssiValue as String?
            }
            ,
            viewModel.distance.observe { distanceValue in
                self?.distance.text = distanceValue as String?
            }
            ,
            viewModel.state.observe { state in
                self?.connectionStatus.text = state as String?
            }
            ,
            viewModel.services.observe { servicesList in
                self?.services = servicesList as? [BluetoothServiceViewModel] ?? []
                self?.servicesList.reloadData()
                self?.servicesList.layoutIfNeeded()
            }
            ]
        }
    }

    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return services.count
    }

    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        return collectionView.dequeueTypedReusableCell(
            withReuseIdentifier: BluetoothServiceView.Companion.identifier,
            for: indexPath
        ) { (serviceCell: BluetoothServiceView) in
            serviceCell.parent = self
            serviceCell.service = services[indexPath.row]
        }
    }

    func collectionView(_ collectionView: UICollectionView, willDisplay cell: UICollectionViewCell, forItemAt indexPath: IndexPath) {
        if !isInvalidating {
            (cell as? BluetoothServiceView)?.startMonitoring()
        }
    }

    func collectionView(_ collectionView: UICollectionView, didEndDisplaying cell: UICollectionViewCell, forItemAt indexPath: IndexPath) {
        if !isInvalidating {
            (cell as? BluetoothServiceView)?.stopMonitoring()
        }
    }

    func updateListSize() {
        isInvalidating = true
        servicesList.collectionViewLayout.invalidateLayout()
        servicesList.layoutIfNeeded()
        isInvalidating = false
    }
}
