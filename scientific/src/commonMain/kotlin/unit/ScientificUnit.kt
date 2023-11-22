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
import com.splendo.kaluga.base.utils.RoundingMode
import com.splendo.kaluga.base.utils.round
import com.splendo.kaluga.base.utils.toDecimal
import com.splendo.kaluga.base.utils.toDouble
import com.splendo.kaluga.scientific.PhysicalQuantity
import kotlinx.serialization.Serializable

/**
 * A unit of measurement for a [PhysicalQuantity]
 * @param Quantity the type of [PhysicalQuantity] to measure
 */
sealed interface ScientificUnit<Quantity : PhysicalQuantity> : MeasurementUsage, com.splendo.kaluga.base.utils.Serializable {

    /**
     * The symbol representing the unit
     */
    val symbol: String

    /**
     * The [MeasurementSystem] the unit is used in
     */
    val system: MeasurementSystem

    /**
     * The [Quantity] of the unit
     */
    val quantity: Quantity

    /**
     * Converts a value of this unit into the value of the [ScientificUnit] of [Quantity] to be used according to the SI
     * @param value the [Decimal] value to convert to the value in the equivalent SI Unit
     * @return the [Decimal] value in the equivalent SI unit
     */
    fun toSIUnit(value: Decimal): Decimal

    /**
     * Converts a value of the [ScientificUnit] of [Quantity] to be used according to the SI into a value of this unit
     * @param value the [Decimal] value in the SI Unit to convert to the equivalent value in this unit
     * @return the [Decimal] value in this unit
     */
    fun fromSIUnit(value: Decimal): Decimal
}

/**
 * A [ScientificUnit] used in a given [MeasurementSystem]
 * @param System the type of [MeasurementSystem] the unit is used in
 * @param Quantity the type of [PhysicalQuantity] to measure
 */
interface SystemScientificUnit<System : MeasurementSystem, Quantity : PhysicalQuantity> : ScientificUnit<Quantity> {
    override val system: System
}

/**
 * A [SystemScientificUnit] for [MeasurementSystem.Metric]
 */
interface MetricScientificUnit<Quantity : PhysicalQuantity> : SystemScientificUnit<MeasurementSystem.Metric, Quantity>, MeasurementUsage.UsedInMetric

/**
 * A [SystemScientificUnit] for [MeasurementSystem.Imperial]
 */
interface ImperialScientificUnit<Quantity : PhysicalQuantity> : SystemScientificUnit<MeasurementSystem.Imperial, Quantity>, MeasurementUsage.UsedInImperial

/**
 * A [SystemScientificUnit] for [MeasurementSystem.USCustomary]
 */
interface USCustomaryScientificUnit<Quantity : PhysicalQuantity> : SystemScientificUnit<MeasurementSystem.USCustomary, Quantity>, MeasurementUsage.UsedInUSCustomary

/**
 * A [SystemScientificUnit] for [MeasurementSystem.UKImperial]
 */
interface UKImperialScientificUnit<Quantity : PhysicalQuantity> :
    SystemScientificUnit<MeasurementSystem.UKImperial, Quantity>,
    MeasurementUsage.UsedInUKImperial

/**
 * A [SystemScientificUnit] for [MeasurementSystem.MetricAndUKImperial]
 */
interface MetricAndUKImperialScientificUnit<Quantity : PhysicalQuantity> :
    SystemScientificUnit<MeasurementSystem.MetricAndUKImperial, Quantity>,
    MeasurementUsage.UsedInMetricAndUKImperial

/**
 * A [SystemScientificUnit] for [MeasurementSystem.MetricAndImperial]
 */
interface MetricAndImperialScientificUnit<Quantity : PhysicalQuantity> :
    SystemScientificUnit<MeasurementSystem.MetricAndImperial, Quantity>,
    MeasurementUsage.UsedInMetricAndImperial

/**
 * A class implementation of [ScientificUnit]
 * @param Quantity the type of [PhysicalQuantity] to measure
 */
@Serializable
sealed class AbstractScientificUnit<Quantity : PhysicalQuantity> : ScientificUnit<Quantity>

/**
 * Converts a value in a [ScientificUnit] to the value of another unit with the same [PhysicalQuantity]
 * @param Quantity the type of [PhysicalQuantity] of both units
 * @param value the [Number] value to convert
 * @param to the [ScientificUnit] to convert the value into
 * @return the [Double] value in [to] that is equivalent to [value] in this unit
 */
fun <Quantity : PhysicalQuantity> ScientificUnit<Quantity>.convert(value: Number, to: ScientificUnit<Quantity>) = convert(value.toDecimal(), to).toDouble()

/**
 * Converts a value in a [ScientificUnit] to the value of another unit with the same [PhysicalQuantity] and rounds it
 * @param Quantity the type of [PhysicalQuantity] of both units
 * @param value the [Number] value to convert
 * @param to the [ScientificUnit] to convert the value into
 * @param round The number of digits a rounded value should have after its decimal point
 * @param roundingMode The [RoundingMode] to apply when scaling
 * @return the [Double] value in [to] that is equivalent to [value] in this unit
 */
fun <Quantity : PhysicalQuantity> ScientificUnit<Quantity>.convert(
    value: Number,
    to: ScientificUnit<Quantity>,
    round: Int,
    roundingMode: RoundingMode = RoundingMode.RoundHalfEven,
) = convert(value.toDecimal(), to).round(round, roundingMode).toDouble()

/**
 * Converts a value in a [ScientificUnit] to the value of another unit with the same [PhysicalQuantity]
 * @param Quantity the type of [PhysicalQuantity] of both units
 * @param value the [Decimal] value to convert
 * @param to the [ScientificUnit] to convert the value into
 * @param round The number of digits a rounded value should have after its decimal point
 * @param roundingMode The [RoundingMode] to apply when scaling
 * @return the [Decimal] value in [to] that is equivalent to [value] in this unit
 */
fun <Quantity : PhysicalQuantity> ScientificUnit<Quantity>.convert(
    value: Decimal,
    to: ScientificUnit<Quantity>,
    round: Int,
    roundingMode: RoundingMode = RoundingMode.RoundHalfEven,
) = convert(value, to).round(round, roundingMode)

/**
 * Converts a value in a [ScientificUnit] to the value of another unit with the same [PhysicalQuantity]
 * @param Quantity the type of [PhysicalQuantity] of both units
 * @param value the [Decimal] value to convert
 * @param to the [ScientificUnit] to convert the value into
 * @return the [Decimal] value in [to] that is equivalent to [value] in this unit
 */
fun <Quantity : PhysicalQuantity> ScientificUnit<Quantity>.convert(value: Decimal, to: ScientificUnit<Quantity>) = if (this == to) value else to.fromSIUnit(toSIUnit(value))

/**
 * The set of all [AbstractScientificUnit] supported by this library
 */
val Units: Set<AbstractScientificUnit<*>> get() = AccelerationUnits +
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
    KinematicViscosityUnits +
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
