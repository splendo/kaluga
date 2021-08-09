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

package com.splendo.kaluga.example.system.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.splendo.kaluga.architecture.viewmodel.KalugaViewModelFragment
import com.splendo.kaluga.example.R
import com.splendo.kaluga.example.databinding.FragmentNetworkBinding
import com.splendo.kaluga.example.shared.viewmodel.system.network.NetworkViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class NetworkFragment : KalugaViewModelFragment<NetworkViewModel>(R.layout.fragment_network) {

    override val viewModel: NetworkViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentNetworkBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding.root
    }
}
