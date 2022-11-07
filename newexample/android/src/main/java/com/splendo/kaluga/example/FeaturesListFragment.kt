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

package com.splendo.kaluga.example

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.splendo.kaluga.architecture.navigation.ActivityNavigator
import com.splendo.kaluga.architecture.navigation.NavigationSpec
import com.splendo.kaluga.architecture.viewmodel.KalugaViewModelFragment
import com.splendo.kaluga.example.alerts.AlertsActivity
import com.splendo.kaluga.example.architecture.ArchitectureInputActivity
import com.splendo.kaluga.example.beacons.BeaconsActivity
import com.splendo.kaluga.example.bluetooth.BluetoothActivity
import com.splendo.kaluga.example.databinding.ViewListButtonBinding
import com.splendo.kaluga.example.datetimepicker.DateTimePickerActivity
import com.splendo.kaluga.example.keyboard.KeyboardManagerActivity
import com.splendo.kaluga.example.link.LinksActivity
import com.splendo.kaluga.example.loading.LoadingActivity
import com.splendo.kaluga.example.location.LocationActivity
import com.splendo.kaluga.example.permissions.PermissionsDemoListActivity
import com.splendo.kaluga.example.platformspecific.PlatformSpecificActivity
import com.splendo.kaluga.example.resources.ResourcesActivity
import com.splendo.kaluga.example.shared.viewmodel.featureList.Feature
import com.splendo.kaluga.example.shared.viewmodel.featureList.FeatureListNavigationAction
import com.splendo.kaluga.example.shared.viewmodel.featureList.FeatureListViewModel
import com.splendo.kaluga.example.system.SystemActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class FeaturesListFragment : KalugaViewModelFragment<FeatureListViewModel>(R.layout.fragment_features_list) {

    override val viewModel: FeatureListViewModel by viewModel {
        parametersOf(
            ActivityNavigator<FeatureListNavigationAction> { action ->
                when (action) {
                    FeatureListNavigationAction.Location -> NavigationSpec.Activity<LocationActivity>()
                    FeatureListNavigationAction.Permissions -> NavigationSpec.Activity<PermissionsDemoListActivity>()
                    FeatureListNavigationAction.Alerts -> NavigationSpec.Activity<AlertsActivity>()
                    FeatureListNavigationAction.DateTimePicker -> NavigationSpec.Activity<DateTimePickerActivity>()
                    FeatureListNavigationAction.LoadingIndicator -> NavigationSpec.Activity<LoadingActivity>()
                    FeatureListNavigationAction.Architecture -> NavigationSpec.Activity<ArchitectureInputActivity>()
                    FeatureListNavigationAction.Keyboard -> NavigationSpec.Activity<KeyboardManagerActivity>()
                    FeatureListNavigationAction.Links -> NavigationSpec.Activity<LinksActivity>()
                    FeatureListNavigationAction.System -> NavigationSpec.Activity<SystemActivity>()
                    FeatureListNavigationAction.Bluetooth -> NavigationSpec.Activity<BluetoothActivity>()
                    FeatureListNavigationAction.Beacons -> NavigationSpec.Activity<BeaconsActivity>()
                    FeatureListNavigationAction.Resources -> NavigationSpec.Activity<ResourcesActivity>()
                    FeatureListNavigationAction.PlatformSpecific -> NavigationSpec.Activity<PlatformSpecificActivity>()
                }
            }
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = FeaturesAdapter(viewModel).apply {

            view.findViewById<RecyclerView>(R.id.features_list).adapter = this
        }
        viewModel.feature.observeInitialized { adapter.features = it }
    }
}

class FeaturesAdapter(private val viewModel: FeatureListViewModel) : RecyclerView.Adapter<FeaturesAdapter.FeatureViewHolder>() {

    class FeatureViewHolder(val binding: ViewListButtonBinding) : RecyclerView.ViewHolder(binding.root) {
        val button = binding.button
    }

    var features: List<Feature> = emptyList()
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeatureViewHolder {
        val binding = ViewListButtonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FeatureViewHolder(binding)
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
