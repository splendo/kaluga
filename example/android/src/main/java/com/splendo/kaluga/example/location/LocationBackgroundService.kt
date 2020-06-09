package com.splendo.kaluga.example.location

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.Observer
import com.splendo.kaluga.example.R
import com.splendo.kaluga.example.shared.viewmodel.location.LocationViewModel
import com.splendo.kaluga.permissions.Permission
import org.koin.core.KoinComponent
import org.koin.core.get

class LocationBackgroundService : androidx.lifecycle.LifecycleService(), KoinComponent {

    companion object {
        const val notificationId = 1
        const val channelId = "location_channel"
        const val channelName = "Kaluga Location"

        private val permission = Permission.Location(background = true, precise = true)
    }

    private val notificationService by lazy { applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager }

    private val viewModel = LocationViewModel(permission, get())

    override fun onCreate() {
        super.onCreate()

        viewModel.location.observe(this, Observer {message ->
            NotificationManagerCompat.from(applicationContext).notify(notificationId, getNotification(message))
        })

        startForeground(notificationId, getNotification(""))
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        viewModel.didResume()
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.didPause()
        viewModel.onCleared()
        stopForeground(true)
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

    private fun getNotification(message: String): Notification {
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
        return notification
    }
}
