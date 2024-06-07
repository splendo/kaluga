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

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContract
import com.splendo.kaluga.architecture.navigation.ActivityNavigator
import com.splendo.kaluga.architecture.navigation.NavigationSpec
import com.splendo.kaluga.architecture.navigation.parseTypeOfOrNull
import com.splendo.kaluga.architecture.viewmodel.KalugaViewModelActivity
import com.splendo.kaluga.example.databinding.ActivityArchitectureInputBinding
import com.splendo.kaluga.example.shared.viewmodel.architecture.ArchitectureNavigationAction
import com.splendo.kaluga.example.shared.viewmodel.architecture.ArchitectureViewModel
import com.splendo.kaluga.example.shared.viewmodel.architecture.InputDetails
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class XMLArchitectureActivity : KalugaViewModelActivity<ArchitectureViewModel>() {

    class Contract : ActivityResultContract<Intent, InputDetails?>() {

        override fun createIntent(context: Context, input: Intent): Intent = input

        override fun parseResult(resultCode: Int, intent: Intent?): InputDetails? {
            return intent.parseTypeOfOrNull(InputDetails.serializer())
        }
    }

    override val viewModel: ArchitectureViewModel by viewModel {
        parametersOf(
            ActivityNavigator<ArchitectureNavigationAction<*>> { action ->
                when (action) {
                    is ArchitectureNavigationAction.Details -> NavigationSpec.Activity<ArchitectureDetailsActivity>(
                        launchType = NavigationSpec.Activity.LaunchType.ActivityContract<XMLArchitectureActivity> { contract },
                    )
                    is ArchitectureNavigationAction.BottomSheet -> NavigationSpec.Dialog(BottomSheetRootDialogFragment.TAG) { BottomSheetRootDialogFragment() }
                }
            },
        )
    }

    private val contract = registerForActivityResult(Contract()) { inputDetails ->
        inputDetails?.let {
            viewModel.nameInput.post(it.name)
            viewModel.numberInput.post(it.number.toString())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityArchitectureInputBinding.inflate(layoutInflater, null, false)
        setContentView(binding.root)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }
}
