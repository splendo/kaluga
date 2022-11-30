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

package com.splendo.kaluga.architecture.compose.navigation.result

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.splendo.kaluga.architecture.compose.navigation.Route
import com.splendo.kaluga.architecture.navigation.NavigationBundle
import com.splendo.kaluga.architecture.navigation.NavigationBundleSpec
import com.splendo.kaluga.architecture.navigation.NavigationBundleSpecRow
import com.splendo.kaluga.architecture.navigation.NavigationBundleSpecType
import com.splendo.kaluga.architecture.navigation.SingleValueNavigationSpec
import com.splendo.kaluga.architecture.navigation.toNavigationBundle
import com.splendo.kaluga.architecture.navigation.toTypedProperty

/**
 * Handles a [Route.Result] matching a given [NavigationBundleSpec]
 * @param spec The [NavigationBundleSpec] used to create the [Route.Result]
 * @param retain If `true` the result will be retained in the [NavBackStackEntry]. It will be deleted otherwise.
 * @param onResult Method for handling the received result
 */
@Composable
fun <R : NavigationBundleSpecRow<*>> NavHostController.HandleResult(
    spec: NavigationBundleSpec<R>,
    retain: Boolean = false,
    onResult: NavigationBundle<R>.() -> Unit
) = HandleResult(retain) { toNavigationBundle(spec).onResult() }

/**
 * Handles a [Route.Result] matching a given [NavigationBundleSpecType]
 * Requires that the [Route.Result] is described by a [SingleValueNavigationSpec] matching the [NavigationBundleSpecType]
 * @param type The [NavigationBundleSpecType] stored in the result
 * @param retain If `true` the result will be retained in the [NavBackStackEntry]. It will be deleted otherwise.
 * @param onResult Method for handling the received result
 */
@Composable
fun <R> NavHostController.HandleResult(
    type: NavigationBundleSpecType<R>,
    retain: Boolean = false,
    onResult: R.() -> Unit
) = HandleResult(retain) { toTypedProperty(type).onResult() }

@Composable
internal fun NavHostController.HandleResult(retain: Boolean = false, onResult: Bundle.() -> Unit) {
    // Check if we have a result in the current BackStack.
    val result = currentBackStackEntry?.savedStateHandle?.getStateFlow<Bundle?>(Route.Result.KEY, null)?.collectAsState()
    result?.value?.let {
        onResult(it)
        // If retain is set we keep the result, otherwise clean up.
        if (!retain) {
            currentBackStackEntry?.savedStateHandle?.remove<Bundle>(Route.Result.KEY)
        }
    }
}
