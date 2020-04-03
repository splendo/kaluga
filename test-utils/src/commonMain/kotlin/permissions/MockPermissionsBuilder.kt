package com.splendo.kaluga.test.permissions

import com.splendo.kaluga.permissions.BasePermissionsBuilder
import com.splendo.kaluga.permissions.Permission
import com.splendo.kaluga.permissions.PermissionManager
import com.splendo.kaluga.permissions.bluetooth.BaseBluetoothPermissionManagerBuilder
import com.splendo.kaluga.permissions.bluetooth.BluetoothPermissionStateRepo
import com.splendo.kaluga.permissions.calendar.BaseCalendarPermissionManagerBuilder
import com.splendo.kaluga.permissions.calendar.CalendarPermissionStateRepo
import com.splendo.kaluga.permissions.camera.BaseCameraPermissionManagerBuilder
import com.splendo.kaluga.permissions.camera.CameraPermissionStateRepo
import com.splendo.kaluga.permissions.contacts.BaseContactsPermissionManagerBuilder
import com.splendo.kaluga.permissions.contacts.ContactsPermissionStateRepo
import com.splendo.kaluga.permissions.location.BaseLocationPermissionManagerBuilder
import com.splendo.kaluga.permissions.location.LocationPermissionStateRepo
import com.splendo.kaluga.permissions.microphone.BaseMicrophonePermissionManagerBuilder
import com.splendo.kaluga.permissions.microphone.MicrophonePermissionStateRepo
import com.splendo.kaluga.permissions.notifications.BaseNotificationsPermissionManagerBuilder
import com.splendo.kaluga.permissions.notifications.NotificationsPermissionStateRepo
import com.splendo.kaluga.permissions.storage.BaseStoragePermissionManagerBuilder
import com.splendo.kaluga.permissions.storage.StoragePermissionStateRepo
import kotlinx.coroutines.CoroutineScope

class MockPermissionsBuilder : BasePermissionsBuilder {
    
    lateinit var bluetoothPMManager: MockPermissionManager<Permission.Bluetooth>
    override val bluetoothPMBuilder: BaseBluetoothPermissionManagerBuilder = object : BaseBluetoothPermissionManagerBuilder {
        override fun create(repo: BluetoothPermissionStateRepo, coroutineScope: CoroutineScope): PermissionManager<Permission.Bluetooth> {
            bluetoothPMManager = MockPermissionManager(repo, coroutineScope)
            return bluetoothPMManager
        }
    }
    
    lateinit var calendarPMManager: MockPermissionManager<Permission.Calendar>
    override val calendarPMBuilder: BaseCalendarPermissionManagerBuilder = object : BaseCalendarPermissionManagerBuilder {
        override fun create(calendar: Permission.Calendar, repo: CalendarPermissionStateRepo, coroutineScope: CoroutineScope): PermissionManager<Permission.Calendar> {
            calendarPMManager = MockPermissionManager(repo, coroutineScope)
            return calendarPMManager
        }
    }
    
    lateinit var cameraPMManager: MockPermissionManager<Permission.Camera>
    override val cameraPMBuilder: BaseCameraPermissionManagerBuilder = object : BaseCameraPermissionManagerBuilder {
        override fun create(repo: CameraPermissionStateRepo, coroutineScope: CoroutineScope): PermissionManager<Permission.Camera> {
            cameraPMManager = MockPermissionManager(repo, coroutineScope)
            return cameraPMManager
        }
    }

    lateinit var contactsPMManager: MockPermissionManager<Permission.Contacts>
    override val contactsPMBuilder: BaseContactsPermissionManagerBuilder = object : BaseContactsPermissionManagerBuilder {
        override fun create(contacts: Permission.Contacts, repo: ContactsPermissionStateRepo, coroutineScope: CoroutineScope): PermissionManager<Permission.Contacts> {
            contactsPMManager = MockPermissionManager(repo, coroutineScope)
            return contactsPMManager
        }
    }
    
    lateinit var locationPMManager: MockPermissionManager<Permission.Location>
    override val locationPMBuilder: BaseLocationPermissionManagerBuilder = object : BaseLocationPermissionManagerBuilder {
        override fun create(location: Permission.Location, repo: LocationPermissionStateRepo, coroutineScope: CoroutineScope): PermissionManager<Permission.Location> {
            locationPMManager = MockPermissionManager(repo, coroutineScope)
            return locationPMManager
        }
    }
    
    lateinit var microphonePMManager: MockPermissionManager<Permission.Microphone>
    override val microphonePMBuilder: BaseMicrophonePermissionManagerBuilder = object : BaseMicrophonePermissionManagerBuilder {
        override fun create(repo: MicrophonePermissionStateRepo, coroutineScope: CoroutineScope): PermissionManager<Permission.Microphone> {
            microphonePMManager = MockPermissionManager(repo, coroutineScope)
            return microphonePMManager
        }
    }
    
    lateinit var notificationsPMManager: MockPermissionManager<Permission.Notifications>
    override val notificationsPMBuilder: BaseNotificationsPermissionManagerBuilder = object : BaseNotificationsPermissionManagerBuilder {
        override fun create(notifications: Permission.Notifications, repo: NotificationsPermissionStateRepo, coroutineScope: CoroutineScope): PermissionManager<Permission.Notifications> {
            notificationsPMManager = MockPermissionManager(repo, coroutineScope)
            return notificationsPMManager
        }
    }
    
    lateinit var storagePMManager: MockPermissionManager<Permission.Storage>
    override val storagePMBuilder: BaseStoragePermissionManagerBuilder = object : BaseStoragePermissionManagerBuilder {
        override fun create(storage: Permission.Storage, repo: StoragePermissionStateRepo, coroutineScope: CoroutineScope): PermissionManager<Permission.Storage> {
            storagePMManager = MockPermissionManager(repo, coroutineScope)
            return storagePMManager
        }
    }
}