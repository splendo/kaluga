//
//  BluetoothPermissionViewController.swift
//  Demo
//
//  Created by arnoid on 21/10/2019.
//  Copyright © 2019 Splendo. All rights reserved.
//

import UIKit
import Foundation
import KotlinNativeFramework

class BluetoothPermissionViewController: UITableViewController {

    override func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        tableView.deselectRow(at: indexPath, animated: true)
        
        switch (indexPath.row) {
        case 0:
            KotlinNativeFramework().permissionStatus(alertBuilder: AlertsAlertBuilder(viewController: self))
        case 1:
            KotlinNativeFramework().permissionRequest(alertBuilder: AlertsAlertBuilder(viewController: self))
        default:
            break
        }
    }

}
