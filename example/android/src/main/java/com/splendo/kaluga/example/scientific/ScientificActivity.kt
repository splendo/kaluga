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
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.splendo.kaluga.architecture.navigation.ActivityNavigator
import com.splendo.kaluga.architecture.navigation.NavigationBundleSpecType
import com.splendo.kaluga.architecture.navigation.NavigationSpec
import com.splendo.kaluga.architecture.navigation.toTypedProperty
import com.splendo.kaluga.architecture.viewmodel.KalugaViewModelActivity
import com.splendo.kaluga.example.databinding.ActivityScientificBinding
import com.splendo.kaluga.example.shared.viewmodel.scientific.ScientificNavigationAction
import com.splendo.kaluga.example.shared.viewmodel.scientific.ScientificViewModel
import com.splendo.kaluga.example.view.ButtonAdapter
import com.splendo.kaluga.example.view.VerticalSpaceItemDecoration
import com.splendo.kaluga.resources.dpToPixel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

object ScientificConverterButtonBinding {
    @BindingAdapter("scientificConverterButtons")
    @JvmStatic
    fun bindButtons(view: RecyclerView, buttons: List<ScientificViewModel.Button>?) {
        val adapter = (view.adapter as? ButtonAdapter) ?: return
        adapter.buttons = buttons?.map { it.button }.orEmpty()
    }
}

class ScientificActivity : KalugaViewModelActivity<ScientificViewModel>() {

    companion object {
        const val LEFT_UNIT_REQUEST_KEY: String = "LeftUnit"
        const val RIGHT_UNIT_REQUEST_KEY: String = "RightUnit"
    }

    class LeftUnitDialogSelectionFragment : ScientificUnitSelectionDialogFragment() {
        override val requestKey: String = LEFT_UNIT_REQUEST_KEY
    }

    class RightUnitSelectionDialogFragment : ScientificUnitSelectionDialogFragment() {
        override val requestKey: String = RIGHT_UNIT_REQUEST_KEY
    }

    override val viewModel: ScientificViewModel by viewModel {
        parametersOf(
            ActivityNavigator<ScientificNavigationAction<*>> { action ->
                when (action) {
                    is ScientificNavigationAction.SelectUnit -> {
                        NavigationSpec.Dialog(ScientificUnitSelectionDialogFragment.TAG) {
                            when (action) {
                                is ScientificNavigationAction.SelectUnit.Left -> LeftUnitDialogSelectionFragment()
                                is ScientificNavigationAction.SelectUnit.Right -> RightUnitSelectionDialogFragment()
                            }
                        }
                    }
                    is ScientificNavigationAction.Converter -> NavigationSpec.Activity<ScientificConverterActivity>()
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

        val binding = ActivityScientificBinding.inflate(layoutInflater, null, false)
        setContentView(binding.root)
        binding.viewModel = viewModel
        binding.convertersList.adapter = ButtonAdapter()
        binding.convertersList.addItemDecoration(VerticalSpaceItemDecoration(10.0f.dpToPixel(this).toInt()))
        binding.lifecycleOwner = this
    }
}
