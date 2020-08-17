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

package com.splendo.kaluga.example

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.splendo.kaluga.architecture.viewmodel.KalugaViewModelFragment
import com.splendo.kaluga.example.shared.viewmodel.featureList.Feature
import com.splendo.kaluga.example.shared.viewmodel.featureList.FeatureListViewModel
import kotlinx.android.synthetic.main.fragment_features_list.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class FeaturesListFragment : KalugaViewModelFragment<FeatureListViewModel>(R.layout.fragment_features_list) {

    override val viewModel: FeatureListViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = FeaturesAdapter(viewModel).apply {
            features_list.adapter = this
        }
        viewModel.feature.observe(this, Observer { adapter.features = it })
    }
}

class FeaturesAdapter(private val viewModel: FeatureListViewModel) : RecyclerView.Adapter<FeaturesAdapter.FeatureViewHolder>() {

    class FeatureViewHolder(val button: AppCompatButton) : RecyclerView.ViewHolder(button)

    var features: List<Feature> = emptyList()
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeatureViewHolder {
        val button = LayoutInflater.from(parent.context).inflate(R.layout.view_feature_button, parent, false) as AppCompatButton
        return FeatureViewHolder(button)
    }

    override fun getItemCount(): Int = features.size

    override fun onBindViewHolder(holder: FeatureViewHolder, position: Int) {
        features.getOrNull(position)?.let { feature ->
            holder.button.text = feature.title
            holder.button.setOnClickListener { viewModel.onFeaturePressed(feature) }
        } ?: run {
            holder.button.text = null
            holder.button.setOnClickListener(null)
        }
    }
}
