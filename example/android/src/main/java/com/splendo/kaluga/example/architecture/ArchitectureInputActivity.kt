/*

Copyright 2019 Splendo Consulting B.V. The Netherlands

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

import android.content.Intent
import android.os.Bundle
import com.splendo.kaluga.architecture.navigation.NavigationBundleSpecType
import com.splendo.kaluga.architecture.navigation.SingleValueNavigationSpec
import com.splendo.kaluga.architecture.navigation.toNavigationBundle
import com.splendo.kaluga.architecture.viewmodel.KalugaViewModelActivity
import com.splendo.kaluga.example.databinding.ActivityArchitectureInputBinding
import com.splendo.kaluga.example.shared.viewmodel.architecture.ArchitectureInputViewModel
import com.splendo.kaluga.example.shared.viewmodel.architecture.InputDetails
import org.koin.androidx.viewmodel.ext.android.viewModel

class ArchitectureInputActivity : KalugaViewModelActivity<ArchitectureInputViewModel>() {

    companion object {
        val requestCode = 2
    }

    override val viewModel: ArchitectureInputViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityArchitectureInputBinding.inflate(layoutInflater, null, false)
        setContentView(binding.root)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ArchitectureInputActivity.requestCode && resultCode == ArchitectureDetailsActivity.resultCode) {
            val type = NavigationBundleSpecType.SerializedType(InputDetails.serializer())
            data?.extras?.toNavigationBundle(SingleValueNavigationSpec(type))?.let { bundle ->
                val inputDetails = bundle.get(type)
                viewModel.nameInput.post(inputDetails.name)
                viewModel.numberInput.post(inputDetails.number.toString())
            }
        }
    }
}
