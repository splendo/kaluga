//
//  SystemViewController.swift
//  Demo
//
//  Created by Corrado Quattrocchi on 04/02/2021.
//  Copyright © 2021 Splendo. All rights reserved.
//

import Foundation
import UIKit
import KotlinNativeFramework

class SystemViewController : UITableViewController {
    
    private let knArchitectureFramework = KNArchitectureFramework()
    private lazy var viewModel: SystemViewModel = {
        return self.knArchitectureFramework.createSystemViewModel(parent: self)
    }()
    
    private var modules = [String]()
    private var onModuleTapped: ((KotlinInt) -> KotlinUnit)? = nil
    private var lifecycleManager: LifecycleManager!
    
    deinit {
        lifecycleManager.unbind()
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        lifecycleManager = knArchitectureFramework.bind(viewModel: viewModel, to: self) { [weak self] in
            guard let viewModel = self?.viewModel else {
                return []
            }
            return [
                viewModel.observeModules { (modules: [String], onButtonTapped: @escaping (KotlinInt) -> KotlinUnit) in
                    self?.modules = modules
                    self?.onModuleTapped = onButtonTapped
                    self?.tableView.reloadData()
                }
            ]
        }
    }
    
    override func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    
    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return modules.count
    }
    
    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: SystemListCell.Const.identifier, for: indexPath) as! SystemListCell
        cell.label.text = modules[indexPath.row]
        return cell
    }
    
    override func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let _ = onModuleTapped?(KotlinInt.init(int: Int32(indexPath.row)))
        tableView.deselectRow(at: indexPath, animated: true)
    }
    
}

class SystemListCell : UITableViewCell {
    
    struct Const {
        static let identifier = "SystemListCell"
    }
    
    @IBOutlet weak var label: UILabel!
    
}
