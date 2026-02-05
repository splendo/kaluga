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
import kotlinx.serialization.modules.PolymorphicModuleBuilder
import kotlinx.serialization.modules.SerializersModuleBuilder
import kotlinx.serialization.modules.polymorphic

/**
 * Set of all [CatalysticActivity]
 */
val CatalysticActivityUnits: Set<CatalysticActivity> get() = setOf(
    Katal,
    Nanokatal,
    Microkatal,
    Millikatal,
    Centikatal,
    Decikatal,
    Decakatal,
    Hectokatal,
    Kilokatal,
    Megakatal,
    Gigakatal,
)

/**
 * An [DefinedScientificUnit] for [PhysicalQuantity.CatalysticActivity]
 * SI unit is [Katal]
 */
@Serializable
sealed class CatalysticActivity :
    DefinedScientificUnit<PhysicalQuantity.CatalysticActivity>(),
    MetricAndImperialScientificUnit<PhysicalQuantity.CatalysticActivity>

@Serializable
data object Katal : CatalysticActivity(), MetricBaseUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.CatalysticActivity> {
    override val symbol = "kat"
    override val system = MeasurementSystem.MetricAndImperial
    override val quantity = PhysicalQuantity.CatalysticActivity
    override fun fromSIUnit(value: Decimal): Decimal = value
    override fun toSIUnit(value: Decimal): Decimal = value
}

@Serializable
sealed class KatalMultiple :
    CatalysticActivity(),
    MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.CatalysticActivity, Katal>

@Serializable
data object Nanokatal : KatalMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.CatalysticActivity, Katal> by Nano(Katal)

@Serializable
data object Microkatal : KatalMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.CatalysticActivity, Katal> by Micro(Katal)

@Serializable
data object Millikatal : KatalMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.CatalysticActivity, Katal> by Milli(Katal)

@Serializable
data object Centikatal : KatalMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.CatalysticActivity, Katal> by Centi(Katal)

@Serializable
data object Decikatal : KatalMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.CatalysticActivity, Katal> by Deci(Katal)

@Serializable
data object Decakatal : KatalMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.CatalysticActivity, Katal> by Deca(Katal)

@Serializable
data object Hectokatal : KatalMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.CatalysticActivity, Katal> by Hecto(Katal)

@Serializable
data object Kilokatal : KatalMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.CatalysticActivity, Katal> by Kilo(Katal)

@Serializable
data object Megakatal : KatalMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.CatalysticActivity, Katal> by Mega(Katal)

@Serializable
data object Gigakatal : KatalMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.CatalysticActivity, Katal> by Giga(Katal)

internal fun SerializersModuleBuilder.setupForCatalysticActivity() {
    polymorphic(CatalysticActivity::class) {
        registerCatalysticActivityClasses()
    }
}

internal fun PolymorphicModuleBuilder<CatalysticActivity>.registerCatalysticActivityClasses() {
    subclass(Katal::class, Katal.serializer())
    subclass(Centikatal::class, Centikatal.serializer())
    subclass(Decakatal::class, Decakatal.serializer())
    subclass(Decikatal::class, Decikatal.serializer())
    subclass(Gigakatal::class, Gigakatal.serializer())
    subclass(Hectokatal::class, Hectokatal.serializer())
    subclass(Kilokatal::class, Kilokatal.serializer())
    subclass(Megakatal::class, Megakatal.serializer())
    subclass(Microkatal::class, Microkatal.serializer())
    subclass(Millikatal::class, Millikatal.serializer())
    subclass(Nanokatal::class, Nanokatal.serializer())
}
