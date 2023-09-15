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

package com.splendo.kaluga.example.resources.xml

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.splendo.kaluga.architecture.navigation.ActivityNavigator
import com.splendo.kaluga.architecture.navigation.NavigationSpec
import com.splendo.kaluga.architecture.viewmodel.KalugaViewModelActivity
import com.splendo.kaluga.example.databinding.ActivityResourcesBinding
import com.splendo.kaluga.example.databinding.ViewListButtonBinding
import com.splendo.kaluga.example.shared.viewmodel.resources.Resource
import com.splendo.kaluga.example.shared.viewmodel.resources.ResourcesListNavigationAction
import com.splendo.kaluga.example.shared.viewmodel.resources.ResourcesListViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class XMLResourcesActivity : KalugaViewModelActivity<ResourcesListViewModel>() {
    override val viewModel: ResourcesListViewModel by viewModel {
        parametersOf(
            ActivityNavigator<ResourcesListNavigationAction> { action ->
                when (action) {
                    is ResourcesListNavigationAction.Label -> NavigationSpec.Activity<LabelActivity>()
                    is ResourcesListNavigationAction.Color -> NavigationSpec.Activity<ColorActivity>()
                    is ResourcesListNavigationAction.Image -> NavigationSpec.Activity<ImagesActivity>()
                    is ResourcesListNavigationAction.Button -> NavigationSpec.Activity<ButtonActivity>()
                }
            },
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityResourcesBinding.inflate(layoutInflater, null, false)
        binding.resourcesList.adapter = ResourcesAdapter(viewModel)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        setContentView(binding.root)
    }
}

object ResourcesBinding {
    @BindingAdapter("resources")
    @JvmStatic
    fun bindResources(view: RecyclerView, resources: List<Resource>?) {
        val adapter = (view.adapter as? ResourcesAdapter) ?: return
        adapter.resources = resources.orEmpty()
    }
}

class ResourcesAdapter(private val viewModel: ResourcesListViewModel) :
    RecyclerView.Adapter<ResourcesAdapter.ResourceViewHolder>() {

        class ResourceViewHolder(val binding: ViewListButtonBinding) :
            RecyclerView.ViewHolder(binding.root) {
                val button = binding.button
            }

        var resources: List<Resource> = emptyList()
            set(newValue) {
                field = newValue
                notifyDataSetChanged()
            }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResourceViewHolder {
            val binding = ViewListButtonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ResourceViewHolder(binding)
        }

        override fun getItemCount(): Int = resources.size

        override fun onBindViewHolder(holder: ResourceViewHolder, position: Int) {
            resources.getOrNull(position)?.let { resource ->
                holder.button.text = resource.title
                holder.button.setOnClickListener { viewModel.onResourceSelected(resource) }
            } ?: run {
                holder.button.text = null
                holder.button.setOnClickListener(null)
            }
        }
    }
