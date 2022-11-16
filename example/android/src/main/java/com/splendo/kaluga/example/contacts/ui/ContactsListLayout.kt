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

package com.splendo.kaluga.example.contacts.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.splendo.kaluga.architecture.compose.state
import com.splendo.kaluga.architecture.compose.viewModel.ViewModelComposable
import com.splendo.kaluga.architecture.compose.viewModel.store
import com.splendo.kaluga.architecture.navigation.Navigator
import com.splendo.kaluga.example.R
import com.splendo.kaluga.example.shared.model.contacts.ContactDetails
import com.splendo.kaluga.example.shared.viewmodel.contacts.ContactsListNavigation
import com.splendo.kaluga.example.shared.viewmodel.contacts.ContactsListViewModel

@Composable
fun ContactsListLayout(navigator: Navigator<ContactsListNavigation<*>>) {
    val viewModel = store {
        remember {
            ContactsListViewModel(navigator)
        }
    }

    ViewModelComposable(viewModel) {
        val items by contacts.state()
        LazyColumn(modifier = Modifier.padding(Padding.default)) {
            items(items) { contactDetails ->
                ListItem(contactDetails) {
                    onContactClick(contactDetails)
                }

                DefaultSpacer()
            }
        }
    }
}

@Composable
private fun ListItem(
    contactDetails: ContactDetails,
    onClickHandler: () -> Unit
) {
    Row(
        modifier = Modifier
            .clickable(onClick = onClickHandler)
            .fillMaxWidth()
    ) {
        val image = painterResource(id = R.drawable.ic_account)
        Image(
            modifier = Modifier.size(48.dp),
            painter = image,
            contentDescription = ""
        )

        DefaultSpacer()

        Text(
            modifier = Modifier.align(CenterVertically),
            text = contactDetails.name,
            style = MaterialTheme.typography.h5
        )
    }
}

@Composable
@Preview
private fun ListItemPreview() {
    ListItem(ContactDetails("name", "email")) { }
}
