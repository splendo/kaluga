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

package com.splendo.kaluga.example.shared.platformspecific.compose.contacts.viewModel

import com.splendo.kaluga.architecture.navigation.NavigationBundleSpecType
import com.splendo.kaluga.architecture.navigation.Navigator
import com.splendo.kaluga.architecture.navigation.SingleValueNavigationAction
import com.splendo.kaluga.architecture.observable.FlowInitializedObservable
import com.splendo.kaluga.architecture.observable.toInitializedObservable
import com.splendo.kaluga.architecture.viewmodel.NavigatingViewModel
import com.splendo.kaluga.example.shared.platformspecific.compose.contacts.model.ContactDetails
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

sealed class ContactsListNavigation<T>(value: T, type: NavigationBundleSpecType<T>) : SingleValueNavigationAction<T>(value, type) {
    data class ShowContactDetails(val contactDetails: ContactDetails) : ContactsListNavigation<ContactDetails>(
        contactDetails,
        NavigationBundleSpecType.SerializedType(ContactDetails.serializer())
    )
}

class ContactsListViewModel(
    navigator: Navigator<ContactsListNavigation<*>>
) : NavigatingViewModel<ContactsListNavigation<*>>(navigator) {
    val contacts: FlowInitializedObservable<List<ContactDetails>> =
        MutableStateFlow(emptyList<ContactDetails>())
            .also {
                coroutineScope.launch {
                    it.value = loadContacts()
                }
            }.toInitializedObservable(coroutineScope)

    private suspend fun loadContacts(): List<ContactDetails> {
        // a real call shall happen here
        return listOf("Alice", "Bob", "Charlie", "David").map { name ->
            ContactDetails(name = name, email = "${name.lowercase()}@example.com")
        }
    }

    fun onContactClick(contactDetails: ContactDetails) {
        navigator.navigate(
            ContactsListNavigation.ShowContactDetails(contactDetails)
        )
    }
}
