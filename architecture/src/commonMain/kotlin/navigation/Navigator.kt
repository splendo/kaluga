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

/**
 * Action that describes the intent to navigate
 * @param bundle The [NavigationBundle] containing data used to configure navigation
 */
open class NavigationAction<B : NavigationBundleSpecRow<*>>(val bundle: NavigationBundle<B>?)

/**
 * A [NavigationAction] that has a [SingleValueNavigationSpec] bundle
 * @param value The value passed by the action
 * @param type The [NavigationBundleSpecType] describing the object passed by the action
 */
open class SingleValueNavigationAction<T>(
    val value: T,
    val type: NavigationBundleSpecType<T>
) : NavigationAction<SingleValueNavigationSpec.Row<T>>(
    SingleValueNavigationSpec(type).toBundle {
        it.convertValue(value)
    }
)

/**
 * Exception thrown by a [Navigator]
 */
open class NavigationException(message: String?) : RuntimeException(message)

/**
 * Class that can trigger a given [NavigationAction]
 */
expect interface Navigator<A : NavigationAction<*>> {
    /**
     * Triggers a given [NavigationAction]
     * @param action The [NavigationAction] to trigger
     * @throws [NavigationException] if navigation fails.
     */
    fun navigate(action: A)
}

/**
 * Triggers a given [NavigationAction] and returns `true` if it succeeded.
 * @param action The [NavigationAction] to trigger.
 * @return `true` if the navigation succeeded, false otherwise
 */
fun <A : NavigationAction<*>> Navigator<A>.navigateWithSuccess(action: A): Boolean = try {
    navigate(action)
    true
} catch (e: NavigationException) {
    false
}

/**
 * Triggers a given [NavigationAction] or executes a closure if the navigation failed to complete
 * @param action The [NavigationAction] to trigger.
 * @param onFailure Closure for handling case when navigation failed.
 */
fun <A : NavigationAction<*>> Navigator<A>.navigateOrElse(action: A, onFailure: () -> Unit) {
    if (!navigateWithSuccess(action))
        onFailure()
}
