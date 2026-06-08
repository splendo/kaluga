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

package com.splendo.kaluga.architecture.navigation

import com.splendo.kaluga.architecture.lifecycle.LifecycleSubscribable

/**
 * Action that describes the intent to navigate.
 * @param B the type of [NavigationBundleSpecRow] associated with this action.
 * @property bundle The [NavigationBundle] containing rows of [B] used to configure navigation.
 */
open class NavigationAction<B : NavigationBundleSpecRow<*>>(val bundle: NavigationBundle<B>?)

/**
 * A [NavigationAction] that has a [SingleValueNavigationSpec] bundle.
 * @param T the value type to be passed when navigating using this action.
 * @property value The value passed by the action
 * @property type The [NavigationBundleSpecType] associated with [T].
 */
open class SingleValueNavigationAction<T>(val value: T, val type: NavigationBundleSpecType<T>) :
    NavigationAction<SingleValueNavigationSpec.Row<T>>(
        SingleValueNavigationSpec(type).toBundle {
            it.convertValue(value)
        },
    )

/**
 * Exception thrown by a [Navigator]
 * @param message The message of this exception.
 */
open class NavigationException(message: String?) : RuntimeException(message)

/**
 * Class that can trigger a given [NavigationAction]
 * @param Action the type of [NavigationAction] this navigator should respond to.
 */
expect interface Navigator<Action : NavigationAction<*>> : LifecycleSubscribable {
    /**
     * Triggers a given [NavigationAction]
     * @param action The [Action] to trigger
     * @throws [NavigationException] if navigation fails.
     */
    fun navigate(action: Action)
}

/**
 * Triggers a given [NavigationAction] and returns `true` if it succeeded.
 * @param Action The type of [NavigationAction] to be given.
 * @param action The [Action] to trigger.
 * @return `true` if the navigation succeeded, false otherwise
 */
fun <Action : NavigationAction<*>> Navigator<Action>.navigateWithSuccess(action: Action): Boolean = try {
    navigate(action)
    true
} catch (e: NavigationException) {
    false
}

/**
 * Triggers a given [NavigationAction] or executes a closure if the navigation failed to complete.
 * @param Action The type of [NavigationAction] to be given.
 * @param action The [Action] to trigger.
 * @param onFailure Closure for handling case when navigation failed.
 */
fun <Action : NavigationAction<*>> Navigator<Action>.navigateOrElse(action: Action, onFailure: () -> Unit) {
    if (!navigateWithSuccess(action)) {
        onFailure()
    }
}
