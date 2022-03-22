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
sealed class PhysicalQuantity : com.splendo.kaluga.base.utils.Serializable {
    @Serializable
    object Dimensionless : PhysicalQuantity()
    @Serializable
    object AerobicCapacity : PhysicalQuantity()
    @Serializable
    object Acceleration : PhysicalQuantity()
    @Serializable
    object Action : PhysicalQuantity()
    @Serializable
    object AmountOfSubstance : PhysicalQuantity()
    @Serializable
    object Angle : PhysicalQuantity()
    @Serializable
    object AngularAcceleration : PhysicalQuantity()
    @Serializable
    object AngularVelocity : PhysicalQuantity()
    @Serializable
    object Area : PhysicalQuantity()
    @Serializable
    object AreaDensity : PhysicalQuantity()
    @Serializable
    object CatalysticActivity : PhysicalQuantity()
    @Serializable
    object Density : PhysicalQuantity()
    @Serializable
    object DynamicViscosity : PhysicalQuantity()
    @Serializable
    object ElectricCapacitance : PhysicalQuantity()
    @Serializable
    object ElectricCharge : PhysicalQuantity()
    @Serializable
    object ElectricConductance : PhysicalQuantity()
    @Serializable
    object ElectricCurrent : PhysicalQuantity()
    @Serializable
    object ElectricInductance : PhysicalQuantity()
    @Serializable
    object ElectricResistance : PhysicalQuantity()
    @Serializable
    object Energy : PhysicalQuantity()
    @Serializable
    object Force : PhysicalQuantity()
    @Serializable
    object Frequency : PhysicalQuantity()
    @Serializable
    object HeatCapacity : PhysicalQuantity()
    @Serializable
    object Illuminance : PhysicalQuantity()
    @Serializable
    object IonizingRadiationAbsorbedDose : PhysicalQuantity()
    @Serializable
    object IonizingRadiationEquivalentDose : PhysicalQuantity()
    @Serializable
    object LinearMassDensity : PhysicalQuantity()
    @Serializable
    object Jolt : PhysicalQuantity()
    @Serializable
    object Length : PhysicalQuantity()
    @Serializable
    object Luminance : PhysicalQuantity()
    @Serializable
    object LuminousEnergy : PhysicalQuantity()
    @Serializable
    object LuminousExposure : PhysicalQuantity()
    @Serializable
    object LuminousFlux : PhysicalQuantity()
    @Serializable
    object LuminousIntensity : PhysicalQuantity()
    @Serializable
    object MassFlowRate : PhysicalQuantity()
    @Serializable
    object MagneticFlux : PhysicalQuantity()
    @Serializable
    object MagneticInduction : PhysicalQuantity()
    @Serializable
    object Molality : PhysicalQuantity()
    @Serializable
    object Molarity : PhysicalQuantity()
    @Serializable
    object MolarEnergy : PhysicalQuantity()
    @Serializable
    object MolarMass : PhysicalQuantity()
    @Serializable
    object MolarVolume : PhysicalQuantity()
    @Serializable
    object Momentum : PhysicalQuantity()
    @Serializable
    object Power : PhysicalQuantity()
    @Serializable
    object Pressure : PhysicalQuantity()
    @Serializable
    object Radioactivity : PhysicalQuantity()
    @Serializable
    object SolidAngle : PhysicalQuantity()
    @Serializable
    object SpecificEnergy : PhysicalQuantity()
    @Serializable
    object SpecificHeatCapacity : PhysicalQuantity()
    @Serializable
    object SpecificVolume : PhysicalQuantity()
    @Serializable
    object Speed : PhysicalQuantity()
    @Serializable
    object SurfaceTension : PhysicalQuantity()
    @Serializable
    object Temperature : PhysicalQuantity()
    @Serializable
    object ThermalResistance : PhysicalQuantity()
    @Serializable
    object Time : PhysicalQuantity()
    @Serializable
    object Voltage : PhysicalQuantity()
    @Serializable
    object Volume : PhysicalQuantity()
    @Serializable
    object VolumetricFlow : PhysicalQuantity()
    @Serializable
    object VolumetricFlux : PhysicalQuantity()
    @Serializable
    object Weight : PhysicalQuantity()
    @Serializable
    object Yank : PhysicalQuantity()
}
