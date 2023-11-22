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
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.splendo.kaluga.architecture.navigation.Navigator
import com.splendo.kaluga.example.databinding.FragmentBottomSheetSubPageBinding
import com.splendo.kaluga.example.shared.viewmodel.architecture.BottomSheetSubPageNavigation
import com.splendo.kaluga.example.shared.viewmodel.architecture.BottomSheetSubPageViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class BottomSheetSubPageFragment(private val navigator: Navigator<BottomSheetSubPageNavigation>) : Fragment() {

    val viewModel: BottomSheetSubPageViewModel by viewModel {
        parametersOf(navigator)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        val binding = FragmentBottomSheetSubPageBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        view?.let {
            it.isFocusableInTouchMode = true
            it.requestFocus()
            it.setOnKeyListener { _, keyCode, _ ->
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    viewModel.onBackPressed()
                    true
                } else {
                    false
                }
            }
        }
    }
}
