/*
 Copyright 2021 Splendo Consulting B.V. The Netherlands

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

interface SciUnit<T> : Serializable {
    val value:T
}

interface percentage<T> : SciUnit<T>, Serializable {
    override val value : T
    val valid: percentage<T> //Not sure if this should be here
}
interface Index<T> : SciUnit<T>, Serializable

inline class closedPercentageDouble(override val value:Double) : percentage<Double>, Serializable{
    override val valid: percentage<Double>
        get() {
            if (value in 0.0..1.0){ //Valid values in 0-1 range, since it's a percentage.
                return closedPercentageDouble(value)
            }
            else {
                throw Exception("Value out of valid range")
            }
        }
}