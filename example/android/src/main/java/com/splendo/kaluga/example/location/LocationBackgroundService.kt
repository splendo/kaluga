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

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.splendo.kaluga.example.R
import com.splendo.kaluga.location.BaseLocationManager
import com.splendo.kaluga.location.Location
import com.splendo.kaluga.location.LocationStateRepoBuilder
import com.splendo.kaluga.location.location
import com.splendo.kaluga.logging.RestrictedLogLevel
import com.splendo.kaluga.logging.RestrictedLogger
import com.splendo.kaluga.permissions.location.LocationPermission
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class LocationBackgroundService : androidx.lifecycle.LifecycleService() {

    companion object {
        private const val NOTIFICATION_ID = 1
        private const val CHANNEL_ID = "location_channel"
        private const val CHANNEL_NAME = "Kaluga Location"

        private val locationPermission = LocationPermission(background = true, precise = true)
    }

    private val notificationService by lazy { applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager }
    private val repoBuilder: LocationStateRepoBuilder by inject()

    override fun onCreate() {
        super.onCreate()

        val locationRepo = repoBuilder.create(
            locationPermission,
            { permission, permissions ->
                BaseLocationManager.Settings(
                    permission,
                    permissions,
                    logger = RestrictedLogger(RestrictedLogLevel.None),
                )
            },
            lifecycleScope.coroutineContext,
        )

        lifecycleScope.launch {
            locationRepo.location()
                .map(::format)
                .flowWithLifecycle(lifecycle, Lifecycle.State.CREATED)
                .collect { message ->
                    if (ActivityCompat.checkSelfPermission(
                            this@LocationBackgroundService,
                            Manifest.permission.POST_NOTIFICATIONS,
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        NotificationManagerCompat.from(applicationContext)
                            .notify(NOTIFICATION_ID, getNotification(message))
                    }
                }
        }

        startForeground(NOTIFICATION_ID, getNotification(""))
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            stopForeground(STOP_FOREGROUND_REMOVE)
        } else {
            @Suppress("DEPRECATION")
            stopForeground(true)
        }
        NotificationManagerCompat.from(applicationContext).cancel(NOTIFICATION_ID)
    }

    private fun createChannelIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && notificationService.getNotificationChannel(CHANNEL_ID) == null) {
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT).apply {
                setSound(null, null)
                enableVibration(false)
                setShowBadge(false)
            }
            notificationService.createNotificationChannel(channel)
        }
    }

    private fun getNotification(message: String): Notification {
        createChannelIfNeeded()
        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(applicationContext.getString(R.string.location_background))
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setSound(null)
            .setVibrate(null)
            .setBadgeIconType(NotificationCompat.BADGE_ICON_NONE)
            .build()
        notification.flags = Notification.FLAG_NO_CLEAR or Notification.FLAG_ONGOING_EVENT
        return notification
    }
}

private fun format(location: Location): String = when (location) {
    is Location.KnownLocation -> "${location.latitudeDMS} ${location.longitudeDMS}"
    is Location.UnknownLocation -> "Unknown Location. Reason: ${location.reason.name}"
}
