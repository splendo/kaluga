//
//  DateTimePickerViewController.swift
//  Demo
//
//  Created by Gijs van Veen on 05/12/2020.
//  Copyright © 2020 Splendo. All rights reserved.
//

import UIKit
import KotlinNativeFramework

class DateTimePickerViewController : UIViewController {
    
    @IBOutlet private var timeLabel: UILabel!
    
    lazy var viewModel = DateTimePickerViewModel(dateTimePickerPresenterBuilder: DateTimePickerPresenter.Builder(viewController: self))
    private var lifecycleManager: LifecycleManager!
    
    deinit {
        lifecycleManager.unbind()
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        lifecycleManager = KNArchitectureFramework().bind(viewModel: viewModel, to: self, onLifecycleChanges: { [weak self] in
            guard let viewModel = self?.viewModel else {
                return []
            }
            return [
                viewModel.dateLabel.observe(onNext: { (time) in
                    if let timeString = (time as? String) {
                        self?.timeLabel.text = String(timeString)
                    }
            })
            ]
        })
    }
    
    @IBAction
    func selectDatePressed() {
        viewModel.onSelectDatePressed()
    }
    
    @IBAction
    func selectTimePressed() {
        viewModel.onSelectTimePressed()
    }
}
