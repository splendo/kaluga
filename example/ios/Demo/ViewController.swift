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
        KotlinNativeFramework().makeAlert(from: self, title: "Hello, Kaluga", buttonText: "OK") {
            debugPrint("OK")
        }.show(animated: true, completion: nil)
    }

    @IBAction func onShowWithDismiss(_ sender: Any) {
        let presenter = KotlinNativeFramework().makeAlert(from: self, title: "Hello, Kaluga", buttonText: "Wait for 3 sec") { }
        presenter.show(animated: true) {
            DispatchQueue.main.asyncAfter(deadline: .now() + 3) {
                presenter.dismiss(animated: true)
            }
        }
    }
}
