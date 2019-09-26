import com.splendo.mpp.location.Location
import com.splendo.mpp.location.LocationFlowable
import com.splendo.mpp.log.debug
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

    fun hello() = com.splendo.mpp.example.shared.helloCommon()

    fun location(label:UILabel, locationManager: CLLocationManager) {
        loc.addCLLocationManager()

       runBlocking {
           MainScope()
                   .launch {
                       debug("main..")
                       loc.flow().collect {
                           debug("collecting...")
                           label.text = "received location: $it"
                           debug("location: $it")
                       }
                       debug("bye main")
                   }

        }
        debug("proceed executing after launching coroutine")

    }
}