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
        bluetoothCell.isFoldedOut = foldedOut.contains(indexPath)
        bluetoothCell.device = devices[indexPath.row]
        bluetoothCell.bluetoothViewController = self
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
        
        let context = UICollectionViewFlowLayoutInvalidationContext()
        context.invalidateItems(at: [indexPath])
        collectionView.collectionViewLayout.invalidateLayout()
    }
    
}

class BluetoothCell: UICollectionViewCell {
    
    fileprivate struct Companion {
        static let identifier = "BluetoothCell"
    }
    
    weak fileprivate var bluetoothViewController: BluetoothViewController? = nil
    
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
    fileprivate var isFoldedOut: Bool = false
    private var deviceJob: Kotlinx_coroutines_coreJob? = nil
    
    func setFoldedOut(isFoldedOut: Bool) {
         if (self.isFoldedOut != isFoldedOut) {
             self.isFoldedOut = isFoldedOut
             foldOutMenu.isHidden = !isFoldedOut
         }
     }
    
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
    
    @IBAction func onMorePressed() {
        guard let bluetooth = self.bluetooth, let identifier = self.device?.identifier else {
            return
        }
        let sb = UIStoryboard(name: "Main", bundle: nil)
        let bluetoothMoreVC: BluetoothMoreViewController
        if #available(iOS 13.0, *) {
            bluetoothMoreVC = sb.instantiateViewController(identifier: "bluetoothMoreVC") as! BluetoothMoreViewController
        } else {
            bluetoothMoreVC = sb.instantiateViewController(withIdentifier: "bluetoothMoreVC") as! BluetoothMoreViewController
        }
        bluetoothMoreVC.bluetooth = bluetooth
        bluetoothMoreVC.identifier = identifier
        bluetoothViewController?.navigationController?.pushViewController(bluetoothMoreVC, animated: true)
    }
    
    func startMonitoring() {
        foldOutMenu.isHidden = !isFoldedOut
        deviceJob?.cancel(cause: nil)
        guard let device = device else {
            return
        }
        deviceJob = bluetooth?.deviceState(forIdentifier: device.identifier, onChange: { deviceState in
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
            
            var hasChanged = self.foldOutMenu.isHidden == self.isFoldedOut
            self.foldOutMenu.isHidden = !self.isFoldedOut
            
            let showMoreButton = self.bluetooth?.isConnected(deviceState: deviceState) ?? false
            hasChanged = hasChanged || showMoreButton == self.moreButtonContainer.isHidden
            self.moreButtonContainer.isHidden = !showMoreButton

            let serviceIdText = String(format: NSLocalizedString("bluetooth_service_uuids", comment: ""), self.bluetooth?.serviceUUIDString(deviceState: deviceState) ?? "")
            if (serviceIdText != self.serviceId.text) {
                self.serviceId.text = serviceIdText
                hasChanged = hasChanged || self.isFoldedOut
            }
            let serviceDataText = String(format: NSLocalizedString("bluetooth_service_data", comment: ""), self.bluetooth?.serviceDataString(deviceState: deviceState) ?? "")
            if (serviceDataText != self.serviceData.text) {
                self.serviceData.text = serviceDataText
                hasChanged = hasChanged || self.isFoldedOut
            }
            
            let manufacturerIdText = String(format: NSLocalizedString("bluetooth_manufacturer_id", comment: ""), deviceState.advertisementData.manufacturerId?.intValue ?? -1)
            if (manufacturerIdText != self.manufacturerId.text) {
                self.manufacturerId.text = manufacturerIdText
                hasChanged = hasChanged || self.isFoldedOut
            }
            let manufacturerDataText = String(format: NSLocalizedString("bluetooth_manufacturer_data", comment: ""), self.bluetooth?.hexString(data: deviceState.advertisementData.manufacturerData) ?? "")
            if (manufacturerDataText != self.manufacturerData.text) {
                self.manufacturerData.text = manufacturerDataText
                hasChanged = hasChanged || self.isFoldedOut
            }

            if hasChanged {
                self.bluetoothViewController?.collectionView.collectionViewLayout.invalidateLayout()
            }
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
