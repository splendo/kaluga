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

package com.splendo.kaluga.example

import android.os.Bundle
import com.google.android.material.tabs.TabLayout
import com.splendo.kaluga.architecture.navigation.ActivityNavigator
import com.splendo.kaluga.architecture.navigation.NavigationSpec
import com.splendo.kaluga.architecture.viewmodel.KalugaViewModelActivity
import com.splendo.kaluga.example.featurelist.FeaturesListFragment
import com.splendo.kaluga.example.info.InfoFragment
import com.splendo.kaluga.example.shared.viewmodel.ExampleTabNavigation
import com.splendo.kaluga.example.shared.viewmodel.ExampleViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ExampleActivity : KalugaViewModelActivity<ExampleViewModel>(R.layout.activity_example) {

    override val viewModel: ExampleViewModel by viewModel {
        parametersOf(
            ActivityNavigator<ExampleTabNavigation> { action ->
                when (action) {
                    ExampleTabNavigation.FeatureList -> NavigationSpec.Fragment(
                        R.id.example_fragment,
                        createFragment = { FeaturesListFragment() },
                    )
                    ExampleTabNavigation.Info -> NavigationSpec.Fragment(
                        R.id.example_fragment,
                        createFragment = { InfoFragment() },
                    )
                }
            },
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val tabs: TabLayout = findViewById(R.id.tabs)

        viewModel.tabs.observeInitialized { exampleTabs ->
            tabs.removeAllTabs()
            exampleTabs.forEach { tab ->
                tabs.addTab(tabs.newTab().setText(tab.title).setTag(tab))
            }
        }

        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
                val exampleTab = tab?.tag as? ExampleViewModel.Tab ?: return
                viewModel.tab.post(exampleTab)
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                val exampleTab = tab?.tag as? ExampleViewModel.Tab ?: return
                viewModel.tab.post(exampleTab)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) { }
        })
    }
}
