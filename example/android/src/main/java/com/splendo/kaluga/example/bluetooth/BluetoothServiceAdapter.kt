package com.splendo.kaluga.example.bluetooth

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.RecyclerView
import com.splendo.kaluga.bluetooth.Bluetooth
import com.splendo.kaluga.bluetooth.Service
import com.splendo.kaluga.bluetooth.device.Identifier
import com.splendo.kaluga.bluetooth.get
import com.splendo.kaluga.bluetooth.services
import com.splendo.kaluga.example.R
import kotlinx.android.synthetic.main.bluetooth_service_item.view.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
class BluetoothServiceAdapter(private val bluetooth: Bluetooth, private val identifier: Identifier, private val lifecycle: Lifecycle) : RecyclerView.Adapter<BluetoothServiceAdapter.BluetoothServiceItemViewHolder>() {

    class BluetoothServiceItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val serviceUUID = itemView.service_uuid
        val characterisicList = itemView.characteristics_list

        fun bindData(service: Service) {
            serviceUUID.text = service.uuid.toString()
            characterisicList.adapter = BluetoothCharacteristicAdapter(service.characteristics)
        }

    }

    private var services: List<Service> = emptyList()

    init {
        lifecycle.coroutineScope.launchWhenResumed {
            bluetooth.devices()[identifier].services().collect{ services ->
                this@BluetoothServiceAdapter.services = services
                notifyDataSetChanged()
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BluetoothServiceItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.bluetooth_service_item, parent, false)
        return BluetoothServiceItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return services.size
    }

    override fun onBindViewHolder(holder: BluetoothServiceItemViewHolder, position: Int) {
        holder.bindData(services[position])
    }
}