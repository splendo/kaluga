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

package com.splendo.kaluga.example.platformspecific.compose.contacts.viewModel

import com.splendo.kaluga.architecture.compose.navigation.BACK_ROUTE
import com.splendo.kaluga.architecture.compose.navigation.route
import com.splendo.kaluga.architecture.navigation.NavigationSpec
import com.splendo.kaluga.example.shared.platformspecific.compose.contacts.viewModel.ContactsNavigation
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/** Maps a navigation action to a route string. */
internal fun contactsNavigationRouteMapper(action: ContactsNavigation<*>): String {
    return when (action) {
        is ContactsNavigation.ContactsListNavigation.Close -> BACK_ROUTE
        is ContactsNavigation.ContactsListNavigation.ShowContactDetails ->
            action.route(Json.encodeToString(action.bundle!!.get(action.type)))
        is ContactsNavigation.ShowContactsList,
        is ContactsNavigation.ContactDetailsNavigation.Close ->
            route<ContactsNavigation.ShowContactsList>()
        else -> throw IllegalStateException("Unsupported action: ${action::class.simpleName}")
    }
}

/** Maps a navigation action to a NavigationSpec. */
internal fun contactsNavigationActivityMapper(action: ContactsNavigation<*>): NavigationSpec {
    return when (action) {
        is ContactsNavigation.ContactDetailsNavigation.SendEmail -> NavigationSpec.Email(
            emailSettings = NavigationSpec.Email.EmailSettings(
                type = NavigationSpec.Email.Type.Plain,
                to = listOf(action.bundle!!.get(action.type))
            )
        )
        else -> throw IllegalStateException("Unsupported action: ${action::class.simpleName}")
    }
}
