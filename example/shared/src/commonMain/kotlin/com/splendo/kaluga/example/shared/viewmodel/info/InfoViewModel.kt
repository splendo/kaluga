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

package com.splendo.kaluga.example.shared.viewmodel.info

import com.splendo.kaluga.architecture.navigation.NavigationBundleSpecType
import com.splendo.kaluga.architecture.navigation.Navigator
import com.splendo.kaluga.architecture.navigation.SingleValueNavigationAction
import com.splendo.kaluga.architecture.observable.observableOf
import com.splendo.kaluga.architecture.viewmodel.NavigatingViewModel
import com.splendo.kaluga.review.ReviewManager
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable
data class DialogSpec(val title: String, val message: String)

@Serializable
data class MailSpec(val to: List<String>, val subject: String)

sealed class InfoNavigation<T>(value: T, type: NavigationBundleSpecType<T>) : SingleValueNavigationAction<T>(value, type) {

    class Dialog(title: String, message: String) : InfoNavigation<DialogSpec>(DialogSpec(title, message), NavigationBundleSpecType.SerializedType(DialogSpec.serializer()))
    class Link(link: String) : InfoNavigation<String>(link, NavigationBundleSpecType.StringType)
    class Mail(to: List<String>, subject: String) : InfoNavigation<MailSpec>(MailSpec(to, subject), NavigationBundleSpecType.SerializedType(MailSpec.serializer()))
}

class InfoViewModel(reviewManagerBuilder: ReviewManager.Builder, navigator: Navigator<InfoNavigation<*>>) :
    NavigatingViewModel<InfoNavigation<*>>(navigator, reviewManagerBuilder) {

    sealed class Button(val title: String) {
        data object About : Button("About")
        data object Website : Button("Kaluga.io")
        data object GitHub : Button("GitHub")
        data object Mail : Button("Contact")
        data object Review : Button("Review")
    }

    val reviewManager = reviewManagerBuilder.create()
    val buttons = observableOf(listOf(Button.About, Button.Website, Button.GitHub, Button.Review, Button.Mail))

    fun onButtonPressed(button: Button) {
        when (button) {
            is Button.About -> InfoNavigation.Dialog("About Us", "Kaluga is developed by Splendo Consulting BV")
            is Button.Website -> InfoNavigation.Link("https://kaluga.splendo.com")
            is Button.GitHub -> InfoNavigation.Link("https://github.com/splendo/kaluga")
            is Button.Mail -> InfoNavigation.Mail(listOf("info@splendo.com"), ("Question about Kaluga"))
            is Button.Review -> {
                coroutineScope.launch {
                    reviewManager.attemptToRequestReview()
                }
                null
            }
        }?.let { navigator.navigate(it) }
    }
}
