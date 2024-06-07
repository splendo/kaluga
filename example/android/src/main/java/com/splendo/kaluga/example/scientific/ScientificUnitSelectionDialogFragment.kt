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

import android.app.ActionBar.LayoutParams
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.splendo.kaluga.architecture.navigation.ActivityNavigator
import com.splendo.kaluga.architecture.navigation.NavigationSpec
import com.splendo.kaluga.architecture.navigation.parseTypeOfOrNull
import com.splendo.kaluga.architecture.viewmodel.KalugaViewModelDialogFragment
import com.splendo.kaluga.example.databinding.DialogScientificUnitSelectionBinding
import com.splendo.kaluga.example.shared.viewmodel.scientific.ScientificUnitSelectionAction
import com.splendo.kaluga.example.shared.viewmodel.scientific.ScientificUnitSelectionViewModel
import com.splendo.kaluga.example.view.ButtonAdapter
import com.splendo.kaluga.example.view.VerticalSpaceItemDecoration
import com.splendo.kaluga.resources.dpToPixel
import com.splendo.kaluga.scientific.PhysicalQuantity
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

abstract class ScientificUnitSelectionDialogFragment : KalugaViewModelDialogFragment<ScientificUnitSelectionViewModel>() {

    companion object {
        val TAG = ScientificUnitSelectionDialogFragment::class.simpleName.orEmpty()
    }

    abstract val requestKey: String

    override val viewModel: ScientificUnitSelectionViewModel by viewModel {
        parametersOf(
            parseTypeOfOrNull(PhysicalQuantity.serializer()),
            ActivityNavigator<ScientificUnitSelectionAction<*>> { action ->
                when (action) {
                    is ScientificUnitSelectionAction.Cancelled -> NavigationSpec.DismissDialog(TAG)
                    is ScientificUnitSelectionAction.DidSelect -> {
                        NavigationSpec.DismissDialog(TAG, fragmentRequestKey = requestKey)
                    }
                }
            },
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DialogScientificUnitSelectionBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.currentUnits.adapter = ButtonAdapter()
        binding.lifecycleOwner = this
        binding.currentUnits.addItemDecoration(VerticalSpaceItemDecoration(10.0f.dpToPixel(requireContext()).toInt()))
        return binding.root
    }

    override fun onResume() {
        super.onResume()

        dialog?.window?.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
    }

    override fun onCancel(dialog: DialogInterface) {
        viewModel.onCancel()
    }
}
