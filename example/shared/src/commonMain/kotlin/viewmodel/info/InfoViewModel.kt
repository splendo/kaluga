/*
 Copyright 2020 Splendo Consulting B.V. The Netherlands

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

import com.splendo.kaluga.architecture.navigation.NavigationAction
import com.splendo.kaluga.architecture.navigation.NavigationBundle
import com.splendo.kaluga.architecture.navigation.NavigationBundleSpec
import com.splendo.kaluga.architecture.navigation.NavigationBundleSpecRow
import com.splendo.kaluga.architecture.navigation.NavigationBundleSpecType
import com.splendo.kaluga.architecture.navigation.Navigator
import com.splendo.kaluga.architecture.navigation.toBundle
import com.splendo.kaluga.architecture.observable.observableOf
import com.splendo.kaluga.architecture.viewmodel.NavigatingViewModel
import com.splendo.kaluga.review.ReviewManager
import kotlinx.coroutines.launch

class DialogSpec : NavigationBundleSpec<DialogSpecRow>(setOf(DialogSpecRow.TitleRow, DialogSpecRow.MessageRow))

sealed class DialogSpecRow : NavigationBundleSpecRow<String>(NavigationBundleSpecType.StringType) {
    object TitleRow : DialogSpecRow()
    object MessageRow : DialogSpecRow()
}

class LinkSpec : NavigationBundleSpec<LinkSpecRow>(setOf(LinkSpecRow.LinkRow))

sealed class LinkSpecRow : NavigationBundleSpecRow<String>(NavigationBundleSpecType.StringType) {
    object LinkRow : LinkSpecRow()
}

class MailSpec : NavigationBundleSpec<MailSpecRow<*>>(setOf(MailSpecRow.ToRow, MailSpecRow.SubjectRow))

sealed class MailSpecRow<V>(associatedType: NavigationBundleSpecType<V>) : NavigationBundleSpecRow<V>(associatedType) {
    object ToRow : MailSpecRow<List<String>>(NavigationBundleSpecType.StringArrayType)
    object SubjectRow : MailSpecRow<String>(NavigationBundleSpecType.StringType)
}

sealed class InfoNavigation<B : NavigationBundleSpecRow<*>>(bundle: NavigationBundle<B>) : NavigationAction<B>(bundle) {

    class Dialog(bundle: NavigationBundle<DialogSpecRow>) : InfoNavigation<DialogSpecRow>(bundle)
    class Link(bundle: NavigationBundle<LinkSpecRow>) : InfoNavigation<LinkSpecRow>(bundle)
    class Mail(bundle: NavigationBundle<MailSpecRow<*>>) : InfoNavigation<MailSpecRow<*>>(bundle)
}

class InfoViewModel(
    val reviewManagerBuilder: ReviewManager.Builder,
    navigator: Navigator<InfoNavigation<*>>
) : NavigatingViewModel<InfoNavigation<*>>(navigator) {

    sealed class Button(val title: String) {
        object About : Button("About")
        object Website : Button("Kaluga.io")
        object GitHub : Button("GitHub")
        object Mail : Button("Contact")
        object Review : Button("Review")
    }

    val reviewManager = reviewManagerBuilder.create()
    val buttons = observableOf(listOf(Button.About, Button.Website, Button.GitHub, Button.Review, Button.Mail))

    fun onButtonPressed(button: Button) {
        when (button) {
            is Button.About -> InfoNavigation.Dialog(DialogSpec().toBundle { row ->
                when (row) {
                    is DialogSpecRow.TitleRow -> row.convertValue("About Us")
                    is DialogSpecRow.MessageRow -> row.convertValue("Kaluga is developed by Splendo Consulting BV")
                }
            })
            is Button.Website -> InfoNavigation.Link(LinkSpec().toBundle { row ->
                when (row) {
                    is LinkSpecRow.LinkRow -> row.convertValue("https://kaluga.splendo.com")
                }
            })
            is Button.GitHub -> InfoNavigation.Link(LinkSpec().toBundle { row ->
                when (row) {
                    is LinkSpecRow.LinkRow -> row.convertValue("https://github.com/splendo/kaluga")
                }
            })
            is Button.Mail -> InfoNavigation.Mail(MailSpec().toBundle { row ->
                when (row) {
                    is MailSpecRow.ToRow -> row.convertValue(listOf("info@splendo.com"))
                    is MailSpecRow.SubjectRow -> row.convertValue("Question about Kaluga")
                }
            })
            is Button.Review -> {
                coroutineScope.launch {
                    reviewManager.attemptToRequestReview()
                }
                null
            }
        }?.let { navigator.navigate(it) }
    }
}
