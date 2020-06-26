package location

import permissions.KNPermissionsFramework
import com.splendo.kaluga.location.LocationStateRepoBuilder

class KNLocationFramework {
    fun getPermissionRepoBuilder(): LocationStateRepoBuilder { return LocationStateRepoBuilder(permissions=KNPermissionsFramework().getPermissions()) }
}