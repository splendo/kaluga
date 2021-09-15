/*
 Copyright 2021 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.architecture.compose.navigation

import com.splendo.kaluga.architecture.lifecycle.LifecycleSubscribable
import com.splendo.kaluga.architecture.lifecycle.LifecycleSubscriber
import com.splendo.kaluga.architecture.navigation.ActivityNavigator
import com.splendo.kaluga.architecture.navigation.NavigationAction
import com.splendo.kaluga.architecture.navigation.NavigationSpec
import com.splendo.kaluga.architecture.navigation.Navigator

/** Routes navigation actions to underlying navigators defined by [navigatorForAction]. */
class CombinedNavigator<A : NavigationAction<*>>(
    private val navigatorForAction: CombinedNavigator<A>.(A) -> Navigator<A>
): Navigator<A>, LifecycleSubscribable by LifecycleSubscriber() {

    /** @return [ActivityNavigator] constructed with this navigation mapper.*/
    fun ((A) -> NavigationSpec).toActivityNavigator() = ActivityNavigator(this)

    override fun navigate(action: A) {
        val navigator = navigatorForAction(action)
        if (navigator is LifecycleSubscribable && navigator.manager == null) {
            // navigator depends on the lifecycle but was not subscribed yet
            manager?.let { manager ->
                with (navigator) {
                    subscribe(manager)
                    navigate(action)
                    unsubscribe()
                }
            }
        } else {
            navigator.navigate(action)
        }
    }
}
