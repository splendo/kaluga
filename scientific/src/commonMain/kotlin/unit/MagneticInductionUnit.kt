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
import com.splendo.kaluga.base.utils.div
import com.splendo.kaluga.base.utils.times
import com.splendo.kaluga.base.utils.toDecimal
import com.splendo.kaluga.scientific.PhysicalQuantity
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.PolymorphicModuleBuilder
import kotlinx.serialization.modules.SerializersModuleBuilder
import kotlinx.serialization.modules.polymorphic

/**
 * Set of all [MagneticInduction]
 */
val MagneticInductionUnits: Set<MagneticInduction> get() = setOf(
    Tesla,
    Nanotesla,
    Microtesla,
    Millitesla,
    Centitesla,
    Decitesla,
    Decatesla,
    Hectotesla,
    Kilotesla,
    Megatesla,
    Gigatesla,
    Gauss,
)

/**
 * An [DefinedScientificUnit] for [PhysicalQuantity.MagneticInduction]
 * SI unit is [Tesla]
 */
@Serializable
sealed class MagneticInduction :
    DefinedScientificUnit<PhysicalQuantity.MagneticInduction>(),
    MetricAndImperialScientificUnit<PhysicalQuantity.MagneticInduction>

@Serializable
data object Tesla : MagneticInduction(), MetricBaseUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.MagneticInduction> {
    override val symbol = "T"
    override val system = MeasurementSystem.MetricAndImperial
    override val quantity = PhysicalQuantity.MagneticInduction
    override fun fromSIUnit(value: Decimal): Decimal = value
    override fun toSIUnit(value: Decimal): Decimal = value
}

@Serializable
sealed class TeslaMultiple :
    MagneticInduction(),
    MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.MagneticInduction, Tesla>

@Serializable
data object Nanotesla : TeslaMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.MagneticInduction, Tesla> by Nano(Tesla)

@Serializable
data object Microtesla : TeslaMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.MagneticInduction, Tesla> by Micro(Tesla)

@Serializable
data object Millitesla : TeslaMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.MagneticInduction, Tesla> by Milli(Tesla)

@Serializable
data object Centitesla : TeslaMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.MagneticInduction, Tesla> by Centi(Tesla)

@Serializable
data object Decitesla : TeslaMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.MagneticInduction, Tesla> by Deci(Tesla)

@Serializable
data object Decatesla : TeslaMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.MagneticInduction, Tesla> by Deca(Tesla)

@Serializable
data object Hectotesla : TeslaMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.MagneticInduction, Tesla> by Hecto(Tesla)

@Serializable
data object Kilotesla : TeslaMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.MagneticInduction, Tesla> by Kilo(Tesla)

@Serializable
data object Megatesla : TeslaMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.MagneticInduction, Tesla> by Mega(Tesla)

@Serializable
data object Gigatesla : TeslaMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.MagneticInduction, Tesla> by Giga(Tesla)

@Serializable
data object Gauss : MagneticInduction() {
    private val GAUSS_IN_TESLA = 10000.toDecimal()
    override val symbol = "G"
    override val system = MeasurementSystem.MetricAndImperial
    override val quantity = PhysicalQuantity.MagneticInduction
    override fun fromSIUnit(value: Decimal): Decimal = value * GAUSS_IN_TESLA
    override fun toSIUnit(value: Decimal): Decimal = value / GAUSS_IN_TESLA
}

internal fun SerializersModuleBuilder.setupForMagneticInduction() {
    polymorphic(MagneticInduction::class) {
        registerMagneticInductionClasses()
    }
}

internal fun PolymorphicModuleBuilder<MagneticInduction>.registerMagneticInductionClasses() {
    subclass(Gauss::class, Gauss.serializer())
    subclass(Tesla::class, Tesla.serializer())
    subclass(Centitesla::class, Centitesla.serializer())
    subclass(Decatesla::class, Decatesla.serializer())
    subclass(Decitesla::class, Decitesla.serializer())
    subclass(Gigatesla::class, Gigatesla.serializer())
    subclass(Hectotesla::class, Hectotesla.serializer())
    subclass(Kilotesla::class, Kilotesla.serializer())
    subclass(Megatesla::class, Megatesla.serializer())
    subclass(Microtesla::class, Microtesla.serializer())
    subclass(Millitesla::class, Millitesla.serializer())
    subclass(Nanotesla::class, Nanotesla.serializer())
}
