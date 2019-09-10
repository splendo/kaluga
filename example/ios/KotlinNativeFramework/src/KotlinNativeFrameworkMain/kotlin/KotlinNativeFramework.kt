import com.splendo.mpp.location.LocationFlowable
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect

//
//  KotlinNativeFramework.kt
//  KotlinNativeFramework
//
//  Created by Tijl Houtbeckers on 2019-08-08.
//  Copyright © 2019 Splendo. All rights reserved.
//

class KotlinNativeFramework {
    fun helloFromKotlin() = "Hello from Kotlin!"
    fun helloFromKotlinLocation() {
        "Hello from Kotlin!"
    }

    val loc = LocationFlowable()

    init {
        println("yo")
        println("yay")
    }

    fun location() {

        MainScope()
        .launch {
            println("main..")
            loc.flow().collect {
                println("location: $it")
//                delay(600000000)
            }
            //delay(600000000)
    }
}





}