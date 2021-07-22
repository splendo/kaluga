package permissions

import com.splendo.kaluga.permissions.Permissions
import com.splendo.kaluga.permissions.PermissionsBuilder
import com.splendo.kaluga.permissions.bluetooth.registerBluetoothPermission
import com.splendo.kaluga.permissions.calendar.registerCalendarPermission
import com.splendo.kaluga.permissions.camera.registerCameraPermission
import com.splendo.kaluga.permissions.contacts.registerContactsPermission
import com.splendo.kaluga.permissions.location.registerLocationPermission
import com.splendo.kaluga.permissions.microphone.registerMicrophonePermission
import com.splendo.kaluga.permissions.notifications.registerNotificationsPermission
import com.splendo.kaluga.permissions.storage.registerStoragePermission

class KNPermissionsFramework {
    fun getPermissions(): Permissions {
        return Permissions(
            PermissionsBuilder().apply {
                registerBluetoothPermission()
                registerCameraPermission()
                registerStoragePermission()
                registerLocationPermission()
                registerNotificationsPermission()
                registerContactsPermission()
                registerMicrophonePermission()
                registerCalendarPermission()
            }
        )
    }
}