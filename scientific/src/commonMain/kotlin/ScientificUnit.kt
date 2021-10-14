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
package com.splendo.kaluga.scientific

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.base.utils.toDecimal
import com.splendo.kaluga.base.utils.toDouble
import kotlinx.serialization.Serializable

sealed interface ScientificUnit<Type : MeasurementType> : MeasurementUsage {
    val symbol: String
    val system: MeasurementSystem
    val type: Type
    fun toSIUnit(value: Decimal): Decimal
    fun fromSIUnit(value: Decimal): Decimal
}

interface SystemScientificUnit<System : MeasurementSystem, Type : MeasurementType> : ScientificUnit<Type> {
    override val system: System
}

interface SIScientificUnit<Type : MeasurementType> : SystemScientificUnit<MeasurementSystem.SI, Type>, MeasurementUsage.UsedGlobally
interface MetricScientificUnit<Type : MeasurementType> : SystemScientificUnit<MeasurementSystem.Metric, Type>, MeasurementUsage.UsedInMetric
interface CommonImperialScientificUnit<Type : MeasurementType> : SystemScientificUnit<MeasurementSystem.CommonImperial, Type>, MeasurementUsage.UsedInCommonImperial
interface USCustomaryScientificUnit<Type : MeasurementType> : SystemScientificUnit<MeasurementSystem.USCustomary, Type>, MeasurementUsage.UsedInUSCustomary
interface UKImperialScientificUnit<Type : MeasurementType> : SystemScientificUnit<MeasurementSystem.UKImperial, Type>, MeasurementUsage.UsedInUKImperial
interface MetricAndUKImperialScientificUnit<Type : MeasurementType> : SystemScientificUnit<MeasurementSystem.MetricAndUKImperial, Type>, MeasurementUsage.UsedOutsideUS

@Serializable
sealed class AbstractScientificUnit<Type : MeasurementType> : ScientificUnit<Type>

fun <Unit : MeasurementType> ScientificUnit<Unit>.convert(
    value: Double,
    to: ScientificUnit<Unit>
): Double = convert(value.toDecimal(), to).toDouble()

fun <Unit : MeasurementType> ScientificUnit<Unit>.convert(
    value: Decimal,
    to: ScientificUnit<Unit>
): Decimal = to.fromSIUnit(toSIUnit(value))
