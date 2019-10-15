//
//  ViewController.swift
//  Demo
//
//  Created by Tijl Houtbeckers on 2019-08-08.
//  Copyright © 2019 Splendo. All rights reserved.
//

import UIKit
import CoreLocation
import KotlinNativeFramework

class ViewController: UIViewController {

    //MARK: Properties

    @IBOutlet weak var label: UILabel!

    override func viewDidLoad() {
        super.viewDidLoad()

        label.text = KotlinNativeFramework().hello()

        let lm = CLLocationManager()
        lm.requestWhenInUseAuthorization()

        KotlinNativeFramework().location(label: label, locationManager: lm)
    }

    @IBAction func onShowAlert(_ sender: Any) {
        KotlinNativeFramework()
            .makeAlert(from: self, title: "Hello, Kaluga", message: nil, actions: [
                AlertsAlert.Action(title: "Default", style: .default_) { debugPrint("OK") },
                AlertsAlert.Action(title: "Destructive", style: .destructive) { debugPrint("Not OK") },
                AlertsAlert.Action(title: "Cancel", style: .cancel) { debugPrint("Cancel") },
            ])
            .show(animated: true, completion: nil)
    }

    @IBAction func onShowWithDismiss(_ sender: Any) {
        let presenter = KotlinNativeFramework().makeAlert(from: self, title: "Wait for 3 sec...", message: "Automatic dismissible", actions: [
            AlertsAlert.Action(title: "OK", style: .cancel) {},
        ])
        presenter.show(animated: true) {
            DispatchQueue.main.asyncAfter(deadline: .now() + 3) {
                presenter.dismiss(animated: true)
            }
        }
    }
}
