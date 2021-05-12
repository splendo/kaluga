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

//Based on https://github.com/splendo/health-app/blob/monitor/SplendoMonitor/SplendoHealth/Domain/EventBus/Events/OutputData/Measurement/CardiacOutputDataSource.swift

package com.splendo.kaluga.base.utils

// Add/Use Closed ranges

interface CardiacOutputData<T> : SciUnit<T>


inline class CardiacOutput (override val value: Double) : CardiacOutputData<Double> {
    val valid : CardiacOutput
    get() {
        if (value in 0.01..10.0){ //Valid values in 0-1 range, since it's a percentage.
                return CardiacOutput(value)
        }
        else {
            throw Exception("Value out of valid range")
        }
    }
}

inline class StrokeVolume (override val value: Double) : CardiacOutputData<Double> {
    val valid : StrokeVolume
    get() {
        if (value in 0.01..200.0) {
            return StrokeVolume(value)
        }else {
                throw Exception("Value out of valid range")
        }
    }
}
inline class HeartRate (override val value: Double) : CardiacOutputData<Double> {
    val valid : HeartRate
        get() {
            if (value in 0.0..300.0) {
                return HeartRate(value)
            }else {
                throw Exception("Value out of valid range")
            }
        }
}
inline class CardiacIndex (override val value: Double) : CardiacOutputData<Double> {
    val valid : CardiacIndex
        get() {
            if (value in 1.0..10.0) {
                return CardiacIndex(value)
            }else {
                throw Exception("Value out of valid range")
            }
        }
}
inline class StrokeVolumeIndex (override val value: Double) : CardiacOutputData<Double> {
    val valid : StrokeVolumeIndex
        get() {
            if (value in 10.0..80.0) {
                return StrokeVolumeIndex(value)
            }else {
                throw Exception("Value out of valid range")
            }
        }
}
inline class SystemicVascularResistance (override val value: Double) : CardiacOutputData<Double> {
    val valid : SystemicVascularResistance
        get() {
            if (value in 500.0..2000.0) {
                return SystemicVascularResistance(value)
            }else {
                throw Exception("Value out of valid range")
            }
        }
}
inline class SystemicVascularResistanceIndex (override val value: Double) : CardiacOutputData<Double> {
    val valid : SystemicVascularResistanceIndex
        get() {
            if (value in 1000.0..5000.0) {
                return SystemicVascularResistanceIndex(value)
            }else {
                throw Exception("Value out of valid range")
            }
        }
}
inline class LeftCardiacWorkIndex (override val value: Double) : CardiacOutputData<Double> {
    val valid : LeftCardiacWorkIndex
        get() {
            if (value in 1.0..6.0) {
                return LeftCardiacWorkIndex(value)
            }else {
                throw Exception("Value out of valid range")
            }
        }
}
inline class ContractilityIndex (override val value: Double) : CardiacOutputData<Double> {
    val valid : ContractilityIndex
        get() {
            return ContractilityIndex(value) //No ranges within value is expected so always return?
        }
}
inline class VentricularEjectionTime (override val value: Double) : CardiacOutputData<Double> {
    val valid : VentricularEjectionTime
        get(){
            if (value in 0.1..0.9){ //in milliseconds
                return VentricularEjectionTime(value)
            } else {
                throw Exception("Value out of valid range")
            }
        }
}
inline class ThoracicFlowInversionTime (override val value: Double) : CardiacOutputData<Double> {
    val valid : ThoracicFlowInversionTime
        get() {
            if (value in 0.1..0.9){
                return ThoracicFlowInversionTime(value)
            } else {
                throw Exception("Value out of valid range")
            }
        }
}
inline class EjectionFraction (override val value: Double) : CardiacOutputData<Double> {
    val valid : EjectionFraction
        get() {
            if (value in 0.0..1.0){
                return EjectionFraction(value)
            } else {
                throw Exception("Value out of valid range")
            }
        }
}
inline class EndDiastolicVolume (override val value: Double) : CardiacOutputData<Double> {
    val valid : EndDiastolicVolume
        get () {
            if (value in 50.0..300.0){
                return EndDiastolicVolume(value)
            } else {
                throw Exception("Value out of valid range")
            }
        }
}
inline class FillingIndex (override val value: Double) : CardiacOutputData<Double> {
    val valid : FillingIndex
    get (){
        return FillingIndex(value)
    }
}
inline class SystolicArterialPressure (override val value: Double) : CardiacOutputData<Double> {
    val valid : SystolicArterialPressure
    get() {
        if (value in 10.0..400.0){
            return SystolicArterialPressure(value)
        } else {
            throw Exception("Value out of valid range")
        }
    }
}
inline class MeanArterialPressure (override val value: Double) : CardiacOutputData<Double> {
    val valid : MeanArterialPressure
    get() {
        if (value in 10.0..400.0){
            return MeanArterialPressure(value)
        } else {
            throw Exception("Value out of valid range")
        }
    }
}
inline class DiastolicArterialPressure (override val value: Double) : CardiacOutputData<Double> {
    val valid : DiastolicArterialPressure
    get() {
        if (value in 10.0..400.0){
            return DiastolicArterialPressure(value)
        } else {
            throw Exception("Value out of valid range")
        }
    }
}
inline class EDFR (override val value: Double) : CardiacOutputData<Double> {
    val valid : EDFR
    get() {
        return EDFR(value)
    }
}
inline class ThoracicFluidIndex (override val value: Double) : CardiacOutputData<Double> {
 val valid : ThoracicFluidIndex
    get() {
        return ThoracicFluidIndex(value)
    }
}
inline class SignalQuality (override val value: Double) : CardiacOutputData<Double> {
    val valid : SignalQuality
        get() {
            if (value in 0.0..1.0){
                return SignalQuality(value)
            } else {
                throw Exception("Value out of valid range")
            }
        }
}
inline class BreathingFrequency (override val value: Double) : CardiacOutputData<Double> {
    val valid : BreathingFrequency
    get() {
        return BreathingFrequency(value)
    }
}
