package location

import com.splendo.kaluga.location.LocationStateRepoBuilder

class KNLocationFramework {
    fun getPermissionRepoBuilder(): LocationStateRepoBuilder { return LocationStateRepoBuilder() }
}
