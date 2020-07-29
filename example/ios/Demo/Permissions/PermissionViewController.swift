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

class PermissionViewController: UIViewController {
    
    private struct Const {
        static let storyboard = UIStoryboard(name: "Main", bundle: nil)
        static let permissionVc = "Permission"
        
        static let permissions = KNPermissionsFramework().getPermissions()
    }
    
    static func create(permission: Permission) -> PermissionViewController {
        let vc = Const.storyboard.instantiateViewController(withIdentifier: Const.permissionVc) as! PermissionViewController
        vc.viewModel = KNArchitectureFramework().createPermissionViewModel(permissions: Const.permissions, permission: permission)
        return vc
    }
    
    @IBOutlet weak var permissionStateLabel: UILabel!
    @IBOutlet weak var requestPermissionButton: UIButton!
    
    var viewModel: PermissionViewModel!
    private var lifecycleManager: LifecycleManager!
    
    deinit {
        lifecycleManager.unbind()
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        requestPermissionButton.setTitle(NSLocalizedString("permission_request", comment: ""), for: .normal)
        
        lifecycleManager = KNArchitectureFramework().bind(viewModel: viewModel, to: self, onLifecycleChanges: { [weak self] (disposeBag) in
            self?.viewModel.permissionStateMessage.observe(onNext: { (message) in
                self?.permissionStateLabel.text = NSLocalizedString(message as String? ?? "", comment: "")
                }).addTo(disposeBag: disposeBag)
            
            self?.viewModel.requestMessage.observe(onNext: { (optionalMessage) in
                guard let message = optionalMessage as String? else {
                    return
                }
                
                let alert = UIAlertController(title: NSLocalizedString("permission_request", comment: ""), message: NSLocalizedString(message, comment: ""), preferredStyle: .alert)
                alert.addAction(UIAlertAction(title: NSLocalizedString("OK", comment: ""), style: .default, handler: nil))
                self?.present(alert, animated: true, completion: nil)
                
                }).addTo(disposeBag: disposeBag)
            
            self?.viewModel.showPermissionButton.observe(onNext: { (show) in
                self?.requestPermissionButton.isHidden = !(show as? Bool ?? false)
                }).addTo(disposeBag: disposeBag)
        })
    }
    
    
    @IBAction func requestPermission(sender: Any?) {
        viewModel.requestPermission()
    }

}
