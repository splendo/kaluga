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
    // Do any additional setup after loading the view.
//        NSLog("%@", KotlinNativeFramework().helloFromKotlin())
        
        label.text = ""

        KotlinNativeFramework().location(label: label)
        
        let lm = CLLocationManager()
        lm.requestWhenInUseAuthorization()

        let cl = ComponentsLocationFlowable()
        cl.addCLLocationManager(locationManager: lm)

    }



}
