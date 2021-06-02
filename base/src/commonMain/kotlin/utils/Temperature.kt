/*
 Copyright 2020 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.base.utils


interface Temperature<T>:SciUnit<T>, Serializable {
    override val value: T
    val fahrenheit:Fahrenheit<T>
    val celsius:Celsius<T>
}

interface Celsius<T>:Temperature<T>, Serializable
interface Fahrenheit<T>:Temperature<T>, Serializable
inline class CelsiusDouble(override val value:Double):Celsius<Double>, Serializable {
    override val fahrenheit: Fahrenheit<Double>
        get() = FahrenheitDouble(value * 9 / 5 + 32)
    override val celsius: Celsius<Double>
        get() = this
}
inline class FahrenheitDouble(override val value:Double):Fahrenheit<Double>, Serializable {
    override val fahrenheit: Fahrenheit<Double>
        get() = this
    override val celsius: Celsius<Double>
        get() = CelsiusDouble((value - 32) * 5 / 9)
}

fun Double.celsius() = CelsiusDouble(this)
fun Double.fahrenheit() = FahrenheitDouble(this)