//
//  CollectionViewHeaderCell.swift
//  Demo
//
//  Created by Gijs van Veen on 12/06/2020.
//  Copyright © 2020 Splendo. All rights reserved.
//

import UIKit

class CollectionViewHeaderCell: UICollectionReusableView {

    static let reuseIdentifier = "CollectionViewHeaderCell"
    
    @IBOutlet fileprivate weak var titleLabel: UILabel!
    
    var title: String? {
       get { titleLabel.text }
       set { titleLabel.text = newValue }
    }
}
