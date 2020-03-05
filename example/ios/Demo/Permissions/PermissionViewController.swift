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

enum PermissionType {
    case Bluetooth
    case Calendar
    case Camera
    case Contacts
    case Location
    case Microphone
    case Notifications(options: UNAuthorizationOptions)
    case Storage
}

class PermissionViewController: UITableViewController {
    
    let knPermissionsFramework = KNPermissionsFramework()
    
    var permissionType: PermissionType? = nil

    override func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        tableView.deselectRow(at: indexPath, animated: true)
        
        switch (indexPath.row) {
        case 0:
            handlePermissionStatusPressed()
        case 1:
            handlePermissionRequestPressed()
        default:
            break
        }
    }
    
    private func handlePermissionStatusPressed() {
        guard let permissionType = permissionType else {
            return
        }
        let alertBuilder = AlertsAlertBuilder(viewController: self)
        switch(permissionType) {
        case .Bluetooth: knPermissionsFramework.permissionStatusBluetooth(alertBuilder: alertBuilder)
        case .Calendar: knPermissionsFramework.permissionStatusCalendar(alertBuilder: alertBuilder)
        case .Camera: knPermissionsFramework.permissionStatusCamera(alertBuilder: alertBuilder)
        case .Contacts: knPermissionsFramework.permissionStatusContacts(alertBuilder: alertBuilder)
        case .Location: knPermissionsFramework.permissionStatusLocation(alertBuilder: alertBuilder)
        case .Microphone: knPermissionsFramework.permissionStatusMicrophone(alertBuilder: alertBuilder)
        case .Notifications(let options): knPermissionsFramework.permissionStatusNotifications(alertBuilder: alertBuilder, options: UInt64(options.rawValue))
        case .Storage: knPermissionsFramework.permissionStatusStorage(alertBuilder: alertBuilder)
        }
    }
    
    private func handlePermissionRequestPressed() {
        guard let permissionType = permissionType else {
            return
        }
        let alertBuilder = AlertsAlertBuilder(viewController: self)
        switch(permissionType) {
        case .Bluetooth: knPermissionsFramework.permissionRequestBluetooth(alertBuilder: alertBuilder)
        case .Calendar: knPermissionsFramework.permissionRequestCalendar(alertBuilder: alertBuilder)
        case .Camera: knPermissionsFramework.permissionRequestCamera(alertBuilder: alertBuilder)
        case .Contacts: knPermissionsFramework.permissionRequestContacts(alertBuilder: alertBuilder)
        case .Location: knPermissionsFramework.permissionRequestLocation(alertBuilder: alertBuilder)
        case .Microphone: knPermissionsFramework.permissionRequestMicrophone(alertBuilder: alertBuilder)
        case .Notifications(let options): knPermissionsFramework.permissionRequestNotifications(alertBuilder: alertBuilder, options: UInt64(options.rawValue))
        case .Storage: knPermissionsFramework.permissionRequestStorage(alertBuilder: alertBuilder)
        }
    }

}
