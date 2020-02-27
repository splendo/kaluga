package com.splendo.kaluga.example.location

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.splendo.kaluga.example.shared.LocationPrinter
import com.splendo.kaluga.location.LocationManager
import com.splendo.kaluga.location.LocationStateRepo
import com.splendo.kaluga.log.debug
import com.splendo.kaluga.permissions.Permission
import com.splendo.kaluga.permissions.location.LocationPermissionManagerBuilder
import com.splendo.kaluga.permissions.location.LocationPermissionStateRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class LocationBackgroundService : Service(), CoroutineScope {

    private lateinit var locationStateRepo: LocationStateRepo
    private var coroutineJob: Job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + coroutineJob

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()

        val locationManagerBuilder = LocationManager.Builder(applicationContext, inBackground = true)
        val locationPermissionStateRepo = LocationPermissionStateRepo(
            Permission.Location(
                background = true,
                precise = true
            ), LocationPermissionManagerBuilder(applicationContext)
        )
        locationStateRepo = LocationStateRepo(locationPermissionStateRepo,
            autoRequestPermission = true,
            autoEnableLocations = true,
            locationManagerBuilder = locationManagerBuilder
        )
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val result = super.onStartCommand(intent, flags, startId)

        launch {
            val printer = LocationPrinter(locationStateRepo)
            printer.printTo {
                debug("Background Location: $it")
            }
        }
        return result
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineJob.cancel()
    }
}