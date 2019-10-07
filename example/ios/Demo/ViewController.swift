//
//  ViewController.swift
//  Demo
//
//  Created by Tijl Houtbeckers on 2019-08-08.
//  Copyright © 2019 Splendo. All rights reserved.
//

import UIKit
import KotlinNativeFramework
import CoreLocation

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

    class AlertIdentifier: ComponentsAlertIdentifier { }
    
    @IBAction func onShowAlert(_ sender: Any) {
        let action = ComponentsAlert.Action(title: "OK", style: .default_) {
            debugPrint("Handler called!")
        }
        let alert = ComponentsAlert(
            identifier: AlertIdentifier(),
            title: "Hello",
            message: "World",
            style: .alert,
            actions: [action]
        )
        KotlinNativeFramework().showAlert(alert: alert, parent: self, animated: true) {
            debugPrint("Presenting completed")
        }
    }
}
