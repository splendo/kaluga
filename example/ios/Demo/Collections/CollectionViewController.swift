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
    private var lifecycleManager: ArchitectureLifecycleManager!
    
    deinit {
        lifecycleManager.unbind()
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()

        collectionView.register(
            UINib.init(nibName: CollectionViewCell.reuseIdentifier, bundle: nil),
            forCellWithReuseIdentifier: CollectionViewCell.reuseIdentifier
        )

        lifecycleManager = KNArchitectureFramework().bind(viewModel: viewModel, to: self, onLifecycleChanges: { [weak self] disposeBag in
            
            
//            self?.viewModel.observeItems(disposeBag: disposeBag, onItemsChanged: { (items) in
//                print(items.count)
//                self?.items = items
//                self?.collectionView.reloadData()
//            })
        })

    }
//
//    override func numberOfSections(in collectionView: UICollectionView) -> Int {
//        return 1
//    }
//
//    override func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
//        return items.count
//    }
//
//    override func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
//        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: CollectionViewCell.reuseIdentifier, for: indexPath) as! CollectionViewCell
//        cell.setTitle(items[indexPath.row].title)
//        return cell
//    }
}
