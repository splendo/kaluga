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

package com.splendo.kaluga.example.contacts.viewModel

import com.splendo.kaluga.architecture.compose.navigation.Route
import com.splendo.kaluga.architecture.compose.navigation.next
import com.splendo.kaluga.architecture.navigation.NavigationSpec
import com.splendo.kaluga.example.shared.viewmodel.contacts.ContactDetailsNavigation
import com.splendo.kaluga.example.shared.viewmodel.contacts.ContactsListNavigation

/** Maps a navigation action to a route string. */
internal fun contactListNavigationRouteMapper(action: ContactsListNavigation<*>): Route {
    return when (action) {
        is ContactsListNavigation.ShowContactDetails -> action.next
    }
}

/** Maps a navigation action to a route string. */
internal fun contactDetailsNavigationRouteMapper(action: ContactDetailsNavigation<*>): Route {
    return when (action) {
        is ContactDetailsNavigation.Close -> Route.Back
        else -> throw IllegalStateException("Unsupported action: ${action::class.simpleName}")
    }
}

/** Maps a navigation action to a NavigationSpec. */
internal fun contactDetailsNavigationActivityMapper(action: ContactDetailsNavigation<*>): NavigationSpec {
    return when (action) {
        is ContactDetailsNavigation.SendEmail -> NavigationSpec.Email(
            emailSettings = NavigationSpec.Email.EmailSettings(
                type = NavigationSpec.Email.Type.Plain,
                to = listOf(action.bundle!!.get(action.type))
            )
        )
        else -> throw IllegalStateException("Unsupported action: ${action::class.simpleName}")
    }
}
