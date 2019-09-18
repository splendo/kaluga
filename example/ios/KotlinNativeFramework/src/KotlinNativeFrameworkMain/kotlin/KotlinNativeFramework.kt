import com.splendo.mpp.example.shared.Shared
import com.splendo.mpp.location.Location
import com.splendo.mpp.location.LocationFlowable
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import platform.CoreLocation.CLLocationManager
import platform.UIKit.UILabel

//
//  KotlinNativeFramework.kt
//  KotlinNativeFramework
//
//  Created by Tijl Houtbeckers on 2019-08-08.
//  Copyright © 2019 Splendo. All rights reserved.
//

class KotlinNativeFramework {
    private val loc = LocationFlowable()

    init {
        println(Shared().helloCommon())
    }

    fun location(label:UILabel, locationManager: CLLocationManager) {
        loc.addCLLocationManager()

       runBlocking {
           MainScope()
                   .launch {
                       println("main..")
                       loc.flow().collect {
                           println("collecting...")
                           label.text = "received location: $it"
                           println("location: $it")
                       }
                       println("bye main")
                   }

       }

    }
}