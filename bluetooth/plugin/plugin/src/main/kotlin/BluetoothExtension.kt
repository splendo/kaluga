/*
 Copyright 2026 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.bluetooth.plugin

import com.google.devtools.ksp.gradle.KspExtension
import org.gradle.api.model.ObjectFactory
import org.gradle.kotlin.dsl.setProperty

enum class BluetoothTarget {
    CLIENT,
    SERVER,
}

enum class ImplementFor {
    BLUETOOTH,
    SIMULATOR,
}

open class BluetoothExtension(private val kspExtension: KspExtension, objects: ObjectFactory) {

    val target = objects.setProperty<BluetoothTarget>().apply {
        add(BluetoothTarget.CLIENT)
    }

    val implementFor = objects.setProperty<ImplementFor>().apply {
        add(ImplementFor.BLUETOOTH)
    }

    fun afterEvaluate() {
        kspExtension.arg("target", target.get().joinToString(separator = ",") { it.name })
        kspExtension.arg("implementFor", implementFor.get().joinToString(separator = ",") { it.name })
    }
}
