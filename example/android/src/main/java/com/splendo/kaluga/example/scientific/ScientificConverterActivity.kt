/*
 Copyright 2023 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.example.scientific

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import com.splendo.kaluga.architecture.navigation.ActivityNavigator
import com.splendo.kaluga.architecture.navigation.NavigationBundleSpecType
import com.splendo.kaluga.architecture.navigation.NavigationSpec
import com.splendo.kaluga.architecture.navigation.parseTypeOf
import com.splendo.kaluga.architecture.navigation.toTypedProperty
import com.splendo.kaluga.architecture.viewmodel.KalugaViewModelActivity
import com.splendo.kaluga.example.databinding.ActivityScientificConverterBinding
import com.splendo.kaluga.example.shared.viewmodel.scientific.ScientificConverterNavigationAction
import com.splendo.kaluga.example.shared.viewmodel.scientific.ScientificConverterViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ScientificConverterActivity : KalugaViewModelActivity<ScientificConverterViewModel>() {

    companion object {
        const val LEFT_UNIT_REQUEST_KEY: String = "ConverterLeftUnit"
        const val RIGHT_UNIT_REQUEST_KEY: String = "ConverterRightUnit"
    }
    class LeftUnitDialogSelectionFragment : ScientificUnitSelectionDialogFragment() {
        override val requestKey: String = LEFT_UNIT_REQUEST_KEY
    }

    class RightUnitSelectionDialogFragment : ScientificUnitSelectionDialogFragment() {
        override val requestKey: String = RIGHT_UNIT_REQUEST_KEY
    }

    override val viewModel: ScientificConverterViewModel by viewModel {
        parametersOf(
            parseTypeOf(ScientificConverterViewModel.Arguments.serializer()),
            ActivityNavigator<ScientificConverterNavigationAction<*>> { action ->
                when (action) {
                    is ScientificConverterNavigationAction.Close -> NavigationSpec.Close()
                    is ScientificConverterNavigationAction.SelectUnit -> NavigationSpec.Dialog(ScientificUnitSelectionDialogFragment.TAG) {
                        when (action) {
                            is ScientificConverterNavigationAction.SelectUnit.Left -> LeftUnitDialogSelectionFragment()
                            is ScientificConverterNavigationAction.SelectUnit.Right -> RightUnitSelectionDialogFragment()
                        }
                    }
                }
            },
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportFragmentManager.setFragmentResultListener(LEFT_UNIT_REQUEST_KEY, this) { _, result ->
            viewModel.didSelectLeftUnit(result.toTypedProperty(NavigationBundleSpecType.IntegerType))
        }
        supportFragmentManager.setFragmentResultListener(RIGHT_UNIT_REQUEST_KEY, this) { _, result ->
            viewModel.didSelectRightUnit(result.toTypedProperty(NavigationBundleSpecType.IntegerType))
        }

        val binding = ActivityScientificConverterBinding.inflate(layoutInflater, null, false)
        setContentView(binding.root)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    viewModel.onClosePressed()
                }
            },
        )
    }
}
