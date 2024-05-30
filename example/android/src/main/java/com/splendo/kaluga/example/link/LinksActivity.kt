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

package com.splendo.kaluga.example.link

import android.content.Intent
import android.os.Bundle
import com.splendo.kaluga.architecture.navigation.ActivityNavigator
import com.splendo.kaluga.architecture.navigation.NavigationSpec
import com.splendo.kaluga.architecture.viewmodel.KalugaViewModelActivity
import com.splendo.kaluga.example.databinding.ActivityLinkBinding
import com.splendo.kaluga.example.shared.viewmodel.link.BrowserNavigationActions
import com.splendo.kaluga.example.shared.viewmodel.link.LinksViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.net.URL

class LinksActivity : KalugaViewModelActivity<LinksViewModel>() {

    override val viewModel: LinksViewModel by viewModel {
        parametersOf(
            ActivityNavigator<BrowserNavigationActions<*>> {
                when (it) {
                    is BrowserNavigationActions.OpenWebView -> NavigationSpec.Browser(
                        URL(it.value),
                        NavigationSpec.Browser.Type.Normal,
                    )
                }
            },
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupBindings()

        handleAppLinks(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleAppLinks(intent)
    }

    private fun handleAppLinks(intent: Intent) {
        intent.let { i ->
            i.data?.let { uri ->
                val url = URL(uri.scheme, uri.host, uri.path)
                viewModel.handleIncomingLink("$url?${uri.encodedQuery}")
            }
        }
    }

    private fun setupBindings() {
        val binding = ActivityLinkBinding.inflate(layoutInflater, null, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        setContentView(binding.root)
    }
}
