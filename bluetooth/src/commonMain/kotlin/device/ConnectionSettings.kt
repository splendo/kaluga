/*
 Copyright (c) 2020. Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.bluetooth.device

import com.splendo.kaluga.bluetooth.UUID
import com.splendo.kaluga.bluetooth.uuidString
import com.splendo.kaluga.logging.ContextualLogger
import com.splendo.kaluga.logging.Logger
import com.splendo.kaluga.logging.RestrictedLogLevel
import com.splendo.kaluga.logging.RestrictedLogger

/**
 * Settings to apply to a [Device] when connecting
 * @property reconnectionSettings the [ReconnectionSettings] to apply when reconnecting
 */
class ConnectionSettings private constructor(
    val reconnectionSettings: ReconnectionSettings = ReconnectionSettings.Always,
    private val connectionLoggerBuilders: (Identifier) -> ConnectionLogger,
) {

    /**
     * Constructor
     * @param reconnectionSettings the [ReconnectionSettings] to apply when reconnecting
     * @param rootLogger the [Logger] to use as the root for all logging as configured by [buildLogger]
     * @param buildLogger the [LoggerDSL] to use for logging
     */
    constructor(
        reconnectionSettings: ReconnectionSettings = ReconnectionSettings.Always,
        rootLogger: Logger = RestrictedLogger(RestrictedLogLevel.None),
        buildLogger: LoggerDSL.() -> Unit,
    ) : this(reconnectionSettings, { identifier ->
        ConnectionLoggerDSLImpl(identifier, rootLogger).apply(buildLogger).build()
    })

    /**
     * Constructor
     * @param reconnectionSettings the [ReconnectionSettings] to apply when reconnecting
     * @param logger the [Logger] to use for logging state
     * @param dataLogger the [Logger] to use for logging of sensitive, potentially high volume data
     */
    constructor(
        reconnectionSettings: ReconnectionSettings = ReconnectionSettings.Always,
        logger: Logger = RestrictedLogger(RestrictedLogLevel.None),
        dataLogger: Logger = RestrictedLogger(RestrictedLogLevel.None),
    ) : this(reconnectionSettings, rootLogger = logger, {
        logData(contextualLogger.withLogger(dataLogger))
    })

    internal fun logger(identifier: Identifier) = connectionLoggerBuilders(identifier)

    /**
     * Behaviour to apply when a [Device] disconnects unexpectedly
     */
    sealed class ReconnectionSettings {
        /**
         * Should always try to reconnect when an unexpected disconnect occurs
         */
        data object Always : ReconnectionSettings()

        /**
         * Should never try to reconnect when an unexpected disconnect occurs
         */
        data object Never : ReconnectionSettings()
    }

    internal class ConnectionLogger(val stateLogger: StateLogger, val dataLogger: DataLogger) {
        class StateLogger(val stateChangeLogger: Logger, val actionLogger: Logger)

        class DataLogger(private val logger: Logger, specificLoggers: Map<UUID, ServiceLogger>, private val defaultLogger: (UUID) -> ServiceLogger) : Logger by logger {
            private val loggers = specificLoggers.toMutableMap()

            operator fun get(uuid: UUID) = loggers.getOrPut(uuid) {
                defaultLogger(uuid)
            }
        }

        class ServiceLogger(
            private val logger: Logger,
            specificLoggers: Map<UUID, CharacteristicLogger> = emptyMap(),
            private val defaultLogger: (UUID) -> CharacteristicLogger = { CharacteristicLogger(ContextualLogger(logger, "Characteristic ${it.uuidString}")) },
        ) : Logger by logger {

            private val loggers = specificLoggers.toMutableMap()

            operator fun get(uuid: UUID) = loggers.getOrPut(uuid) {
                defaultLogger(uuid)
            }
        }

        class CharacteristicLogger(
            private val logger: Logger,
            specificLoggers: Map<UUID, Logger> = emptyMap(),
            private val defaultLogger: (UUID) -> Logger = { ContextualLogger(logger, "Descriptor ${it.uuidString}") },
        ) : Logger by logger {

            private val loggers = specificLoggers.toMutableMap()

            operator fun get(uuid: UUID) = loggers.getOrPut(uuid) {
                defaultLogger(uuid)
            }
        }
    }

    /**
     * DSL used by all layers of logging for [ConnectionSettings]
     * @property contextualLogger the [ContextualLogger] to use for logging at this level
     */
    interface CommonLoggerDSL {
        val contextualLogger: ContextualLogger
    }

    /**
     * A [CommonLoggerDSL] that sets up the logging for a [ConnectableDevice]
     */
    interface LoggerDSL : CommonLoggerDSL {

        /**
         * The [CommonLoggerDSL] that sets up the logging of a [ConnectableDeviceState]
         */
        interface StateLoggerDSL : CommonLoggerDSL {

            /**
             * Sets the logging to use when [ConnectableDeviceState] changes occur.
             * @param logger the [ContextualLogger] to use for logging state changes.
             */
            fun logStateChangesBy(logger: ContextualLogger = contextualLogger.withAppendedTag("STATE_CHANGES"))

            /**
             * Sets up the logging to use when [DeviceAction] changes occur.
             * @param logger the [ContextualLogger] to use for logging action changes.
             */
            fun logAction(logger: ContextualLogger = contextualLogger.withAppendedTag("ACTION"))
        }

        /**
         * The [CommonLoggerDSL] that sets up the logging of the data handled by a [ConnectableDevice]
         */
        interface DataLoggerDSL : CommonLoggerDSL {

            /**
             * Creates the default [ContextualLogger] to use for a [com.splendo.kaluga.bluetooth.RemoteService] of a given [UUID]
             * @param uuid the [UUID] of the [com.splendo.kaluga.bluetooth.RemoteService]
             * @return the [ContextualLogger] to use by default
             */
            fun defaultLoggerForService(uuid: UUID) = contextualLogger.withAppendedContext("Service" to uuid.uuidString)

            /**
             * Sets up the [ServiceLoggerDSL] for a [com.splendo.kaluga.bluetooth.RemoteService] of a given [UUID]
             * @param uuid the [UUID] of the [com.splendo.kaluga.bluetooth.RemoteService]
             * @param logger the [ContextualLogger] to use for logging
             * @param serviceLogger the [ServiceLoggerDSL] to setup service logging.
             */
            fun logService(uuid: UUID, logger: ContextualLogger = defaultLoggerForService(uuid), serviceLogger: ServiceLoggerDSL.() -> Unit = {})

            /**
             * Sets up the [ServiceLoggerDSL] for all [com.splendo.kaluga.bluetooth.RemoteService] not explicitly configured hy [logService]
             * @param logger creates a [ContextualLogger] to use for logging of a given [UUID]
             * @param serviceLogger the [ServiceLoggerDSL] to setup default service logging.
             */
            fun logAnyService(logger: (UUID) -> ContextualLogger = ::defaultLoggerForService, serviceLogger: ServiceLoggerDSL.() -> Unit = {})
        }

        /**
         * The [CommonLoggerDSL] that sets up the logging of the data handled by a [com.splendo.kaluga.bluetooth.RemoteService]
         */
        interface ServiceLoggerDSL : CommonLoggerDSL {

            /**
             * Creates the default [ContextualLogger] to use for a [com.splendo.kaluga.bluetooth.RemoteCharacteristic] of a given [UUID]
             * @param uuid the [UUID] of the [com.splendo.kaluga.bluetooth.RemoteCharacteristic]
             * @return the [ContextualLogger] to use by default
             */
            fun defaultLoggerForCharacteristic(uuid: UUID) = contextualLogger.withAppendedContext("Characteristic" to uuid.uuidString)

            /**
             * Sets up the [CharacteristicLoggerDSL] for a [com.splendo.kaluga.bluetooth.RemoteCharacteristic] of a given [UUID]
             * @param uuid the [UUID] of the [com.splendo.kaluga.bluetooth.RemoteCharacteristic]
             * @param logger the [ContextualLogger] to use for logging
             * @param characteristicLogger the [ServiceLoggerDSL] to setup characteristic logging.
             */
            fun logCharacteristic(uuid: UUID, logger: ContextualLogger = defaultLoggerForCharacteristic(uuid), characteristicLogger: CharacteristicLoggerDSL.() -> Unit = {})

            /**
             * Sets up the [CharacteristicLoggerDSL] for all [com.splendo.kaluga.bluetooth.RemoteCharacteristic] not explicitly configured hy [logCharacteristic]
             * @param logger creates a [ContextualLogger] to use for logging of a given [UUID]
             * @param characteristicLogger the [ServiceLoggerDSL] to setup default characteristic logging.
             */
            fun logAnyCharacteristic(logger: (UUID) -> ContextualLogger = ::defaultLoggerForCharacteristic, characteristicLogger: CharacteristicLoggerDSL.() -> Unit = {})
        }

        /**
         * The [CommonLoggerDSL] that sets up the logging of the data handled by a [com.splendo.kaluga.bluetooth.RemoteDescriptor]
         */
        interface CharacteristicLoggerDSL : CommonLoggerDSL {

            /**
             * Creates the default [ContextualLogger] to use for a [com.splendo.kaluga.bluetooth.RemoteDescriptor] of a given [UUID]
             * @param uuid the [UUID] of the [com.splendo.kaluga.bluetooth.RemoteDescriptor]
             * @return the [ContextualLogger] to use by default
             */
            fun defaultLoggerForDescriptor(uuid: UUID) = contextualLogger.withAppendedContext("Descriptor" to uuid.uuidString)

            /**
             * Sets the [ContextualLogger] for a [com.splendo.kaluga.bluetooth.RemoteDescriptor] of a given [UUID]
             * @param uuid the [UUID] of the [com.splendo.kaluga.bluetooth.RemoteDescriptor]
             * @param logger the [ContextualLogger] to use for logging
             */
            fun logDescriptor(uuid: UUID, logger: ContextualLogger = defaultLoggerForDescriptor(uuid))

            /**
             * Sets the [ContextualLogger] for all [com.splendo.kaluga.bluetooth.RemoteDescriptor] not explicitly configured hy [logAnyDescriptor]
             * @param logger creates a [ContextualLogger] to use for logging of a given [UUID]
             */
            fun logAnyDescriptor(logger: (UUID) -> ContextualLogger = ::defaultLoggerForDescriptor)
        }

        /**
         * Sets up the [StateLoggerDSL] for logging of [ConnectableDeviceState]
         * @param logger the [ContextualLogger] to use for logging state.
         * @param stateLogger the [StateLoggerDSL] to setup state logging.
         */
        fun logState(logger: ContextualLogger = contextualLogger.withAppendedTag("STATE"), stateLogger: StateLoggerDSL.() -> Unit = {})

        /**
         * Sets up the [DataLoggerDSL] for logging of data handled by a [ConnectableDevice]
         * @param logger the [ContextualLogger] to use for logging data.
         * @param dataLogger the [DataLoggerDSL] to setup data logging.
         */
        fun logData(logger: ContextualLogger = contextualLogger.withAppendedTag("DATA"), dataLogger: DataLoggerDSL.() -> Unit = {})
    }
}

private class ConnectionLoggerDSLImpl(identifier: Identifier, rootLogger: Logger) : ConnectionSettings.LoggerDSL {

    inner class StateLoggerDSLImpl(override val contextualLogger: ContextualLogger = this.contextualLogger.withAppendedTag("STATE")) :
        ConnectionSettings.LoggerDSL.StateLoggerDSL {
        private var stateChangesLogger: ContextualLogger = contextualLogger.withAppendedTag("STATE_CHANGES")
        private var actionLogger: ContextualLogger = contextualLogger.withAppendedTag("ACTION")

        override fun logStateChangesBy(logger: ContextualLogger) {
            stateChangesLogger = logger
        }

        override fun logAction(logger: ContextualLogger) {
            actionLogger = logger
        }

        fun build() = ConnectionSettings.ConnectionLogger.StateLogger(
            stateChangesLogger,
            actionLogger,
        )
    }

    inner class DataLoggerDSLImpl(override val contextualLogger: ContextualLogger = this.contextualLogger.withAppendedTag("DATA")) : ConnectionSettings.LoggerDSL.DataLoggerDSL {

        private val specificLoggers = mutableMapOf<UUID, ConnectionSettings.ConnectionLogger.ServiceLogger>()
        private var defaultLogger: (UUID) -> ConnectionSettings.ConnectionLogger.ServiceLogger = {
            ServiceLoggerDSLImpl(defaultLoggerForService(it)).build()
        }

        override fun logService(uuid: UUID, logger: ContextualLogger, serviceLogger: ConnectionSettings.LoggerDSL.ServiceLoggerDSL.() -> Unit) {
            specificLoggers[uuid] = ServiceLoggerDSLImpl(logger).apply(serviceLogger).build()
        }

        override fun logAnyService(logger: (UUID) -> ContextualLogger, serviceLogger: ConnectionSettings.LoggerDSL.ServiceLoggerDSL.() -> Unit) {
            defaultLogger = {
                ServiceLoggerDSLImpl(logger(it)).apply(serviceLogger).build()
            }
        }

        fun build() = ConnectionSettings.ConnectionLogger.DataLogger(
            contextualLogger,
            specificLoggers.toMap(),
            defaultLogger,
        )
    }

    class ServiceLoggerDSLImpl(override val contextualLogger: ContextualLogger) : ConnectionSettings.LoggerDSL.ServiceLoggerDSL {

        private val specificLoggers = mutableMapOf<UUID, ConnectionSettings.ConnectionLogger.CharacteristicLogger>()
        private var defaultLogger: (UUID) -> ConnectionSettings.ConnectionLogger.CharacteristicLogger = {
            CharacteristicLoggerDSLImpl(defaultLoggerForCharacteristic(it)).build()
        }

        override fun logCharacteristic(uuid: UUID, logger: ContextualLogger, characteristicLogger: ConnectionSettings.LoggerDSL.CharacteristicLoggerDSL.() -> Unit) {
            specificLoggers[uuid] = CharacteristicLoggerDSLImpl(logger).apply(characteristicLogger).build()
        }

        override fun logAnyCharacteristic(logger: (UUID) -> ContextualLogger, characteristicLogger: ConnectionSettings.LoggerDSL.CharacteristicLoggerDSL.() -> Unit) {
            defaultLogger = {
                CharacteristicLoggerDSLImpl(logger(it)).apply(characteristicLogger).build()
            }
        }

        fun build(): ConnectionSettings.ConnectionLogger.ServiceLogger = ConnectionSettings.ConnectionLogger.ServiceLogger(
            contextualLogger,
            specificLoggers.toMap(),
            defaultLogger,
        )
    }

    class CharacteristicLoggerDSLImpl(override val contextualLogger: ContextualLogger) : ConnectionSettings.LoggerDSL.CharacteristicLoggerDSL {

        private val specificLoggers = mutableMapOf<UUID, Logger>()
        private var defaultLogger: (UUID) -> Logger = {
            defaultLoggerForDescriptor(it)
        }

        override fun logDescriptor(uuid: UUID, logger: ContextualLogger) {
            specificLoggers[uuid] = logger
        }

        override fun logAnyDescriptor(logger: (UUID) -> ContextualLogger) {
            defaultLogger = logger
        }

        fun build(): ConnectionSettings.ConnectionLogger.CharacteristicLogger = ConnectionSettings.ConnectionLogger.CharacteristicLogger(
            contextualLogger,
            specificLoggers.toMap(),
            defaultLogger,
        )
    }

    override val contextualLogger: ContextualLogger = ContextualLogger(rootLogger, "Bluetooth ${identifier.stringValue}")

    private var stateLogger: StateLoggerDSLImpl = StateLoggerDSLImpl()
    private var dataLogger: DataLoggerDSLImpl = DataLoggerDSLImpl()

    override fun logState(logger: ContextualLogger, stateLogger: ConnectionSettings.LoggerDSL.StateLoggerDSL.() -> Unit) {
        this.stateLogger = StateLoggerDSLImpl(logger).apply(stateLogger)
    }

    override fun logData(logger: ContextualLogger, dataLogger: ConnectionSettings.LoggerDSL.DataLoggerDSL.() -> Unit) {
        this.dataLogger = DataLoggerDSLImpl().apply(dataLogger)
    }

    fun build(): ConnectionSettings.ConnectionLogger = ConnectionSettings.ConnectionLogger(
        stateLogger.build(),
        dataLogger.build(),
    )
}
