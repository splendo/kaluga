package com.splendo.kaluga.example.location

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.splendo.kaluga.example.R
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

    companion object {
        const val notificationId = 1
        const val channelId = "location_channel"
        const val channelName = "Kaluga Location"
    }

    private lateinit var locationStateRepo: LocationStateRepo
    private var coroutineJob: Job = Job()
    private var locationJob: Job? = null
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + coroutineJob

    private val notificationManager: NotificationManagerCompat by lazy { NotificationManagerCompat.from(applicationContext) }
    private val notificationService by lazy { applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager }

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

        locationJob = launch {
            val printer = LocationPrinter(locationStateRepo, this)
            printer.printTo{message ->
                createChannelIfNeeded()
                val builder = NotificationCompat.Builder(applicationContext, channelId)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentTitle(applicationContext.getString(R.string.location_background))
                    .setContentText(message)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setSound(null)
                    .setVibrate(null)
                    .setBadgeIconType(NotificationCompat.BADGE_ICON_NONE)
                val notification = builder.build()

                notification.flags = Notification.FLAG_NO_CLEAR or Notification.FLAG_ONGOING_EVENT

                NotificationManagerCompat.from(applicationContext).notify(notificationId, notification)
                debug("Background Location: $message")
            }
        }
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineJob.cancel()
        locationJob?.cancel()
        NotificationManagerCompat.from(applicationContext).cancel(notificationId)
    }

    private fun createChannelIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && notificationService.getNotificationChannel(
                channelId) == null) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, channelName, importance)
            channel.setSound(null, null)
            channel.enableVibration(false)
            channel.setShowBadge(false)
            notificationService.createNotificationChannel(channel)
        }
    }
}