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
import androidx.fragment.app.commit
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.splendo.kaluga.architecture.lifecycle.ActivityLifecycleSubscribable
import com.splendo.kaluga.architecture.navigation.ActivityNavigator
import com.splendo.kaluga.architecture.navigation.NavigationSpec
import com.splendo.kaluga.architecture.viewmodel.KalugaViewModelBottomSheetDialogFragment
import com.splendo.kaluga.example.R
import com.splendo.kaluga.example.databinding.FragmentBottomSheetBinding
import com.splendo.kaluga.example.databinding.FragmentBottomSheetRootBinding
import com.splendo.kaluga.example.shared.viewmodel.architecture.BottomSheetNavigation
import com.splendo.kaluga.example.shared.viewmodel.architecture.BottomSheetSubPageNavigation
import com.splendo.kaluga.example.shared.viewmodel.architecture.BottomSheetViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class BottomSheetRootDialogFragment : KalugaViewModelBottomSheetDialogFragment<BottomSheetViewModel>() {

    companion object {
        val TAG = BottomSheetRootDialogFragment::class.simpleName.orEmpty()
    }

    private val subPageNavigator = ActivityNavigator<BottomSheetSubPageNavigation> { action ->
        when (action) {
            is BottomSheetSubPageNavigation.Back -> NavigationSpec.PopFragment(true) { childFragmentManager!! }
            is BottomSheetSubPageNavigation.Close -> NavigationSpec.DismissDialog(TAG)
        }
    }
    override val viewModel: BottomSheetViewModel by viewModel {
        parametersOf(
            ActivityNavigator<BottomSheetNavigation> { action ->
                when (action) {
                    is BottomSheetNavigation.Close -> NavigationSpec.DismissDialog(TAG)
                    is BottomSheetNavigation.SubPage -> NavigationSpec.Fragment(
                        R.id.fragment_container_view,
                        backStackSettings = NavigationSpec.Fragment.BackStackSettings.Add(),
                        getFragmentManager = {
                            childFragmentManager!!
                        },
                    ) {
                        BottomSheetSubPageFragment(subPageNavigator)
                    }
                }
            },
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)

        val binding = FragmentBottomSheetRootBinding.inflate(inflater, container, false)
        if (savedInstanceState == null) {
            childFragmentManager.commit {
                addToBackStack(null)
                add(binding.fragmentContainerView.id, BottomSheetFragment(viewModel))
            }
        }

        viewLifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onCreate(owner: LifecycleOwner) {
                super.onCreate(owner)

                subPageNavigator.subscribe(ActivityLifecycleSubscribable.LifecycleManager(activity, viewLifecycleOwner, parentFragmentManager, childFragmentManager))
            }

            override fun onDestroy(owner: LifecycleOwner) {
                super.onDestroy(owner)

                subPageNavigator.unsubscribe()
            }
        })

        return binding.root
    }
}

class BottomSheetFragment(val viewModel: BottomSheetViewModel) : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)

        val binding = FragmentBottomSheetBinding.inflate(inflater, container, false)
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
                    viewModel.onClosePressed()
                    true
                } else {
                    false
                }
            }
        }
    }
}
