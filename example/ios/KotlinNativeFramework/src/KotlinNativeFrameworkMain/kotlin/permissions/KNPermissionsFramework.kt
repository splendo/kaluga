package permissions

import com.splendo.kaluga.permissions.Permissions
import com.splendo.kaluga.permissions.PermissionsBuilder

class KNPermissionsFramework {
    fun getPermissions(): Permissions { return Permissions(PermissionsBuilder()) }
}