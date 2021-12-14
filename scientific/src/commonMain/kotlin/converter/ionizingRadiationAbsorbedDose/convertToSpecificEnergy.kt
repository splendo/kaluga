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

package com.splendo.kaluga.scientific.converter.ionizingRadiationAbsorbedDose

import com.splendo.kaluga.scientific.PhysicalQuantity
import com.splendo.kaluga.scientific.ScientificValue
import com.splendo.kaluga.scientific.converter.specificEnergy.specificEnergy
import com.splendo.kaluga.scientific.unit.Erg
import com.splendo.kaluga.scientific.unit.Gram
import com.splendo.kaluga.scientific.unit.IonizingRadiationAbsorbedDose
import com.splendo.kaluga.scientific.unit.Joule
import com.splendo.kaluga.scientific.unit.Kilogram
import com.splendo.kaluga.scientific.unit.MeasurementSystem
import com.splendo.kaluga.scientific.unit.MetricMultipleUnit
import com.splendo.kaluga.scientific.unit.Rad
import com.splendo.kaluga.scientific.unit.per
import kotlin.jvm.JvmName

@JvmName("specificEnergyFromRad")
fun ScientificValue<PhysicalQuantity.IonizingRadiationAbsorbedDose, Rad>.asSpecificEnergy() =
    (Erg per Gram).specificEnergy(this)

@JvmName("specificEnergyFromRadMultiple")
fun <RadUnit> ScientificValue<PhysicalQuantity.IonizingRadiationAbsorbedDose, RadUnit>.asSpecificEnergy() where RadUnit : IonizingRadiationAbsorbedDose, RadUnit : MetricMultipleUnit<MeasurementSystem.MetricAndImperial, PhysicalQuantity.IonizingRadiationAbsorbedDose, Rad> =
    (Erg per Gram).specificEnergy(this)

@JvmName("specificEnergyFromAbsorbedDose")
fun <AbsorbedDoseUnit : IonizingRadiationAbsorbedDose> ScientificValue<PhysicalQuantity.IonizingRadiationAbsorbedDose, AbsorbedDoseUnit>.asSpecificEnergy() =
    (Joule per Kilogram).specificEnergy(this)