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

package com.splendo.kaluga.example.compose

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.splendo.kaluga.architecture.navigation.ActivityNavigator
import com.splendo.kaluga.architecture.navigation.NavigationSpec
import com.splendo.kaluga.architecture.viewmodel.KalugaViewModelActivity
import com.splendo.kaluga.example.databinding.ActivityComposeOrAndroidUiBinding
import com.splendo.kaluga.example.databinding.ViewListButtonBinding
import com.splendo.kaluga.example.shared.viewmodel.compose.ComposeOrXMLNavigationAction
import com.splendo.kaluga.example.shared.viewmodel.compose.ComposeOrXMLSelectionViewModel
import com.splendo.kaluga.example.shared.viewmodel.compose.UIType
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

abstract class ComposeOrXMLActivity<ComposeActivity : AppCompatActivity, XMLActivity : AppCompatActivity>(
    composeActivityClass: Class<ComposeActivity>,
    XMLActivityClass: Class<XMLActivity>
) : KalugaViewModelActivity<ComposeOrXMLSelectionViewModel>() {

    override val viewModel: ComposeOrXMLSelectionViewModel by viewModel {
        parametersOf(
            ActivityNavigator<ComposeOrXMLNavigationAction> { action ->
                when (action) {
                    is ComposeOrXMLNavigationAction.Compose -> NavigationSpec.Activity(composeActivityClass)
                    is ComposeOrXMLNavigationAction.XML -> NavigationSpec.Activity(XMLActivityClass)
                }
            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityComposeOrAndroidUiBinding.inflate(LayoutInflater.from(this), null, false)
        binding.uiTypesList.adapter = UITypesAdapter(viewModel)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        setContentView(binding.root)
    }
}

object UITypeBinding {
    @BindingAdapter("uiTypes")
    @JvmStatic
    fun bindUITypes(view: RecyclerView, uiTypes: List<UIType>?) {
        val adapter = (view.adapter as? UITypesAdapter) ?: return
        adapter.uiTypes = uiTypes.orEmpty()
    }
}

class UITypesAdapter(private val viewModel: ComposeOrXMLSelectionViewModel) : RecyclerView.Adapter<UITypesAdapter.UITypesViewHolder>() {

    class UITypesViewHolder(val binding: ViewListButtonBinding) : RecyclerView.ViewHolder(binding.root) {
        val button = binding.button
    }

    var uiTypes: List<UIType> = emptyList()
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UITypesViewHolder {
        val binding = ViewListButtonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UITypesViewHolder(binding)
    }

    override fun getItemCount(): Int = uiTypes.size

    override fun onBindViewHolder(holder: UITypesViewHolder, position: Int) {
        uiTypes.getOrNull(position)?.let { uiType ->
            holder.button.text = uiType.title
            holder.button.setOnClickListener { viewModel.onUITypePressed(uiType) }
        } ?: run {
            holder.button.text = null
            holder.button.setOnClickListener(null)
        }
    }
}
