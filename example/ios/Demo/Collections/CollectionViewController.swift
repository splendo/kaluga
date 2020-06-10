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
        
        let dataSource = DataSource(source: viewModel.items, identifier: { _ in return CollectionViewCell.reuseIdentifier} ) { (cell, item) in
            guard let cell = cell as? CollectionViewCell,
             let item = item as? DefaultCollectionItemViewModel else {
                return
            }
            
            cell.setTitle(item.item.title)
        }

        lifecycleManager = viewModel.addLifecycleManager(parent: self, onLifecycle: { [weak self] disposeBag in
            guard let uwSelf = self else {
                return
            }
            
            dataSource.bindTo(collectionView: uwSelf.collectionView).addTo(disposeBag: disposeBag)
            
        })

    }

}
