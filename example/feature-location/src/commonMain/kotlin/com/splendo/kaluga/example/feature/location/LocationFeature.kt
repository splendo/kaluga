/*
 Copyright 2026 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.example.feature.location

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.splendo.kaluga.example.arch.DetailScaffold
import com.splendo.kaluga.example.arch.FeatureContribution
import com.splendo.kaluga.location.LocationStateRepoBuilder
import com.splendo.kaluga.permissions.base.BasePermissionManager
import com.splendo.kaluga.permissions.base.Permissions
import com.splendo.kaluga.permissions.base.PermissionsBuilder
import com.splendo.kaluga.permissions.location.registerLocationPermissionIfNotRegistered
import org.koin.core.module.Module
import org.koin.dsl.bind
import org.koin.dsl.module
import kotlin.coroutines.CoroutineContext

class LocationContribution : FeatureContribution {
    override val id = "location"
    override val label = "Location"
    override fun register(builder: NavGraphBuilder, navController: NavController) {
        builder.composable(id) {
            DetailScaffold(title = label, onBack = { navController.popBackStack() }) {
                LocationScreen()
            }
        }
    }
}

/** Platform-specific [LocationStateRepoBuilder] factory — Android needs a `DefaultLocationManager.Builder`
 *  while Apple variants take only the permissions lambda. */
internal expect fun newLocationStateRepoBuilder(
    permissionsBuilder: suspend (CoroutineContext) -> Permissions,
): LocationStateRepoBuilder

val locationFeatureModule: Module = module {
    single {
        newLocationStateRepoBuilder { context ->
            val builder = get<PermissionsBuilder>()
            builder.registerLocationPermissionIfNotRegistered(
                settings = BasePermissionManager.Settings(logger = get()),
            )
            Permissions(builder, context)
        }
    }
    single { LocationContribution() } bind FeatureContribution::class
}
