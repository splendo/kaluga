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

package com.splendo.kaluga.example.feature.scientific.model

import com.splendo.kaluga.scientific.unit.Acceleration
import com.splendo.kaluga.scientific.unit.CombinedImperialAcceleration
import com.splendo.kaluga.scientific.unit.CombinedMetricAcceleration
import com.splendo.kaluga.scientific.unit.ImperialMetricAndImperialAccelerationWrapper
import com.splendo.kaluga.scientific.unit.MetricMetricAndImperialAccelerationWrapper
import com.splendo.kaluga.scientific.unit.Action
import com.splendo.kaluga.scientific.unit.AngularAcceleration
import com.splendo.kaluga.scientific.unit.AngularVelocity
import com.splendo.kaluga.scientific.unit.AreaDensity
import com.splendo.kaluga.scientific.unit.Density
import com.splendo.kaluga.scientific.unit.DynamicViscosity
import com.splendo.kaluga.scientific.unit.HeatCapacity
import com.splendo.kaluga.scientific.unit.ImperialMetricAndImperialEnergyWrapper
import com.splendo.kaluga.scientific.unit.ImperialMetricAndImperialPowerWrapper
import com.splendo.kaluga.scientific.unit.Jolt
import com.splendo.kaluga.scientific.unit.KinematicViscosity
import com.splendo.kaluga.scientific.unit.LinearMassDensity
import com.splendo.kaluga.scientific.unit.LuminousEnergy
import com.splendo.kaluga.scientific.unit.LuminousExposure
import com.splendo.kaluga.scientific.unit.MassFlowRate
import com.splendo.kaluga.scientific.unit.MetricMetricAndImperialEnergyWrapper
import com.splendo.kaluga.scientific.unit.MetricMetricAndImperialPowerWrapper
import com.splendo.kaluga.scientific.unit.Molality
import com.splendo.kaluga.scientific.unit.MolarEnergy
import com.splendo.kaluga.scientific.unit.MolarMass
import com.splendo.kaluga.scientific.unit.MolarVolume
import com.splendo.kaluga.scientific.unit.Molarity
import com.splendo.kaluga.scientific.unit.Momentum
import com.splendo.kaluga.scientific.unit.ScientificUnit
import com.splendo.kaluga.scientific.unit.SpecificEnergy
import com.splendo.kaluga.scientific.unit.SpecificHeatCapacity
import com.splendo.kaluga.scientific.unit.SpecificVolume
import com.splendo.kaluga.scientific.unit.Speed
import com.splendo.kaluga.scientific.unit.SurfaceTension
import com.splendo.kaluga.scientific.unit.ThermalResistance
import com.splendo.kaluga.scientific.unit.UKImperialImperialForceWrapper
import com.splendo.kaluga.scientific.unit.UKImperialImperialVolumeWrapper
import com.splendo.kaluga.scientific.unit.UKImperialImperialWeightWrapper
import com.splendo.kaluga.scientific.unit.UKImperialPressureWrapper
import com.splendo.kaluga.scientific.unit.USCustomaryImperialForceWrapper
import com.splendo.kaluga.scientific.unit.USCustomaryImperialPressureWrapper
import com.splendo.kaluga.scientific.unit.USCustomaryImperialVolumeWrapper
import com.splendo.kaluga.scientific.unit.USCustomaryImperialWeightWrapper
import com.splendo.kaluga.scientific.unit.VolumetricFlow
import com.splendo.kaluga.scientific.unit.VolumetricFlux
import com.splendo.kaluga.scientific.unit.Yank

val ScientificUnit<*>.name: String get() = when (this) {
    // Acceleration has *both* combined units (a speed/time pair like `Kilometer per Hour per
    // Second`) and named units (`GUnit`, `Gal` and all their SI-prefixed multiples) that share
    // the same `speed = Meter per Second` / `per = Second` overrides. Only the combined variants
    // can be rendered via the compound formula; the named ones fall through to their class name.
    is CombinedMetricAcceleration -> "${speed.name} per ${per.name}"

    is CombinedImperialAcceleration -> "${speed.name} per ${per.name}"

    is MetricMetricAndImperialAccelerationWrapper -> "${metricAndImperial.name} (Metric)"

    is ImperialMetricAndImperialAccelerationWrapper -> "${metricAndImperial.name} (Imperial)"

    is Action -> "${energy.name}-${time.name}"

    is AngularAcceleration -> "${angularVelocity.name} per ${per.name}"

    is AngularVelocity -> "${angle.name} per ${per.name}"

    is AreaDensity -> "${weight.name} per ${per.name}"

    is Density -> "${weight.name} per ${per.name}"

    is DynamicViscosity -> "${pressure.name}-${time.name}"

    is ImperialMetricAndImperialEnergyWrapper -> "${metricAndImperialEnergy.name} (Imperial)"

    is ImperialMetricAndImperialPowerWrapper -> "${metricAndImperialPower.name} (Imperial)"

    is HeatCapacity -> "${energy.name} per ${per.name}"

    is Jolt -> "${acceleration.name} per ${per.name}"

    is KinematicViscosity -> "${area.name} per ${time.name}"

    is LinearMassDensity -> "${weight.name} per ${per.name}"

    is LuminousEnergy -> "${luminousFlux.name}-${time.name}"

    is LuminousExposure -> "${illuminance.name}-${time.name}"

    is MassFlowRate -> "${weight.name} per ${per.name}"

    is Molality -> "${amountOfSubstance.name} per ${per.name}"

    is MolarEnergy -> "${energy.name} per ${per.name}"

    is Molarity -> "${amountOfSubstance.name} per ${per.name}"

    is MolarMass -> "${weight.name} per ${per.name}"

    is MolarVolume -> "${volume.name} per ${per.name}"

    is Momentum -> "${mass.name}-${speed.name}"

    is MetricMetricAndImperialEnergyWrapper -> "${metricAndImperialEnergy.name} (Metric)"

    is MetricMetricAndImperialPowerWrapper -> "${metricAndImperialPower.name} (Metric)"

    is SpecificEnergy -> "${energy.name} per ${per.name}"

    is SpecificHeatCapacity -> "${heatCapacity.name} per ${perWeight.name}"

    is SpecificVolume -> "${volume.name} per ${per.name}"

    is Speed -> "${distance.name} per ${per.name}"

    is SurfaceTension -> "${force.name} per ${per.name}"

    is ThermalResistance -> "${temperature.name} per ${per.name}"

    is USCustomaryImperialForceWrapper -> "${imperial.name} (US)"

    is USCustomaryImperialPressureWrapper -> "${imperial.name} (US)"

    is USCustomaryImperialVolumeWrapper -> "${imperial.name} (US)"

    is USCustomaryImperialWeightWrapper -> "${imperial.name} (US)"

    is UKImperialImperialForceWrapper -> "${imperial.name} (UK)"

    is UKImperialPressureWrapper -> "${imperial.name} (UK)"

    is UKImperialImperialVolumeWrapper -> "${imperial.name} (UK)"

    is UKImperialImperialWeightWrapper -> "${imperial.name} (UK)"

    is VolumetricFlow -> "${volume.name} per ${per.name}"

    is VolumetricFlux -> "${volumetricFlow.name}-${per.name}"

    is Yank -> "${force.name} per ${per.name}"

    else -> this::class.simpleName ?: ""
}
