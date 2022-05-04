package location

import com.splendo.kaluga.location.LocationStateRepoBuilder
import permissions.KNPermissionsFramework

class KNLocationFramework {
    fun getPermissionRepoBuilder(): LocationStateRepoBuilder { return LocationStateRepoBuilder(permissions = KNPermissionsFramework().getPermissions()) }
}
