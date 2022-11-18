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

package com.splendo.kaluga.example.architecture

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContract
import com.splendo.kaluga.architecture.navigation.ActivityNavigator
import com.splendo.kaluga.architecture.navigation.NavigationBundleSpecType
import com.splendo.kaluga.architecture.navigation.NavigationSpec
import com.splendo.kaluga.architecture.navigation.toTypedProperty
import com.splendo.kaluga.architecture.viewmodel.KalugaViewModelActivity
import com.splendo.kaluga.example.databinding.ActivityArchitectureInputBinding
import com.splendo.kaluga.example.shared.viewmodel.architecture.ArchitectureInputViewModel
import com.splendo.kaluga.example.shared.viewmodel.architecture.InputDetails
import com.splendo.kaluga.example.shared.viewmodel.architecture.InputNavigation
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ArchitectureInputActivity : KalugaViewModelActivity<ArchitectureInputViewModel>() {

    inner class Contract : ActivityResultContract<Intent, InputDetails?>() {

        override fun createIntent(context: Context, input: Intent): Intent = input

        override fun parseResult(
            resultCode: Int,
            intent: Intent?
        ): InputDetails? = intent?.extras?.toTypedProperty(NavigationBundleSpecType.SerializedType(InputDetails.serializer()))
    }

    override val viewModel: ArchitectureInputViewModel by viewModel {
        parametersOf(
            ActivityNavigator<InputNavigation> {
                NavigationSpec.Activity<ArchitectureDetailsActivity>(
                    launchType = NavigationSpec.Activity.LaunchType.ActivityContract<ArchitectureInputActivity> { contract }
                )
            }
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
