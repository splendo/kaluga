/*
 Copyright 2022 Splendo Consulting B.V. The Netherlands

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

 */

package com.splendo.kaluga.example.location

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.splendo.kaluga.example.R
import com.splendo.kaluga.example.shared.viewmodel.location.LocationViewModel
import com.splendo.kaluga.permissions.location.LocationPermission

class LocationBackgroundService : androidx.lifecycle.LifecycleService() {

    companion object {
        const val notificationId = 1
        const val channelId = "location_channel"
        const val channelName = "Kaluga Location"

        private val permission = LocationPermission(background = true, precise = true)
    }

    private val notificationService by lazy { applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager }

    private val viewModel = LocationViewModel(permission)

    override fun onCreate() {
        super.onCreate()

        viewModel.location.observeInitialized { message ->
            NotificationManagerCompat.from(applicationContext).notify(notificationId, getNotification(message))
        }

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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            stopForeground(STOP_FOREGROUND_REMOVE)
        } else {
            stopForeground(true)
        }
        NotificationManagerCompat.from(applicationContext).cancel(notificationId)
    }

    private fun createChannelIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && notificationService.getNotificationChannel(
                channelId
            ) == null
        ) {
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
