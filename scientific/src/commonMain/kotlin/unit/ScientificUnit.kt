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
package com.splendo.kaluga.scientific.unit

import com.splendo.kaluga.base.utils.Decimal
import com.splendo.kaluga.base.utils.RoundingMode
import com.splendo.kaluga.base.utils.round
import com.splendo.kaluga.base.utils.toDecimal
import com.splendo.kaluga.base.utils.toDouble
import com.splendo.kaluga.scientific.PhysicalQuantity
import kotlinx.serialization.Serializable

sealed interface ScientificUnit<Quantity : PhysicalQuantity> : MeasurementUsage, com.splendo.kaluga.base.utils.Serializable {
    val symbol: String
    val system: MeasurementSystem
    val quantity: Quantity
    fun toSIUnit(value: Decimal): Decimal
    fun fromSIUnit(value: Decimal): Decimal
}

interface SystemScientificUnit<System : MeasurementSystem, Quantity : PhysicalQuantity> : ScientificUnit<Quantity> {
    override val system: System
}

interface MetricScientificUnit<Quantity : PhysicalQuantity> : SystemScientificUnit<MeasurementSystem.Metric, Quantity>, MeasurementUsage.UsedInMetric
interface ImperialScientificUnit<Quantity : PhysicalQuantity> : SystemScientificUnit<MeasurementSystem.Imperial, Quantity>, MeasurementUsage.UsedInImperial
interface USCustomaryScientificUnit<Quantity : PhysicalQuantity> : SystemScientificUnit<MeasurementSystem.USCustomary, Quantity>, MeasurementUsage.UsedInUSCustomary
interface UKImperialScientificUnit<Quantity : PhysicalQuantity> : SystemScientificUnit<MeasurementSystem.UKImperial, Quantity>, MeasurementUsage.UsedInUKImperial
interface MetricAndUKImperialScientificUnit<Quantity : PhysicalQuantity> : SystemScientificUnit<MeasurementSystem.MetricAndUKImperial, Quantity>, MeasurementUsage.UsedInMetricAndUKImperial
interface MetricAndImperialScientificUnit<Quantity : PhysicalQuantity> : SystemScientificUnit<MeasurementSystem.MetricAndImperial, Quantity>, MeasurementUsage.UsedInMetricAndImperial

@Serializable
sealed class AbstractScientificUnit<Quantity : PhysicalQuantity> : ScientificUnit<Quantity>

fun <Quantity : PhysicalQuantity> ScientificUnit<Quantity>.convert(
    value: Number,
    to: ScientificUnit<Quantity>
) = convert(value.toDecimal(), to).toDouble()

fun <Quantity : PhysicalQuantity> ScientificUnit<Quantity>.convert(
    value: Number,
    to: ScientificUnit<Quantity>,
    round: Int,
    roundingMode: RoundingMode = RoundingMode.RoundHalfEven
) = convert(value.toDecimal(), to).round(round, roundingMode).toDouble()

fun <Quantity : PhysicalQuantity> ScientificUnit<Quantity>.convert(
    value: Decimal,
    to: ScientificUnit<Quantity>,
    round: Int,
    roundingMode: RoundingMode = RoundingMode.RoundHalfEven
) = convert(value, to).round(round, roundingMode)

fun <Quantity : PhysicalQuantity> ScientificUnit<Quantity>.convert(
    value: Decimal,
    to: ScientificUnit<Quantity>
) = if (this == to) value else to.fromSIUnit(toSIUnit(value))

val Units: Set<ScientificUnit<*>> get() = AccelerationUnits +
    ActionUnits +
    AmountOfSubstanceUnits +
    AngleUnits +
    AngularAccelerationUnits +
    AngularVelocityUnits +
    AreaDensityUnits +
    AreaUnits +
    CatalysticActivityUnits +
    DensityUnits +
    DynamicViscosityUnits +
    ElectricCapacitanceUnits +
    ElectricChargeUnits +
    ElectricConductanceUnits +
    ElectricCurrentUnits +
    ElectricInductanceUnits +
    ElectricResistanceUnits +
    EnergyUnits +
    ForceUnits +
    FrequencyUnits +
    HeatCapacityUnits +
    IlluminanceUnits +
    IonizingRadiationAbsorbedDoseUnits +
    IonizingRadiationEquivalentDoseUnits +
    JoltUnits +
    LengthUnits +
    LinearMassDensityUnits +
    LuminanceUnits +
    LuminousEnergyUnits +
    LuminousExposureUnits +
    LuminousFluxUnits +
    LuminousIntensityUnits +
    MagneticFluxUnits +
    MagneticInductionUnits +
    MassFlowRateUnits +
    MolalityUnits +
    MolarEnergyUnits +
    MolarityUnits +
    MolarMassUnits +
    MolarVolumeUnits +
    MomentumUnits +
    PowerUnits +
    PressureUnits +
    RadioactivityUnits +
    SolidAngleUnits +
    SpecificEnergyUnits +
    SpecificHeatCapacityUnits +
    SpecificVolumeUnits +
    SpeedUnits +
    SurfaceTensionUnits +
    TemperatureUnits +
    ThermalResistanceUnits +
    TimeUnits +
    VoltageUnits +
    VolumetricFlowUnits +
    VolumetricFluxUnits +
    VolumeUnits +
    WeightUnits +
    YankUnits +
    DimensionlessUnits
