/*
 Copyright 2023 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.system.network

/**
 * The type of connection to a network
 */
sealed class NetworkConnectionType {

    /**
     * A [NetworkConnectionType] where it is unknown what th current connection state is
     * @property reason the [Reason] the network type is unknown.
     */
    sealed class Unknown(open val reason: Reason) : NetworkConnectionType() {

        /**
         * A reason why an [Unknown] network type may occur
         */
        enum class Reason {

            /**
             * The system is in the process of connecting to a network
             */
            CONNECTING,

            /**
             * The system is in the process of disconnecting from a network
             */
            LOSING,

            /**
             * There is no clear reason why the network state is unknown
             */
            NOT_CLEAR,
        }

        /**
         * An [Unknown] reason where no previously [Known] network state is available
         * @param reason the [Reason] the network type is unknown.
         */
        data class WithoutLastNetwork(override val reason: Reason) : Unknown(reason)

        /**
         * An [Unknown] reason where a previously [Known] network state is available
         * @property lastKnown the last [Known] network state available before the network became [Unknown]
         * @param reason the [Reason] the network type is unknown.
         */
        data class WithLastNetwork(
            val lastKnown: Known,
            override val reason: Reason,
        ) : Unknown(reason)
    }

    /**
     * A [NetworkConnectionType] where the current connection state is known
     */
    sealed class Known : NetworkConnectionType() {
        /**
         * A [Known] network type where the network is available
         */
        sealed class Available : Known() {

            /**
             * If `true` using the network may result in some costs
             */
            abstract val isExpensive: Boolean
        }

        /**
         * An [Available] network connected to the cellular network
         */
        data object Cellular : Available() {
            override val isExpensive: Boolean = true
        }

        /**
         * An [Available] network connected to WIFI.
         * @property isExpensive If `true` using the network may result in some costs
         */
        data class Wifi(override val isExpensive: Boolean = false) : Available()

        /**
         * A [Known] network type where no network is available
         */
        data object Absent : Known()
    }
}

/**
 * Converts a [NetworkConnectionType] into an [NetworkConnectionType.Unknown] given a [NetworkConnectionType.Unknown.Reason]
 * @param reason the [NetworkConnectionType.Unknown.Reason] the [NetworkConnectionType] became unknown
 * @return the [NetworkConnectionType.Unknown] with [reason].
 * If the [NetworkConnectionType] this method had a [NetworkConnectionType.Known], this will return [NetworkConnectionType.Unknown.WithLastNetwork], otherwise [NetworkConnectionType.Unknown.WithoutLastNetwork]
 */
fun NetworkConnectionType.unknown(reason: NetworkConnectionType.Unknown.Reason) = when (this) {
    is NetworkConnectionType.Known -> NetworkConnectionType.Unknown.WithLastNetwork(this, reason)
    is NetworkConnectionType.Unknown.WithLastNetwork -> NetworkConnectionType.Unknown.WithLastNetwork(
        this.lastKnown,
        reason,
    )
    is NetworkConnectionType.Unknown.WithoutLastNetwork -> NetworkConnectionType.Unknown.WithoutLastNetwork(
        reason,
    )
}
