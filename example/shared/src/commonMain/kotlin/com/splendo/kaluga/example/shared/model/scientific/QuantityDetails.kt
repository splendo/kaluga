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
import com.splendo.kaluga.scientific.unit.*

data class QuantityDetails<Quantity : PhysicalQuantity>(
    val quantity: Quantity,
    val units: Set<AbstractScientificUnit<Quantity>>,
    val converters: List<QuantityConverter<Quantity, *>>,
) {
    @Suppress("UNCHECKED_CAST")
    fun convert(value: Decimal, unit: AbstractScientificUnit<*>, to: AbstractScientificUnit<*>): ScientificValue<Quantity, *>? =
        if (unit.quantity == quantity && to.quantity == quantity) {
            DefaultScientificValue(value, unit as AbstractScientificUnit<Quantity>).convert(to as AbstractScientificUnit<Quantity>)
        } else {
            null
        }
}

val allPhysicalQuantities: Set<PhysicalQuantity> = setOf(
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
    PhysicalQuantity.Yank,
)

internal val PhysicalQuantity.quantityDetails: QuantityDetails<*>? get() = when (this) {
    is PhysicalQuantity.Dimensionless -> null
    is PhysicalQuantity.Acceleration -> QuantityDetails(this, AccelerationUnits, converters)
    is PhysicalQuantity.Action -> QuantityDetails(this, ActionUnits, converters)
    is PhysicalQuantity.AmountOfSubstance -> QuantityDetails(this, AmountOfSubstanceUnits, converters)
    is PhysicalQuantity.Angle -> QuantityDetails(this, AngleUnits, converters)
    is PhysicalQuantity.AngularAcceleration -> QuantityDetails(this, AngularAccelerationUnits, converters)
    is PhysicalQuantity.AngularVelocity -> QuantityDetails(this, AngularVelocityUnits, converters)
    is PhysicalQuantity.Area -> QuantityDetails(this, AreaUnits, converters)
    is PhysicalQuantity.AreaDensity -> QuantityDetails(this, AreaDensityUnits, converters)
    is PhysicalQuantity.CatalysticActivity -> QuantityDetails(this, CatalysticActivityUnits, converters)
    is PhysicalQuantity.Density -> QuantityDetails(this, DensityUnits, converters)
    is PhysicalQuantity.DynamicViscosity -> QuantityDetails(this, DynamicViscosityUnits, converters)
    is PhysicalQuantity.ElectricCapacitance -> QuantityDetails(this, ElectricCapacitanceUnits, converters)
    is PhysicalQuantity.ElectricCharge -> QuantityDetails(this, ElectricChargeUnits, converters)
    is PhysicalQuantity.ElectricConductance -> QuantityDetails(this, ElectricConductanceUnits, converters)
    is PhysicalQuantity.ElectricCurrent -> QuantityDetails(this, ElectricCurrentUnits, converters)
    is PhysicalQuantity.ElectricInductance -> QuantityDetails(this, ElectricInductanceUnits, converters)
    is PhysicalQuantity.ElectricResistance -> QuantityDetails(this, ElectricResistanceUnits, converters)
    is PhysicalQuantity.Energy -> QuantityDetails(this, EnergyUnits, converters)
    is PhysicalQuantity.Force -> QuantityDetails(this, ForceUnits, converters)
    is PhysicalQuantity.Frequency -> QuantityDetails(this, FrequencyUnits, converters)
    is PhysicalQuantity.HeatCapacity -> QuantityDetails(this, HeatCapacityUnits, converters)
    is PhysicalQuantity.Illuminance -> QuantityDetails(this, IlluminanceUnits, converters)
    is PhysicalQuantity.IonizingRadiationAbsorbedDose -> QuantityDetails(this, IonizingRadiationAbsorbedDoseUnits, converters)
    is PhysicalQuantity.IonizingRadiationEquivalentDose -> QuantityDetails(this, IonizingRadiationEquivalentDoseUnits, converters)
    is PhysicalQuantity.Jolt -> QuantityDetails(this, JoltUnits, converters)
    is PhysicalQuantity.KinematicViscosity -> QuantityDetails(this, KinematicViscosityUnits, converters)
    is PhysicalQuantity.Length -> QuantityDetails(this, LengthUnits, converters)
    is PhysicalQuantity.LinearMassDensity -> QuantityDetails(this, LinearMassDensityUnits, converters)
    is PhysicalQuantity.Luminance -> QuantityDetails(this, LuminanceUnits, converters)
    is PhysicalQuantity.LuminousEnergy -> QuantityDetails(this, LuminousEnergyUnits, converters)
    is PhysicalQuantity.LuminousExposure -> QuantityDetails(this, LuminousExposureUnits, converters)
    is PhysicalQuantity.LuminousFlux -> QuantityDetails(this, LuminousFluxUnits, converters)
    is PhysicalQuantity.LuminousIntensity -> QuantityDetails(this, LuminousIntensityUnits, converters)
    is PhysicalQuantity.MagneticFlux -> QuantityDetails(this, MagneticFluxUnits, converters)
    is PhysicalQuantity.MagneticInduction -> QuantityDetails(this, MagneticInductionUnits, converters)
    is PhysicalQuantity.MassFlowRate -> QuantityDetails(this, MassFlowRateUnits, converters)
    is PhysicalQuantity.Molality -> QuantityDetails(this, MolalityUnits, converters)
    is PhysicalQuantity.MolarEnergy -> QuantityDetails(this, MolarEnergyUnits, converters)
    is PhysicalQuantity.Molarity -> QuantityDetails(this, MolarityUnits, converters)
    is PhysicalQuantity.MolarMass -> QuantityDetails(this, MolarMassUnits, converters)
    is PhysicalQuantity.MolarVolume -> QuantityDetails(this, MolarVolumeUnits, converters)
    is PhysicalQuantity.Momentum -> QuantityDetails(this, MomentumUnits, converters)
    is PhysicalQuantity.Power -> QuantityDetails(this, PowerUnits, converters)
    is PhysicalQuantity.Pressure -> QuantityDetails(this, PressureUnits, converters)
    is PhysicalQuantity.Radioactivity -> QuantityDetails(this, RadioactivityUnits, converters)
    is PhysicalQuantity.SolidAngle -> QuantityDetails(this, SolidAngleUnits, converters)
    is PhysicalQuantity.SpecificEnergy -> QuantityDetails(this, SpecificEnergyUnits, converters)
    is PhysicalQuantity.SpecificHeatCapacity -> QuantityDetails(this, SpecificHeatCapacityUnits, converters)
    is PhysicalQuantity.SpecificVolume -> QuantityDetails(this, SpecificVolumeUnits, converters)
    is PhysicalQuantity.Speed -> QuantityDetails(this, SpeedUnits, converters)
    is PhysicalQuantity.SurfaceTension -> QuantityDetails(this, SurfaceTensionUnits, converters)
    is PhysicalQuantity.Temperature -> QuantityDetails(this, TemperatureUnits, converters)
    is PhysicalQuantity.ThermalResistance -> QuantityDetails(this, ThermalResistanceUnits, converters)
    is PhysicalQuantity.Time -> QuantityDetails(this, TimeUnits, converters)
    is PhysicalQuantity.Voltage -> QuantityDetails(this, VoltageUnits, converters)
    is PhysicalQuantity.Volume -> QuantityDetails(this, VolumeUnits, converters)
    is PhysicalQuantity.VolumetricFlow -> QuantityDetails(this, VolumetricFlowUnits, converters)
    is PhysicalQuantity.VolumetricFlux -> QuantityDetails(this, VolumetricFluxUnits, converters)
    is PhysicalQuantity.Weight -> QuantityDetails(this, WeightUnits, converters)
    is PhysicalQuantity.Yank -> QuantityDetails(this, YankUnits, converters)
}

val PhysicalQuantity.name: String get() = this::class.simpleName.orEmpty().split("(?=\\p{Upper})".toRegex()).joinToString(" ")
