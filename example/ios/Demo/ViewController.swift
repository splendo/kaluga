//
//  ViewController.swift
//  Demo
//
//  Created by Tijl Houtbeckers on 2019-08-08.
//  Copyright © 2019 Splendo. All rights reserved.
//

import UIKit
import KotlinNativeFramework


class ViewController: UIViewController {

    override func viewDidLoad() {
    super.viewDidLoad()
    // Do any additional setup after loading the view.
        NSLog("%@", KotlinNativeFramework().helloFromKotlin())
    }



}
