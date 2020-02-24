//
//  PermissionListViewController.swift
//  Demo
//
//  Created by Gijs van Veen on 21/02/2020.
//  Copyright © 2020 Splendo. All rights reserved.
//

import UIKit

class PermissionListViewController: UITableViewController {

    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        guard let permissionViewController =  segue.destination as? PermissionViewController else {
            return
        }
        
        switch segue.identifier {
        case "BluetoothPermissionSegue": permissionViewController.permissionType = PermissionType.Bluetooth
        case "CalendarPermissionSegue": permissionViewController.permissionType = PermissionType.Calendar
        case "CameraPermissionSegue": permissionViewController.permissionType = PermissionType.Camera
        case "ContactsPermissionSegue": permissionViewController.permissionType = PermissionType.Contacts
        case "LocationPermissionSegue": permissionViewController.permissionType = PermissionType.Location
        case "MicrophonePermissionSegue": permissionViewController.permissionType = PermissionType.Microphone
        case "NotificationsPermissionSegue": permissionViewController.permissionType = PermissionType.Notifications(options: [UNAuthorizationOptions.alert, UNAuthorizationOptions.sound])
        case "StoragePermissionSegue": permissionViewController.permissionType = PermissionType.Storage
        default: ()
        }
    }
    
}
