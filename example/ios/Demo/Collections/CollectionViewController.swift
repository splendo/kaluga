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
    
    var items = [Collection_viewCollectionViewItem]()
    let viewModel = SharedCollectionViewViewModel(repository: SharedItemsRepository())

    override func viewDidLoad() {
        super.viewDidLoad()

        collectionView.register(
            UINib.init(nibName: CollectionViewCell.reuseIdentifier, bundle: nil),
            forCellWithReuseIdentifier: CollectionViewCell.reuseIdentifier
        )

        viewModel.subscribe { items in
            self.items = items
            self.collectionView.reloadData()
        }
    }

    override func numberOfSections(in collectionView: UICollectionView) -> Int {
        return 1
    }

    override func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return items.count
    }

    override func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: CollectionViewCell.reuseIdentifier, for: indexPath) as! CollectionViewCell
        cell.setTitle(items[indexPath.row].title)
        return cell
    }
}
