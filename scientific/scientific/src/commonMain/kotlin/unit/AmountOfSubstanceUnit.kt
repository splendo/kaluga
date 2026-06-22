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

import com.splendo.kaluga.base.decimal.Decimal
import com.splendo.kaluga.base.decimal.toDecimal
import com.splendo.kaluga.scientific.PhysicalQuantity
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.PolymorphicModuleBuilder
import kotlinx.serialization.modules.SerializersModuleBuilder
import kotlinx.serialization.modules.polymorphic

/**
 * Set of all [AmountOfSubstance]
 */
val AmountOfSubstanceUnits: Set<AmountOfSubstance> get() = setOf(
    Mole,
    Nanomole,
    Micromole,
    Millimole,
    Centimole,
    Decimole,
    Decamole,
    Hectomole,
    Kilomole,
    Megamole,
    Gigamole,
)

/**
 * An [DefinedScientificUnit] for [PhysicalQuantity.AmountOfSubstance]
 * SI unit is [Mole]
 */
@Serializable
sealed class AmountOfSubstance :
    DefinedScientificUnit<PhysicalQuantity.AmountOfSubstance>(),
    MetricAndImperialScientificUnit<PhysicalQuantity.AmountOfSubstance>

@Serializable
data object Mole : AmountOfSubstance(), MetricBaseUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.AmountOfSubstance> {
    override val symbol = "mol"
    override val system = MeasurementSystem.MetricAndImperial
    override val quantity = PhysicalQuantity.AmountOfSubstance
    override fun fromSIUnit(value: Decimal): Decimal = value
    override fun toSIUnit(value: Decimal): Decimal = value
}

@Serializable
sealed class MoleMultiple :
    AmountOfSubstance(),
    MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.AmountOfSubstance, Mole>

@Serializable
data object Nanomole : MoleMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.AmountOfSubstance, Mole> by Nano(Mole)

@Serializable
data object Micromole : MoleMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.AmountOfSubstance, Mole> by Micro(Mole)

@Serializable
data object Millimole : MoleMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.AmountOfSubstance, Mole> by Milli(Mole)

@Serializable
data object Centimole : MoleMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.AmountOfSubstance, Mole> by Centi(Mole)

@Serializable
data object Decimole : MoleMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.AmountOfSubstance, Mole> by Deci(Mole)

@Serializable
data object Decamole : MoleMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.AmountOfSubstance, Mole> by Deca(Mole)

@Serializable
data object Hectomole : MoleMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.AmountOfSubstance, Mole> by Hecto(Mole)

@Serializable
data object Kilomole : MoleMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.AmountOfSubstance, Mole> by Kilo(Mole)

@Serializable
data object Megamole : MoleMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.AmountOfSubstance, Mole> by Mega(Mole)

@Serializable
data object Gigamole : MoleMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.AmountOfSubstance, Mole> by Giga(Mole)

/**
 * The proportionality factor that relates the number of constituent particles (usually molecules, atoms or ions) in a sample with the amount of substance in that sample
 */
val AvogadroConstant = "6.02214076e23".toDecimal()

internal fun SerializersModuleBuilder.setupForAmountOfSubstance() {
    polymorphic(AmountOfSubstance::class) {
        registerAmountOfSubstanceClasses()
    }
}

internal fun PolymorphicModuleBuilder<AmountOfSubstance>.registerAmountOfSubstanceClasses() {
    subclass(Mole::class, Mole.serializer())
    subclass(Centimole::class, Centimole.serializer())
    subclass(Decamole::class, Decamole.serializer())
    subclass(Decimole::class, Decimole.serializer())
    subclass(Gigamole::class, Gigamole.serializer())
    subclass(Hectomole::class, Hectomole.serializer())
    subclass(Kilomole::class, Kilomole.serializer())
    subclass(Megamole::class, Megamole.serializer())
    subclass(Micromole::class, Micromole.serializer())
    subclass(Millimole::class, Millimole.serializer())
    subclass(Nanomole::class, Nanomole.serializer())
}
