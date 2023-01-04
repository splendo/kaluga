/*
 Copyright 2023 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.example.shared.model.scientific

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.example.shared.model.scientific.converters.QuantityConverter
import com.splendo.kaluga.example.shared.model.scientific.converters.converters
import com.splendo.kaluga.scientific.DefaultScientificValue
import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.convert
import com.splendo.kaluga.scientific.unit.AccelerationUnits
import com.splendo.kaluga.scientific.unit.ActionUnits
import com.splendo.kaluga.scientific.unit.AmountOfSubstanceUnits
import com.splendo.kaluga.scientific.unit.AngleUnits
import com.splendo.kaluga.scientific.unit.AngularAccelerationUnits
import com.splendo.kaluga.scientific.unit.AngularVelocityUnits
import com.splendo.kaluga.scientific.unit.ScientificUnit

data class QuantityDetails<Quantity: PhysicalQuantity>(
    val quantity: Quantity,
    val units: Set<ScientificUnit<Quantity>>,
    val converters: List<QuantityConverter<Quantity, *, *>>
) {
    fun convert(value: Decimal, unit: ScientificUnit<*>, to: ScientificUnit<*>): ScientificValue<Quantity, *>? = if (unit.quantity == quantity && to.quantity == quantity) {
        DefaultScientificValue(value, unit as ScientificUnit<Quantity>).convert(to as ScientificUnit<Quantity>)
    } else {
        null
    }
}

val allPhysicalQuantities = setOf(
    PhysicalQuantity.Acceleration,
    PhysicalQuantity.Action,
    PhysicalQuantity.AmountOfSubstance,
    PhysicalQuantity.Angle,
    PhysicalQuantity.AngularAcceleration,
    PhysicalQuantity.AngularVelocity,
    PhysicalQuantity.Area,
    PhysicalQuantity.AreaDensity,
    PhysicalQuantity.CatalysticActivity,
    PhysicalQuantity.Density,
    PhysicalQuantity.Dimensionless,
    PhysicalQuantity.DynamicViscosity,
    PhysicalQuantity.ElectricCapacitance,
    PhysicalQuantity.ElectricCharge,
    PhysicalQuantity.ElectricConductance,
    PhysicalQuantity.ElectricCurrent,
    PhysicalQuantity.ElectricInductance,
    PhysicalQuantity.ElectricInductance,
    PhysicalQuantity.ElectricResistance,
    PhysicalQuantity.Energy,
    PhysicalQuantity.Force,
    PhysicalQuantity.Frequency,
    PhysicalQuantity.HeatCapacity,
    PhysicalQuantity.Illuminance,
    PhysicalQuantity.IonizingRadiationAbsorbedDose,
    PhysicalQuantity.IonizingRadiationEquivalentDose,
    PhysicalQuantity.LinearMassDensity,
    PhysicalQuantity.Jolt,
    PhysicalQuantity.Length,
    PhysicalQuantity.Luminance,
    PhysicalQuantity.LuminousEnergy,
    PhysicalQuantity.LuminousExposure,
    PhysicalQuantity.LuminousFlux,
    PhysicalQuantity.LuminousIntensity,
    PhysicalQuantity.MassFlowRate,
    PhysicalQuantity.MagneticFlux,
    PhysicalQuantity.MagneticInduction,
    PhysicalQuantity.Molality,
    PhysicalQuantity.Molarity,
    PhysicalQuantity.MolarEnergy,
    PhysicalQuantity.MolarMass,
    PhysicalQuantity.MolarVolume,
    PhysicalQuantity.Momentum,
    PhysicalQuantity.Power,
    PhysicalQuantity.Pressure,
    PhysicalQuantity.Radioactivity,
    PhysicalQuantity.SolidAngle,
    PhysicalQuantity.SpecificEnergy,
    PhysicalQuantity.SpecificHeatCapacity,
    PhysicalQuantity.SpecificVolume,
    PhysicalQuantity.Speed,
    PhysicalQuantity.SurfaceTension,
    PhysicalQuantity.Temperature,
    PhysicalQuantity.ThermalResistance,
    PhysicalQuantity.Time,
    PhysicalQuantity.Voltage,
    PhysicalQuantity.Volume,
    PhysicalQuantity.VolumetricFlow,
    PhysicalQuantity.VolumetricFlux,
    PhysicalQuantity.Weight,
    PhysicalQuantity.Yank
)

val PhysicalQuantity.details: QuantityDetails<*>? get() {
    return when (this) {
        is PhysicalQuantity.Dimensionless -> null
        is PhysicalQuantity.Acceleration -> QuantityDetails(this, AccelerationUnits, converters)
        is PhysicalQuantity.Action -> QuantityDetails(this, ActionUnits, converters)
        is PhysicalQuantity.AmountOfSubstance -> QuantityDetails(this, AmountOfSubstanceUnits, converters)
        is PhysicalQuantity.Angle -> QuantityDetails(this, AngleUnits, converters)
        is PhysicalQuantity.AngularAcceleration -> QuantityDetails(this, AngularAccelerationUnits, converters)
        is PhysicalQuantity.AngularVelocity -> QuantityDetails(this, AngularVelocityUnits, converters)
        else -> null
    }
}
