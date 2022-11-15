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

package com.splendo.kaluga.bluetooth.beacons

import com.splendo.kaluga.base.utils.DefaultKalugaDate
import com.splendo.kaluga.bluetooth.BluetoothService
import com.splendo.kaluga.bluetooth.device.Device
import com.splendo.kaluga.bluetooth.device.Identifier
import com.splendo.kaluga.logging.debug
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

private typealias BeaconJob = Pair<BeaconInfo, Job>
private typealias BeaconsMap = MutableMap<BeaconID, BeaconJob>

class Beacons(
    private val bluetooth: BluetoothService,
    private val timeout: Duration = 10.seconds,
) {

    private companion object { const val TAG = "Beacons" }

    private val monitoringLock = Mutex()
    private val updateLock = Mutex()
    private val cache = mutableMapOf<BeaconID, BeaconJob>()
    private val cacheJob = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Default + cacheJob)
    private var monitoringJob: MutableStateFlow<Job?> = MutableStateFlow(null)

    private val _beacons = MutableStateFlow(emptySet<BeaconInfo>())
    val beacons: StateFlow<Set<BeaconInfo>>
        get() = _beacons.asStateFlow()

    suspend fun startMonitoring(coroutineScope: CoroutineScope) = monitoringLock.withLock {
        debug(TAG, "Start monitoring")
        _beacons.value = emptySet()
        bluetooth.startScanning()
        monitoringJob.value = coroutineScope.launch {
            bluetooth.devices().collect { list ->
                debug(TAG, "Total Bluetooth devices discovered: ${list.size}")
                updateBeacons(list.mapNotNull { createBeaconWith(it) })
            }
        }
    }

    suspend fun stopMonitoring() = monitoringLock.withLock {
        debug(TAG, "Stop monitoring")
        bluetooth.stopScanning()
        monitoringJob.value?.cancel()
        monitoringJob.value = null
        _beacons.value = emptySet()
    }

    suspend fun isMonitoring() = combine(monitoringJob.map { it != null }, bluetooth.isScanning()) { isMonitoring, isScanning -> isMonitoring && isScanning }

    fun isAnyInRange(beaconIds: List<String>) = beacons.map { list ->
        list.any { beaconIds.containsLowerCased(it.beaconID.asString()) }
    }

    private suspend fun updateBeacons(discovered: List<BeaconInfo>) = updateLock.withLock {
        debug(TAG, "Total Beacons discovered: ${discovered.size}")
        discovered.forEach { beacon ->
            if (cache.containsKey(beacon.beaconID)) {
                debug(TAG, "[Found] $beacon")
                cache.remove(beacon.beaconID)?.second?.cancel()
                debug(TAG, "[Removed] $beacon")
            } else {
                debug(TAG, "[New] $beacon")
            }
            cache[beacon.beaconID] = beacon to coroutineScope.launch {
                debug(TAG, "[Added] $beacon")
                delay(beacon.lastSeen.millisecondSinceEpoch + timeout.inWholeMilliseconds - DefaultKalugaDate.now().millisecondSinceEpoch)
                debug(TAG, "[Lost] $beacon")
                cache.remove(beacon.beaconID)
                updateList()
            }
        }
        updateList()
    }

    private fun updateList() = _beacons.update {
        cache.values.map(BeaconJob::first).toSet()
    }

    private fun List<String>.containsLowerCased(element: String): Boolean =
        this.map(String::lowercase).contains(element.lowercase())

    private suspend fun createBeaconWith(device: Device): BeaconInfo? {
        val serviceData = device.info.map { it.advertisementData.serviceData }.firstOrNull() ?: return null
        val data = serviceData[Eddystone.SERVICE_UUID] ?: return null
        val frame = Eddystone.unpack(data) ?: return null
        val rssi = device.info.map { it.rssi }.firstOrNull() ?: 0
        val lastSeen = device.info.map { it.updatedAt }.firstOrNull() ?: DefaultKalugaDate.now()
        return BeaconInfo(device.identifier, frame.uid, frame.txPower, rssi, lastSeen)
    }
}

operator fun Flow<Set<BeaconInfo>>.get(identifier: Identifier) = map { beacons ->
    beacons.firstOrNull { it.identifier == identifier }
}
