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

package com.splendo.kaluga.example.architecture.xml

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import com.splendo.kaluga.architecture.navigation.ActivityNavigator
import com.splendo.kaluga.architecture.navigation.NavigationSpec
import com.splendo.kaluga.architecture.navigation.parseTypeOfOrNull
import com.splendo.kaluga.architecture.viewmodel.KalugaViewModelActivity
import com.splendo.kaluga.example.databinding.ActivityArchitectureDetailsBinding
import com.splendo.kaluga.example.shared.viewmodel.architecture.ArchitectureDetailsNavigationAction
import com.splendo.kaluga.example.shared.viewmodel.architecture.ArchitectureDetailsViewModel
import com.splendo.kaluga.example.shared.viewmodel.architecture.InputDetails
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ArchitectureDetailsActivity : KalugaViewModelActivity<ArchitectureDetailsViewModel>() {

    companion object {
        val resultCode = 1
    }

    override val viewModel: ArchitectureDetailsViewModel by viewModel {
        parseTypeOfOrNull(InputDetails.serializer())?.let { details ->
            parametersOf(
                details,
                ActivityNavigator<ArchitectureDetailsNavigationAction<*>> { action ->
                    when (action) {
                        is ArchitectureDetailsNavigationAction.Close -> NavigationSpec.Close()
                        is ArchitectureDetailsNavigationAction.FinishWithDetails -> NavigationSpec.Close(
                            resultCode,
                        )
                    }
                },
            )
        } ?: parametersOf("", 0)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityArchitectureDetailsBinding.inflate(layoutInflater, null, false)
        setContentView(binding.root)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    viewModel.onBackPressed()
                }
            },
        )
    }
}
