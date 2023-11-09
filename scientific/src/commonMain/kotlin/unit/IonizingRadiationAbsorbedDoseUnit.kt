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

/**
 * Set of all [IonizingRadiationAbsorbedDose]
 */
val IonizingRadiationAbsorbedDoseUnits: Set<IonizingRadiationAbsorbedDose> get() = setOf(
    Gray,
    Nanogray,
    Microgray,
    Milligray,
    Centigray,
    Decigray,
    Decagray,
    Hectogray,
    Kilogray,
    Megagray,
    Gigagray,
    Rad,
    Nanorad,
    Microrad,
    Millirad,
    Centirad,
    Decirad,
    Decarad,
    Hectorad,
    Kilorad,
    Megarad,
    Gigarad,
)

/**
 * An [AbstractScientificUnit] for [PhysicalQuantity.IonizingRadiationAbsorbedDose]
 * SI unit is [Gray]
 */
@Serializable
sealed class IonizingRadiationAbsorbedDose :
    AbstractScientificUnit<PhysicalQuantity.IonizingRadiationAbsorbedDose>(), MetricAndImperialScientificUnit<PhysicalQuantity.IonizingRadiationAbsorbedDose>

@Serializable
data object Gray : IonizingRadiationAbsorbedDose(), MetricBaseUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.IonizingRadiationAbsorbedDose> {
    override val symbol = "Gy"
    override val system = MeasurementSystem.MetricAndImperial
    override val quantity = PhysicalQuantity.IonizingRadiationAbsorbedDose
    override fun fromSIUnit(value: Decimal): Decimal = value
    override fun toSIUnit(value: Decimal): Decimal = value
}

@Serializable
sealed class GrayMultiple : IonizingRadiationAbsorbedDose(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.IonizingRadiationAbsorbedDose, Gray>

@Serializable
data object Nanogray : GrayMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.IonizingRadiationAbsorbedDose, Gray> by Nano(Gray)

@Serializable
data object Microgray : GrayMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.IonizingRadiationAbsorbedDose, Gray> by Micro(Gray)

@Serializable
data object Milligray : GrayMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.IonizingRadiationAbsorbedDose, Gray> by Milli(Gray)

@Serializable
data object Centigray : GrayMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.IonizingRadiationAbsorbedDose, Gray> by Centi(Gray)

@Serializable
data object Decigray : GrayMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.IonizingRadiationAbsorbedDose, Gray> by Deci(Gray)

@Serializable
data object Decagray : GrayMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.IonizingRadiationAbsorbedDose, Gray> by Deca(Gray)

@Serializable
data object Hectogray : GrayMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.IonizingRadiationAbsorbedDose, Gray> by Hecto(Gray)

@Serializable
data object Kilogray : GrayMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.IonizingRadiationAbsorbedDose, Gray> by Kilo(Gray)

@Serializable
data object Megagray : GrayMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.IonizingRadiationAbsorbedDose, Gray> by Mega(Gray)

@Serializable
data object Gigagray : GrayMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.IonizingRadiationAbsorbedDose, Gray> by Giga(Gray)

@Serializable
data object Rad : IonizingRadiationAbsorbedDose(), MetricBaseUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.IonizingRadiationAbsorbedDose> {
    const val GRAY_IN_RAD = 0.01
    override val symbol = "rad"
    override val system = MeasurementSystem.MetricAndImperial
    override val quantity = PhysicalQuantity.IonizingRadiationAbsorbedDose
    override fun fromSIUnit(value: Decimal): Decimal = value / GRAY_IN_RAD.toDecimal()
    override fun toSIUnit(value: Decimal): Decimal = value * GRAY_IN_RAD.toDecimal()
}

@Serializable
sealed class RadMultiple : IonizingRadiationAbsorbedDose(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.IonizingRadiationAbsorbedDose, Rad>

@Serializable
data object Nanorad : RadMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.IonizingRadiationAbsorbedDose, Rad> by Nano(Rad)

@Serializable
data object Microrad : RadMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.IonizingRadiationAbsorbedDose, Rad> by Micro(Rad)

@Serializable
data object Millirad : RadMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.IonizingRadiationAbsorbedDose, Rad> by Milli(Rad)

@Serializable
data object Centirad : RadMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.IonizingRadiationAbsorbedDose, Rad> by Centi(Rad)

@Serializable
data object Decirad : RadMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.IonizingRadiationAbsorbedDose, Rad> by Deci(Rad)

@Serializable
data object Decarad : RadMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.IonizingRadiationAbsorbedDose, Rad> by Deca(Rad)

@Serializable
data object Hectorad : RadMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.IonizingRadiationAbsorbedDose, Rad> by Hecto(Rad)

@Serializable
data object Kilorad : RadMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.IonizingRadiationAbsorbedDose, Rad> by Kilo(Rad)

@Serializable
data object Megarad : RadMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.IonizingRadiationAbsorbedDose, Rad> by Mega(Rad)

@Serializable
data object Gigarad : RadMultiple(), MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.IonizingRadiationAbsorbedDose, Rad> by Giga(Rad)
