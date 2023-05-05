/*
 Copyright 2023 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.location

import platform.CoreLocation.CLAuthorizationStatus
import platform.CoreLocation.CLLocationManager
import platform.CoreLocation.CLLocationManagerDelegateProtocol
import platform.darwin.NSObject

sealed class LocationManagerDelegate {

    abstract val delegate: CLLocationManagerDelegateProtocol
    abstract val updateState: () -> Unit

    class NewLocationManagerDelegate(
        override val updateState: () -> Unit
    ) : LocationManagerDelegate() {
        override val delegate: CLLocationManagerDelegateProtocol = object :
            NSObject(),
            CLLocationManagerDelegateProtocol {
            override fun locationManagerDidChangeAuthorization(manager: CLLocationManager) {
                updateState()
            }
        }
    }

    class OldLocationManagerDelegate(
        override val updateState: () -> Unit
    ) : LocationManagerDelegate() {
        override val delegate: CLLocationManagerDelegateProtocol = object :
            NSObject(),
            CLLocationManagerDelegateProtocol {
            override fun locationManager(
                manager: CLLocationManager,
                didChangeAuthorizationStatus: CLAuthorizationStatus
            ) {
                updateState()
            }
        }
    }
}
