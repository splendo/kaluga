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
import com.splendo.kaluga.architecture.navigation.SingleValueNavigationAction
import com.splendo.kaluga.example.shared.platformspecific.compose.contacts.model.ContactDetails

/** Contacts navigation actions. */
sealed class ContactsNavigation<T>(value: T, type: NavigationBundleSpecType<T>) : SingleValueNavigationAction<T>(value, type) {
    object ShowContactsList : ContactsNavigation<Unit>(Unit, NavigationBundleSpecType.UnitType)

    sealed class ContactsListNavigation<T>(value: T, type: NavigationBundleSpecType<T>) : ContactsNavigation<T>(value, type) {
        class ShowContactDetails(contactDetails: ContactDetails) : ContactsListNavigation<ContactDetails>(
            contactDetails,
            NavigationBundleSpecType.SerializedType(ContactDetails.serializer())
        )
        object Close : ContactDetailsNavigation<Unit>(Unit, NavigationBundleSpecType.UnitType)
    }

    sealed class ContactDetailsNavigation<T>(value: T, type: NavigationBundleSpecType<T>) : ContactsNavigation<T>(value, type) {
        class SendEmail(email: String) : ContactDetailsNavigation<String>(
            email,
            NavigationBundleSpecType.StringType
        )
        object Close : ContactDetailsNavigation<Unit>(Unit, NavigationBundleSpecType.UnitType)
    }
}