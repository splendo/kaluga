package com.splendo.kaluga.location
/*

Copyright 2019 Splendo Consulting B.V. The Netherlands

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

import com.splendo.kaluga.flow.BaseFlowable
import com.splendo.kaluga.location.Location.UnknownReason
import com.splendo.kaluga.log.debug

open class BaseLocationFlowable : BaseFlowable<Location>() {

    private var lastLocation: Location.KnownLocation? = null

    suspend fun set(location: Location.KnownLocation) {
        lastLocation = location
        super.set(location)
    }

    suspend fun setUnknownLocation(reason: UnknownReason = UnknownReason.NOT_CLEAR) {
        val location = lastLocation?.let {
            Location.UnknownLocationWithLastLocation(it, reason)
        } ?: Location.UnknownLocationWithNoLastLocation(reason)
        debug(TAG, "Send to channel: $location")
        set(location)
        debug(TAG, "unknown location sent")
    }

    companion object {
        const val TAG = "BaseLocationFlowable"
    }

}