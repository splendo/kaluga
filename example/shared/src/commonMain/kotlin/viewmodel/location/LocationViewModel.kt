/*
 Copyright 2020 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.example.shared.viewmodel.location

import com.splendo.kaluga.architecture.observable.toObservable
import com.splendo.kaluga.architecture.viewmodel.BaseViewModel
import com.splendo.kaluga.base.flow.HotFlowable
import com.splendo.kaluga.location.Location
import com.splendo.kaluga.location.LocationStateRepoBuilder
import com.splendo.kaluga.location.location
import com.splendo.kaluga.permissions.Permission
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class LocationViewModel(permission: Permission.Location, repoBuilder: LocationStateRepoBuilder) : BaseViewModel() {

    private val locationStateRepo = repoBuilder.create(permission)

    private val _location = HotFlowable("")
    val location = _location.toObservable(coroutineScope)

    override fun onResume(scope: CoroutineScope) {
        scope.launch {
            locationStateRepo.flow().location().map { location ->
                when (location) {
                    is Location.KnownLocation -> "${location.latitudeDMS} ${location.longitudeDMS}"
                    is Location.UnknownLocation -> {
                        val lastKnownLocation = (location as? Location.UnknownLocation.WithLastLocation)?.let {
                            " Last Known Location: ${location.lastKnownLocation.latitudeDMS} ${location.lastKnownLocation.longitudeDMS}"
                        }

                        "Unknown Location. Reason: ${location.reason.name}${ lastKnownLocation ?: "" }"
                    }
                }
            }.collect {
                _location.set(it)
            }
        }
    }

    public override fun onCleared() {
        super.onCleared()
    }
}
