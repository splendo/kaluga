/*
 Copyright 2022 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.scientific.unit

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.scientific.PhysicalQuantity
import kotlinx.serialization.Serializable

/**
 * Set of all [MagneticFlux]
 */
val MagneticFluxUnits: Set<MagneticFlux> get() = setOf(
    Weber,
    Nanoweber,
    Microweber,
    Milliweber,
    Centiweber,
    Deciweber,
    Decaweber,
    Hectoweber,
    Kiloweber,
    Megaweber,
    Gigaweber,
    Maxwell,
)

/**
 * An [AbstractScientificUnit] for [PhysicalQuantity.MagneticFlux]
 * SI unit is [Weber]
 */
@Serializable
sealed class MagneticFlux : AbstractScientificUnit<PhysicalQuantity.MagneticFlux>(), MetricAndImperialScientificUnit<PhysicalQuantity.MagneticFlux>

@Serializable
data object Weber : MagneticFlux(), MetricBaseUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.MagneticFlux> {
    override val symbol = "Wb"
    override val system = MeasurementSystem.MetricAndImperial
    override val quantity = PhysicalQuantity.MagneticFlux
    override fun fromSIUnit(value: Decimal): Decimal = value
    override fun toSIUnit(value: Decimal): Decimal = value
}

@Serializable
sealed class WeberMultiple : MagneticFlux(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.MagneticFlux, Weber>

@Serializable
data object Nanoweber : WeberMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.MagneticFlux, Weber> by Nano(Weber)

@Serializable
data object Microweber : WeberMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.MagneticFlux, Weber> by Micro(Weber)

@Serializable
data object Milliweber : WeberMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.MagneticFlux, Weber> by Milli(Weber)

@Serializable
data object Centiweber : WeberMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.MagneticFlux, Weber> by Centi(Weber)

@Serializable
data object Deciweber : WeberMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.MagneticFlux, Weber> by Deci(Weber)

@Serializable
data object Decaweber : WeberMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.MagneticFlux, Weber> by Deca(Weber)

@Serializable
data object Hectoweber : WeberMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.MagneticFlux, Weber> by Hecto(Weber)

@Serializable
data object Kiloweber : WeberMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.MagneticFlux, Weber> by Kilo(Weber)

@Serializable
data object Megaweber : WeberMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.MagneticFlux, Weber> by Mega(Weber)

@Serializable
data object Gigaweber : WeberMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.MagneticFlux, Weber> by Giga(Weber)

@Serializable
data object Maxwell : MagneticFlux() {
    override val symbol = "Mx"
    override val system = MeasurementSystem.MetricAndImperial
    override val quantity = PhysicalQuantity.MagneticFlux
    override fun fromSIUnit(value: Decimal): Decimal = Abvolt.fromSIUnit(value)
    override fun toSIUnit(value: Decimal): Decimal = Abvolt.toSIUnit(value)
}
