//
//  PermissionListViewController.swift
//  Demo
//
//  Created by Gijs van Veen on 21/02/2020.
//  Copyright © 2020 Splendo. All rights reserved.
//

import UIKit
import KotlinNativeFramework

class PermissionListViewController : UITableViewController {
    
    private lazy var viewModel: SharedPermissionsListViewModel = KNArchitectureFramework().createPermissionListViewModel(parent: self) { (permission) -> UIViewController in
        return PermissionViewController.create(permission: permission)
    }
    private var lifecycleManager: ArchitectureLifecycleManager!

    private var permissions = [SharedPermissionView]()
    private var onSelected: ((KotlinInt) -> KotlinUnit)? = nil
    
    deinit {
        lifecycleManager.unbind()
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        lifecycleManager = KNArchitectureFramework().bind(viewModel: viewModel, to: self) { [weak self] (disposeBag) in
            guard let viewModel = self?.viewModel else { return }
            viewModel.observePermissions(disposeBag: disposeBag) { (permissionViews: [SharedPermissionView], onSelected: @escaping (KotlinInt) -> KotlinUnit) in
                self?.permissions = permissionViews
                self?.onSelected = onSelected
                self?.tableView.reloadData()
            }
        }
    }
    
    override func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    
    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return permissions.count
    }
    
    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: PermissionsListCell.Const.identifier, for: indexPath) as! PermissionsListCell
        cell.label.text = NSLocalizedString(permissions[indexPath.row].title, comment: "")
        return cell
    }
    
    override func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let _ = onSelected?(KotlinInt.init(int: Int32(indexPath.row)))
        tableView.deselectRow(at: indexPath, animated: true)
    }
    
}

class PermissionsListCell : UITableViewCell {
    
    struct Const {
        static let identifier = "PermissionsListCell"
    }
    
    @IBOutlet weak var label: UILabel!
    
}
