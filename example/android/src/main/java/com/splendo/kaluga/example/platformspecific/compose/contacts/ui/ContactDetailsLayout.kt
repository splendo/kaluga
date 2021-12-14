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

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.splendo.kaluga.architecture.compose.navigation.CombinedNavigator
import com.splendo.kaluga.architecture.compose.navigation.HardwareBackButtonNavigation
import com.splendo.kaluga.architecture.compose.navigation.NavHostRouteController
import com.splendo.kaluga.architecture.compose.navigation.RouteNavigator
import com.splendo.kaluga.architecture.compose.navigation.rememberCombinedNavigator
import com.splendo.kaluga.architecture.compose.viewModel.ViewModelComposable
import com.splendo.kaluga.architecture.compose.viewModel.store
import com.splendo.kaluga.architecture.navigation.Navigator
import com.splendo.kaluga.example.R
import com.splendo.kaluga.example.platformspecific.compose.contacts.viewModel.contactDetailsNavigationActivityMapper
import com.splendo.kaluga.example.platformspecific.compose.contacts.viewModel.contactDetailsNavigationRouteMapper
import com.splendo.kaluga.example.shared.platformspecific.compose.contacts.model.ContactDetails
import com.splendo.kaluga.example.shared.platformspecific.compose.contacts.viewModel.ContactDetailsNavigation
import com.splendo.kaluga.example.shared.platformspecific.compose.contacts.viewModel.ContactDetailsViewModel

@Composable
fun ContactDetailsLayout(contactDetails: ContactDetails, navHostController: NavHostController) {
    val routeNavigator = RouteNavigator(
        NavHostRouteController(navHostController),
        ::contactDetailsNavigationRouteMapper
    )

    val navigator = rememberCombinedNavigator { action: ContactDetailsNavigation<*> ->
        when (action) {
            is ContactDetailsNavigation.Close -> routeNavigator
            is ContactDetailsNavigation.SendEmail -> ::contactDetailsNavigationActivityMapper.toActivityNavigator()
        }
    }

    val viewModel = store {
        remember {
            ContactDetailsViewModel(contactDetails, navigator)
        }
    }
    ViewModelComposable(viewModel) {
        HardwareBackButtonNavigation(::back)

        Column {
            val image = painterResource(id = R.drawable.ic_account)
            Image(
                modifier = Modifier
                    .size(320.dp)
                    .align(CenterHorizontally),
                painter = image,
                contentDescription = ""
            )

            Column(
                modifier = Modifier.padding(horizontal = Padding.x2)
                    .fillMaxWidth()
            ) {
                Text(text = "Name:", style = MaterialTheme.typography.h6)
                Text(text = contactDetails.name, style = MaterialTheme.typography.body1)
                DefaultSpacer()

                Text(text = "Email:", style = MaterialTheme.typography.h6)
                Text(text = contactDetails.email, style = MaterialTheme.typography.body1)
                DefaultSpacer()

                Button(onClick = ::sendEmail) {
                    Text(text = sendEmailButtonText, style = MaterialTheme.typography.button)
                }
            }
        }
    }
}
