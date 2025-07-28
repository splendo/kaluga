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

package com.splendo.kaluga.scientific.converter

import com.splendo.kaluga.base.utils.div
import com.splendo.kaluga.base.utils.toDecimal
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
import com.splendo.kaluga.scientific.unit.BritishThermalUnit
import com.splendo.kaluga.scientific.unit.Celsius
import com.splendo.kaluga.scientific.unit.Centig
import com.splendo.kaluga.scientific.unit.Centimeter
import com.splendo.kaluga.scientific.unit.CubicFoot
import com.splendo.kaluga.scientific.unit.CubicMeter
import com.splendo.kaluga.scientific.unit.Decidyne
import com.splendo.kaluga.scientific.unit.Decierg
import com.splendo.kaluga.scientific.unit.Decimole
import com.splendo.kaluga.scientific.unit.Decirad
import com.splendo.kaluga.scientific.unit.DeciroentgenEquivalentMan
import com.splendo.kaluga.scientific.unit.Dyne
import com.splendo.kaluga.scientific.unit.Erg
import com.splendo.kaluga.scientific.unit.Fahrenheit
import com.splendo.kaluga.scientific.unit.Foot
import com.splendo.kaluga.scientific.unit.GUnit
import com.splendo.kaluga.scientific.unit.Grain
import com.splendo.kaluga.scientific.unit.GrainForce
import com.splendo.kaluga.scientific.unit.Gram
import com.splendo.kaluga.scientific.unit.GramForce
import com.splendo.kaluga.scientific.unit.Gray
import com.splendo.kaluga.scientific.unit.Hour
import com.splendo.kaluga.scientific.unit.ImperialGallon
import com.splendo.kaluga.scientific.unit.ImperialStandardGravityAcceleration
import com.splendo.kaluga.scientific.unit.ImperialTon
import com.splendo.kaluga.scientific.unit.ImperialTonForce
import com.splendo.kaluga.scientific.unit.Joule
import com.splendo.kaluga.scientific.unit.Kilogram
import com.splendo.kaluga.scientific.unit.KilogramForce
import com.splendo.kaluga.scientific.unit.Kip
import com.splendo.kaluga.scientific.unit.MassFlowRate
import com.splendo.kaluga.scientific.unit.Meter
import com.splendo.kaluga.scientific.unit.MetricStandardGravityAcceleration
import com.splendo.kaluga.scientific.unit.Milligram
import com.splendo.kaluga.scientific.unit.MilligramForce
import com.splendo.kaluga.scientific.unit.Molality
import com.splendo.kaluga.scientific.unit.MolarMass
import com.splendo.kaluga.scientific.unit.Mole
import com.splendo.kaluga.scientific.unit.Newton
import com.splendo.kaluga.scientific.unit.Ounce
import com.splendo.kaluga.scientific.unit.OunceForce
import com.splendo.kaluga.scientific.unit.Pound
import com.splendo.kaluga.scientific.unit.PoundForce
import com.splendo.kaluga.scientific.unit.Poundal
import com.splendo.kaluga.scientific.unit.Rad
import com.splendo.kaluga.scientific.unit.RoentgenEquivalentMan
import com.splendo.kaluga.scientific.unit.Second
import com.splendo.kaluga.scientific.unit.Sievert
import com.splendo.kaluga.scientific.unit.SquareFoot
import com.splendo.kaluga.scientific.unit.SquareMeter
import com.splendo.kaluga.scientific.unit.Tonne
import com.splendo.kaluga.scientific.unit.TonneForce
import com.splendo.kaluga.scientific.unit.UsLiquidGallon
import com.splendo.kaluga.scientific.unit.UsTon
import com.splendo.kaluga.scientific.unit.UsTonForce
import com.splendo.kaluga.scientific.unit.WattHour
import com.splendo.kaluga.scientific.unit.per
import com.splendo.kaluga.scientific.unit.ukImperial
import com.splendo.kaluga.scientific.unit.usCustomary
import com.splendo.kaluga.scientific.unit.x
import kotlin.test.Test

class WeightUnitTest {

    @Test
    fun weightFromAmountOfSubstanceAndMolalityTest() {
        assertEqualScientificValue(1(Kilogram), 2(Decimole) / 2(Decimole per Kilogram))
        assertEqualScientificValue(1(Pound), 2(Decimole) / 2(Decimole per Pound), round = 30)
        assertEqualScientificValue(1(ImperialTon), 2(Decimole) / 2(Decimole per ImperialTon), round = 30)
        assertEqualScientificValue(1(UsTon), 2(Decimole) / 2(Decimole per UsTon), round = 30)
        assertEqualScientificValue(
            1(Kilogram),
            2(Decimole) / 2(Decimole per Kilogram).convert((Decimole per Pound) as Molality),
        )
    }

    @Test
    fun weightFromAreaDensityAndAreaTest() {
        assertEqualScientificValue(4(Kilogram), 2(Kilogram per SquareMeter) * 2(SquareMeter))
        assertEqualScientificValue(4(Kilogram), 2(SquareMeter) * 2(Kilogram per SquareMeter))
        assertEqualScientificValue(4(Pound), 2(Pound per SquareFoot) * 2(SquareFoot), round = 32)
        assertEqualScientificValue(4(Pound), 2(SquareFoot) * 2(Pound per SquareFoot), round = 32)
        assertEqualScientificValue(4(ImperialTon), 2(ImperialTon per SquareFoot) * 2(SquareFoot), round = 32)
        assertEqualScientificValue(4(ImperialTon), 2(SquareFoot) * 2(ImperialTon per SquareFoot), round = 32)
        assertEqualScientificValue(4(UsTon), 2(UsTon per SquareFoot) * 2(SquareFoot), round = 32)
        assertEqualScientificValue(4(UsTon), 2(SquareFoot) * 2(UsTon per SquareFoot), round = 32)
        assertEqualScientificValue(
            4(Kilogram),
            2(Kilogram per SquareMeter) * 2(SquareMeter).convert(SquareFoot),
            round = 32,
        )
        assertEqualScientificValue(
            4(Kilogram),
            2(SquareMeter).convert(SquareFoot) * 2(Kilogram per SquareMeter),
            round = 32,
        )
    }

    @Test
    fun weightFromDensityAndVolumeTest() {
        assertEqualScientificValue(4(Kilogram), 2(Kilogram per CubicMeter) * 2(CubicMeter))
        assertEqualScientificValue(4(Kilogram), 2(CubicMeter) * 2(Kilogram per CubicMeter))
        assertEqualScientificValue(4(Pound), 2(Pound per CubicFoot) * 2(CubicFoot), round = 32)
        assertEqualScientificValue(4(Pound), 2(CubicFoot) * 2(Pound per CubicFoot), round = 32)
        assertEqualScientificValue(4(Pound), 2(Pound per CubicFoot) * 2(CubicFoot.ukImperial), round = 32)
        assertEqualScientificValue(4(Pound), 2(CubicFoot.ukImperial) * 2(Pound per CubicFoot), round = 32)
        assertEqualScientificValue(4(Pound), 2(Pound per CubicFoot) * 2(CubicFoot.usCustomary), round = 32)
        assertEqualScientificValue(4(Pound), 2(CubicFoot.usCustomary) * 2(Pound per CubicFoot), round = 32)
        assertEqualScientificValue(4(ImperialTon), 2(ImperialTon per CubicFoot) * 2(CubicFoot), round = 32)
        assertEqualScientificValue(4(ImperialTon), 2(CubicFoot) * 2(ImperialTon per CubicFoot), round = 32)
        assertEqualScientificValue(
            4(ImperialTon),
            2(ImperialTon per ImperialGallon) * 2(ImperialGallon),
            round = 32,
        )
        assertEqualScientificValue(
            4(ImperialTon),
            2(ImperialGallon) * 2(ImperialTon per ImperialGallon),
            round = 32,
        )
        assertEqualScientificValue(4(UsTon), 2(UsTon per CubicFoot) * 2(CubicFoot), round = 32)
        assertEqualScientificValue(4(UsTon), 2(CubicFoot) * 2(UsTon per CubicFoot), round = 32)
        assertEqualScientificValue(4(UsTon), 2(UsTon per UsLiquidGallon) * 2(UsLiquidGallon), round = 32)
        assertEqualScientificValue(4(UsTon), 2(UsLiquidGallon) * 2(UsTon per UsLiquidGallon), round = 32)
        assertEqualScientificValue(
            4(Kilogram),
            2(Kilogram per CubicMeter) * 2(CubicMeter).convert(CubicFoot),
            round = 32,
        )
        assertEqualScientificValue(
            4(Kilogram),
            2(CubicMeter).convert(CubicFoot) * 2(Kilogram per CubicMeter),
            round = 32,
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
        assertEqualScientificValue(1(Pound), 2(WattHour) / 2(WattHour per Pound), round = 30)
        assertEqualScientificValue(1(ImperialTon), 2(WattHour) / 2(WattHour per ImperialTon), round = 30)
        assertEqualScientificValue(1(UsTon), 2(WattHour) / 2(WattHour per UsTon), round = 30)
        assertEqualScientificValue(1(Kilogram), 2(Joule) / 2(Joule per Kilogram))
        assertEqualScientificValue(
            1(Pound),
            2(BritishThermalUnit) / 2(BritishThermalUnit per Pound),
            round = 30,
        )
        assertEqualScientificValue(
            1(ImperialTon),
            2(BritishThermalUnit) / 2(BritishThermalUnit per ImperialTon),
            round = 30,
        )
        assertEqualScientificValue(
            1(UsTon),
            2(BritishThermalUnit) / 2(BritishThermalUnit per UsTon),
            round = 30,
        )
        assertEqualScientificValue(
            1(Kilogram),
            2(Joule).convert(BritishThermalUnit) / 2(Joule per Kilogram),
            round = 32,
        )
    }

    @Test
    fun weightFromForceAndAccelerationTest() {
        assertEqualScientificValue(1(Gram), 2(Dyne) / 2(Centimeter per Second per Second))
        assertEqualScientificValue((1.toDecimal() / MetricStandardGravityAcceleration.decimalValue)(Gram), 2(Dyne) / 2(Centig))
        assertEqualScientificValue(1(Gram), 20(Decidyne) / 2(Centimeter per Second per Second))
        assertEqualScientificValue((1.toDecimal() / MetricStandardGravityAcceleration.decimalValue)(Gram), 20(Decidyne) / 2(Centig))

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

        assertEqualScientificValue((1.toDecimal() / ImperialStandardGravityAcceleration.decimalValue)(Pound), 2(Poundal) / 2(GUnit), round = 32)
        assertEqualScientificValue(1(Pound), 2(Poundal) / 2(Foot per Second per Second))
        assertEqualScientificValue(
            1(Pound),
            2(PoundForce) / 2(GUnit),
        )
        assertEqualScientificValue(
            1(Pound),
            2(PoundForce) / 2(GUnit.imperial),
        )
        assertEqualScientificValue(
            1(Pound),
            2(PoundForce) / (2 * ImperialStandardGravityAcceleration),
        )
        assertEqualScientificValue(
            1(Ounce),
            2(OunceForce) / 2(GUnit),
        )
        assertEqualScientificValue(
            1(Ounce),
            2(OunceForce) / (2 * ImperialStandardGravityAcceleration),
        )
        assertEqualScientificValue(
            1(Grain),
            2(GrainForce) / 2(GUnit),
            round = 32,
        )
        assertEqualScientificValue(
            1(Grain),
            2(GrainForce) / (2 * ImperialStandardGravityAcceleration),
            round = 32,
        )

        assertEqualScientificValue(
            1000(Pound.usCustomary),
            2(Kip) / 2(GUnit),
        )
        assertEqualScientificValue(
            1000(Pound.usCustomary),
            2(Kip) / (2 * ImperialStandardGravityAcceleration),
            round = 30,
        )
        assertEqualScientificValue(
            1(UsTon),
            2(UsTonForce) / 2(GUnit),
        )
        assertEqualScientificValue(
            1(UsTon),
            2(UsTonForce) / (2 * ImperialStandardGravityAcceleration),
            round = 32,
        )
        assertEqualScientificValue(
            1(Pound.ukImperial),
            2(PoundForce.ukImperial) / 2(GUnit),
        )
        assertEqualScientificValue(
            1(Pound.ukImperial),
            2(PoundForce.ukImperial) / (2 * ImperialStandardGravityAcceleration),
        )
        assertEqualScientificValue(
            1(ImperialTon),
            2(ImperialTonForce) / 2(GUnit),
        )
        assertEqualScientificValue(
            1(ImperialTon),
            2(ImperialTonForce) / (2 * ImperialStandardGravityAcceleration),
            round = 30,
        )

        assertEqualScientificValue(
            1(Kilogram),
            2(Newton).convert(PoundForce) / 2(Meter per Second per Second),
            round = 30,
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
            round = 30,
        )
        assertEqualScientificValue(
            1(Pound.ukImperial),
            2(BritishThermalUnit per Celsius) / 2(BritishThermalUnit per Celsius per Pound),
            round = 30,
        )
        assertEqualScientificValue(
            1(Pound.usCustomary),
            2(WattHour per Fahrenheit) / 2(WattHour per Fahrenheit per Pound),
            round = 30,
        )
        assertEqualScientificValue(
            1(Pound.usCustomary),
            2(BritishThermalUnit per Fahrenheit) / 2(BritishThermalUnit per Fahrenheit per Pound),
            round = 30,
        )
        assertEqualScientificValue(
            1(Kilogram),
            2(WattHour per Celsius) / 2(WattHour per Celsius per Kilogram).convert(
                BritishThermalUnit per Fahrenheit per Pound,
            ),
            round = 32,
        )
    }

    @Test
    fun weightFromLinearMassDensityAndLength() {
        assertEqualScientificValue(4(Kilogram), 2(Kilogram per Meter) * 2(Meter))
        assertEqualScientificValue(4(Kilogram), 2(Meter) * 2(Kilogram per Meter))
        assertEqualScientificValue(4(Pound), 2(Pound per Foot) * 2(Foot), round = 32)
        assertEqualScientificValue(4(Pound), 2(Foot) * 2(Pound per Foot), round = 32)
        assertEqualScientificValue(4(ImperialTon), 2(ImperialTon per Foot) * 2(Foot), round = 32)
        assertEqualScientificValue(4(ImperialTon), 2(Foot) * 2(ImperialTon per Foot), round = 32)
        assertEqualScientificValue(4(UsTon), 2(UsTon per Foot) * 2(Foot), round = 32)
        assertEqualScientificValue(4(UsTon), 2(Foot) * 2(UsTon per Foot), round = 32)
        assertEqualScientificValue(4(Kilogram), 2(Kilogram per Meter) * 2(Meter).convert(Foot), round = 32)
        assertEqualScientificValue(4(Kilogram), 2(Meter).convert(Foot) * 2(Kilogram per Meter), round = 32)
    }

    @Test
    fun weightFromMassFlowRateAndTime() {
        assertEqualScientificValue(4(Kilogram), 2(Kilogram per Hour) * 2(Hour), round = 32)
        assertEqualScientificValue(4(Kilogram), 2(Hour) * 2(Kilogram per Hour), round = 32)
        assertEqualScientificValue(4(Pound), 2(Pound per Hour) * 2(Hour), round = 32)
        assertEqualScientificValue(4(Pound), 2(Hour) * 2(Pound per Hour), round = 32)
        assertEqualScientificValue(4(ImperialTon), 2(ImperialTon per Hour) * 2(Hour), round = 32)
        assertEqualScientificValue(4(ImperialTon), 2(Hour) * 2(ImperialTon per Hour), round = 32)
        assertEqualScientificValue(4(UsTon), 2(UsTon per Hour) * 2(Hour), round = 32)
        assertEqualScientificValue(4(UsTon), 2(Hour) * 2(UsTon per Hour), round = 32)
        assertEqualScientificValue(
            4(Kilogram),
            2(Kilogram per Hour).convert((Pound per Hour) as MassFlowRate) * 2(Hour),
            round = 32,
        )
        assertEqualScientificValue(
            4(Kilogram),
            2(Hour) * 2(Kilogram per Hour).convert((Pound per Hour) as MassFlowRate),
            round = 32,
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
            round = 30,
        )
        assertEqualScientificValue(
            4(Kilogram),
            2(Decimole) * 2(Kilogram per Decimole).convert((Pound per Mole) as MolarMass),
            round = 30,
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
            round = 32,
        )
    }

    @Test
    fun weightFromVolumeAndSpecificVolumeTest() {
        assertEqualScientificValue(1(Kilogram), 2(CubicMeter) / 2(CubicMeter per Kilogram))
        assertEqualScientificValue(1(Pound), 2(CubicFoot) / 2(CubicFoot per Pound), round = 30)
        assertEqualScientificValue(
            1(Pound.ukImperial),
            2(CubicFoot) / 2(CubicFoot per Pound.ukImperial),
            round = 30,
        )
        assertEqualScientificValue(
            1(Pound.usCustomary),
            2(CubicFoot) / 2(CubicFoot per Pound.usCustomary),
            round = 30,
        )
        assertEqualScientificValue(
            1(Pound.ukImperial),
            2(CubicFoot.ukImperial) / 2(CubicFoot per Pound),
            round = 30,
        )
        assertEqualScientificValue(
            1(Pound.usCustomary),
            2(CubicFoot.usCustomary) / 2(CubicFoot per Pound),
            round = 30,
        )
        assertEqualScientificValue(
            1(ImperialTon),
            2(ImperialGallon) / 2(ImperialGallon per ImperialTon),
            round = 30,
        )
        assertEqualScientificValue(1(UsTon), 2(UsLiquidGallon) / 2(UsLiquidGallon per UsTon), round = 30)
        assertEqualScientificValue(
            1(Kilogram),
            2(CubicMeter).convert(CubicFoot) / 2(CubicMeter per Kilogram),
            round = 32,
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
            round = 32,
        )
        assertEqualScientificValue(
            1(Pound.ukImperial),
            2(PoundForce.ukImperial per Hour) / ((2 * ImperialStandardGravityAcceleration) / 1(Hour)),
            round = 32,
        )
        assertEqualScientificValue(
            1(Pound.usCustomary),
            2(PoundForce.usCustomary per Hour) / ((2 * ImperialStandardGravityAcceleration) / 1(Hour)),
            round = 32,
        )
        assertEqualScientificValue(
            1(Kilogram),
            2(Newton per Hour).convert(PoundForce per Hour) / 2(Meter per Second per Second per Hour),
            round = 32,
        )
    }
}
