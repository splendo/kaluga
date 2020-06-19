//
//  CollectionViewCell.swift
//  Demo
//
//  Created by Grigory Avdyushin on 20/03/2020.
//  Copyright © 2020 Splendo. All rights reserved.
//

import UIKit

class CollectionViewCell: UICollectionViewCell {

    static let reuseIdentifier = "CollectionViewCell"
    
    @IBOutlet fileprivate weak var titleLabel: UILabel!
    
    var title: String? {
       get { titleLabel.text }
       set { titleLabel.text = newValue }
    }
}
