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

import com.splendo.kaluga.scientific.assertEqualScientificValue
import com.splendo.kaluga.scientific.convert
import com.splendo.kaluga.scientific.converter.acceleration.div
import com.splendo.kaluga.scientific.converter.amountOfSubstance.div
import com.splendo.kaluga.scientific.converter.amountOfSubstance.times
import com.splendo.kaluga.scientific.converter.area.times
import com.splendo.kaluga.scientific.converter.areaDensity.times
import com.splendo.kaluga.scientific.converter.density.times
import com.splendo.kaluga.scientific.converter.energy.div
import com.splendo.kaluga.scientific.converter.force.div
import com.splendo.kaluga.scientific.converter.heatCapacity.div
import com.splendo.kaluga.scientific.converter.length.times
import com.splendo.kaluga.scientific.converter.linearMassDensity.times
import com.splendo.kaluga.scientific.converter.massFlowRate.times
import com.splendo.kaluga.scientific.converter.molarMass.times
import com.splendo.kaluga.scientific.converter.momentum.div
import com.splendo.kaluga.scientific.converter.time.times
import com.splendo.kaluga.scientific.converter.volume.div
import com.splendo.kaluga.scientific.converter.volume.times
import com.splendo.kaluga.scientific.converter.yank.div
import com.splendo.kaluga.scientific.invoke
import com.splendo.kaluga.scientific.times
import kotlin.test.Test

class WeightUnitTest {

    @Test
    fun kilogramConversionTest() {
        assertScientificConversion(1.0, Kilogram, 1e+12, Nanogram)
        assertScientificConversion(1.0, Kilogram, 1e+9, Microgram)
        assertScientificConversion(1.0, Kilogram, 1e+6, Milligram)
        assertScientificConversion(1.0, Kilogram, 100_000.0, Centigram)
        assertScientificConversion(1.0, Kilogram, 10_000.0, Decigram)
        assertScientificConversion(1.0, Kilogram, 1_000.0, Gram)
        assertScientificConversion(1.0, Kilogram, 100.0, Decagram)
        assertScientificConversion(1.0, Kilogram, 10.0, Hectogram)
        assertScientificConversion(1.0, Kilogram, 0.001, Megagram)
        assertScientificConversion(1.0, Kilogram, 0.001, Tonne)
        assertScientificConversion(1.0, Kilogram, 1e-6, Gigagram)

        assertScientificConversion(1.0, Kilogram, 6.02214076e+26, Dalton)
        assertScientificConversion(1.0, Kilogram, 2.20462262, Pound, 8)
    }

    @Test
    fun daltonConversionTest() {
        assertScientificConversion(1.0, Dalton, 1e+9, Nanodalton)
        assertScientificConversion(1.0, Dalton, 1e+6, Microdalton)
        assertScientificConversion(1.0, Dalton, 1_000.0, Millidalton)
        assertScientificConversion(1.0, Dalton, 100.0, Centidalton)
        assertScientificConversion(1.0, Dalton, 10.0, Decidalton)
        assertScientificConversion(1.0, Dalton, 0.1, Decadalton)
        assertScientificConversion(1.0, Dalton, 0.01, HectoDalton)
        assertScientificConversion(1.0, Dalton, 0.001, Kilodalton)
        assertScientificConversion(1.0, Dalton, 1e-6, Megadalton)
        assertScientificConversion(1.0, Dalton, 1e-9, Gigadalton)
    }

    @Test
    fun poundConversionTest() {
        assertScientificConversion(1.0, Pound, 7_000.0, Grain)
        assertScientificConversion(1.0, Pound, 256.0, Dram)
        assertScientificConversion(1.0, Pound, 16.0, Ounce)
        assertScientificConversion(1.0, Pound, 0.07142857, Stone, 8)
        assertScientificConversion(1.0, Pound, 0.03108095, Slug, 8)

        // uk ton
        assertScientificConversion(1.0, Pound, 0.00044643, ImperialTon, 8)
        assertScientificConversion(1.0, Pound, 1.0, Pound.ukImperial)
        // us ton
        assertScientificConversion(1.0, Pound, 0.0005, UsTon)
        assertScientificConversion(1.0, Pound, 1.0, Pound.usCustomary)
    }

    @Test
    fun weightFromAmountOfSubstanceAndMolalityTest() {
        assertEqualScientificValue(1(Kilogram), 2(Decimole) / 2(Decimole per Kilogram))
        assertEqualScientificValue(1(Pound), 2(Decimole) / 2(Decimole per Pound))
        assertEqualScientificValue(1(ImperialTon), 2(Decimole) / 2(Decimole per ImperialTon))
        assertEqualScientificValue(1(UsTon), 2(Decimole) / 2(Decimole per UsTon))
        assertEqualScientificValue(
            1(Kilogram),
            2(Decimole) / 2(Decimole per Kilogram).convert((Decimole per Pound) as Molality),
            8,
        )
    }

    @Test
    fun weightFromAreaDensityAndAreaTest() {
        assertEqualScientificValue(4(Kilogram), 2(Kilogram per SquareMeter) * 2(SquareMeter))
        assertEqualScientificValue(4(Kilogram), 2(SquareMeter) * 2(Kilogram per SquareMeter))
        assertEqualScientificValue(4(Pound), 2(Pound per SquareFoot) * 2(SquareFoot))
        assertEqualScientificValue(4(Pound), 2(SquareFoot) * 2(Pound per SquareFoot))
        assertEqualScientificValue(4(ImperialTon), 2(ImperialTon per SquareFoot) * 2(SquareFoot))
        assertEqualScientificValue(4(ImperialTon), 2(SquareFoot) * 2(ImperialTon per SquareFoot))
        assertEqualScientificValue(4(UsTon), 2(UsTon per SquareFoot) * 2(SquareFoot))
        assertEqualScientificValue(4(UsTon), 2(SquareFoot) * 2(UsTon per SquareFoot))
        assertEqualScientificValue(
            4(Kilogram),
            2(Kilogram per SquareMeter) * 2(SquareMeter).convert(SquareFoot),
        )
        assertEqualScientificValue(
            4(Kilogram),
            2(SquareMeter).convert(SquareFoot) * 2(Kilogram per SquareMeter),
        )
    }

    @Test
    fun weightFromDensityAndVolumeTest() {
        assertEqualScientificValue(4(Kilogram), 2(Kilogram per CubicMeter) * 2(CubicMeter))
        assertEqualScientificValue(4(Kilogram), 2(CubicMeter) * 2(Kilogram per CubicMeter))
        assertEqualScientificValue(4(Pound), 2(Pound per CubicFoot) * 2(CubicFoot))
        assertEqualScientificValue(4(Pound), 2(CubicFoot) * 2(Pound per CubicFoot))
        assertEqualScientificValue(4(Pound), 2(Pound per CubicFoot) * 2(CubicFoot.ukImperial))
        assertEqualScientificValue(4(Pound), 2(CubicFoot.ukImperial) * 2(Pound per CubicFoot))
        assertEqualScientificValue(4(Pound), 2(Pound per CubicFoot) * 2(CubicFoot.usCustomary))
        assertEqualScientificValue(4(Pound), 2(CubicFoot.usCustomary) * 2(Pound per CubicFoot))
        assertEqualScientificValue(4(ImperialTon), 2(ImperialTon per CubicFoot) * 2(CubicFoot))
        assertEqualScientificValue(4(ImperialTon), 2(CubicFoot) * 2(ImperialTon per CubicFoot))
        assertEqualScientificValue(
            4(ImperialTon),
            2(ImperialTon per ImperialGallon) * 2(ImperialGallon),
        )
        assertEqualScientificValue(
            4(ImperialTon),
            2(ImperialGallon) * 2(ImperialTon per ImperialGallon),
        )
        assertEqualScientificValue(4(UsTon), 2(UsTon per CubicFoot) * 2(CubicFoot))
        assertEqualScientificValue(4(UsTon), 2(CubicFoot) * 2(UsTon per CubicFoot))
        assertEqualScientificValue(4(UsTon), 2(UsTon per UsLiquidGallon) * 2(UsLiquidGallon))
        assertEqualScientificValue(4(UsTon), 2(UsLiquidGallon) * 2(UsTon per UsLiquidGallon))
        assertEqualScientificValue(
            4(Kilogram),
            2(Kilogram per CubicMeter) * 2(CubicMeter).convert(CubicFoot),
        )
        assertEqualScientificValue(
            4(Kilogram),
            2(CubicMeter).convert(CubicFoot) * 2(Kilogram per CubicMeter),
        )
    }

    @Test
    fun weightFromEnergyAndIonizingRadiationAbsorbedDoseTest() {
        assertEqualScientificValue(1(Gram), 200(Erg) / 2(Rad))
        assertEqualScientificValue(1(Gram), 2000(Decierg) / 2(Rad))
        assertEqualScientificValue(1(Gram), 200(Erg) / 20(Decirad))
        assertEqualScientificValue(1(Gram), 2000(Decierg) / 20(Decirad))
        assertEqualScientificValue(1(Kilogram), 2(Joule) / 2(Gray))
    }

    @Test
    fun weightFromEnergyAndIonizingRadiationEquivalentDoseTest() {
        assertEqualScientificValue(1(Gram), 200(Erg) / 2(RoentgenEquivalentMan))
        assertEqualScientificValue(1(Gram), 2000(Decierg) / 2(RoentgenEquivalentMan))
        assertEqualScientificValue(1(Gram), 200(Erg) / 20(DeciroentgenEquivalentMan))
        assertEqualScientificValue(1(Gram), 2000(Decierg) / 20(DeciroentgenEquivalentMan))
        assertEqualScientificValue(1(Kilogram), 2(Joule) / 2(Sievert))
    }

    @Test
    fun weightFromEnergyAndSpecificEnergyTest() {
        assertEqualScientificValue(1(Kilogram), 2(WattHour) / 2(WattHour per Kilogram))
        assertEqualScientificValue(1(Pound), 2(WattHour) / 2(WattHour per Pound))
        assertEqualScientificValue(1(ImperialTon), 2(WattHour) / 2(WattHour per ImperialTon))
        assertEqualScientificValue(1(UsTon), 2(WattHour) / 2(WattHour per UsTon))
        assertEqualScientificValue(1(Kilogram), 2(Joule) / 2(Joule per Kilogram))
        assertEqualScientificValue(
            1(Pound),
            2(BritishThermalUnit) / 2(BritishThermalUnit per Pound),
        )
        assertEqualScientificValue(
            1(ImperialTon),
            2(BritishThermalUnit) / 2(BritishThermalUnit per ImperialTon),
        )
        assertEqualScientificValue(
            1(UsTon),
            2(BritishThermalUnit) / 2(BritishThermalUnit per UsTon),
        )
        assertEqualScientificValue(
            1(Kilogram),
            2(Joule).convert(BritishThermalUnit) / 2(Joule per Kilogram),
            8,
        )
    }

    @Test
    fun weightFromForceAndAccelerationTest() {
        assertEqualScientificValue(1(Gram), 2(Dyne) / 2(Centimeter per Second per Second))
        assertEqualScientificValue((1 / MetricStandardGravityAcceleration.value)(Gram), 2(Dyne) / 2(Centig))
        assertEqualScientificValue(1(Gram), 20(Decidyne) / 2(Centimeter per Second per Second))
        assertEqualScientificValue((1 / MetricStandardGravityAcceleration.value)(Gram), 20(Decidyne) / 2(Centig))

        assertEqualScientificValue(1(Kilogram), 2(Newton) / 2(Meter per Second per Second))
        assertEqualScientificValue(
            1(Kilogram),
            2(KilogramForce) / (2 * MetricStandardGravityAcceleration),
        )
        assertEqualScientificValue(
            1(Kilogram),
            2(KilogramForce) / 2(GUnit),
        )
        assertEqualScientificValue(
            1(Kilogram),
            2(KilogramForce) / 2(GUnit.metric),
        )

        assertEqualScientificValue(1(Gram), 2(GramForce) / 2(GUnit))
        assertEqualScientificValue(1(Gram), 2(GramForce) / (2 * MetricStandardGravityAcceleration))
        assertEqualScientificValue(
            1(Milligram),
            2(MilligramForce) / 2(GUnit),
        )
        assertEqualScientificValue(
            1(Milligram),
            2(MilligramForce) / (2 * MetricStandardGravityAcceleration),
        )
        assertEqualScientificValue(
            1(Tonne),
            2(TonneForce) / 2(GUnit),
        )
        assertEqualScientificValue(
            1(Tonne),
            2(TonneForce) / (2 * MetricStandardGravityAcceleration),
        )

        assertEqualScientificValue((1 / ImperialStandardGravityAcceleration.value)(Pound), 2(Poundal) / 2(GUnit), round = 10)
        assertEqualScientificValue(1(Pound), 2(Poundal) / 2(Foot per Second per Second))
        assertEqualScientificValue(
            1(Pound),
            2(PoundForce) / 2(GUnit),
            8,
        )
        assertEqualScientificValue(
            1(Pound),
            2(PoundForce) / 2(GUnit.imperial),
            8,
        )
        assertEqualScientificValue(
            1(Pound),
            2(PoundForce) / (2 * ImperialStandardGravityAcceleration),
            8,
        )
        assertEqualScientificValue(
            1(Ounce),
            2(OunceForce) / 2(GUnit),
            8,
        )
        assertEqualScientificValue(
            1(Ounce),
            2(OunceForce) / (2 * ImperialStandardGravityAcceleration),
            8,
        )
        assertEqualScientificValue(
            1(Grain),
            2(GrainForce) / 2(GUnit),
            8,
        )
        assertEqualScientificValue(
            1(Grain),
            2(GrainForce) / (2 * ImperialStandardGravityAcceleration),
            8,
        )

        assertEqualScientificValue(
            1000(Pound.usCustomary),
            2(Kip) / 2(GUnit),
            8,
        )
        assertEqualScientificValue(
            1000(Pound.usCustomary),
            2(Kip) / (2 * ImperialStandardGravityAcceleration),
            8,
        )
        assertEqualScientificValue(
            1(UsTon),
            2(UsTonForce) / 2(GUnit),
            8,
        )
        assertEqualScientificValue(
            1(UsTon),
            2(UsTonForce) / (2 * ImperialStandardGravityAcceleration),
            8,
        )
        assertEqualScientificValue(
            1(Pound.ukImperial),
            2(PoundForce.ukImperial) / 2(GUnit),
            8,
        )
        assertEqualScientificValue(
            1(Pound.ukImperial),
            2(PoundForce.ukImperial) / (2 * ImperialStandardGravityAcceleration),
            8,
        )
        assertEqualScientificValue(
            1(ImperialTon),
            2(ImperialTonForce) / 2(GUnit),
            8,
        )
        assertEqualScientificValue(
            1(ImperialTon),
            2(ImperialTonForce) / (2 * ImperialStandardGravityAcceleration),
            8,
        )

        assertEqualScientificValue(
            1(Kilogram),
            2(Newton).convert(PoundForce) / 2(Meter per Second per Second),
            8,
        )
    }

    @Test
    fun weightFromHeatCapacityAndSpecificHeatCapacityTest() {
        assertEqualScientificValue(
            1(Kilogram),
            2(WattHour per Celsius) / 2(WattHour per Celsius per Kilogram),
        )
        assertEqualScientificValue(
            1(Kilogram),
            2(Joule per Celsius) / 2(Joule per Celsius per Kilogram),
        )
        assertEqualScientificValue(
            1(Pound.ukImperial),
            2(WattHour per Celsius) / 2(WattHour per Celsius per Pound),
        )
        assertEqualScientificValue(
            1(Pound.ukImperial),
            2(BritishThermalUnit per Celsius) / 2(BritishThermalUnit per Celsius per Pound),
        )
        assertEqualScientificValue(
            1(Pound.usCustomary),
            2(WattHour per Fahrenheit) / 2(WattHour per Fahrenheit per Pound),
        )
        assertEqualScientificValue(
            1(Pound.usCustomary),
            2(BritishThermalUnit per Fahrenheit) / 2(BritishThermalUnit per Fahrenheit per Pound),
        )
        assertEqualScientificValue(
            1(Kilogram),
            2(WattHour per Celsius) / 2(WattHour per Celsius per Kilogram).convert(
                BritishThermalUnit per Fahrenheit per Pound,
            ),
            8,
        )
    }

    @Test
    fun weightFromLinearMassDensityAndLength() {
        assertEqualScientificValue(4(Kilogram), 2(Kilogram per Meter) * 2(Meter))
        assertEqualScientificValue(4(Kilogram), 2(Meter) * 2(Kilogram per Meter))
        assertEqualScientificValue(4(Pound), 2(Pound per Foot) * 2(Foot))
        assertEqualScientificValue(4(Pound), 2(Foot) * 2(Pound per Foot))
        assertEqualScientificValue(4(ImperialTon), 2(ImperialTon per Foot) * 2(Foot))
        assertEqualScientificValue(4(ImperialTon), 2(Foot) * 2(ImperialTon per Foot))
        assertEqualScientificValue(4(UsTon), 2(UsTon per Foot) * 2(Foot))
        assertEqualScientificValue(4(UsTon), 2(Foot) * 2(UsTon per Foot))
        assertEqualScientificValue(4(Kilogram), 2(Kilogram per Meter) * 2(Meter).convert(Foot))
        assertEqualScientificValue(4(Kilogram), 2(Meter).convert(Foot) * 2(Kilogram per Meter))
    }

    @Test
    fun weightFromMassFlowRateAndTime() {
        assertEqualScientificValue(4(Kilogram), 2(Kilogram per Hour) * 2(Hour))
        assertEqualScientificValue(4(Kilogram), 2(Hour) * 2(Kilogram per Hour))
        assertEqualScientificValue(4(Pound), 2(Pound per Hour) * 2(Hour))
        assertEqualScientificValue(4(Pound), 2(Hour) * 2(Pound per Hour))
        assertEqualScientificValue(4(ImperialTon), 2(ImperialTon per Hour) * 2(Hour))
        assertEqualScientificValue(4(ImperialTon), 2(Hour) * 2(ImperialTon per Hour))
        assertEqualScientificValue(4(UsTon), 2(UsTon per Hour) * 2(Hour))
        assertEqualScientificValue(4(UsTon), 2(Hour) * 2(UsTon per Hour))
        assertEqualScientificValue(
            4(Kilogram),
            2(Kilogram per Hour).convert((Pound per Hour) as MassFlowRate) * 2(Hour),
            8,
        )
        assertEqualScientificValue(
            4(Kilogram),
            2(Hour) * 2(Kilogram per Hour).convert((Pound per Hour) as MassFlowRate),
            8,
        )
    }

    @Test
    fun weightFromMolarMassAndAmountOfSubstance() {
        assertEqualScientificValue(4(Kilogram), 2(Kilogram per Decimole) * 2(Decimole))
        assertEqualScientificValue(4(Kilogram), 2(Decimole) * 2(Kilogram per Decimole))
        assertEqualScientificValue(4(Pound), 2(Pound per Decimole) * 2(Decimole))
        assertEqualScientificValue(4(Pound), 2(Decimole) * 2(Pound per Decimole))
        assertEqualScientificValue(4(ImperialTon), 2(ImperialTon per Decimole) * 2(Decimole))
        assertEqualScientificValue(4(ImperialTon), 2(Decimole) * 2(ImperialTon per Decimole))
        assertEqualScientificValue(4(UsTon), 2(UsTon per Decimole) * 2(Decimole))
        assertEqualScientificValue(4(UsTon), 2(Decimole) * 2(UsTon per Decimole))
        assertEqualScientificValue(
            4(Kilogram),
            2(Kilogram per Decimole).convert((Pound per Mole) as MolarMass) * 2(Decimole),
            8,
        )
        assertEqualScientificValue(
            4(Kilogram),
            2(Decimole) * 2(Kilogram per Decimole).convert((Pound per Mole) as MolarMass),
            8,
        )
    }

    @Test
    fun weightFromMomentumAndSpeedTest() {
        assertEqualScientificValue(
            1(Kilogram),
            2(Kilogram x (Meter per Second)) / 2(Meter per Second),
        )
        assertEqualScientificValue(1(Pound), 2(Pound x (Foot per Second)) / 2(Foot per Second))
        assertEqualScientificValue(
            1(ImperialTon),
            2(ImperialTon x (Foot per Second)) / 2(Foot per Second),
        )
        assertEqualScientificValue(1(UsTon), 2(UsTon x (Foot per Second)) / 2(Foot per Second))
        assertEqualScientificValue(
            1(Kilogram),
            2(Kilogram x (Meter per Second)) / 2(Meter per Second).convert(Foot per Second),
        )
    }

    @Test
    fun weightFromVolumeAndSpecificVolumeTest() {
        assertEqualScientificValue(1(Kilogram), 2(CubicMeter) / 2(CubicMeter per Kilogram))
        assertEqualScientificValue(1(Pound), 2(CubicFoot) / 2(CubicFoot per Pound))
        assertEqualScientificValue(
            1(Pound.ukImperial),
            2(CubicFoot) / 2(CubicFoot per Pound.ukImperial),
        )
        assertEqualScientificValue(
            1(Pound.usCustomary),
            2(CubicFoot) / 2(CubicFoot per Pound.usCustomary),
        )
        assertEqualScientificValue(
            1(Pound.ukImperial),
            2(CubicFoot.ukImperial) / 2(CubicFoot per Pound),
        )
        assertEqualScientificValue(
            1(ImperialTon),
            2(ImperialGallon) / 2(ImperialGallon per ImperialTon),
        )
        assertEqualScientificValue(
            1(Pound.usCustomary),
            2(CubicFoot.usCustomary) / 2(CubicFoot per Pound),
        )
        assertEqualScientificValue(1(UsTon), 2(UsLiquidGallon) / 2(UsLiquidGallon per UsTon))
        assertEqualScientificValue(
            1(Kilogram),
            2(CubicMeter).convert(CubicFoot) / 2(CubicMeter per Kilogram),
        )
    }

    @Test
    fun weightFromYankAndJoltTest() {
        assertEqualScientificValue(
            1(Kilogram),
            2(Newton per Hour) / 2(Meter per Second per Second per Hour),
        )
        assertEqualScientificValue(
            1(Pound),
            2(PoundForce per Hour) / ((2 * ImperialStandardGravityAcceleration) / 1(Hour)),
            8,
        )
        assertEqualScientificValue(
            1(Pound.ukImperial),
            2(PoundForce.ukImperial per Hour) / ((2 * ImperialStandardGravityAcceleration) / 1(Hour)),
            8,
        )
        assertEqualScientificValue(
            1(Pound.usCustomary),
            2(PoundForce.usCustomary per Hour) / ((2 * ImperialStandardGravityAcceleration) / 1(Hour)),
            8,
        )
        assertEqualScientificValue(
            1(Kilogram),
            2(Newton per Hour).convert(PoundForce per Hour) / 2(Meter per Second per Second per Hour),
            8,
        )
    }
}
