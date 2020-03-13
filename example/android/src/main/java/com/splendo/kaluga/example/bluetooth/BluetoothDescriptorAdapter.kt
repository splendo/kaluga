package com.splendo.kaluga.example.bluetooth

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.asLiveData
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.RecyclerView
import com.splendo.kaluga.base.utils.toHexString
import com.splendo.kaluga.bluetooth.*
import com.splendo.kaluga.bluetooth.device.Identifier
import com.splendo.kaluga.example.R
import kotlinx.android.synthetic.main.bluetooth_descriptor_item.view.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class BluetoothDescriptorAdapter(private val bluetooth: Bluetooth, private val identifier: Identifier, private val serviceUUID: UUID, private val characteristicUUID: UUID, private val lifecycle: Lifecycle) : RecyclerView.Adapter<BluetoothDescriptorAdapter.BluetoothDescriptorItemViewHolder>() {

    class BluetoothDescriptorItemViewHolder(itemView: View, private val bluetooth: Bluetooth, private val identifier: Identifier, private val serviceUUID: UUID, private val characteristicUUID: UUID, private val lifecycle: Lifecycle) : RecyclerView.ViewHolder(itemView) {

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
                bluetooth.devices()[identifier].services()[serviceUUID].characteristics()[characteristicUUID].descriptors()[descriptorUUID].first()?.readValue()
                bluetooth.devices()[identifier].services()[serviceUUID].characteristics()[characteristicUUID].descriptors()[descriptorUUID].value().asLiveData().observe({lifecycle}) {
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
            bluetooth.devices()[identifier].services()[serviceUUID].characteristics()[characteristicUUID].descriptors().collect{ newDescriptors ->
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
        return BluetoothDescriptorItemViewHolder(view, bluetooth, identifier, serviceUUID, characteristicUUID, lifecycle)
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