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

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.splendo.kaluga.architecture.navigation.NavigationBundle
import com.splendo.kaluga.architecture.navigation.NavigationBundleSpec
import com.splendo.kaluga.architecture.navigation.NavigationBundleSpecRow
import com.splendo.kaluga.architecture.navigation.NavigationBundleSpecType
import com.splendo.kaluga.architecture.viewmodel.BaseLifecycleViewModel
import kotlinx.serialization.KSerializer
import kotlin.reflect.KClass
import kotlin.reflect.safeCast

/**
 * A Handler that will allow a [NavHostController] to handle [Route.Result] of a given type [R] to be received by a given [ViewModel]
 */
sealed class NavHostResultHandler<ViewModel : BaseLifecycleViewModel, R> {
    abstract val viewModelClass: KClass<ViewModel>
    abstract val retain: Boolean
    abstract val onResult: ViewModel.(R) -> Unit
    @Composable
    internal abstract fun NavHostController.HandleResult(callback: (R) -> Unit)

    /**
     * [NavHostResultHandler] that handles a result of a [NavigationBundle]
     */
    data class Bundle<ViewModel : BaseLifecycleViewModel, R : NavigationBundleSpecRow<*>>(
        override val viewModelClass: KClass<ViewModel>,
        val spec: NavigationBundleSpec<R>,
        override val retain: Boolean = false,
        override val onResult: ViewModel.(NavigationBundle<R>) -> Unit
    ) : NavHostResultHandler<ViewModel, NavigationBundle<R>>() {
        @Composable
        override fun NavHostController.HandleResult(callback: (NavigationBundle<R>) -> Unit) = HandleResult(spec, retain) { callback(this) }
    }

    /**
     * [NavHostResultHandler] that handles a result of a [NavigationBundleSpecType]
     */
    data class Type<ViewModel : BaseLifecycleViewModel, R>(
        override val viewModelClass: KClass<ViewModel>,
        val spec: NavigationBundleSpecType<R>,
        override val retain: Boolean,
        override val onResult: ViewModel.(R) -> Unit
    ) : NavHostResultHandler<ViewModel, R>() {

        @Composable
        override fun NavHostController.HandleResult(callback: (R) -> Unit) = HandleResult(spec, retain) { callback(this) }
    }

    @Composable
    internal fun HandleResult(viewModel: BaseLifecycleViewModel, navHostController: NavHostController) {
        viewModelClass.safeCast(viewModel)?.let { vm ->
            navHostController.HandleResult { vm.onResult(it) }
        }
    }
}

/**
 * Creates a [NavHostResultHandler.Bundle] of [R] for this [NavigationBundleSpec]
 */
inline fun <reified ViewModel : BaseLifecycleViewModel, R : NavigationBundleSpecRow<*>> NavigationBundleSpec<R>.NavHostResultHandler(
    retain: Boolean = false,
    noinline onResult: ViewModel.(NavigationBundle<R>) -> Unit
) = NavHostResultHandler.Bundle(ViewModel::class, this, retain, onResult)

/**
 * Creates a [NavHostResultHandler.Type] of [R] for this [NavigationBundleSpecType]
 */
inline fun <reified ViewModel : BaseLifecycleViewModel, R> NavigationBundleSpecType<R>.NavHostResultHandler(
    retain: Boolean = false,
    noinline onResult: ViewModel.(R) -> Unit
) = NavHostResultHandler.Type(ViewModel::class, this, retain, onResult)

/**
 * Creates a [NavHostResultHandler.Type] of [R] for this [KSerializer]
 */
inline fun <reified ViewModel : BaseLifecycleViewModel, R> KSerializer<R>.NavHostResultHandler(
    retain: Boolean = false,
    noinline onResult: ViewModel.(R) -> Unit
) = NavHostResultHandler.Type(ViewModel::class, NavigationBundleSpecType.SerializedType(this), retain, onResult)
