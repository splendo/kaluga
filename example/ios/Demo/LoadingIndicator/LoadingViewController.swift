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

    override func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        tableView.deselectRow(at: indexPath, animated: true)
        if indexPath.row == 0 {
            showLoadingIndicator()
        }
    }

    fileprivate func showLoadingIndicator() {
        let view = ActivityViewController(nibName: nil, bundle: nil)
        let indicator = KotlinNativeFramework().loadingIndicator(view: view)
        indicator.present(controller: self, animated: true) {
            DispatchQueue.main.asyncAfter(deadline: .now() + 5) {
                indicator.dismiss(animated: true) { }
            }
        }
    }
}
