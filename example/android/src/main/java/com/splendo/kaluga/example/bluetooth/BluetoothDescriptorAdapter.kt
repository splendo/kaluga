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
import androidx.lifecycle.asLiveData
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.RecyclerView
import com.splendo.kaluga.base.utils.toHexString
import com.splendo.kaluga.bluetooth.Descriptor
import com.splendo.kaluga.bluetooth.get
import com.splendo.kaluga.bluetooth.value
import com.splendo.kaluga.example.R
import kotlinx.android.synthetic.main.bluetooth_descriptor_item.view.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class BluetoothDescriptorAdapter(private val descriptorsFlow: Flow<List<Descriptor>>, private val lifecycle: Lifecycle) : RecyclerView.Adapter<BluetoothDescriptorAdapter.BluetoothDescriptorItemViewHolder>() {

    class BluetoothDescriptorItemViewHolder(itemView: View, private val descriptorsFlow: Flow<List<Descriptor>>, private val lifecycle: Lifecycle) : RecyclerView.ViewHolder(itemView) {

        private val uuid = itemView.descriptor_uuid
        private val valueField = itemView.descriptor_value

        private var descriptor: Descriptor? = null
        private var valueJob: Job? = null

        fun bindData(descriptor: Descriptor) {
            this.descriptor = descriptor
            uuid.text = descriptor.uuid.toString()
        }

        @ExperimentalStdlibApi
        internal fun startUpdating() {
            valueJob?.cancel()
            valueJob = lifecycle.coroutineScope.launch {
                val descriptorUUID = descriptor?.uuid ?: return@launch
                descriptorsFlow[descriptorUUID].first()?.readValue()
                descriptorsFlow[descriptorUUID].value().asLiveData().observe({lifecycle}) {
                    valueField.text = it?.toHexString()
                }
            }
        }

        internal fun stopUpdating() {
            valueJob?.cancel()
        }

    }

    private var descriptors = emptyList<Descriptor>()
    private var descriptorsJob: Job? = null

    fun startMonitoring() {
        descriptorsJob?.cancel()
        descriptorsJob = lifecycle.coroutineScope.launch {
            descriptorsFlow.collect{ newDescriptors ->
                descriptors = newDescriptors
                notifyDataSetChanged()
            }
        }
    }

    fun stopMonitoring() {
        descriptorsJob?.cancel()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BluetoothDescriptorItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.bluetooth_descriptor_item, parent, false)
        return BluetoothDescriptorItemViewHolder(view, descriptorsFlow, lifecycle)
    }

    override fun getItemCount(): Int {
        return descriptors.size
    }

    override fun onBindViewHolder(holder: BluetoothDescriptorItemViewHolder, position: Int) {
        holder.bindData(descriptors[position])
    }

    @ExperimentalStdlibApi
    override fun onViewAttachedToWindow(holder: BluetoothDescriptorItemViewHolder) {
        super.onViewAttachedToWindow(holder)

        holder.startUpdating()
    }

    override fun onViewDetachedFromWindow(holder: BluetoothDescriptorItemViewHolder) {
        super.onViewDetachedFromWindow(holder)

        holder.stopUpdating()
    }


}
