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

package com.splendo.kaluga.example.featurelist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.splendo.kaluga.architecture.navigation.ActivityNavigator
import com.splendo.kaluga.architecture.navigation.NavigationSpec
import com.splendo.kaluga.architecture.viewmodel.KalugaViewModelFragment
import com.splendo.kaluga.example.alerts.AlertsActivity
import com.splendo.kaluga.example.architecture.ArchitectureActivity
import com.splendo.kaluga.example.beacons.BeaconsActivity
import com.splendo.kaluga.example.bluetooth.BluetoothActivity
import com.splendo.kaluga.example.databinding.FragmentFeaturesListBinding
import com.splendo.kaluga.example.databinding.ViewListButtonBinding
import com.splendo.kaluga.example.datetime.TimerActivity
import com.splendo.kaluga.example.datetimepicker.DateTimePickerActivity
import com.splendo.kaluga.example.keyboard.KeyboardActivity
import com.splendo.kaluga.example.link.LinksActivity
import com.splendo.kaluga.example.loading.LoadingActivity
import com.splendo.kaluga.example.location.LocationActivity
import com.splendo.kaluga.example.media.MediaActivity
import com.splendo.kaluga.example.permissions.PermissionsListActivity
import com.splendo.kaluga.example.resources.ResourcesActivity
import com.splendo.kaluga.example.scientific.ScientificActivity
import com.splendo.kaluga.example.shared.viewmodel.featureList.Feature
import com.splendo.kaluga.example.shared.viewmodel.featureList.FeatureListNavigationAction
import com.splendo.kaluga.example.shared.viewmodel.featureList.FeatureListViewModel
import com.splendo.kaluga.example.system.SystemActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class FeaturesListFragment : KalugaViewModelFragment<FeatureListViewModel>() {

    override val viewModel: FeatureListViewModel by viewModel {
        parametersOf(
            ActivityNavigator<FeatureListNavigationAction> { action ->
                when (action) {
                    FeatureListNavigationAction.Location -> NavigationSpec.Activity<LocationActivity>()
                    FeatureListNavigationAction.Permissions -> NavigationSpec.Activity<PermissionsListActivity>()
                    FeatureListNavigationAction.Alerts -> NavigationSpec.Activity<AlertsActivity>()
                    FeatureListNavigationAction.DateTime -> NavigationSpec.Activity<TimerActivity>()
                    FeatureListNavigationAction.DateTimePicker -> NavigationSpec.Activity<DateTimePickerActivity>()
                    FeatureListNavigationAction.LoadingIndicator -> NavigationSpec.Activity<LoadingActivity>()
                    FeatureListNavigationAction.Architecture -> NavigationSpec.Activity<ArchitectureActivity>()
                    FeatureListNavigationAction.Keyboard -> NavigationSpec.Activity<KeyboardActivity>()
                    FeatureListNavigationAction.Links -> NavigationSpec.Activity<LinksActivity>()
                    FeatureListNavigationAction.Media -> NavigationSpec.Activity<MediaActivity>()
                    FeatureListNavigationAction.System -> NavigationSpec.Activity<SystemActivity>()
                    FeatureListNavigationAction.Bluetooth -> NavigationSpec.Activity<BluetoothActivity>()
                    FeatureListNavigationAction.Beacons -> NavigationSpec.Activity<BeaconsActivity>()
                    FeatureListNavigationAction.Resources -> NavigationSpec.Activity<ResourcesActivity>()
                    FeatureListNavigationAction.Scientific -> NavigationSpec.Activity<ScientificActivity>()
                    FeatureListNavigationAction.PlatformSpecific -> throw java.lang.RuntimeException("Not supported")
                }
            },
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)

        val binding = FragmentFeaturesListBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.featuresList.adapter = FeaturesAdapter(viewModel)
        return binding.root
    }
}

object FeatureBinding {
    @BindingAdapter("features")
    @JvmStatic
    fun bindFeatures(view: RecyclerView, features: List<Feature>?) {
        val adapter = (view.adapter as? FeaturesAdapter) ?: return
        adapter.features = features.orEmpty()
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
