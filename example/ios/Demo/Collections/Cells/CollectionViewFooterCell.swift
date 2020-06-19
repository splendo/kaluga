//
//  CollectionViewFooterCell.swift
//  Demo
//
//  Created by Gijs van Veen on 12/06/2020.
//  Copyright © 2020 Splendo. All rights reserved.
//

import UIKit

class CollectionViewFooterCell: UICollectionReusableView {

    static let reuseIdentifier = "CollectionViewFooterCell"
    
    @IBOutlet fileprivate weak var countLabel: UILabel!
    
    func setCount(_ count: Int) {
        countLabel.text = String(format: NSLocalizedString("list_total", comment: ""), count)
    }
}
