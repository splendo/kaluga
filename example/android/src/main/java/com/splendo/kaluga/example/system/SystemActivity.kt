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

package com.splendo.kaluga.example.system

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.splendo.kaluga.architecture.viewmodel.KalugaViewModelActivity
import com.splendo.kaluga.example.R
import com.splendo.kaluga.example.shared.viewmodel.system.SystemFeatures
import com.splendo.kaluga.example.shared.viewmodel.system.SystemViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SystemActivity : KalugaViewModelActivity<SystemViewModel>(R.layout.activity_system) {
    override val viewModel: SystemViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val adapter = SystemFeatureAdapter(viewModel).apply {
            findViewById<RecyclerView>(R.id.system_features_list).adapter = this
        }

        viewModel.modules.observe(this) {
            adapter.modules = it
        }
    }
}

class SystemFeatureAdapter(
    private val viewModel: SystemViewModel
) : RecyclerView.Adapter<SystemFeatureAdapter.SystemFeatureViewHolder>() {

    inner class SystemFeatureViewHolder(val button: Button) : RecyclerView.ViewHolder(button)

    var modules: List<SystemFeatures> = emptyList()
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SystemFeatureViewHolder {
        val button = LayoutInflater.from(parent.context).inflate(R.layout.view_feature_button, parent, false) as AppCompatButton
        return SystemFeatureViewHolder(button)
    }

    override fun onBindViewHolder(holder: SystemFeatureViewHolder, position: Int) {
        modules.getOrNull(position)?.let { feature ->
            holder.button.text = feature.name
            holder.button.setOnClickListener { viewModel.onButtonTapped(feature) }
        } ?: run {
            holder.button.text = null
            holder.button.setOnClickListener(null)
        }
    }

    override fun getItemCount(): Int = modules.size

}