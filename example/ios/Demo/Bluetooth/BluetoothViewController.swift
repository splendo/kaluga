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
    
    override func awakeFromNib() {
        super.awakeFromNib()
        
        let flowLayout = FittingWidthAutomaticHeightCollectionViewFlowLayout()
        flowLayout.estimatedItemSize = UICollectionViewFlowLayout.automaticSize
        flowLayout.sectionInset = UIEdgeInsets(top: 10, left: 10, bottom: 10, right: 10)
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
                self.navigationItem.setRightBarButton(UIBarButtonItem(title: "Stop Scanning", style: .plain, target: self, action: #selector(self.stopScanning)), animated: true)
            } else {
                self.navigationItem.setRightBarButton(UIBarButtonItem(title: "Start Scanning", style: .plain, target: self, action: #selector(self.startScanning)), animated: true)
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
        bluetoothCell.device = devices[indexPath.row]
        return bluetoothCell
    }
    
    override func collectionView(_ collectionView: UICollectionView, willDisplay cell: UICollectionViewCell, forItemAt indexPath: IndexPath) {
        (cell as? BluetoothCell)?.startMonitoring()
    }
    
    override func collectionView(_ collectionView: UICollectionView, didEndDisplaying cell: UICollectionViewCell, forItemAt indexPath: IndexPath) {
        (cell as? BluetoothCell)?.stopMonitoring()
    }
    
}

class BluetoothCell: UICollectionViewCell {
    
    fileprivate struct Companion {
        static let identifier = "BluetoothCell"
    }
    
    @IBOutlet var deviceName: UILabel!
    @IBOutlet var deviceIdentifier: UILabel!
    @IBOutlet var rssi: UILabel!
    @IBOutlet var txPower: UILabel!
    @IBOutlet var connectButton: UIButton!
    @IBOutlet var disconnectButton: UIButton!
    
    fileprivate var device: BluetoothDevice? = nil
    
    func setDevice(device: BluetoothDevice) {
        self.device = device
    }
    
    func startMonitoring() {
        
    }
    
    func stopMonitoring() {
        
    }
    
    override func preferredLayoutAttributesFitting(_ layoutAttributes: UICollectionViewLayoutAttributes) -> UICollectionViewLayoutAttributes {
        let layoutAttributes = super.preferredLayoutAttributesFitting(layoutAttributes)
        layoutIfNeeded()
        layoutAttributes.frame.size = systemLayoutSizeFitting(UIView.layoutFittingCompressedSize, withHorizontalFittingPriority: .required, verticalFittingPriority: .fittingSizeLevel)
        return layoutAttributes
    }
    
}
