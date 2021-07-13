//
//  BluetoothDeviceDetailsViewController.swift
//  Demo
//
//  Created by Gijs van Veen on 16/07/2020.
//  Copyright © 2020 Splendo. All rights reserved.
//

import UIKit
import KotlinNativeFramework

class BluetoothDeviceDetailsViewController : UIViewController, UICollectionViewDelegate, UICollectionViewDataSource {
    
    struct Const {
        static let storyboard = UIStoryboard(name: "Main", bundle: nil)
        static let storyboardId = "BluetoothDeviceDetails"
    }
    
    static func create(deviceUuid: UUID, bluetooth: Bluetooth) -> BluetoothDeviceDetailsViewController {
        let vc = Const.storyboard.instantiateViewController(withIdentifier: Const.storyboardId) as! BluetoothDeviceDetailsViewController
        if #available(iOS 13.0, *) {
            vc.isModalInPresentation = true
        }
        vc.viewModel = KNArchitectureFramework().createBluetoothDeviceDetailsViewModel(identifier: deviceUuid, bluetooth: bluetooth)
        return vc
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
    
    private var isInvalidating: Bool = false
    
    deinit {
        lifecycleManager.unbind()
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        
        servicesHeader.text = NSLocalizedString("bluetooth_services_header", comment: "")
        deviceIdentifier.text = viewModel.identifierString
        
        let flowLayout = FittingWidthAutomaticHeightCollectionViewFlowLayout()
        flowLayout.estimatedItemSize = UICollectionViewFlowLayout.automaticSize
        flowLayout.sectionInset = UIEdgeInsets(top: 10, left: 8, bottom: 10, right: 8)
        flowLayout.minimumLineSpacing = 4
        servicesList.collectionViewLayout = flowLayout

        lifecycleManager = KNArchitectureFramework().bind(viewModel: viewModel, to: self, onLifecycleChanges: { [weak self] in
            
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
        })
    }
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return services.count
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let serviceCell = collectionView.dequeueReusableCell(withReuseIdentifier: BluetoothServiceView.Companion.identifier, for: indexPath) as! BluetoothServiceView
        serviceCell.parent = self
        serviceCell.service = services[indexPath.row]
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
    
    fileprivate weak var parent: BluetoothDeviceDetailsViewController?
    fileprivate var service: BluetoothServiceViewModel?
    private let disposeBag = DisposeBag()
    
    private var isInvalidating: Bool = false

    @IBOutlet var serviceHeader: UILabel!
    @IBOutlet var serviceIdentifier: UILabel!
    @IBOutlet var characteristicsHeader: UILabel!
    @IBOutlet var characteristicsList: UICollectionView!
    @IBOutlet var characteristicsListHeight: NSLayoutConstraint!
    
    private var characteristics: [BluetoothCharacteristicViewModel] = []
    
    override func awakeFromNib() {
        super.awakeFromNib()
        
        serviceHeader.text = NSLocalizedString("bluetooth_service", comment: "")
        characteristicsHeader.text = NSLocalizedString("bluetooth_characteristics", comment: "")
        
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
        disposeBag.dispose()
        guard let service = self.service else {
            return
        }
        service.didResume()
        
        serviceIdentifier.text = service.uuid
        service.characteristics.observe { [weak self] characteristics in
            self?.characteristics = characteristics as? [BluetoothCharacteristicViewModel] ?? []
            self?.characteristicsList.reloadData()
            self?.updateListSize(isInvalidating: false)
        }.addTo(disposeBag: disposeBag)
    }
    
    fileprivate func stopMonitoring() {
        service?.didPause()
        disposeBag.dispose()
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
        characteristicCell.characteristic = characteristics[indexPath.row]
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
    fileprivate var characteristic: BluetoothCharacteristicViewModel?
    private let disposeBag = DisposeBag()
    
    private var isInvalidating: Bool = false
    
    @IBOutlet var characteristicIdentifier: UILabel!
    @IBOutlet var characteristicValue: UILabel!
    @IBOutlet var descriptorsHeader: UILabel!
    @IBOutlet var descriptorsList: UICollectionView!
    
    @IBOutlet var descriptorsListHeight: NSLayoutConstraint!
    
    private var descriptors: [BluetoothDescriptorViewModel] = []
    
    override func awakeFromNib() {
        super.awakeFromNib()
        
        descriptorsHeader.text = NSLocalizedString("bluetooth_descriptors", comment: "")
        
        let flowLayout = FittingWidthAutomaticHeightCollectionViewFlowLayout()
        flowLayout.estimatedItemSize = UICollectionViewFlowLayout.automaticSize
        flowLayout.sectionInset = UIEdgeInsets(top: 10, left: 8, bottom: 10, right: 8)
        flowLayout.minimumLineSpacing = 4
        descriptorsList.collectionViewLayout = flowLayout
        descriptorsList.register(UINib(nibName: "BluetoothDescriptorCell", bundle: nil), forCellWithReuseIdentifier: BluetoothDescriptorView.Companion.identifier)
    }
    
    fileprivate func startMonitoring() {
        disposeBag.dispose()
        guard let characteristic = self.characteristic else {
            return
        }
        characteristic.didResume()
        
        characteristicIdentifier.text = characteristic.uuid
        characteristic.descriptors.observe { [weak self] descriptors in
            self?.descriptors = descriptors as? [BluetoothDescriptorViewModel] ?? []
            self?.descriptorsList.reloadData()
            self?.updateListSize(isInvalidating: false)
        }.addTo(disposeBag: disposeBag)
        
        characteristic.value.observe { [weak self] value in
            self?.characteristicValue.text = value as String?
        }.addTo(disposeBag: disposeBag)
    }
    
    fileprivate func stopMonitoring() {
        disposeBag.dispose()
        characteristic?.didPause()
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
        descriptorCell.descriptor = descriptors[indexPath.row]
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
    
    fileprivate var descriptor: BluetoothDescriptorViewModel?
    private let disposeBag = DisposeBag()
    
    @IBOutlet var descriptorIdentifier: UILabel!
    @IBOutlet var descriptorValue: UILabel!
    
    fileprivate func startMonitoring() {
        disposeBag.dispose()
        guard let descriptor = self.descriptor else {
            return
        }
        descriptor.didResume()
        
        descriptorIdentifier.text = descriptor.uuid
        
        descriptor.value.observe { [weak self] value in
            self?.descriptorValue.text = value as String?
        }.addTo(disposeBag: disposeBag)
    }
    
    fileprivate func stopMonitoring() {
        disposeBag.dispose()
        descriptor?.didResume()
    }
    
    override func preferredLayoutAttributesFitting(_ layoutAttributes: UICollectionViewLayoutAttributes) -> UICollectionViewLayoutAttributes {
        let layoutAttributes = super.preferredLayoutAttributesFitting(layoutAttributes)
        layoutIfNeeded()
        layoutAttributes.frame.size = systemLayoutSizeFitting(UIView.layoutFittingCompressedSize, withHorizontalFittingPriority: .required, verticalFittingPriority: .fittingSizeLevel)
        return layoutAttributes
    }
}
