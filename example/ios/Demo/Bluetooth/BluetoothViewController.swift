/*
 Copyright 2021 Splendo Consulting B.V. The Netherlands

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
import KotlinNativeFramework

class BluetoothViewController : UICollectionViewController {
    
    lazy var viewModel = KNArchitectureFramework().createBluetoothListViewModel(parent: self, bluetooth: KNBluetoothFramework().bluetooth) { uuid, bluetooth in
        return BluetoothDeviceDetailsViewController.create(deviceUuid: uuid, bluetooth: bluetooth)
    }
    
    private var devices: [BluetoothListDeviceViewModel] = []
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
        
        lifecycleManager = KNArchitectureFramework().bind(viewModel: viewModel, to: self, onLifecycleChanges: { [weak self] in
            guard let viewModel = self?.viewModel else { return [] }
            
            return [
                viewModel.isScanning.observe { isScanning in
                    self?.updateNavigationItem(isScanning: isScanning as? Bool ?? false)
                },
                viewModel.devices.observe { devices in
                    self?.devices = devices as? [BluetoothListDeviceViewModel] ?? []
                    self?.collectionView?.reloadData()
                    self?.collectionView.layoutIfNeeded()
                },
                viewModel.title.observe { title in
                    self?.updateTitle(title: title as String?)
                }
            ]
        })
    }
    
    private func updateNavigationItem(isScanning: Bool) {
        if (isScanning) {
            self.navigationItem.setRightBarButton(UIBarButtonItem(title: NSLocalizedString("bluetooth_stop_scanning", comment: ""), style: .plain, target: self, action: #selector(self.toggleScanning)), animated: true)
        } else {
            self.navigationItem.setRightBarButton(UIBarButtonItem(title: NSLocalizedString("bluetooth_start_scanning", comment: ""), style: .plain, target: self, action: #selector(self.toggleScanning)), animated: true)
        }
    }

    private func updateTitle(title: String?) {
        self.title = title
    }
    
    @objc private func toggleScanning() {
        viewModel.onScanPressed()
    }
    
    override func numberOfSections(in collectionView: UICollectionView) -> Int {
        return 1
    }
    
    override func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        devices.count
    }
    
    override func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let bluetoothCell = collectionView.dequeueReusableCell(withReuseIdentifier: BluetoothCell.Companion.identifier, for: indexPath) as! BluetoothCell
        bluetoothCell.device = devices[indexPath.row]
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
        devices[indexPath.row].toggleFoldOut()
        let context = UICollectionViewFlowLayoutInvalidationContext()
        context.invalidateItems(at: [indexPath])
        collectionView.collectionViewLayout.invalidateLayout()
    }
}

class BluetoothCell: UICollectionViewCell {
    
    fileprivate struct Companion {
        static let identifier = "BluetoothCell"
    }
    
    private let disposeBag = DisposeBag()
    
    @IBOutlet var deviceName: UILabel!
    @IBOutlet var deviceIdentifier: UILabel!
    @IBOutlet var rssi: UILabel!
    @IBOutlet var txPower: UILabel!
    @IBOutlet var buttonContainer: UIStackView!
    @IBOutlet var connectButton: UIButton!
    @IBOutlet var disconnectButton: UIButton!
    
    @IBOutlet var connectionStatus: UILabel!
    @IBOutlet var foldOutMenu: UIView!
    @IBOutlet var serviceId: UILabel!
    @IBOutlet var serviceData: UILabel!
    @IBOutlet var manufacturerId: UILabel!
    @IBOutlet var manufacturerData: UILabel!
    @IBOutlet var moreButtonContainer: UIView!
    @IBOutlet var moreButton: UIButton!
    
    fileprivate var device: BluetoothListDeviceViewModel? = nil
    
    @IBAction func onConnectPressed() {
        device?.onConnectPressed()
    }
    
    @IBAction func onDisonnectPressed() {
        device?.onDisconnectPressed()
    }
    
    @IBAction func onMorePressed() {
        device?.onMorePressed()
    }
    
    func startMonitoring() {
        disposeBag.dispose()
        guard let device = device else {
            return
        }
        device.didResume()

        deviceIdentifier.text = device.identifierString

        device.name.observe { [weak self] name in
            self?.deviceName.text = name as? String
        }.addTo(disposeBag: disposeBag)

        device.rssi.observe { [weak self] rssiValue in
            self?.rssi.text = rssiValue as? String
        }.addTo(disposeBag: disposeBag)

        device.txPower.observe { [weak self] txPowerValue in
            self?.txPower.text = txPowerValue as? String
        }.addTo(disposeBag: disposeBag)

        device.status.observe { [weak self] status in
            self?.connectionStatus.text = status as? String
        }.addTo(disposeBag: disposeBag)

        device.isConnectButtonVisible.observe { [weak self] isVisible in
            self?.buttonContainer.alpha = (isVisible as? Bool ?? false) ? 1.0 : 0.0
        }.addTo(disposeBag: disposeBag)

        device.connectButtonState.observe { [weak self] connectButtonState in
            let state: BluetoothListDeviceViewModel.ConnectButtonState = connectButtonState as? BluetoothListDeviceViewModel.ConnectButtonState ?? BluetoothListDeviceViewModel.ConnectButtonState.disconnect
            switch state {
                case BluetoothListDeviceViewModel.ConnectButtonState.connect:
                    self?.connectButton.isHidden = false
                    self?.disconnectButton.isHidden = true
                case BluetoothListDeviceViewModel.ConnectButtonState.disconnect:
                    self?.connectButton.isHidden = true
                    self?.disconnectButton.isHidden = false
                default: ()
            }
        }.addTo(disposeBag: disposeBag)

        device.isFoldedOut.observe { [weak self] isFoldedOut in
            self?.foldOutMenu.isHidden = !((isFoldedOut as? Bool) ?? false)
        }

        device.isMoreButtonVisible.observe { [weak self] isMoreVisible in
            self?.moreButtonContainer.isHidden = !(isMoreVisible as? Bool ?? false)
        }

        device.serviceUUIDs.observe { [weak self] serviceUUIDS in
            self?.serviceId.text = serviceUUIDS as? String
        }

        device.serviceData.observe { [weak self] serviceData in
            self?.serviceData.text = serviceData as? String
        }

        device.manufacturerId.observe { [weak self] manufacturerId in
            self?.manufacturerId.text = manufacturerId as? String
        }

        device.manufacturerData.observe { [weak self] manufacturerData in
            self?.manufacturerData.text = manufacturerData as? String
        }
    }
    
    func stopMonitoring() {
        disposeBag.dispose()
        device?.didPause()
    }
}
