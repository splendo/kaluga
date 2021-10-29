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

import kotlinx.serialization.Serializable

@Serializable
sealed class MeasurementType {
    @Serializable
    object Acceleration : MeasurementType()
    @Serializable
    object Action : MeasurementType()
    @Serializable
    object AmountOfSubstance : MeasurementType()
    @Serializable
    object Angle : MeasurementType()
    @Serializable
    object AngularAcceleration : MeasurementType()
    @Serializable
    object AngularVelocity : MeasurementType()
    @Serializable
    object Area : MeasurementType()
    @Serializable
    object AreaDensity : MeasurementType()
    @Serializable
    object CatalysticActivity : MeasurementType()
    @Serializable
    object Density : MeasurementType()
    @Serializable
    object DynamicViscosity : MeasurementType()
    @Serializable
    object ElectricCapacitance : MeasurementType()
    @Serializable
    object ElectricCharge : MeasurementType()
    @Serializable
    object ElectricConductance : MeasurementType()
    @Serializable
    object ElectricCurrent : MeasurementType()
    @Serializable
    object ElectricInductance : MeasurementType()
    @Serializable
    object ElectricResistance : MeasurementType()
    @Serializable
    object Energy : MeasurementType()
    @Serializable
    object Force : MeasurementType()
    @Serializable
    object Frequency : MeasurementType()
    @Serializable
    object HeatCapacity : MeasurementType()
    @Serializable
    object Illuminance : MeasurementType()
    @Serializable
    object IonizingRadiationAbsorbedDose : MeasurementType()
    @Serializable
    object IonizingRadiationEquivalentDose : MeasurementType()
    @Serializable
    object Irradiance : MeasurementType()
    @Serializable
    object LinearMassDensity : MeasurementType()
    @Serializable
    object Jolt : MeasurementType()
    @Serializable
    object Length : MeasurementType()
    @Serializable
    object Luminance : MeasurementType()
    @Serializable
    object LuminousEnergy : MeasurementType()
    @Serializable
    object LuminousExposure : MeasurementType()
    @Serializable
    object LuminousFlux : MeasurementType()
    @Serializable
    object LuminousIntensity : MeasurementType()
    @Serializable
    object MassFlowRate : MeasurementType()
    @Serializable
    object MagneticFlux : MeasurementType()
    @Serializable
    object MagneticInduction : MeasurementType()
    @Serializable
    object Molality : MeasurementType()
    @Serializable
    object Molarity : MeasurementType()
    @Serializable
    object MolarEnergy : MeasurementType()
    @Serializable
    object MolarMass : MeasurementType()
    @Serializable
    object MolarVolume : MeasurementType()
    @Serializable
    object Momentum : MeasurementType()
    @Serializable
    object Power : MeasurementType()
    @Serializable
    object Pressure : MeasurementType()
    @Serializable
    object Radioactivity : MeasurementType()
    @Serializable
    object SolidAngle : MeasurementType()
    @Serializable
    object SpecificEnergy : MeasurementType()
    @Serializable
    object SpecificHeatCapacity : MeasurementType()
    @Serializable
    object SpecificVolume : MeasurementType()
    @Serializable
    object Speed : MeasurementType()
    @Serializable
    object SurfaceTension : MeasurementType()
    @Serializable
    object Temperature : MeasurementType()
    @Serializable
    object ThermalResistance : MeasurementType()
    @Serializable
    object Time : MeasurementType()
    @Serializable
    object Voltage : MeasurementType()
    @Serializable
    object Volume : MeasurementType()
    @Serializable
    object VolumetricFlow : MeasurementType()
    @Serializable
    object VolumetricFlux : MeasurementType()
    @Serializable
    object Weight : MeasurementType()
    @Serializable
    object Yank : MeasurementType()
}
