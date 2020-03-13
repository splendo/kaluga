package com.splendo.kaluga.example.bluetooth

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.splendo.kaluga.bluetooth.Bluetooth
import com.splendo.kaluga.bluetooth.Service
import com.splendo.kaluga.bluetooth.device.Identifier
import com.splendo.kaluga.bluetooth.get
import com.splendo.kaluga.bluetooth.services
import com.splendo.kaluga.example.R
import kotlinx.android.synthetic.main.activity_bluetooth.*
import kotlinx.android.synthetic.main.bluetooth_service_item.view.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.distinctUntilChanged

@InternalCoroutinesApi
class BluetoothServiceAdapter(private val bluetooth: Bluetooth, private val identifier: Identifier, private val lifecycle: Lifecycle) : RecyclerView.Adapter<BluetoothServiceAdapter.BluetoothServiceItemViewHolder>() {

    class BluetoothServiceItemViewHolder(itemView: View, private val bluetooth: Bluetooth, private val identifier: Identifier, private val lifecycle: Lifecycle) : RecyclerView.ViewHolder(itemView) {

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
            characteristicsAdapter = BluetoothCharacteristicAdapter(bluetooth, identifier, service.uuid, lifecycle)
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
            bluetooth.devices()[identifier].services().collect{ newServices ->
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
        return BluetoothServiceItemViewHolder(view, bluetooth, identifier, lifecycle)
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