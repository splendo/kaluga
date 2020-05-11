//
//  FeaturesListViewController.swift
//  Demo
//
//  Created by Gijs van Veen on 11/05/2020.
//  Copyright © 2020 Splendo. All rights reserved.
//

import UIKit
import KotlinNativeFramework

class FeaturesListViewController : UITableViewController {
    
    private lazy var viewModel: SharedFeatureListViewModel = KNArchitectureFramework().createFeatureListViewModel(parent: self)
    private let disposeBag = ArchitectureDisposeBag()

    private var features = [String]()
    private var onSelected: ((KotlinInt) -> KotlinUnit)? = nil
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        viewModel.didResume()
        
        viewModel.observeFeatures(disposeBag: disposeBag) { (features: [String], onSelected: @escaping (KotlinInt) -> KotlinUnit) in
            self.features = features
            self.onSelected = onSelected
            self.tableView.reloadData()
        }
    }
    
    override func viewDidDisappear(_ animated: Bool) {
        super.viewDidDisappear(animated)
        viewModel.didPause()
        disposeBag.dispose()
    }
    
    override func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    
    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return features.count
    }
    
    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: FeaturesListCell.Const.identifier, for: indexPath) as! FeaturesListCell
        cell.label.text = features[indexPath.row]
        return cell
    }
    
    override func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let _ = onSelected?(KotlinInt.init(int: Int32(indexPath.row)))
        tableView.deselectRow(at: indexPath, animated: true)
    }
    
}

class FeaturesListCell : UITableViewCell {
    
    struct Const {
        static let identifier = "FeaturesListCell"
    }
    
    @IBOutlet weak var label: UILabel!
    
}
