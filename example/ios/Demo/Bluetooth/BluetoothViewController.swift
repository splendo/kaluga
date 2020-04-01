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
    
    
    let bluetooth = KNBluetoothFramework()
    
    private var devices: [BluetoothDevice] = []
    
    private var foldedOut: Set<IndexPath> = Set()
    
    override func awakeFromNib() {
        super.awakeFromNib()
        
        let flowLayout = FittingWidthAutomaticHeightCollectionViewFlowLayout()
        flowLayout.estimatedItemSize = UICollectionViewFlowLayout.automaticSize
        flowLayout.sectionInset = UIEdgeInsets(top: 10, left: 0, bottom: 10, right: 0)
        flowLayout.minimumLineSpacing = 4
        collectionView.collectionViewLayout = flowLayout
        
        updateNavigationItem()
        
        bluetooth.devices { newDevices in
            self.devices = newDevices
            self.collectionView.reloadData()
        }
    }
    
    private func updateNavigationItem() {
        bluetooth.isScanning { isScanning in
            if (isScanning.boolValue) {
                self.navigationItem.setRightBarButton(UIBarButtonItem(title: NSLocalizedString("bluetooth_stop_scanning", comment: ""), style: .plain, target: self, action: #selector(self.stopScanning)), animated: true)
            } else {
                self.navigationItem.setRightBarButton(UIBarButtonItem(title: NSLocalizedString("bluetooth_start_scanning", comment: ""), style: .plain, target: self, action: #selector(self.startScanning)), animated: true)
            }
        }
    }
    
    @objc private func startScanning() {
        bluetooth.startScanning {
            self.updateNavigationItem()
        }
    }
    
    @objc private func stopScanning() {
        bluetooth.stopScanning {
            self.updateNavigationItem()
        }
    }
    
    
    override func numberOfSections(in collectionView: UICollectionView) -> Int {
        return 1
    }
    
    override func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        devices.count
    }
    
    override func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let bluetoothCell = collectionView.dequeueReusableCell(withReuseIdentifier: BluetoothCell.Companion.identifier, for: indexPath) as! BluetoothCell
        bluetoothCell.bluetooth = bluetooth
        bluetoothCell.device = devices[indexPath.row]
        bluetoothCell.collectionView = collectionView
        return bluetoothCell
    }
    
    override func collectionView(_ collectionView: UICollectionView, willDisplay cell: UICollectionViewCell, forItemAt indexPath: IndexPath) {
        (cell as? BluetoothCell)?.startMonitoring()
    }
    
    override func collectionView(_ collectionView: UICollectionView, didEndDisplaying cell: UICollectionViewCell, forItemAt indexPath: IndexPath) {
        (cell as? BluetoothCell)?.stopMonitoring()
    }
    
    override func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        let cell = collectionView.cellForItem(at: indexPath)
        if (foldedOut.contains(indexPath)) {
            foldedOut.remove(indexPath)
            (cell as? BluetoothCell)?.setFoldedOut(isFoldedOut: false)
        } else {
            foldedOut.update(with: indexPath)
            (cell as? BluetoothCell)?.setFoldedOut(isFoldedOut: true)
        }
    }
    
}

class BluetoothCell: UICollectionViewCell {
    
    fileprivate struct Companion {
        static let identifier = "BluetoothCell"
    }
    
    weak fileprivate var collectionView: UICollectionView? = nil
    
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
    
    fileprivate var bluetooth: KNBluetoothFramework? = nil
    fileprivate var device: BluetoothDevice? = nil
    private var isFoldedOut: Bool = false
    private var deviceJob: Kotlinx_coroutines_coreJob? = nil
    
    @IBAction func onConnectPressed() {
        if let identifier = device?.identifier {
            bluetooth?.connect(identifier: identifier)
        }
    }
    
    @IBAction func onDisonnectPressed() {
        if let identifier = device?.identifier {
            bluetooth?.disconnect(identifier: identifier)
        }
    }
    
    func setFoldedOut(isFoldedOut: Bool) {
        if (self.isFoldedOut != isFoldedOut) {
            self.isFoldedOut = isFoldedOut
            foldOutMenu.isHidden = !isFoldedOut
            collectionView?.collectionViewLayout.invalidateLayout()
        }
    }
    
    func startMonitoring() {
        deviceJob?.cancel(cause: nil)
        guard let device = device else {
            return
        }
        deviceJob = bluetooth?.deviceState(forDevice: device, onChange: { deviceState in
            self.deviceName.text = deviceState.advertisementData.name ?? NSLocalizedString("bluetooth_no_name", comment: "")
            self.deviceIdentifier.text = deviceState.identifier.uuidString
            self.rssi.text = String(format: NSLocalizedString("rssi", comment: ""), deviceState.rssi)
            
            self.txPower.isHidden = deviceState.advertisementData.txPowerLevel == Int32.min
            self.txPower.text = String(format: NSLocalizedString("txPower", comment: ""), deviceState.advertisementData.txPowerLevel)
            
            self.buttonContainer.alpha = deviceState.advertisementData.isConnectible ? 1.0 : 0.0

            self.connectButton.isHidden = !(self.bluetooth?.isDisconnectedOrDisconnecting(deviceState: deviceState) ?? true)
            self.disconnectButton.isHidden = self.bluetooth?.isDisconnectedOrDisconnecting(deviceState: deviceState) ?? true
            
            
            if let connectionStateKey = self.bluetooth?.connectionStateKey(deviceState: deviceState) {
                self.connectionStatus.text = NSLocalizedString(connectionStateKey, comment: "")
            } else {
                self.connectionStatus.text = nil
            }
            
            self.moreButtonContainer.isHidden = !(self.bluetooth?.isConnected(deviceState: deviceState) ?? false)

            self.serviceId.text = String(format: NSLocalizedString("bluetooth_service_uuids", comment: ""), self.bluetooth?.serviceUUIDString(deviceState: deviceState) ?? "")
            self.serviceData.text = String(format: NSLocalizedString("bluetooth_service_data", comment: ""), self.bluetooth?.serviceDataString(deviceState: deviceState) ?? "")
            
            self.manufacturerId.text = String(format: NSLocalizedString("bluetooth_manufacturer_id", comment: ""), deviceState.advertisementData.manufacturerId?.intValue ?? -1)
            self.manufacturerData.text = String(format: NSLocalizedString("bluetooth_manufacturer_data", comment: ""), self.bluetooth?.hexString(data: deviceState.advertisementData.manufacturerData) ?? "")
            
        })
    }
    
    func stopMonitoring() {
        deviceJob?.cancel(cause: nil)
        deviceJob = nil
    }
    
    
    override func preferredLayoutAttributesFitting(_ layoutAttributes: UICollectionViewLayoutAttributes) -> UICollectionViewLayoutAttributes {
        let layoutAttributes = super.preferredLayoutAttributesFitting(layoutAttributes)
        layoutIfNeeded()
        layoutAttributes.frame.size = systemLayoutSizeFitting(UIView.layoutFittingCompressedSize, withHorizontalFittingPriority: .required, verticalFittingPriority: .fittingSizeLevel)
        return layoutAttributes
    }
    
}
