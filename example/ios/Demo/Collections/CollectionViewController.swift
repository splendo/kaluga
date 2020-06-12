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
        
        let dataSource = DataSource(source: viewModel.items, headerBinder: nil, itemBinder: SimpleItemCellBinder(identifier: { (_) -> String in
            return CollectionViewCell.reuseIdentifier
        }, bind: { (item, cell) in
        guard let cell = cell as? CollectionViewCell,
         let itemViewModel = item as? DefaultCollectionItemViewModel,
            let item = itemViewModel.item as? CollectionItem else {
            return
        }
        
            cell.setTitle(item.title)
        }), footerBinder: nil)
        
        lifecycleManager = viewModel.addLifecycleManager(parent: self, onLifecycle: { [weak self] disposeBag in
            guard let uwSelf = self else {
                return
            }
            
            dataSource.bindTo(collectionView: uwSelf.collectionView).addTo(disposeBag: disposeBag)
        })
    }
}
