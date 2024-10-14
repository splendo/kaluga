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

package com.splendo.kaluga.base

import platform.UIKit.UIDevice

/**
 * Wrapper for the iOS OS Version
 * @property major The major version of the OS
 * @property minor The minor version of the OS
 * @property patch The patch version of the OS
 */
data class IOSVersion(val major: Int, val minor: Int = 0, val patch: Int = 0) : Comparable<IOSVersion> {

    companion object {
        /**
         * Gets the [IOSVersion] for the current device
         */
        val systemVersion: IOSVersion
            get() {
                val versions = UIDevice.currentDevice.systemVersion.split(".").map { it.toIntOrNull() ?: 0 }
                return IOSVersion(versions.getOrNull(0) ?: 0, versions.getOrNull(1) ?: 0, versions.getOrNull(2) ?: 0)
            }
    }

    override fun compareTo(other: IOSVersion): Int = when {
        this.major > other.major -> 1
        this.major == other.major -> {
            when {
                this.minor > other.minor -> 1
                this.minor == other.minor -> this.patch.compareTo(other.patch)
                else -> -1
            }
        }
        else -> -1
    }
}
