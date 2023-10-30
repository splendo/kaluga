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

package com.splendo.kaluga.test.location

import com.splendo.kaluga.location.Location
import com.splendo.kaluga.location.LocationState
import com.splendo.kaluga.location.known
import com.splendo.kaluga.location.orUnknown
import com.splendo.kaluga.location.unknownLocationOf

sealed class MockLocationState {

    sealed class Inactive : MockLocationState()
    object NotInitialized : Inactive(), LocationState.NotInitialized {

        override val location: Location = Location.UnknownLocation.WithoutLastLocation(Location.UnknownLocation.Reason.NOT_CLEAR)
        fun startInitializing() = { Initializing(location) }
    }

    data class Deinitialized(override val location: Location) :
        Inactive(), LocationState.Deinitialized {
            override val reinitialize = suspend { Initializing(location) }
        }

    sealed class Active : MockLocationState() {
        abstract val location: Location
        val deinitialized: suspend () -> Deinitialized = { Deinitialized(location) }
    }

    class PermittedHandler(val location: Location) {
        val revokePermission = suspend { Disabled.NotPermitted(location) }
    }

    data class Initializing(
        override val location: Location,
    ) : Active(), LocationState.Initializing {

        override fun initialize(hasPermission: Boolean, enabled: Boolean): suspend () -> LocationState.Initialized =
            suspend {
                when {
                    !hasPermission -> Disabled.NotPermitted(location)
                    !enabled -> Disabled.NoGPS(location)
                    else -> Enabled(location.known.orUnknown)
                }
            }
    }

    data class Enabled(override val location: Location) : Active(), LocationState.Enabled {

        private val permittedHandler: PermittedHandler = PermittedHandler(location)

        override val disable = suspend {
            Disabled.NoGPS(location.unknownLocationOf(Location.UnknownLocation.Reason.NO_GPS))
        }

        override fun updateWithLocation(location: Location.KnownLocation): suspend () -> LocationState.Enabled = {
            copy(location = location)
        }

        override val revokePermission: suspend () -> Disabled.NotPermitted get() = permittedHandler.revokePermission
    }

    sealed class Disabled : Active() {

        class NoGPS(lastKnownLocation: Location) : Disabled(), LocationState.Disabled.NoGPS {

            override val location: Location = lastKnownLocation.unknownLocationOf(Location.UnknownLocation.Reason.NO_GPS)
            private val permittedHandler = PermittedHandler(location)

            override val enable: suspend () -> LocationState.Enabled = {
                Enabled(location.known.orUnknown)
            }

            override val revokePermission: suspend () -> NotPermitted = permittedHandler.revokePermission
        }

        class NotPermitted(lastKnownLocation: Location) : Disabled(), LocationState.Disabled.NotPermitted {

            override val location: Location = lastKnownLocation.unknownLocationOf(Location.UnknownLocation.Reason.PERMISSION_DENIED)

            override fun permit(enabled: Boolean): suspend () -> LocationState.Permitted = {
                if (enabled) Enabled(location.known.orUnknown) else NoGPS(location.unknownLocationOf(Location.UnknownLocation.Reason.NO_GPS))
            }
        }
    }
}
