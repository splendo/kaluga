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
import com.splendo.kaluga.example.shared.viewmodel.location.LocationViewModel
import com.splendo.kaluga.example.shared.viewmodel.permissions.NotificationPermissionViewModel
import com.splendo.kaluga.permissions.location.LocationPermission
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.launch

class LocationBackgroundService : androidx.lifecycle.LifecycleService() {

    companion object {
        private const val NOTIFICATION_ID = 1
        private const val CHANNEL_ID = "location_channel"
        private const val CHANNEL_NAME = "Kaluga Location"

        private val locationPermission = LocationPermission(background = true, precise = true)
    }

    private val notificationService by lazy { applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager }

    private val viewModel = LocationViewModel(locationPermission)
    private val notificationsViewModel = NotificationPermissionViewModel()

    override fun onCreate() {
        super.onCreate()

        lifecycleScope.launch {
            combine(
                viewModel.location.stateFlow,
                notificationsViewModel.hasPermission.stateFlow,
            ) { message, hasNotificationsPermission ->
                if (hasNotificationsPermission) message else null
            }.flowWithLifecycle(lifecycle, Lifecycle.State.CREATED)
                .collect { message ->
                    if (message != null && ActivityCompat.checkSelfPermission(
                            this@LocationBackgroundService,
                            Manifest.permission.POST_NOTIFICATIONS,
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        NotificationManagerCompat.from(applicationContext)
                            .notify(NOTIFICATION_ID, getNotification(message))
                    }
                }
        }

        lifecycleScope.launch {
            notificationsViewModel.hasPermission.stateFlow
                .filterNot { it }
                .flowWithLifecycle(lifecycle, Lifecycle.State.CREATED)
                .collect {
                    notificationsViewModel.requestPermission()
                }
        }

        startForeground(NOTIFICATION_ID, getNotification(""))
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        viewModel.didResume()
        notificationsViewModel.didResume()
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.didPause()
        viewModel.onCleared()
        notificationsViewModel.didPause()
        notificationsViewModel.onCleared()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            stopForeground(STOP_FOREGROUND_REMOVE)
        } else {
            @Suppress("DEPRECATION")
            stopForeground(true)
        }
        NotificationManagerCompat.from(applicationContext).cancel(NOTIFICATION_ID)
    }

    private fun createChannelIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && notificationService.getNotificationChannel(
                CHANNEL_ID,
            ) == null
        ) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance)
            channel.setSound(null, null)
            channel.enableVibration(false)
            channel.setShowBadge(false)
            notificationService.createNotificationChannel(channel)
        }
    }

    private fun getNotification(message: String): Notification {
        createChannelIfNeeded()
        val builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
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
