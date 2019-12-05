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
        switch (indexPath.row) {
        case 0: showLoadingIndicator(.system)
        case 1: showLoadingIndicator(.custom, title: "This is a custom title")
        default: ()
        }
    }

    fileprivate func showLoadingIndicator(_ style: LoadingIndicatorLoadingIndicatorStyle, title: String? = nil) {
        let indicator = KotlinNativeFrameworkKt.activityIndicator(
            viewController: self,
            style: style,
            title: title
        )
        indicator.show(animated: true)
        DispatchQueue.main.asyncAfter(deadline: .now() + 5) {
            indicator.dismiss(animated: true)
        }
    }
}
