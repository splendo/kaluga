//
//  BluetoothViewController.swift
//  Demo
//
//  Created by Gijs van Veen on 30/03/2020.
//  Copyright © 2020 Splendo. All rights reserved.
//

import UIKit
import KotlinNativeFramework

class BluetoothViewController : UICollectionViewController {
    
    lazy var viewModel = KNArchitectureFramework().createBluetoothListViewModel(parent: self, bluetooth: KNBluetoothFramework().bluetooth) { uuid, bluetooth in
        return BluetoothDeviceDetailsViewController.create(deviceUuid: uuid, bluetooth: bluetooth)
    }
    
    private var devices: [BluetoothListDeviceViewModel] = []
    private var lifecycleManager: LifecycleManager!
    private var isInvalidating: Bool = false
    
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
            
            viewModel.devices.observe { devices in
                self?.devices = devices as? [BluetoothListDeviceViewModel] ?? []
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
        devices.count
    }
    
    override func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let bluetoothCell = collectionView.dequeueReusableCell(withReuseIdentifier: BluetoothCell.Companion.identifier, for: indexPath) as! BluetoothCell
        bluetoothCell.device = devices[indexPath.row]
        bluetoothCell.bluetoothViewController = self
        return bluetoothCell
    }
    
    override func collectionView(_ collectionView: UICollectionView, willDisplay cell: UICollectionViewCell, forItemAt indexPath: IndexPath) {
        guard let btCell = cell as? BluetoothCell else {
            return
        }
        if (!isInvalidating) {
            btCell.startMonitoring()
        }
    }
    
    override func collectionView(_ collectionView: UICollectionView, didEndDisplaying cell: UICollectionViewCell, forItemAt indexPath: IndexPath) {
        guard let btCell = cell as? BluetoothCell else {
            return
        }
        
        if (!isInvalidating) {
            btCell.stopMonitoring()
        }
    }
    
    override func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        devices[indexPath.row].toggleFoldOut()
        let context = UICollectionViewFlowLayoutInvalidationContext()
        context.invalidateItems(at: [indexPath])
        collectionView.collectionViewLayout.invalidateLayout()
    }
    
    fileprivate func resizeCollectionView() {
        isInvalidating = true
        collectionView.collectionViewLayout.invalidateLayout()
        collectionView.layoutIfNeeded()
        isInvalidating = false
    }
    
}

class BluetoothCell: UICollectionViewCell {
    
    fileprivate struct Companion {
        static let identifier = "BluetoothCell"
    }
    
    weak fileprivate var bluetoothViewController: BluetoothViewController? = nil
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
    
    
    override func preferredLayoutAttributesFitting(_ layoutAttributes: UICollectionViewLayoutAttributes) -> UICollectionViewLayoutAttributes {
        let layoutAttributes = super.preferredLayoutAttributesFitting(layoutAttributes)
        layoutIfNeeded()
        layoutAttributes.frame.size = systemLayoutSizeFitting(UIView.layoutFittingCompressedSize, withHorizontalFittingPriority: .required, verticalFittingPriority: .fittingSizeLevel)
        return layoutAttributes
    }
    
}
