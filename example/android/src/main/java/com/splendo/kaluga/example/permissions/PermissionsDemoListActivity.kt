/*
 Copyright 2020 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.example.permissions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.splendo.kaluga.architecture.viewmodel.KalugaViewModelActivity
import com.splendo.kaluga.example.R
import com.splendo.kaluga.example.shared.viewmodel.permissions.PermissionView
import com.splendo.kaluga.example.shared.viewmodel.permissions.PermissionsListViewModel
import kotlinx.android.synthetic.main.activity_permissions_list.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class PermissionsDemoListActivity : KalugaViewModelActivity<PermissionsListViewModel>(R.layout.activity_permissions_list) {

    override val viewModel: PermissionsListViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val adapter = PermissionsAdapter(viewModel).apply {
            permissions_list.adapter = this
        }
        viewModel.permissions.observe(this, Observer { adapter.permissions = it })
    }
}

class PermissionsAdapter(private val viewModel: PermissionsListViewModel) : RecyclerView.Adapter<PermissionsAdapter.PermissionsViewHolder>() {

    class PermissionsViewHolder(val button: AppCompatButton) : RecyclerView.ViewHolder(button)

    var permissions: List<PermissionView> = emptyList()
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PermissionsViewHolder {
        val button = LayoutInflater.from(parent.context).inflate(R.layout.view_feature_button, parent, false) as AppCompatButton
        return PermissionsViewHolder(button)
    }

    override fun getItemCount(): Int = permissions.size

    override fun onBindViewHolder(holder: PermissionsViewHolder, position: Int) {
        permissions.getOrNull(position)?.let { permission ->
            holder.button.text = permission.title
            holder.button.setOnClickListener { viewModel.onPermissionPressed(permission) }
        } ?: run {
            holder.button.text = null
            holder.button.setOnClickListener(null)
        }
    }
}
