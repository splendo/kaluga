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

package com.splendo.kaluga.example.platformspecific.compose.contacts.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.google.android.material.composethemeadapter.MdcTheme
import com.splendo.kaluga.architecture.compose.navigation.RouteNavigator
import com.splendo.kaluga.architecture.compose.navigation.SetupNavHost
import com.splendo.kaluga.architecture.compose.navigation.composable
import com.splendo.kaluga.architecture.navigation.NavigationBundleSpecType
import com.splendo.kaluga.example.platformspecific.compose.contacts.viewModel.contactListNavigationRouteMapper
import com.splendo.kaluga.example.shared.platformspecific.compose.contacts.model.ContactDetails
import com.splendo.kaluga.example.shared.platformspecific.compose.contacts.viewModel.ContactsListNavigation

@Composable
fun ContactsLayout() {
    MdcTheme {
        val navigator = RouteNavigator(
            rememberNavController(),
            ::contactListNavigationRouteMapper
        )

        navigator.SetupNavHost(
            rootView = {
                ContactsListLayout(navigator = navigator)
            }
        ) { navHostController ->
            composable<ContactDetails, ContactsListNavigation.ShowContactDetails>(
                type = NavigationBundleSpecType.SerializedType(
                    ContactDetails.serializer()
                )
            ) { details ->
                ContactDetailsLayout(details, navHostController)
            }
        }
    }
}
