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
 */
data class IOSVersion(val major: Int, val minor: Int, val patch: Int) {

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

    /**
     * Compares this to another [IOSVersion]
     * @param version The [IOSVersion] to compare to
     * @return `true` if this version is the same or newer as the given version. `false` otherwise
     */
    fun isOSVersionOrNewer(version: IOSVersion): Boolean {
        return when {
            this.major > version.major -> true
            this.major == version.major -> {
                when {
                    this.minor > version.minor -> true
                    else -> this.minor == version.minor && this.patch >= version.patch
                }

            }
            else -> false
        }
    }

}

