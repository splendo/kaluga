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

package com.splendo.kaluga.architecture.navigation

/**
 * Action that describes the intent to navigate
 * @param bundle The [NavigationBundle] containing data used to configure navigation
 */
abstract class NavigationAction<B : NavigationBundleSpecRow<*>>(val bundle: NavigationBundle<B>?)

/**
 * Class that can trigger a given [NavigationAction]
 */
expect class Navigator<A: NavigationAction<*>> {
    /**
     * Triggers a given [NavigationAction]
     * @param action The [NavigationAction] to trigger
     */
    fun navigate(action: A)
}