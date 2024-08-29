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

package com.splendo.kaluga.example.shared.viewmodel.link

import com.splendo.kaluga.alerts.Alert
import com.splendo.kaluga.alerts.BaseAlertPresenter
import com.splendo.kaluga.alerts.buildAlert
import com.splendo.kaluga.architecture.navigation.NavigationBundleSpecType
import com.splendo.kaluga.architecture.navigation.Navigator
import com.splendo.kaluga.architecture.navigation.SingleValueNavigationAction
import com.splendo.kaluga.architecture.observable.observableOf
import com.splendo.kaluga.architecture.viewmodel.NavigatingViewModel
import com.splendo.kaluga.links.LinksManager
import com.splendo.kaluga.resources.localized
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable
data class Repository(val name: String, val type: String)

sealed class BrowserNavigationActions<T>(value: T, type: NavigationBundleSpecType<T>) : SingleValueNavigationAction<T>(value, type) {
    class OpenWebView(url: String) : BrowserNavigationActions<String>(url, NavigationBundleSpecType.StringType)
}

class LinksViewModel(linkManagerBuilder: LinksManager.Builder, private val alertPresenterBuilder: BaseAlertPresenter.Builder, navigator: Navigator<BrowserNavigationActions<*>>) :
    NavigatingViewModel<BrowserNavigationActions<*>>(navigator, alertPresenterBuilder) {

    val browserButtonText = observableOf("browser_button_text".localized())
    val linksInstructions = observableOf("links_instructions".localized())

    private val linksRepo = linkManagerBuilder.create()

    fun showAlert(title: String, message: String, style: Alert.Action.Style) {
        coroutineScope.launch {
            val action = Alert.Action("Ok", style)
            val alert = alertPresenterBuilder.buildAlert(this) {
                setTitle(title)
                setMessage(message)
                addActions(action)
            }
            alert.show()
        }
    }

    fun openWebPage() {
        val result = linksRepo.validateLink("https://kaluga-links.web.app")
        if (result != null) {
            navigator.navigate(
                BrowserNavigationActions.OpenWebView(result),
            )
        } else {
            showAlert(
                "Error Alert",
                "URL is invalid.",
                Alert.Action.Style.NEGATIVE,
            )
        }
    }

    fun handleIncomingLink(url: String) {
        val result = linksRepo.handleIncomingLink(url, Repository.serializer())
        if (result != null) {
            showAlert(
                "Alert",
                result.toString(),
                Alert.Action.Style.POSITIVE,
            )
        } else {
            showAlert(
                "Error Alert",
                "Query is invalid or empty.",
                Alert.Action.Style.NEGATIVE,
            )
        }
    }
}
