//
//  BluetoothMoreViewController.swift
//  Demo
//
//  Created by Gijs van Veen on 01/04/2020.
//  Copyright © 2020 Splendo. All rights reserved.
//

import UIKit
import KotlinNativeFramework

class BluetoothMoreViewController : UIViewController, UICollectionViewDelegate, UICollectionViewDataSource {
    
    var bluetooth: KNBluetoothFramework!
    var identifier: UUID!
    
    @IBOutlet var deviceName: UILabel!
    @IBOutlet var deviceIdentifier: UILabel!
    @IBOutlet var rssi: UILabel!
    @IBOutlet var txPower: UILabel!
    @IBOutlet var connectionStatus: UILabel!
    @IBOutlet var serviesHeader: UILabel!
    @IBOutlet var servicesList: UICollectionView!
    
    private var services: [BluetoothService] = []
    
    private var isInvalidating: Bool = false
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        
        serviesHeader.text = NSLocalizedString("bluetooth_services_header", comment: "")
        
        let flowLayout = FittingWidthAutomaticHeightCollectionViewFlowLayout()
        flowLayout.estimatedItemSize = UICollectionViewFlowLayout.automaticSize
        flowLayout.sectionInset = UIEdgeInsets(top: 10, left: 8, bottom: 10, right: 8)
        flowLayout.minimumLineSpacing = 4
        servicesList.collectionViewLayout = flowLayout

        self.deviceIdentifier.text = identifier.uuidString
        bluetooth.info(forIdentifier: identifier) { (info) in
            self.deviceName.text = info.name ?? NSLocalizedString("bluetooth_no_name", comment: "")
        }
        bluetooth.rssi(forIdentifier: identifier) { (rssi) in
            self.rssi.text = String(format: NSLocalizedString("rssi", comment: ""), rssi.intValue)
        }
        bluetooth.distance(forIdentifier: identifier) { (distance) in
            self.txPower.text = String(format: NSLocalizedString("distance", comment: ""), distance.doubleValue)
        }
        
        bluetooth.deviceState(forIdentifier: identifier, onChange: { deviceState in
            if let connectionStateKey = self.bluetooth?.connectionStateKey(deviceState: deviceState) {
                self.connectionStatus.text = NSLocalizedString(connectionStateKey, comment: "")
            } else {
                self.connectionStatus.text = nil
            }
        })
        
        bluetooth.services(forIdentifier: identifier) { (services) in
            self.services = services
            self.servicesList.reloadData()
            self.servicesList.layoutIfNeeded()
        }
        
    }
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return services.count
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let serviceCell = collectionView.dequeueReusableCell(withReuseIdentifier: BluetoothServiceView.Companion.identifier, for: indexPath) as! BluetoothServiceView
        serviceCell.parent = self
        serviceCell.deviceIdentifier = identifier
        serviceCell.service = services[indexPath.row]
        serviceCell.bluetooth = bluetooth
        return serviceCell
    }
    
    func collectionView(_ collectionView: UICollectionView, willDisplay cell: UICollectionViewCell, forItemAt indexPath: IndexPath) {
        if (!isInvalidating) {
            (cell as? BluetoothServiceView)?.startMonitoring()
        }
    }
    
    func collectionView(_ collectionView: UICollectionView, didEndDisplaying cell: UICollectionViewCell, forItemAt indexPath: IndexPath) {
        if (!isInvalidating) {
            (cell as? BluetoothServiceView)?.stopMonitoring()
        }
    }
    
    fileprivate func updateListSize() {
        isInvalidating = true
        servicesList.collectionViewLayout.invalidateLayout()
        servicesList.layoutIfNeeded()
        isInvalidating = false
    }
    
}

class BluetoothServiceView: UICollectionViewCell, UICollectionViewDelegate, UICollectionViewDataSource {
    
    fileprivate struct Companion {
        static let identifier = "BluetoothServiceView"
    }
    
    fileprivate weak var parent: BluetoothMoreViewController?
    fileprivate var bluetooth: KNBluetoothFramework?
    fileprivate var deviceIdentifier: UUID?
    fileprivate var service: BluetoothService?
    
    private var isInvalidating: Bool = false

    @IBOutlet var serviceHeader: UILabel!
    @IBOutlet var serviceIdentifier: UILabel!
    @IBOutlet var characteristicsHeader: UILabel!
    @IBOutlet var characteristicsList: UICollectionView!
    @IBOutlet var characteristicsListHeight: NSLayoutConstraint!
    
    private var characteristics: [BluetoothCharacteristic] = []
    private var characteristicsJob: Kotlinx_coroutines_coreJob?
    
    override func awakeFromNib() {
        super.awakeFromNib()
        
        let flowLayout = FittingWidthAutomaticHeightCollectionViewFlowLayout()
        flowLayout.estimatedItemSize = UICollectionViewFlowLayout.automaticSize
        flowLayout.sectionInset = UIEdgeInsets(top: 10, left: 8, bottom: 10, right: 8)
        flowLayout.minimumLineSpacing = 4
        characteristicsList.collectionViewLayout = flowLayout
        characteristicsList.dataSource = self
        characteristicsList.delegate = self
        characteristicsList.register(UINib(nibName: "BluetoothCharacteristicCell", bundle: nil), forCellWithReuseIdentifier: BluetoothCharacteristicView.Companion.identifier)
    }
    
    fileprivate func startMonitoring() {
        characteristicsJob?.cancel(cause: nil)
        serviceHeader.text = NSLocalizedString("bluetooth_service", comment: "")
        characteristicsHeader.text = NSLocalizedString("bluetooth_characteristics", comment: "")
        guard let service = self.service else {
            return
        }
        
        serviceIdentifier.text = bluetooth?.serviceIdentifier(forService: service)
        
        guard let bluetooth = self.bluetooth, let deviceIdentifier = self.deviceIdentifier else {
            return
        }
        
        bluetooth.characteristics(forDeviceIdentifier: deviceIdentifier, andService: service) { characteristics in
            self.characteristics = characteristics
            self.characteristicsList.reloadData()
            self.updateListSize(isInvalidating: false)
        }
    }
    
    fileprivate func stopMonitoring() {
        characteristicsJob?.cancel(cause: nil)
        characteristicsJob = nil
    }
    
    fileprivate func updateListSize(isInvalidating: Bool) {
        self.isInvalidating = isInvalidating
        characteristicsList.collectionViewLayout.invalidateLayout()
        characteristicsList.layoutIfNeeded()
        let height = characteristicsList.contentSize.height
        characteristicsListHeight.constant = height
        parent?.updateListSize()
        self.isInvalidating = false
    }
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return characteristics.count
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let characteristicCell = collectionView.dequeueReusableCell(withReuseIdentifier: BluetoothCharacteristicView.Companion.identifier, for: indexPath) as! BluetoothCharacteristicView
        characteristicCell.parent = self
        characteristicCell.deviceIdentifier = deviceIdentifier
        characteristicCell.service = service
        characteristicCell.characteristic = characteristics[indexPath.row]
        characteristicCell.bluetooth = bluetooth
        return characteristicCell
    }
    
    func collectionView(_ collectionView: UICollectionView, willDisplay cell: UICollectionViewCell, forItemAt indexPath: IndexPath) {
        if (!isInvalidating) {
            (cell as? BluetoothCharacteristicView)?.startMonitoring()
        }
    }
    
    func collectionView(_ collectionView: UICollectionView, didEndDisplaying cell: UICollectionViewCell, forItemAt indexPath: IndexPath) {
        if (!isInvalidating) {
            (cell as? BluetoothCharacteristicView)?.stopMonitoring()
        }
    }
    
    override func preferredLayoutAttributesFitting(_ layoutAttributes: UICollectionViewLayoutAttributes) -> UICollectionViewLayoutAttributes {
        let layoutAttributes = super.preferredLayoutAttributesFitting(layoutAttributes)
        layoutIfNeeded()
        layoutAttributes.frame.size = systemLayoutSizeFitting(UIView.layoutFittingCompressedSize, withHorizontalFittingPriority: .required, verticalFittingPriority: .fittingSizeLevel)
        return layoutAttributes
    }
    
}

class BluetoothCharacteristicView : UICollectionViewCell, UICollectionViewDelegate, UICollectionViewDataSource {
    
    fileprivate struct Companion {
        static let identifier = "BluetoothCharacteristicView"
    }
    
    fileprivate weak var parent: BluetoothServiceView?
    fileprivate var bluetooth: KNBluetoothFramework?
    fileprivate var deviceIdentifier: UUID?
    fileprivate var service: BluetoothService?
    fileprivate var characteristic: BluetoothCharacteristic?
    
    private var isInvalidating: Bool = false
    
    @IBOutlet var characteristicIdentifier: UILabel!
    @IBOutlet var characteristicValue: UILabel!
    @IBOutlet var descriptorsHeader: UILabel!
    @IBOutlet var descriptorsList: UICollectionView!
    
    @IBOutlet var descriptorsListHeight: NSLayoutConstraint!
    
    private var descriptors: [BluetoothDescriptor] = []
    private var descriptorsJob: Kotlinx_coroutines_coreJob?
    
    private var readJob: Kotlinx_coroutines_coreJob?
    
    override func awakeFromNib() {
        super.awakeFromNib()
        
        let flowLayout = FittingWidthAutomaticHeightCollectionViewFlowLayout()
        flowLayout.estimatedItemSize = UICollectionViewFlowLayout.automaticSize
        flowLayout.sectionInset = UIEdgeInsets(top: 10, left: 8, bottom: 10, right: 8)
        flowLayout.minimumLineSpacing = 4
        descriptorsList.collectionViewLayout = flowLayout
        descriptorsList.register(UINib(nibName: "BluetoothDescriptorCell", bundle: nil), forCellWithReuseIdentifier: BluetoothDescriptorView.Companion.identifier)
    }
    
    fileprivate func startMonitoring() {
        descriptorsJob?.cancel(cause: nil)
        readJob?.cancel(cause: nil)
        descriptorsHeader.text = NSLocalizedString("bluetooth_descriptors", comment: "")
        
        guard let characteristic = self.characteristic else {
            return
        }
        
        characteristicIdentifier.text = bluetooth?.characteristicIdentifier(forCharacteristic: characteristic)
        
        guard let bluetooth = self.bluetooth, let deviceIdentifier = self.deviceIdentifier, let service = self.service else {
            return
        }
        
        descriptorsJob = bluetooth.descriptors(forDeviceIdentifier: deviceIdentifier, service: service, andCharacteristic: characteristic) { descriptors in
            self.descriptors = descriptors
            self.descriptorsList.reloadData()
            self.updateListSize(isInvalidating: false)
        }
        
        readJob = bluetooth.characteristicValue(forDeviceIdentifier: deviceIdentifier, service: service, andCharacteristic: characteristic, onChange: { value in
            self.characteristicValue.text = value ?? NSLocalizedString("bluetooth_attribute_no_value", comment: "")
        })
    }
    
    fileprivate func stopMonitoring() {
        descriptorsJob?.cancel(cause: nil)
        descriptorsJob = nil
        
        readJob?.cancel(cause: nil)
        readJob = nil
    }
    
    private func updateListSize(isInvalidating: Bool) {
        self.isInvalidating = isInvalidating
        descriptorsList.collectionViewLayout.invalidateLayout()
        descriptorsList.layoutIfNeeded()
        let height = descriptorsList.contentSize.height
        descriptorsListHeight.constant = height
        parent?.updateListSize(isInvalidating: true)
        self.isInvalidating = false
    }
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return descriptors.count
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let descriptorCell = collectionView.dequeueReusableCell(withReuseIdentifier: BluetoothDescriptorView.Companion.identifier, for: indexPath) as! BluetoothDescriptorView
        descriptorCell.deviceIdentifier = deviceIdentifier
        descriptorCell.service = service
        descriptorCell.characteristic = characteristic
        descriptorCell.descriptor = descriptors[indexPath.row]
        descriptorCell.bluetooth = bluetooth
        return descriptorCell
    }
    
    func collectionView(_ collectionView: UICollectionView, willDisplay cell: UICollectionViewCell, forItemAt indexPath: IndexPath) {
        if (!isInvalidating) {
            (cell as? BluetoothDescriptorView)?.startMonitoring()
        }
    }
    
    func collectionView(_ collectionView: UICollectionView, didEndDisplaying cell: UICollectionViewCell, forItemAt indexPath: IndexPath) {
        if (!isInvalidating) {
            (cell as? BluetoothDescriptorView)?.stopMonitoring()
        }
    }
    
    override func preferredLayoutAttributesFitting(_ layoutAttributes: UICollectionViewLayoutAttributes) -> UICollectionViewLayoutAttributes {
        let layoutAttributes = super.preferredLayoutAttributesFitting(layoutAttributes)
        layoutIfNeeded()
        layoutAttributes.frame.size = systemLayoutSizeFitting(UIView.layoutFittingCompressedSize, withHorizontalFittingPriority: .required, verticalFittingPriority: .fittingSizeLevel)
        return layoutAttributes
    }
    
}

class BluetoothDescriptorView : UICollectionViewCell {
    
    fileprivate struct Companion {
        static let identifier = "BluetoothDescriptorView"
    }
    
    fileprivate var bluetooth: KNBluetoothFramework?
    fileprivate var deviceIdentifier: UUID?
    fileprivate var service: BluetoothService?
    fileprivate var characteristic: BluetoothCharacteristic?
    fileprivate var descriptor: BluetoothDescriptor?
    
    @IBOutlet var descriptorIdentifier: UILabel!
    @IBOutlet var descriptorValue: UILabel!
    
    private var readJob: Kotlinx_coroutines_coreJob?
    
    fileprivate func startMonitoring() {
        readJob?.cancel(cause: nil)
        guard let descriptor = self.descriptor else {
            return
        }
        
        descriptorIdentifier.text = bluetooth?.descriptorIdentifier(forDescriptor: descriptor)
        
        guard let bluetooth = self.bluetooth, let deviceIdentifier = self.deviceIdentifier, let service = self.service, let characteristic = self.characteristic else {
            return
        }
        
        readJob = bluetooth.desciptorValue(forDeviceIdentifier: deviceIdentifier, service: service, characteristic: characteristic, andDescriptor: descriptor, onChange: { value in
            self.descriptorValue.text = value ?? NSLocalizedString("bluetooth_attribute_no_value", comment: "")
        })
    }
    
    fileprivate func stopMonitoring() {
        readJob?.cancel(cause: nil)
        readJob = nil
    }
    
    override func preferredLayoutAttributesFitting(_ layoutAttributes: UICollectionViewLayoutAttributes) -> UICollectionViewLayoutAttributes {
        let layoutAttributes = super.preferredLayoutAttributesFitting(layoutAttributes)
        layoutIfNeeded()
        layoutAttributes.frame.size = systemLayoutSizeFitting(UIView.layoutFittingCompressedSize, withHorizontalFittingPriority: .required, verticalFittingPriority: .fittingSizeLevel)
        return layoutAttributes
    }
    
}
