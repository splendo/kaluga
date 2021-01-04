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


interface OxygenationData<T> : SciUnit<T> {
    override val value: T
}


inline class TSI(override val value: Double) : OxygenationData<Double>{
        val valid : TSI
        get() {
            if (value in 0.0..1.0){ //Valid values in 0-1 range, since it's a percentage.
                return TSI(value)
            }
            else {
                throw Exception("Value out of valid range")
            }
        }
}

inline class O2HB(override val value: Double) : OxygenationData<Double>{
    val valid : O2HB
    get() {
        if (value in -319.9..319.9){
            return O2HB(value)
        }
        else {
            throw Exception("Value out of valid range")
        }
    }
}

inline class HHB(override val value: Double) : OxygenationData<Double>{
    val valid : HHB
        get() {
            if (value in -319.9..319.9){
                return HHB(value)
            }
            else {
                throw Exception("Value out of valid range")
            }
        }
}

inline class tHB(override val value: Double) : OxygenationData<Double>{
    val valid : tHB
        get() {
            if (value in -319.9..319.9){
                return tHB(value)
            }
            else {
                throw Exception("Value out of valid range")
            }
        }
}

inline class HbDiff(override val value: Double) : OxygenationData<Double>{
    val valid : HbDiff
        get() {
            if (value in -319.9..319.9){
                return HbDiff(value)
            }
            else {
                throw Exception("Value out of valid range")
            }
        }
}