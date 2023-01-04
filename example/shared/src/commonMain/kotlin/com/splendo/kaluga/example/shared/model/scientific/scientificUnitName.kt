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

import com.splendo.kaluga.scientific.unit.*

val ScientificUnit<*>.name: String get() = when (this) {
    is Acceleration -> "${speed.name} per ${per.name}"
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
