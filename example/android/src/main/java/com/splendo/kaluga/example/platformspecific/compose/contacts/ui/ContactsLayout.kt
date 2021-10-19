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

package com.splendo.kaluga.example.platformspecific.compose.contacts.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.material.composethemeadapter.MdcTheme
import com.splendo.kaluga.architecture.compose.navigation.RouteNavigator
import com.splendo.kaluga.architecture.compose.navigation.rememberCombinedNavigator
import com.splendo.kaluga.architecture.compose.navigation.route
import com.splendo.kaluga.example.platformspecific.compose.contacts.viewModel.contactsNavigationActivityMapper
import com.splendo.kaluga.example.platformspecific.compose.contacts.viewModel.contactsNavigationRouteMapper
import com.splendo.kaluga.example.shared.platformspecific.compose.contacts.model.ContactDetails
import com.splendo.kaluga.example.shared.platformspecific.compose.contacts.viewModel.ContactsNavigation
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

@Composable
fun ContactsLayout() {
    MdcTheme {
        val routeNavigator = RouteNavigator(
            rememberNavController(),
            ::contactsNavigationRouteMapper
        )

        val combinedNavigator = rememberCombinedNavigator { action: ContactsNavigation<*> ->
            when (action) {
                is ContactsNavigation.ContactsListNavigation.Close,
                is ContactsNavigation.ContactsListNavigation.ShowContactDetails,
                is ContactsNavigation.ShowContactsList,
                is ContactsNavigation.ContactDetailsNavigation.Close -> routeNavigator
                is ContactsNavigation.ContactDetailsNavigation.SendEmail ->
                    ::contactsNavigationActivityMapper.toActivityNavigator()
            }
        }

        routeNavigator.SetupNavHost(
            startDestination = route<ContactsNavigation.ShowContactsList>()
        ) {
            composable(route<ContactsNavigation.ShowContactsList>()) {
                ContactsListLayout(combinedNavigator)
            }

            composable(route<ContactsNavigation.ContactsListNavigation.ShowContactDetails>("{json}")) {
                val contactDetails: ContactDetails =
                    Json.decodeFromString(it.arguments!!.getString("json")!!)
                ContactDetailsLayout(contactDetails, combinedNavigator)
            }
        }
    }
}
