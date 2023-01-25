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

package com.splendo.kaluga.example.system

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.splendo.kaluga.architecture.navigation.ActivityNavigator
import com.splendo.kaluga.architecture.navigation.NavigationSpec
import com.splendo.kaluga.architecture.viewmodel.KalugaViewModelActivity
import com.splendo.kaluga.example.databinding.ActivitySystemBinding
import com.splendo.kaluga.example.databinding.ViewListButtonBinding
import com.splendo.kaluga.example.shared.viewmodel.system.SystemFeatures
import com.splendo.kaluga.example.shared.viewmodel.system.SystemNavigationActions
import com.splendo.kaluga.example.shared.viewmodel.system.SystemViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class SystemActivity : KalugaViewModelActivity<SystemViewModel>() {
    override val viewModel: SystemViewModel by viewModel {
        parametersOf(
            ActivityNavigator<SystemNavigationActions> { action ->
                when (action) {
                    SystemNavigationActions.Network -> NavigationSpec.Activity<NetworkActivity>()
                }
            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivitySystemBinding.inflate(LayoutInflater.from(this), null, false)
        binding.systemFeaturesList.adapter = SystemFeatureAdapter(viewModel)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        setContentView(binding.root)
    }
}

object SystemFeaturesBinding {
    @BindingAdapter("systemFeatures")
    @JvmStatic
    fun bindSystemFeatures(view: RecyclerView, resources: List<SystemFeatures>?) {
        val adapter = (view.adapter as? SystemFeatureAdapter) ?: return
        adapter.systemFeatures = resources.orEmpty()
    }
}

class SystemFeatureAdapter(
    private val viewModel: SystemViewModel
) : RecyclerView.Adapter<SystemFeatureAdapter.SystemFeatureViewHolder>() {

    inner class SystemFeatureViewHolder(val binding: ViewListButtonBinding) : RecyclerView.ViewHolder(binding.root) {
        val button = binding.button
    }

    var systemFeatures: List<SystemFeatures> = emptyList()
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SystemFeatureViewHolder {
        val binding = ViewListButtonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SystemFeatureViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SystemFeatureViewHolder, position: Int) {
        systemFeatures.getOrNull(position)?.let { feature ->
            holder.button.text = feature.name
            holder.button.setOnClickListener { viewModel.onButtonTapped(feature) }
        } ?: run {
            holder.button.text = null
            holder.button.setOnClickListener(null)
        }
    }

    override fun getItemCount(): Int = systemFeatures.size
}
