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

    lazy var alertPresenter = KotlinNativeFramework().alertPresenter(parent: self)
    
    override func viewDidLoad() {
        super.viewDidLoad()

        label.text = KotlinNativeFramework().hello()

        let lm = CLLocationManager()
        lm.requestWhenInUseAuthorization()

        KotlinNativeFramework().location(label: label, locationManager: lm)
    }

    enum AlertIdentifier: String {
        case basic
        case awaiting
    }

    @IBAction func onShowAlert(_ sender: Any) {
        let action = ComponentsAlert.Action(title: "OK", style: .default_) {
            debugPrint("Handler called!")
        }
        let alert = ComponentsAlert(
            identifier: AlertIdentifier.basic.rawValue,
            title: "Hello",
            message: "World",
            style: .alert,
            actions: [action]
        )
        alertPresenter.show(alert: alert, animated: true) {
            debugPrint("Presenting completed")
        }
    }

    @IBAction func onShowWithDismiss(_ sender: Any) {
        let alert = ComponentsAlert(
            identifier: AlertIdentifier.awaiting.rawValue,
            title: "Alert",
            message: "Waiting...",
            style: .alert,
            actions: []
        )

        alertPresenter.show(alert: alert, animated: true) {
            DispatchQueue.main.asyncAfter(deadline: .now() + 3) {
                self.alertPresenter.dismiss(identifier: alert.identifier, animated: true)
            }
        }
    }
}
