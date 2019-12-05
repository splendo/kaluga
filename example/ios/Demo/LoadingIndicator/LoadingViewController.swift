//
//  LoadingViewController.swift
//  Demo
//
//  Created by Grigory Avdyushin on 31/10/2019.
//  Copyright © 2019 Splendo. All rights reserved.
//

import UIKit
import KotlinNativeFramework

class LoadingViewController: UITableViewController {

    lazy var activityIndicator = KotlinNativeFrameworkKt.activityIndicator(viewController: self)
    
    override func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        tableView.deselectRow(at: indexPath, animated: true)
        switch (indexPath.row) {
        case 0: activityIndicator.showSystem()
        case 1: activityIndicator.showCustom()
        default: ()
        }
    }
}
