//
//  CollectionViewController.swift
//  Demo
//
//  Created by Grigory Avdyushin on 20/03/2020.
//  Copyright © 2020 Splendo. All rights reserved.
//

import UIKit
import KotlinNativeFramework


class CollectionViewController: UICollectionViewController {
    
    lazy var viewModel = KNArchitectureFramework().createCollectionViewViewModel()
    private var lifecycleManager: LifecycleManager!
    
    deinit {
        lifecycleManager.unbind()
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()

        collectionView.register(
            UINib.init(nibName: CollectionViewCell.reuseIdentifier, bundle: nil),
            forCellWithReuseIdentifier: CollectionViewCell.reuseIdentifier
        )
        
        collectionView.register(UINib.init(nibName: CollectionViewHeaderCell.reuseIdentifier, bundle: nil), forSupplementaryViewOfKind: UICollectionView.elementKindSectionHeader, withReuseIdentifier: CollectionViewHeaderCell.reuseIdentifier)
        collectionView.register(UINib.init(nibName: CollectionViewFooterCell.reuseIdentifier, bundle: nil), forSupplementaryViewOfKind: UICollectionView.elementKindSectionFooter, withReuseIdentifier: CollectionViewFooterCell.reuseIdentifier)
        
        let headerBinder = SimpleCollectionHeaderFooterCellBinder(identifier: { (_) in CollectionViewHeaderCell.reuseIdentifier }, bind: { (header, cell) in
        guard let header = header as? CollectionHeader,
            let cell = cell as? CollectionViewHeaderCell else {
                return
            }
            
            cell.setTitle(header.title)
        }, onAppear: nil, onDisappear: nil)
        
        let itemBinder = SimpleCollectionItemCellBinder(identifier: { (_) in return CollectionViewCell.reuseIdentifier}, bind: { (item, cell) in
            guard let cell = cell as? CollectionViewCell,
                let itemViewModel = item as? DefaultCollectionItemViewModel,
                let item = itemViewModel.item as? CollectionItem else {
                    return
            }
        
            cell.setTitle(item.title)
        }, onAppear: nil, onDisappear: nil)
        
        let footerBinder = SimpleCollectionHeaderFooterCellBinder(identifier: { (_) in CollectionViewFooterCell.reuseIdentifier }, bind: { (footer, cell) in
            guard let footer = footer as? CollectionFooter,
                let cell = cell as? CollectionViewFooterCell else {
                    return
            }
            
            cell.setCount(Int(footer.numberOfElements))
        }, onAppear: nil, onDisappear: nil)
        let dataSource = CollectionDataSource(
            source: viewModel.items,
            headerBinder: headerBinder,
            itemBinder: itemBinder,
            footerBinder: footerBinder)
        
        lifecycleManager = viewModel.addLifecycleManager(parent: self, onLifecycle: { [weak self] disposeBag in
            guard let uwSelf = self else {
                return
            }
            
            dataSource.bindCollectionView(collectionView: uwSelf.collectionView).addTo(disposeBag: disposeBag)
        })
    }
}
