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

package com.splendo.kaluga.example.bluetooth

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.splendo.kaluga.bluetooth.Service
import com.splendo.kaluga.bluetooth.characteristics
import com.splendo.kaluga.bluetooth.get
import com.splendo.kaluga.example.R
import kotlinx.android.synthetic.main.bluetooth_service_item.view.*
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect

@InternalCoroutinesApi
class BluetoothServiceAdapter(private val servicesFlow: Flow<List<Service>>, private val lifecycle: Lifecycle) : RecyclerView.Adapter<BluetoothServiceAdapter.BluetoothServiceItemViewHolder>() {

    class BluetoothServiceItemViewHolder(itemView: View, private val servicesFlow: Flow<List<Service>>, private val lifecycle: Lifecycle) : RecyclerView.ViewHolder(itemView) {

        private val serviceUUID = itemView.service_uuid
        private val characterisicList = itemView.characteristics_list

        private var characteristicsAdapter: BluetoothCharacteristicAdapter? = null
            set(value) {
                characterisicList.adapter = value
                field = value
            }

        fun bindData(service: Service) {
            characterisicList.addItemDecoration(DividerItemDecoration(itemView.context, LinearLayoutManager.VERTICAL))
            serviceUUID.text = service.uuid.toString()
            characteristicsAdapter = BluetoothCharacteristicAdapter(servicesFlow[service.uuid].characteristics(), lifecycle)
        }

        fun startUpdating() {
            characteristicsAdapter?.startMonitoring()
        }

        fun stopUpdating() {
            characteristicsAdapter?.stopMonitoring()
        }

    }

    private var services: List<Service> = emptyList()

    init {
        lifecycle.coroutineScope.launchWhenResumed {
            servicesFlow.collect{ newServices ->
                services = newServices
                notifyDataSetChanged()
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BluetoothServiceItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.bluetooth_service_item, parent, false)
        return BluetoothServiceItemViewHolder(view, servicesFlow, lifecycle)
    }

    override fun getItemCount(): Int {
        return services.size
    }

    override fun onBindViewHolder(holder: BluetoothServiceItemViewHolder, position: Int) {
        holder.bindData(services[position])
    }

    @ExperimentalStdlibApi
    override fun onViewAttachedToWindow(holder: BluetoothServiceItemViewHolder) {
        super.onViewAttachedToWindow(holder)

        holder.startUpdating()
    }

    override fun onViewDetachedFromWindow(holder: BluetoothServiceItemViewHolder) {
        super.onViewDetachedFromWindow(holder)

        holder.stopUpdating()
    }
}