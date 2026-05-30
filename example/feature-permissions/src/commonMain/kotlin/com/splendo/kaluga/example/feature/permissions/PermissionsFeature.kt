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

package com.splendo.kaluga.example.feature.permissions

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.savedstate.read
import com.splendo.kaluga.example.arch.DetailScaffold
import com.splendo.kaluga.example.arch.FeatureContribution
import com.splendo.kaluga.permissions.base.BasePermissionManager
import com.splendo.kaluga.permissions.base.PermissionsBuilder
import com.splendo.kaluga.permissions.registerAllPermissions
import org.koin.core.module.Module
import org.koin.dsl.bind
import org.koin.dsl.module

/** Toggle whether bluetooth scanning requires the location permission on Android. Lives in
 *  `:feature-permissions` because it influences how the permission factories are wired; the
 *  bluetooth feature reads it when registering its scanner. */
const val USE_LOCATION_FOR_BLUETOOTH = false

class PermissionsContribution : FeatureContribution {
    override val id = "permissions"
    override val label = "Permissions"
    override fun register(builder: NavGraphBuilder, navController: NavController) {
        builder.composable(id) {
            DetailScaffold(title = label, onBack = { navController.popBackStack() }) {
                PermissionsListScreen(onPermissionSelected = { permissionView ->
                    navController.navigate("permission/${permissionView.name}")
                })
            }
        }
        builder.composable(
            route = "permission/{name}",
            arguments = listOf(navArgument("name") { type = NavType.StringType }),
        ) { entry ->
            val name = entry.arguments?.read { getString("name") } ?: return@composable
            val permissionView = PermissionView.valueOf(name)
            DetailScaffold(title = permissionView.title, onBack = { navController.popBackStack() }) {
                PermissionScreen(permissionView)
            }
        }
    }
}

val permissionsFeatureModule: Module = module {
    single {
        PermissionsBuilder().apply {
            registerAllPermissions(settings = BasePermissionManager.Settings(logger = get()))
        }
    }
    single { PermissionsContribution() } bind FeatureContribution::class
}
