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

package com.splendo.kaluga.example.shared.viewmodel.link

import com.splendo.kaluga.alerts.Alert
import com.splendo.kaluga.alerts.AlertPresenter
import com.splendo.kaluga.alerts.buildAlert
import com.splendo.kaluga.architecture.navigation.NavigationAction
import com.splendo.kaluga.architecture.navigation.NavigationBundle
import com.splendo.kaluga.architecture.navigation.NavigationBundleSpec
import com.splendo.kaluga.architecture.navigation.NavigationBundleSpecRow
import com.splendo.kaluga.architecture.navigation.NavigationBundleSpecType
import com.splendo.kaluga.architecture.navigation.Navigator
import com.splendo.kaluga.architecture.navigation.toBundle
import com.splendo.kaluga.architecture.observable.observableOf
import com.splendo.kaluga.architecture.viewmodel.NavigatingViewModel
import com.splendo.kaluga.links.Links
import com.splendo.kaluga.links.state.LinksStateRepoBuilder
import com.splendo.kaluga.resources.localized
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable
data class Repository(
    val name: String,
    val type: String
)

class BrowserSpec : NavigationBundleSpec<BrowserSpecRow>(setOf(BrowserSpecRow.UrlSpecRow))

sealed class BrowserNavigationActions<B : NavigationBundleSpecRow<*>>(bundle: NavigationBundle<B>?) : NavigationAction<B>(bundle) {
    class OpenWebView(bundle: NavigationBundle<BrowserSpecRow>?) : BrowserNavigationActions<BrowserSpecRow>(bundle)
}

sealed class BrowserSpecRow : NavigationBundleSpecRow<String>(NavigationBundleSpecType.StringType) {
    object UrlSpecRow : BrowserSpecRow()
}

class LinksViewModel(
    linkRepoBuilder: LinksStateRepoBuilder,
    val builder: AlertPresenter.Builder,
    navigator: Navigator<BrowserNavigationActions<BrowserSpecRow>>
    ) : NavigatingViewModel<BrowserNavigationActions<BrowserSpecRow>>(navigator) {

    val browserButtonText = observableOf("browser_button_text".localized())
    val incomingLinkButtonText = observableOf("incoming_link_text".localized())

    private val linksRepo = linkRepoBuilder.create()

    override fun onResume(scope: CoroutineScope) {
        super.onResume(scope)
        scope.launch(Dispatchers.Main) {
            linksRepo.linksEventFlow.collect {
                when (it) {
                    is Links.Incoming.Result<*> -> {
                        val result = (it.data as Repository)
                        showAlert(
                            result.name,
                            result.type,
                            Alert.Action.Style.POSITIVE
                        )
                    }
                    is Links.Outgoing.Link ->
                        navigator.navigate(
                            BrowserNavigationActions.OpenWebView(
                                BrowserSpec().toBundle { row ->
                                    when (row) {
                                        is BrowserSpecRow.UrlSpecRow -> row.convertValue(it.url)
                                    }
                                }
                            )
                        )
                    is Links.Failure ->
                        showAlert(
                            "Error",
                            it.message,
                            Alert.Action.Style.NEGATIVE
                        )
                }
            }
        }
    }

    fun showAlert(title: String, message: String, style: Alert.Action.Style) {
        coroutineScope.launch {
            val action = Alert.Action("Ok", style)
            val alert = builder.buildAlert(this) {
                setTitle(title)
                setMessage(message)
                addActions(action)
            }
            alert.show()
        }
    }

    fun openWebPage() {
        linksRepo.validateLink("https://github.com/splendo/kaluga")
    }

    fun handleIncomingLink() {
        val query = "name=Kaluga&type=Multiplatform library"
        linksRepo.handleIncomingLink(query, Repository.serializer())
    }

}
