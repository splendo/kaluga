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
//Based on https://github.com/splendo/health-app/blob/monitor/SplendoMonitor/SplendoHealth/Domain/EventBus/Events/OutputData/Measurement/OxygenationDataSource.swift
package com.splendo.kaluga.base.utils





interface OxygenationData<T> : SciUnit<T>, Serializable {
    override val value: T
}

interface Concentration<T> : OxygenationData<T>, Serializable {
    override val value: T
}
//Archetypal concentration unit inline class
interface Micromolar<T> : Concentration<T>, Serializable
inline class MicromolarDouble(override val value: Double) : Micromolar<Double>, Serializable{
    val valid : MicromolarDouble
        get() {
            if (value in -319.9..319.9){
                return MicromolarDouble(value)
            }
            else {
                throw Exception("Value out of valid range")
            }
        }
}

/*The NIRS quantities as inline classes */

interface TSI<T> : OxygenationData<T>, percentage<T>, Serializable{
    override val value: T
    override val valid: percentage<T>
}

inline class TSIDouble(override val value: Double) : TSI<Double>, Serializable{
        override val valid : TSIDouble
        get() {
            if (value in 0.0..1.0){ //Valid values in 0-1 range, since it's a percentage.
                return TSIDouble(value)
            }
            else {
                throw Exception("Value out of valid range")
            }
        }
}

interface O2HB<T> : Concentration<T>, Serializable {
    override val value: T
}

inline class O2HBMicromolarDouble(override val value: Double) : O2HB<Double>, Serializable{
    val valid : O2HBMicromolarDouble
    get() {
        if (value in -319.9..319.9){
            return O2HBMicromolarDouble(value)
        }
        else {
            throw Exception("Value out of valid range")
        }
    }
}

interface HHB<T> : Concentration<T>, Serializable {
    override val value: T
}
inline class HHBMicromolarDouble(override val value: Double) : HHB<Double>, Serializable{
    val valid : HHBMicromolarDouble
        get() {
            if (value in -319.9..319.9){
                return HHBMicromolarDouble(value)
            }
            else {
                throw Exception("Value out of valid range")
            }
        }
}

interface tHB<T> : Concentration<T>, Serializable {
    override val value: T
}
inline class tHBMicromolarDouble(override val value: Double) : tHB<Double>, Serializable{
    val valid : tHBMicromolarDouble
        get() {
            if (value in -319.9..319.9){
                return tHBMicromolarDouble(value)
            }
            else {
                throw Exception("Value out of valid range")
            }
        }
}
interface HbDiff<T> : Concentration<T>, Serializable {
    override val value: T
}
inline class HbDiffDouble(override val value: Double) : HbDiff<Double>, Serializable{
    val valid : HbDiffDouble
        get() {
            if (value in -319.9..319.9){
                return HbDiffDouble(value)
            }
            else {
                throw Exception("Value out of valid range")
            }
        }
}