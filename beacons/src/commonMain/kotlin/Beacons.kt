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

import com.splendo.kaluga.base.collections.concurrentMutableMapOf
import com.splendo.kaluga.base.singleThreadDispatcher
import com.splendo.kaluga.base.utils.DefaultKalugaDate
import com.splendo.kaluga.base.utils.minus
import com.splendo.kaluga.base.utils.plus
import com.splendo.kaluga.bluetooth.BluetoothService
import com.splendo.kaluga.bluetooth.device.Device
import com.splendo.kaluga.bluetooth.device.Identifier
import com.splendo.kaluga.logging.Logger
import com.splendo.kaluga.logging.RestrictedLogLevel
import com.splendo.kaluga.logging.RestrictedLogger
import com.splendo.kaluga.logging.debug
import com.splendo.kaluga.logging.info
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
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
import kotlin.coroutines.CoroutineContext
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

private typealias BeaconJob = Pair<BeaconInfo, Job>

/**
 * Scans for [BeaconInfo].
 * Only supports the [Eddystone] protocol
 */
interface Beacons {
    /**
     * [StateFlow] of the set of [BeaconInfo] currently scanned
     */
    val beacons: StateFlow<Set<BeaconInfo>>

    /**
     * Starts monitoring for changes to [BeaconInfo]
     */
    suspend fun startMonitoring()

    /**
     * Stops monitoring for changes to [BeaconInfo]
     */
    suspend fun stopMonitoring()

    /**
     * A [Flow] indicating whether [startMonitoring] has been called
     * @return A [Flow] containing `true` if [startMonitoring] has been called
     */
    suspend fun isMonitoring(): Flow<Boolean>
}

/**
 * Gets a [Flow] that indicates whether any [BeaconID] have been detected
 * @param beaconIds the list of [BeaconID] to scan for
 * @return A [Flow] containing `true` if any of the [BeaconID] have been scanned.
 */
fun Beacons.isAnyInRange(beaconIds: List<BeaconID>): Flow<Boolean> {
    val lowerCasedIds = beaconIds.map { it.asString().lowercase() }
    return beacons.map { list ->
        list.any { lowerCasedIds.contains(it.beaconID.asString().lowercase()) }
    }
}

private val defaultBeaconsDispatcher by lazy {
    singleThreadDispatcher("Beacons")
}

/**
 * Default implementation of [Beacons]
 * @param bluetooth the [BluetoothService] managing bluetooth
 * @param beaconLifetime the [Duration] during which [BeaconInfo] is valid
 * @param logger the [Logger] to use for logging
 * @param coroutineContext the [CoroutineContext] beacons are monitored on
 */
class DefaultBeacons(
    private val bluetooth: BluetoothService,
    private val beaconLifetime: Duration = 10.seconds,
    private val logger: Logger = RestrictedLogger(RestrictedLogLevel.None),
    coroutineContext: CoroutineContext = defaultBeaconsDispatcher,
) : Beacons, CoroutineScope by CoroutineScope(coroutineContext + CoroutineName("Beacons")) {

    private companion object {
        const val TAG = "Beacons"
    }

    private val monitoringLock = Mutex()
    private val updateLock = Mutex()
    private val cache = concurrentMutableMapOf<BeaconID, BeaconJob>()
    private val cacheJob = Job()
    private var monitoringJob: MutableStateFlow<Job?> = MutableStateFlow(null)

    private val _beacons = MutableStateFlow(emptySet<BeaconInfo>())
    override val beacons: StateFlow<Set<BeaconInfo>>
        get() = _beacons.asStateFlow()

    override suspend fun startMonitoring() = monitoringLock.withLock {
        logger.info(TAG, "Start monitoring")
        _beacons.value = emptySet()
        bluetooth.startScanning()
        monitoringJob.value = this@DefaultBeacons.launch {
            bluetooth.scannedDevices().collect { list ->
                logger.debug(TAG, "Total Bluetooth devices discovered: ${list.size}")
                updateBeacons(list.mapNotNull { createBeaconWith(it) })
            }
        }
    }

    override suspend fun stopMonitoring() = monitoringLock.withLock {
        logger.info(TAG, "Stop monitoring")
        bluetooth.stopScanning()
        monitoringJob.value?.cancel()
        monitoringJob.value = null
        _beacons.value = emptySet()
    }

    override suspend fun isMonitoring() = combine(monitoringJob.map { it != null }, bluetooth.isScanning()) { isMonitoring, isScanning -> isMonitoring && isScanning }

    private suspend fun updateBeacons(discovered: List<BeaconInfo>) = updateLock.withLock {
        logger.info(TAG, "Total Beacons discovered: ${discovered.size}")
        discovered.forEach { beacon ->
            cache.synchronized {
                if (containsKey(beacon.beaconID)) {
                    logger.debug(TAG, "[Found] $beacon")
                    remove(beacon.beaconID)?.second?.cancel()
                    logger.debug(TAG, "[Removed] $beacon")
                } else {
                    debug(TAG, "[New] $beacon")
                }
                this[beacon.beaconID] = beacon to this@DefaultBeacons.launch(cacheJob) {
                    logger.debug(TAG, "[Added] $beacon")
                    delay(beacon.lastSeen + beaconLifetime - DefaultKalugaDate.now())
                    logger.debug(TAG, "[Lost] $beacon")
                    cache.remove(beacon.beaconID)
                    updateList()
                }
            }
        }
        updateList()
    }

    private fun updateList() = _beacons.update {
        cache.values.map(BeaconJob::first).toSet()
    }

    private suspend fun createBeaconWith(device: Device): BeaconInfo? {
        val serviceData = device.info.map { it.advertisementData.serviceData }.firstOrNull() ?: return null
        val data = serviceData[Eddystone.SERVICE_UUID] ?: return null
        val frame = Eddystone.unpack(data) ?: return null
        val rssi = device.info.map { it.rssi }.firstOrNull() ?: 0
        val lastSeen = device.info.map { it.updatedAt }.firstOrNull() ?: DefaultKalugaDate.now()
        return BeaconInfo(device.identifier, frame.uid, frame.txPower, rssi, lastSeen)
    }
}

/**
 * Gets a ([Flow] of) [BeaconInfo] from the [Flow] of a collection of [BeaconInfo]
 * @param identifier the [Identifier] of the [BeaconInfo] to get
 * @return a [Flow] containing the [BeaconInfo] matching [Identifier] if available
 */
operator fun Flow<Set<BeaconInfo>>.get(identifier: Identifier) = map { beacons ->
    beacons.firstOrNull { it.identifier == identifier }
}
