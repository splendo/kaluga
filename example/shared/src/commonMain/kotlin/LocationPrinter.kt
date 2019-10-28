package com.splendo.kaluga.example.shared

import com.splendo.kaluga.location.*
import com.splendo.kaluga.runBlocking
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import com.splendo.kaluga.log.debug

class LocationPrinter(val loc:LocationFlowable) {

    fun printTo(printer:(String)->Unit) {
        runBlocking {
            MainScope()
                    .launch {
                        debug("main..")
                        loc.flow().collect {
                            debug("collecting...")
                            printer("received location: $it")
                            debug("location: $it")
                        }
                        debug("bye main")
                    }

        }
    }

}