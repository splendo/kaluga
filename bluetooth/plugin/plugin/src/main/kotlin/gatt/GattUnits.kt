/*
 Copyright 2026 Splendo Consulting B.V. The Netherlands

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

package com.splendo.kaluga.bluetooth.plugin.gatt

/**
 * The Kaluga Scientific types a Bluetooth SIG `<Unit>` maps onto, used to generate a `ScientificValue` value class:
 * [quantity] is the `com.splendo.kaluga.scientific.PhysicalQuantity` member name; [unitType] is the simple name of the
 * concrete unit's type (the `Unit` type parameter of `ScientificValue<Quantity, Unit>`); [unit] is the expression that
 * constructs the unit instance — a single `com.splendo.kaluga.scientific.unit` object (e.g. `Joule`) or a compound
 * expression (e.g. `Meter per Second`, `Pascal x Second`).
 */
internal data class ScientificUnit(val quantity: String, val unitType: String, val unit: String)

/**
 * Maps Bluetooth SIG unit URIs onto Kaluga Scientific units. Units the library has no equivalent for are intentionally
 * absent (the generator falls back to a plain numeric value for those).
 */
internal val bluetoothScientificUnits: Map<String, ScientificUnit> = mapOf(
    "org.bluetooth.unit.absorbed_dose.gray" to ScientificUnit("IonizingRadiationAbsorbedDose", "Gray", "Gray"),
    "org.bluetooth.unit.absorbed_dose_rate.gray_per_second" to ScientificUnit("AbsorbedDoseRate", "AbsorbedDoseRate", "Gray per Second"),
    "org.bluetooth.unit.acceleration.metres_per_second_squared" to ScientificUnit("Acceleration", "MetricAcceleration", "Meter per Second per Second"),
    "org.bluetooth.unit.activity_referred_to_a_radionuclide.becquerel" to ScientificUnit("Radioactivity", "Becquerel", "Becquerel"),
    "org.bluetooth.unit.amount_concentration.mole_per_cubic_metre" to ScientificUnit("Molarity", "MetricMolarity", "Mole per CubicMeter"),
    "org.bluetooth.unit.amount_of_substance.mole" to ScientificUnit("AmountOfSubstance", "Mole", "Mole"),
    "org.bluetooth.unit.angular_acceleration.radian_per_second_squared" to ScientificUnit("AngularAcceleration", "AngularAcceleration", "Radian per Second per Second"),
    "org.bluetooth.unit.angular_velocity.radian_per_second" to ScientificUnit("AngularVelocity", "AngularVelocity", "Radian per Second"),
    "org.bluetooth.unit.angular_velocity.revolution_per_minute" to ScientificUnit("AngularVelocity", "AngularVelocity", "Turn per Minute"),
    "org.bluetooth.unit.area.barn" to ScientificUnit("Area", "Barn", "Barn"),
    "org.bluetooth.unit.area.hectare" to ScientificUnit("Area", "Hectare", "Hectare"),
    "org.bluetooth.unit.area.square_metres" to ScientificUnit("Area", "SquareMeter", "SquareMeter"),
    "org.bluetooth.unit.capacitance.farad" to ScientificUnit("ElectricCapacitance", "Farad", "Farad"),
    "org.bluetooth.unit.catalytic_activity.katal" to ScientificUnit("CatalysticActivity", "Katal", "Katal"),
    "org.bluetooth.unit.catalytic_activity_concentration.katal_per_cubic_metre" to ScientificUnit("CatalyticConcentration", "MetricCatalyticConcentration", "Katal per CubicMeter"),
    "org.bluetooth.unit.density.kilogram_per_cubic_metre" to ScientificUnit("Density", "MetricDensity", "Kilogram per CubicMeter"),
    "org.bluetooth.unit.dose_equivalent.sievert" to ScientificUnit("IonizingRadiationEquivalentDose", "Sievert", "Sievert"),
    "org.bluetooth.unit.dynamic_viscosity.pascal_second" to ScientificUnit("DynamicViscosity", "MetricDynamicViscosity", "Pascal x Second"),
    "org.bluetooth.unit.electric_charge.ampere_hours" to ScientificUnit("ElectricCharge", "AmpereHour", "AmpereHour"),
    "org.bluetooth.unit.electric_charge.coulomb" to ScientificUnit("ElectricCharge", "Coulomb", "Coulomb"),
    "org.bluetooth.unit.electric_charge_density.coulomb_per_cubic_metre" to ScientificUnit("ElectricChargeDensity", "MetricElectricChargeDensity", "Coulomb per CubicMeter"),
    "org.bluetooth.unit.electric_conductance.siemens" to ScientificUnit("ElectricConductance", "Siemens", "Siemens"),
    "org.bluetooth.unit.electric_current.ampere" to ScientificUnit("ElectricCurrent", "Ampere", "Ampere"),
    "org.bluetooth.unit.electric_current_density.ampere_per_square_metre" to ScientificUnit("ElectricCurrentDensity", "MetricElectricCurrentDensity", "Ampere per SquareMeter"),
    "org.bluetooth.unit.electric_field_strength.volt_per_metre" to ScientificUnit("ElectricFieldStrength", "MetricElectricFieldStrength", "Volt per Meter"),
    "org.bluetooth.unit.electric_flux_density.coulomb_per_square_metre" to ScientificUnit("SurfaceChargeDensity", "MetricSurfaceChargeDensity", "Coulomb per SquareMeter"),
    "org.bluetooth.unit.electric_potential_difference.volt" to ScientificUnit("Voltage", "Volt", "Volt"),
    "org.bluetooth.unit.electric_resistance.ohm" to ScientificUnit("ElectricResistance", "Ohm", "Ohm"),
    "org.bluetooth.unit.energy.gram_calorie" to ScientificUnit("Energy", "Calorie", "Calorie"),
    "org.bluetooth.unit.energy.joule" to ScientificUnit("Energy", "Joule", "Joule"),
    "org.bluetooth.unit.energy.kilogram_calorie" to ScientificUnit("Energy", "Kilocalorie", "Kilocalorie"),
    "org.bluetooth.unit.energy.kilowatt_hour" to ScientificUnit("Energy", "KilowattHour", "KilowattHour"),
    "org.bluetooth.unit.energy_density.joule_per_cubic_metre" to ScientificUnit("EnergyDensity", "MetricEnergyDensity", "Joule per CubicMeter"),
    "org.bluetooth.unit.exposure.coulomb_per_kilogram" to ScientificUnit("Exposure", "MetricExposure", "Coulomb per Kilogram"),
    "org.bluetooth.unit.force.newton" to ScientificUnit("Force", "Newton", "Newton"),
    "org.bluetooth.unit.frequency.hertz" to ScientificUnit("Frequency", "Hertz", "Hertz"),
    "org.bluetooth.unit.heat_capacity.joule_per_kelvin" to ScientificUnit("HeatCapacity", "MetricHeatCapacity", "Joule per Kelvin"),
    "org.bluetooth.unit.heat_flux_density.watt_per_square_metre" to ScientificUnit("Irradiance", "MetricIrradiance", "Watt per SquareMeter"),
    "org.bluetooth.unit.illuminance.lux" to ScientificUnit("Illuminance", "Lux", "Lux"),
    "org.bluetooth.unit.inductance.henry" to ScientificUnit("ElectricInductance", "Henry", "Henry"),
    "org.bluetooth.unit.irradiance.watt_per_square_metre" to ScientificUnit("Irradiance", "MetricIrradiance", "Watt per SquareMeter"),
    "org.bluetooth.unit.length.foot" to ScientificUnit("Length", "Foot", "Foot"),
    "org.bluetooth.unit.length.inch" to ScientificUnit("Length", "Inch", "Inch"),
    "org.bluetooth.unit.length.meter" to ScientificUnit("Length", "Meter", "Meter"),
    "org.bluetooth.unit.length.mile" to ScientificUnit("Length", "Mile", "Mile"),
    "org.bluetooth.unit.length.nautical_mile" to ScientificUnit("Length", "NauticalMile", "NauticalMile"),
    "org.bluetooth.unit.length.parsec" to ScientificUnit("Length", "Parsec", "Parsec"),
    "org.bluetooth.unit.length.yard" to ScientificUnit("Length", "Yard", "Yard"),
    "org.bluetooth.unit.luminance.candela_per_square_metre" to ScientificUnit("Luminance", "Nit", "Nit"),
    "org.bluetooth.unit.luminous_flux.lumen" to ScientificUnit("LuminousFlux", "Lumen", "Lumen"),
    "org.bluetooth.unit.luminous_intensity.candela" to ScientificUnit("LuminousIntensity", "Candela", "Candela"),
    "org.bluetooth.unit.magnetic_field_strength.ampere_per_metre" to ScientificUnit("MagneticFieldStrength", "MetricMagneticFieldStrength", "Ampere per Meter"),
    "org.bluetooth.unit.magnetic_flux.weber" to ScientificUnit("MagneticFlux", "Weber", "Weber"),
    "org.bluetooth.unit.magnetic_flux_density.tesla" to ScientificUnit("MagneticInduction", "Tesla", "Tesla"),
    "org.bluetooth.unit.mass.kilogram" to ScientificUnit("Weight", "Kilogram", "Kilogram"),
    "org.bluetooth.unit.mass.pound" to ScientificUnit("Weight", "Pound", "Pound"),
    "org.bluetooth.unit.mass.tonne" to ScientificUnit("Weight", "Tonne", "Tonne"),
    "org.bluetooth.unit.mass_concentration.kilogram_per_cubic_metre" to ScientificUnit("Density", "MetricDensity", "Kilogram per CubicMeter"),
    "org.bluetooth.unit.mass_density.kilogram_per_litre" to ScientificUnit("Density", "MetricDensity", "Kilogram per Liter"),
    "org.bluetooth.unit.mass_density.milligram_per_decilitre" to ScientificUnit("Density", "MetricDensity", "Milligram per Deciliter"),
    "org.bluetooth.unit.mass_density.millimole_per_litre" to ScientificUnit("Molarity", "MetricMolarity", "Millimole per Liter"),
    "org.bluetooth.unit.mass_density.mole_per_litre" to ScientificUnit("Molarity", "MetricMolarity", "Mole per Liter"),
    "org.bluetooth.unit.molar_energy.joule_per_mole" to ScientificUnit("MolarEnergy", "MetricMolarEnergy", "Joule per Mole"),
    "org.bluetooth.unit.molar_entropy.joule_per_mole_kelvin" to ScientificUnit("MolarEntropy", "MetricMolarEntropy", "Joule per Kelvin per Mole"),
    "org.bluetooth.unit.moment_of_force.newton_metre" to ScientificUnit("Torque", "MetricTorque", "Newton x Meter"),
    "org.bluetooth.unit.period.beats_per_minute" to ScientificUnit("Frequency", "BeatsPerMinute", "BeatsPerMinute"),
    "org.bluetooth.unit.permeability.henry_per_metre" to ScientificUnit("Permeability", "MetricPermeability", "Henry per Meter"),
    "org.bluetooth.unit.permittivity.farad_per_metre" to ScientificUnit("Permittivity", "MetricPermittivity", "Farad per Meter"),
    "org.bluetooth.unit.plane_angle.degree" to ScientificUnit("Angle", "Degree", "Degree"),
    "org.bluetooth.unit.plane_angle.minute" to ScientificUnit("Angle", "ArcMinute", "ArcMinute"),
    "org.bluetooth.unit.plane_angle.radian" to ScientificUnit("Angle", "Radian", "Radian"),
    "org.bluetooth.unit.plane_angle.second" to ScientificUnit("Angle", "ArcSecond", "ArcSecond"),
    "org.bluetooth.unit.power.watt" to ScientificUnit("Power", "Watt", "Watt"),
    "org.bluetooth.unit.pressure.bar" to ScientificUnit("Pressure", "Bar", "Bar"),
    "org.bluetooth.unit.pressure.millimetre_of_mercury" to ScientificUnit("Pressure", "MillimeterOfMercury", "MillimeterOfMercury"),
    "org.bluetooth.unit.pressure.pascal" to ScientificUnit("Pressure", "Pascal", "Pascal"),
    "org.bluetooth.unit.pressure.pound_force_per_square_inch" to ScientificUnit("Pressure", "PoundSquareInch", "PoundSquareInch"),
    "org.bluetooth.unit.radiance.watt_per_square_metre_steradian" to ScientificUnit("Radiance", "MetricRadiance", "Watt per Steradian per SquareMeter"),
    "org.bluetooth.unit.radiant_intensity.watt_per_steradian" to ScientificUnit("RadiantIntensity", "MetricAndImperialRadiantIntensity", "Watt per Steradian"),
    "org.bluetooth.unit.solid_angle.steradian" to ScientificUnit("SolidAngle", "Steradian", "Steradian"),
    "org.bluetooth.unit.specific_energy.joule_per_kilogram" to ScientificUnit("SpecificEnergy", "MetricSpecificEnergy", "Joule per Kilogram"),
    "org.bluetooth.unit.specific_heat_capacity.joule_per_kilogram_kelvin" to ScientificUnit("SpecificHeatCapacity", "MetricSpecificHeatCapacity", "Joule per Kelvin per Kilogram"),
    "org.bluetooth.unit.specific_volume.cubic_metre_per_kilogram" to ScientificUnit("SpecificVolume", "MetricSpecificVolume", "CubicMeter per Kilogram"),
    "org.bluetooth.unit.surface_charge_density.coulomb_per_square_metre" to ScientificUnit("SurfaceChargeDensity", "MetricSurfaceChargeDensity", "Coulomb per SquareMeter"),
    "org.bluetooth.unit.surface_density.kilogram_per_square_metre" to ScientificUnit("AreaDensity", "MetricAreaDensity", "Kilogram per SquareMeter"),
    "org.bluetooth.unit.surface_tension.newton_per_metre" to ScientificUnit("SurfaceTension", "MetricSurfaceTension", "Newton per Meter"),
    "org.bluetooth.unit.thermal_conductivity.watt_per_metre_kelvin" to ScientificUnit("ThermalConductivity", "MetricThermalConductivity", "Watt per Kelvin per Meter"),
    "org.bluetooth.unit.thermodynamic_temperature.degree_celsius" to ScientificUnit("Temperature", "Celsius", "Celsius"),
    "org.bluetooth.unit.thermodynamic_temperature.degree_fahrenheit" to ScientificUnit("Temperature", "Fahrenheit", "Fahrenheit"),
    "org.bluetooth.unit.thermodynamic_temperature.kelvin" to ScientificUnit("Temperature", "Kelvin", "Kelvin"),
    "org.bluetooth.unit.time.hour" to ScientificUnit("Time", "Hour", "Hour"),
    "org.bluetooth.unit.time.minute" to ScientificUnit("Time", "Minute", "Minute"),
    "org.bluetooth.unit.time.second" to ScientificUnit("Time", "Second", "Second"),
    "org.bluetooth.unit.unitless" to ScientificUnit("Dimensionless", "One", "One"),
    "org.bluetooth.unit.percentage" to ScientificUnit("Dimensionless", "Percent", "Percent"),
    "org.bluetooth.unit.per_mille" to ScientificUnit("Dimensionless", "Permill", "Permill"),
    "org.bluetooth.unit.velocity.kilometer_per_hour" to ScientificUnit("Speed", "MetricSpeed", "Kilometer per Hour"),
    "org.bluetooth.unit.velocity.knot" to ScientificUnit("Speed", "MetricSpeed", "NauticalMile per Hour"),
    "org.bluetooth.unit.velocity.metres_per_second" to ScientificUnit("Speed", "MetricSpeed", "Meter per Second"),
    "org.bluetooth.unit.velocity.mile_per_hour" to ScientificUnit("Speed", "ImperialSpeed", "Mile per Hour"),
    "org.bluetooth.unit.volume.cubic_metres" to ScientificUnit("Volume", "CubicMeter", "CubicMeter"),
    "org.bluetooth.unit.volume.litre" to ScientificUnit("Volume", "Liter", "Liter"),
)
