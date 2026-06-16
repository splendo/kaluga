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
 * Set of all [IonizingRadiationEquivalentDose]
 */
val IonizingRadiationEquivalentDoseUnits: Set<IonizingRadiationEquivalentDose> get() = setOf(
    Sievert,
    Nanosievert,
    Microsievert,
    Millisievert,
    Centisievert,
    Decisievert,
    Decasievert,
    Hectosievert,
    Kilosievert,
    Megasievert,
    Gigasievert,
    RoentgenEquivalentMan,
    NanoroentgenEquivalentMan,
    MicroroentgenEquivalentMan,
    MilliroentgenEquivalentMan,
    CentiroentgenEquivalentMan,
    DeciroentgenEquivalentMan,
    DecaroentgenEquivalentMan,
    HectoroentgenEquivalentMan,
    KiloroentgenEquivalentMan,
    MegaroentgenEquivalentMan,
    GigaroentgenEquivalentMan,
)

/**
 * An [DefinedScientificUnit] for [PhysicalQuantity.IonizingRadiationEquivalentDose]
 * SI unit is [Sievert]
 */
@Serializable
sealed class IonizingRadiationEquivalentDose :
    DefinedScientificUnit<PhysicalQuantity.IonizingRadiationEquivalentDose>(),
    MetricAndImperialScientificUnit<PhysicalQuantity.IonizingRadiationEquivalentDose>

@Serializable
data object Sievert : IonizingRadiationEquivalentDose(), MetricBaseUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.IonizingRadiationEquivalentDose> {
    override val symbol = "Sv"
    override val system = MeasurementSystem.MetricAndImperial
    override val quantity = PhysicalQuantity.IonizingRadiationEquivalentDose
    override fun fromSIUnit(value: Decimal): Decimal = value
    override fun toSIUnit(value: Decimal): Decimal = value
}

@Serializable
sealed class SievertMultiple :
    IonizingRadiationEquivalentDose(),
    MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.IonizingRadiationEquivalentDose, Sievert>

@Serializable
data object Nanosievert : SievertMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.IonizingRadiationEquivalentDose, Sievert> by Nano(Sievert)

@Serializable
data object Microsievert : SievertMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.IonizingRadiationEquivalentDose, Sievert> by Micro(Sievert)

@Serializable
data object Millisievert : SievertMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.IonizingRadiationEquivalentDose, Sievert> by Milli(Sievert)

@Serializable
data object Centisievert : SievertMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.IonizingRadiationEquivalentDose, Sievert> by Centi(Sievert)

@Serializable
data object Decisievert : SievertMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.IonizingRadiationEquivalentDose, Sievert> by Deci(Sievert)

@Serializable
data object Decasievert : SievertMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.IonizingRadiationEquivalentDose, Sievert> by Deca(Sievert)

@Serializable
data object Hectosievert : SievertMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.IonizingRadiationEquivalentDose, Sievert> by Hecto(Sievert)

@Serializable
data object Kilosievert : SievertMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.IonizingRadiationEquivalentDose, Sievert> by Kilo(Sievert)

@Serializable
data object Megasievert : SievertMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.IonizingRadiationEquivalentDose, Sievert> by Mega(Sievert)

@Serializable
data object Gigasievert : SievertMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.IonizingRadiationEquivalentDose, Sievert> by Giga(Sievert)

@Serializable
data object RoentgenEquivalentMan : IonizingRadiationEquivalentDose(), MetricBaseUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.IonizingRadiationEquivalentDose> {
    private val SIEVERT_IN_REM = "0.01".toDecimal()
    override val symbol = "rem"
    override val system = MeasurementSystem.MetricAndImperial
    override val quantity = PhysicalQuantity.IonizingRadiationEquivalentDose
    override fun fromSIUnit(value: Decimal): Decimal = value / SIEVERT_IN_REM
    override fun toSIUnit(value: Decimal): Decimal = value * SIEVERT_IN_REM
}

@Serializable
sealed class RoentgenEquivalentManMultiple :
    IonizingRadiationEquivalentDose(),
    MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.IonizingRadiationEquivalentDose, RoentgenEquivalentMan>

@Serializable
data object NanoroentgenEquivalentMan :
    RoentgenEquivalentManMultiple(),
    MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.IonizingRadiationEquivalentDose, RoentgenEquivalentMan> by Nano(RoentgenEquivalentMan)

@Serializable
data object MicroroentgenEquivalentMan :
    RoentgenEquivalentManMultiple(),
    MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.IonizingRadiationEquivalentDose, RoentgenEquivalentMan> by Micro(RoentgenEquivalentMan)

@Serializable
data object MilliroentgenEquivalentMan :
    RoentgenEquivalentManMultiple(),
    MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.IonizingRadiationEquivalentDose, RoentgenEquivalentMan> by Milli(RoentgenEquivalentMan)

@Serializable
data object CentiroentgenEquivalentMan :
    RoentgenEquivalentManMultiple(),
    MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.IonizingRadiationEquivalentDose, RoentgenEquivalentMan> by Centi(RoentgenEquivalentMan)

@Serializable
data object DeciroentgenEquivalentMan :
    RoentgenEquivalentManMultiple(),
    MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.IonizingRadiationEquivalentDose, RoentgenEquivalentMan> by Deci(RoentgenEquivalentMan)

@Serializable
data object DecaroentgenEquivalentMan :
    RoentgenEquivalentManMultiple(),
    MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.IonizingRadiationEquivalentDose, RoentgenEquivalentMan> by Deca(RoentgenEquivalentMan)

@Serializable
data object HectoroentgenEquivalentMan :
    RoentgenEquivalentManMultiple(),
    MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.IonizingRadiationEquivalentDose, RoentgenEquivalentMan> by Hecto(RoentgenEquivalentMan)

@Serializable
data object KiloroentgenEquivalentMan :
    RoentgenEquivalentManMultiple(),
    MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.IonizingRadiationEquivalentDose, RoentgenEquivalentMan> by Kilo(RoentgenEquivalentMan)

@Serializable
data object MegaroentgenEquivalentMan :
    RoentgenEquivalentManMultiple(),
    MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.IonizingRadiationEquivalentDose, RoentgenEquivalentMan> by Mega(RoentgenEquivalentMan)

@Serializable
data object GigaroentgenEquivalentMan :
    RoentgenEquivalentManMultiple(),
    MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.IonizingRadiationEquivalentDose, RoentgenEquivalentMan> by Giga(RoentgenEquivalentMan)

internal fun SerializersModuleBuilder.setupForIonizingRadiationEquivalentDose() {
    polymorphic(IonizingRadiationEquivalentDose::class) {
        registerIonizingRadiationEquivalentDoseClasses()
    }
}

internal fun PolymorphicModuleBuilder<IonizingRadiationEquivalentDose>.registerIonizingRadiationEquivalentDoseClasses() {
    subclass(RoentgenEquivalentMan::class, RoentgenEquivalentMan.serializer())
    subclass(CentiroentgenEquivalentMan::class, CentiroentgenEquivalentMan.serializer())
    subclass(DecaroentgenEquivalentMan::class, DecaroentgenEquivalentMan.serializer())
    subclass(DeciroentgenEquivalentMan::class, DeciroentgenEquivalentMan.serializer())
    subclass(GigaroentgenEquivalentMan::class, GigaroentgenEquivalentMan.serializer())
    subclass(HectoroentgenEquivalentMan::class, HectoroentgenEquivalentMan.serializer())
    subclass(KiloroentgenEquivalentMan::class, KiloroentgenEquivalentMan.serializer())
    subclass(MegaroentgenEquivalentMan::class, MegaroentgenEquivalentMan.serializer())
    subclass(MicroroentgenEquivalentMan::class, MicroroentgenEquivalentMan.serializer())
    subclass(MilliroentgenEquivalentMan::class, MilliroentgenEquivalentMan.serializer())
    subclass(NanoroentgenEquivalentMan::class, NanoroentgenEquivalentMan.serializer())
    subclass(Sievert::class, Sievert.serializer())
    subclass(Centisievert::class, Centisievert.serializer())
    subclass(Decasievert::class, Decasievert.serializer())
    subclass(Decisievert::class, Decisievert.serializer())
    subclass(Gigasievert::class, Gigasievert.serializer())
    subclass(Hectosievert::class, Hectosievert.serializer())
    subclass(Kilosievert::class, Kilosievert.serializer())
    subclass(Megasievert::class, Megasievert.serializer())
    subclass(Microsievert::class, Microsievert.serializer())
    subclass(Millisievert::class, Millisievert.serializer())
    subclass(Nanosievert::class, Nanosievert.serializer())
}
